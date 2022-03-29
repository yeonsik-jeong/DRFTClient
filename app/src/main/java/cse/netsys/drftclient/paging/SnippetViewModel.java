package cse.netsys.drftclient.paging;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.paging.SnippetPagingSource;
import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class SnippetViewModel extends ViewModel {
    public static final int PAGE_SIZE = 15;

    private Flowable<PagingData<Snippet>> mPagingDataFlowable;

    public SnippetViewModel() {
        init();
    }

    public Flowable<PagingData<Snippet>> getPagingDataFlowable() {
        return mPagingDataFlowable;
    }

    private void init() {
        PagingConfig pagingConfig = new PagingConfig(
                                        PAGE_SIZE,  // pageSize
                                        (int)Math.round(PAGE_SIZE*0.1),  // prefetchDistance
                        true,  // enablePlaceholders
                                        PAGE_SIZE,  // initialLoadSize
                                PAGE_SIZE*100);  // maxSize

        Pager<Integer, Snippet> pager = new Pager<>(pagingConfig, () -> new SnippetPagingSource());

        mPagingDataFlowable = PagingRx.getFlowable(pager);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        PagingRx.cachedIn(mPagingDataFlowable, viewModelScope);
    }
}
