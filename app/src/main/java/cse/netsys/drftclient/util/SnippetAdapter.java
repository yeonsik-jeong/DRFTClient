package cse.netsys.drftclient.util;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import cse.netsys.drftclient.BaseActivity;
import cse.netsys.drftclient.R;
import cse.netsys.drftclient.db.RemoteKey;
import cse.netsys.drftclient.db.SnippetDatabase;
import cse.netsys.drftclient.model.Snippet;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SnippetAdapter extends PagingDataAdapter<Snippet, SnippetAdapter.ViewHolder> {
    private SnippetDatabase mDatabase;
    private RecyclerView mRecyclerView;
    private OnItemClickListener mListener;

    public SnippetAdapter(@NotNull DiffUtil.ItemCallback<Snippet> diffCallback, SnippetDatabase database) {
        super(diffCallback);
        this.mDatabase = database;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public SnippetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.recyclerview_snippet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SnippetAdapter.ViewHolder holder, int position) {
        Snippet snippet = getItem(position);
        holder.bindTo(snippet);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSnippetId;
        TextView tvSnippetTitle;
        TextView tvSnippetLanguage;
        TextView tvSnippetOwner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View

            tvSnippetId = itemView.findViewById(R.id.tvSnippetId);
            tvSnippetTitle = itemView.findViewById(R.id.tvSnippetTitle);
            tvSnippetLanguage = itemView.findViewById(R.id.tvSnippetLanguage);
            tvSnippetOwner = itemView.findViewById(R.id.tvSnippetOwner);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }

        void bindTo(Snippet snippet) {
            tvSnippetId.setText(Integer.toString(snippet.getId()));
            tvSnippetTitle.setText(snippet.getTitle());
            tvSnippetLanguage.setText(snippet.getLanguage());
            tvSnippetOwner.setText(snippet.getOwner());
        }
    }

    public Snippet getSnippet(int position) {
        return getItem(position);
    }

    public void addItem(Snippet snippet, int position) {  // ToDO: CRUD
        mDatabase.remoteKeyDao().insertRemoteKey(new RemoteKey(snippet.getId(), null, 2))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(BaseActivity.TAG, "removeItem(): deleteRemoteKey() successful"),
                        throwable -> Log.d(BaseActivity.TAG, throwable.getMessage()));
        mDatabase.snippetDao().insertSnippet(snippet)  // Don't need to give the position
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> { notifyItemInserted(position);
                                    Log.d(BaseActivity.TAG, "addItem(): insertSnippet() successful"); },
                            throwable -> Log.d(BaseActivity.TAG, throwable.getMessage()));
//        snapshot().getItems().add(position, snippet);
//        refresh();
//        ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(position+10, 0); // Works well
/*
        List<Snippet> snippetList = new ArrayList<Snippet>();
        snippetList.addAll(getCurrentList());
        snippetList.add(position, snippet);
        submitList(snippetList);
*/
    }

    public void updateItem(Snippet snippet, int position) { // ToDO: CRUD
        mDatabase.snippetDao().updateSnippet(snippet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> { notifyItemChanged(position);
                                    Log.d(BaseActivity.TAG, "updateItem(): updateSnippet() successful"); },
                            throwable -> Log.d(BaseActivity.TAG, throwable.getMessage()));

//        snapshot().getItems().set(position, snippet);
//        refresh();
/*
        List<Snippet> snippetList = new ArrayList<Snippet>();
        snippetList.addAll(getCurrentList());
        snippetList.set(position, snippet);
        submitList(snippetList);
*/
    }

    public void removeItem(int snippetId, int position) { // ToDO: CRUD
        mDatabase.remoteKeyDao().deleteRemoteKey(snippetId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(BaseActivity.TAG, "removeItem(): deleteRemoteKey() successful"),
                            throwable -> Log.d(BaseActivity.TAG, throwable.getMessage()));
        mDatabase.snippetDao().deleteSnippet(snippetId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> { notifyItemRemoved(position);
                                    Log.d(BaseActivity.TAG, "removeItem(): deleteSnippet() successful"); },
                            throwable -> Log.d(BaseActivity.TAG, throwable.getMessage()));

//        snapshot().getItems().remove(position);
//        refresh();
/*        List<Snippet> snippetList = new ArrayList<Snippet>();
        snippetList.addAll(getCurrentList());
        snippetList.remove(position);
        submitList(snippetList);*/
    }

/*    public void addMoreSnippets(List<Snippet> newSnippetList) {
        mSnippets.addAll(newSnippetList);
        submitList(mSnippets);  // DiffUtil takes care of the check
    }*/

/*    public static final DiffUtil.ItemCallback<Snippet> DIFF_CALLBACK = new DiffUtil.ItemCallback<Snippet>() {
        @Override
        public boolean areItemsTheSame(@NonNull Snippet oldItem, @NonNull Snippet newItem) {
//            Log.i(BaseActivity.TAG, "areItemsTheSame() called");
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Snippet oldItem, @NonNull Snippet newItem) {
//            Log.i(BaseActivity.TAG, "areContentsTheSame() called");
            return (oldItem.getUrl().equals(newItem.getUrl())
                    && oldItem.getOwner().equals(newItem.getOwner())
                    && oldItem.getHighlight().equals(newItem.getHighlight())
                    && oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getCode().equals(newItem.getCode())
                    && oldItem.isLinenos() == newItem.isLinenos()
                    && oldItem.getLanguage().equals(newItem.getLanguage())
                    && oldItem.getStyle().equals(newItem.getStyle()));
        }
    };*/
}
