package cse.netsys.drftclient.paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import java.util.List;

import cse.netsys.drftclient.BaseActivity;
import cse.netsys.drftclient.api.DRFTAPIService;
import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.model.SnippetResp;
import cse.netsys.drftclient.api.APIServiceGenerator;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SnippetPagingSource extends RxPagingSource<Integer, Snippet> {
    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Snippet> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if(anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, Snippet> anchorPage = pagingState.closestPageToPosition(anchorPosition);
        if(anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if(prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if(nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Snippet>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        try {
            int page = (loadParams.getKey() != null)? loadParams.getKey(): 1;

            return APIServiceGenerator.createService(DRFTAPIService.class, BaseActivity.API_BASE_URL).listPagedSnippets(page)
                    .subscribeOn(Schedulers.io())
//                    .map(this::toLoadResult)  // Working well
                    .map(snippetResp -> toLoadResult(snippetResp))
//                    .map(SnippetResp::getResults)
//                    .map(snippetList -> toLoadResult(snippetList, page))
                    .onErrorReturn(LoadResult.Error::new);
        } catch(Exception e) {
            return Single.just(new LoadResult.Error(e));
        }
    }

    private LoadResult<Integer, Snippet> toLoadResult(@NonNull SnippetResp response) {
        return new LoadResult.Page<>(response.getResults(), null, response.getNextPageNumber(),
                LoadResult.Page.COUNT_UNDEFINED, LoadResult.Page.COUNT_UNDEFINED);
    }

    private LoadResult<Integer, Snippet> toLoadResult(List<Snippet> snippetList, int page) {
        return new LoadResult.Page(snippetList, page == 1? null: page-1, page+1);
    }
}