package cse.netsys.drftclient.util;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cse.netsys.drftclient.BaseActivity;
import cse.netsys.drftclient.MainActivity;
import cse.netsys.drftclient.R;
import cse.netsys.drftclient.model.Snippet;

public class SnippetAdapter extends ListAdapter<Snippet, SnippetAdapter.ViewHolder> {
    private OnItemClickListener mListener;

    public SnippetAdapter() {
        super(DIFF_CALLBACK);
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
        return new ViewHolder(inflater.inflate(R.layout.recyclerview_snippets, parent, false));
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

    public void addItem(int position, Snippet snippet) {
        List<Snippet> snippetList = new ArrayList<Snippet>();
        snippetList.addAll(getCurrentList());
        snippetList.add(position, snippet);
        submitList(snippetList);
    }

    public void updateItem(int position, Snippet snippet) {
        List<Snippet> snippetList = new ArrayList<Snippet>();
        snippetList.addAll(getCurrentList());
        snippetList.set(position, snippet);
        submitList(snippetList);
    }

    public void removeItem(int position) {
        List<Snippet> snippetList = new ArrayList<Snippet>();
        snippetList.addAll(getCurrentList());
        snippetList.remove(position);
        submitList(snippetList);
    }

/*    public void addMoreSnippets(List<Snippet> newSnippetList) {
        mSnippets.addAll(newSnippetList);
        submitList(mSnippets);  // DiffUtil takes care of the check
    }*/

    public static final DiffUtil.ItemCallback<Snippet> DIFF_CALLBACK = new DiffUtil.ItemCallback<Snippet>() {
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
    };
}
