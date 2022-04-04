package cse.netsys.drftclient.paging;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import cse.netsys.drftclient.db.SnippetDao;
import cse.netsys.drftclient.db.SnippetDatabase;
import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.paging.SnippetPagingSource;
import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class SnippetViewModel extends ViewModel {
    public static final int PAGE_SIZE = 15;

    private SnippetDatabase mDatabase;
    private Flowable<PagingData<Snippet>> mPagingDataFlowable;

/*    public SnippetViewModel() {
        init();
    }*/

    public void setDatabase(SnippetDatabase database) {
        this.mDatabase = database;
    }

    public Flowable<PagingData<Snippet>> getPagingDataFlowable() {
        return mPagingDataFlowable;
    }

    public void init() {
        PagingConfig pagingConfig = new PagingConfig(
                                        PAGE_SIZE);  // pageSize
//                                        (int)Math.round(PAGE_SIZE*0.2),  // prefetchDistance
//                                        true  // enablePlaceholders
//                                        PAGE_SIZE  // initialLoadSize
//                                        MAX_SIZE_UNBOUNDED);  // maxSize unbounded by default

//        Pager<Integer, Snippet> pager = new Pager<>(pagingConfig, () -> new SnippetPagingSource());  // Simple: only PagingSource
        SnippetDao snippetDao = mDatabase.snippetDao();
        Pager<Integer, Snippet> pager = new Pager<>(
                                            pagingConfig,
                                            null, // initialKey
                                            new SnippetRemoteMediator(mDatabase),
                                            () -> snippetDao.pagingSource());

        mPagingDataFlowable = PagingRx.getFlowable(pager);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        PagingRx.cachedIn(mPagingDataFlowable, viewModelScope);
    }
}
