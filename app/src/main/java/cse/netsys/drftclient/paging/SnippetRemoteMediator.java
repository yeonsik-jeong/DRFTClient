package cse.netsys.drftclient.paging;

import static java.lang.Thread.sleep;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.LoadType;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxRemoteMediator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cse.netsys.drftclient.BaseActivity;
import cse.netsys.drftclient.api.APIServiceGenerator;
import cse.netsys.drftclient.api.DRFTAPIService;
import cse.netsys.drftclient.db.RemoteKey;
import cse.netsys.drftclient.db.RemoteKeyDao;
import cse.netsys.drftclient.db.SnippetDatabase;
import cse.netsys.drftclient.db.SnippetDao;
import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.model.SnippetResp;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class SnippetRemoteMediator extends RxRemoteMediator<Integer, Snippet> {
    private SnippetDatabase mDatabase;
    private SnippetDao mSnippetDao;
    private RemoteKeyDao mRemoteKeyDao;
    private Single<RemoteKey> mRemoteKeySingle;

    public SnippetRemoteMediator(SnippetDatabase database) {
        this.mDatabase = database;
        this.mSnippetDao = mDatabase.snippetDao();
        this.mRemoteKeyDao = mDatabase.remoteKeyDao();
    }

    public void setRemoteKeySingle(Single<RemoteKey> remoteKeySingle) {
        this.mRemoteKeySingle = remoteKeySingle;
    }

    @NonNull
    @Override
    public Single<MediatorResult> loadSingle(@NonNull LoadType loadType, @NonNull PagingState<Integer, Snippet> pagingState) {
        // The network load method takes an optional after=<user.id> parameter. For
        // every page after the first, pass the last user ID to let it continue from
        // where it left off. For REFRESH, pass null to load the first page.
        mRemoteKeySingle = null; // nextPageNumber
        Log.d(BaseActivity.TAG, "loadType: " + loadType + " pagingState: " + pagingState);
/*        mRemoteKeyDao.insertRemoteKey(new RemoteKey(0,null,1))  // Initial key
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(() -> Log.d(BaseActivity.TAG, "insertRemoteKey() successful"),
                                throwable -> Log.d(BaseActivity.TAG, throwable.getMessage()));*/

        switch(loadType) {
            case REFRESH:
                RemoteKey initialRemoteKey = new RemoteKey(0,null,1);
                mRemoteKeySingle = Single.just(initialRemoteKey);
        //        mRemoteKeySingle = getRemoteKeySingleClosestToCurrentPosition(pagingState);  // Codelab
                break;
            case PREPEND:
                return Single.just(new MediatorResult.Success(true));
            case APPEND:
                // Query remoteKeyDao for the next RemoteKey.
/*                if(pagingState.lastItemOrNull() != null) {
                    mRemoteKeyDao.remoteKeyByIdSingle(pagingState.lastItemOrNull().getId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(remoteKey -> setRemoteKeySingle(Single.just(remoteKey)),
                                        throwable -> Log.e(BaseActivity.TAG, throwable.getMessage()));
                }*/
//                setRemoteKeySingleForLastItem(pagingState);
/*              // https://developer.android.com/topic/libraries/architecture/paging/v3-network-db#java
                // Because the PREPEND or APPEND requests were queued before the REFRESH request,
                // it is possible that the PagingState passed to those load calls will be out of date by the time they run.
                Log.i(BaseActivity.TAG, "pagingState.lastItemOrNull().getId(): " + pagingState.lastItemOrNull().getId());
                if(pagingState.lastItemOrNull() != null) {
                    mRemoteKeySingle = mRemoteKeyDao.remoteKeyByIdSingle(pagingState.lastItemOrNull().getId());
                }
*/
                mRemoteKeySingle = mRemoteKeyDao.remoteKeyLatestSingle();
                Log.i(BaseActivity.TAG, "mRemoteKeyDao.remoteKeyLatestSingle(): " + mRemoteKeySingle);
//                mRemoteKeySingle = Single.just(new RemoteKey(1,null,1));
                break;
        }

        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return mRemoteKeySingle
                .subscribeOn(Schedulers.io())
                .flatMap((Function<RemoteKey, Single<MediatorResult>>) remoteKey -> {
                    // You must explicitly check if the page key is null when appending,
                    // since null is only valid for initial load. If you receive null
                    // for APPEND, that means you have reached the end of pagination and
                    // there are no more items to load.
                    Log.d(BaseActivity.TAG, "remoteKeySingle.flatmap() inside");
                    if(loadType != LoadType.REFRESH && remoteKey.getNextKey() == null) {
                        return Single.just(new MediatorResult.Success(true));
                    }
                    return APIServiceGenerator.createService(DRFTAPIService.class, BaseActivity.API_BASE_URL).listPagedSnippets(remoteKey.getNextKey())
                            .map(response -> {
                                mDatabase.runInTransaction(() -> {
                                    if(loadType == LoadType.REFRESH) {
                                        mSnippetDao.clearAll();
                                        mRemoteKeyDao.clearAll();
                                    }
                                    // Update RemoteKey for this query.
                                    mRemoteKeyDao.insertAll(generateRemoteKeyListFromSnippetResp(response));
                                    // Insert new users into database, which invalidates the current
                                    // PagingData, allowing Paging to present the updates in the DB.
                                    mSnippetDao.insertAll(response.getResults());
                                });
                                return new MediatorResult.Success(response.getNextPageNumber() == null);
                            });
                })
                .onErrorResumeNext(e -> {
                    if(e instanceof IOException || e instanceof HttpException) {
                        return Single.just(new MediatorResult.Error(e));
                    }
                    return Single.error(e);
                });
    }

    @NonNull
    @Override
    public Single<InitializeAction> initializeSingle() {
        Log.d(BaseActivity.TAG, "initializeSingle() called");
        long cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
        return Single.just(InitializeAction.LAUNCH_INITIAL_REFRESH);

/*        return mSnippetDao.lastUpdatedTimeSingle()
                .map(lastUpdatedMillis -> {
                    if(System.currentTimeMillis() - lastUpdatedMillis >= cacheTimeout) {
                        return InitializeAction.SKIP_INITIAL_REFRESH;
                    } else {
                        return InitializeAction.LAUNCH_INITIAL_REFRESH;
                    }
                });*/
    }

    private List<RemoteKey> generateRemoteKeyListFromSnippetResp(SnippetResp response) {
        List<RemoteKey> remoteKeyList = new ArrayList<>();
        for(Snippet snippet: response.getResults()) {
            RemoteKey remoteKey = new RemoteKey(snippet.getId(), null, response.getNextPageNumber());
            remoteKeyList.add(remoteKey);
        }
        Log.i(BaseActivity.TAG, remoteKeyList.toString());
        return remoteKeyList;
    }

    private Single<RemoteKey> getRemoteKeySingleClosestToCurrentPosition(PagingState<Integer, Snippet> pagingState) {
        return (pagingState.getAnchorPosition() != null)?
                mRemoteKeyDao.remoteKeyByIdSingle(pagingState.closestItemToPosition(pagingState.getAnchorPosition()).getId()): null;
    }

    private Single<RemoteKey> setRemoteKeySingleForLastItem(PagingState<Integer, Snippet> pagingState) {
//        return (pagingState.lastItemOrNull() != null)?
//                mRemoteKeyDao.remoteKeyByIdSingle(pagingState.lastItemOrNull().getId()): null;
        if(pagingState.lastItemOrNull() != null) {
            mRemoteKeyDao.remoteKeyByIdSingle(pagingState.lastItemOrNull().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(remoteKey -> setRemoteKeySingle(Single.just(remoteKey)),
                            throwable -> Log.e(BaseActivity.TAG, throwable.getMessage()));
        }
        return null;
    }

}
