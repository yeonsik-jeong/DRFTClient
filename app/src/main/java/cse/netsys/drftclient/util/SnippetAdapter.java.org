package cse.netsys.drftclient.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cse.netsys.drftclient.R;
import cse.netsys.drftclient.model.Snippet;

public class SnippetAdapter extends RecyclerView.Adapter<SnippetAdapter.ViewHolder> {
    private List<Snippet> snippetList;
    private OnItemClickListener listener;

    public SnippetAdapter(List<Snippet> snippetList) {
        this.snippetList = snippetList;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public List<Snippet> getSnippetList() {
        return snippetList;
    }

    public void setSnippetList(List<Snippet> snippetList) {
        this.snippetList = snippetList;
//        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SnippetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.recyclerview_snippets, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SnippetAdapter.ViewHolder holder, int position) {
        Snippet snippet = snippetList.get(position);

        holder.tvSnippetId.setText(Integer.toString(snippet.getId()));
        holder.tvSnippetTitle.setText(snippet.getTitle());
        holder.tvSnippetLanguage.setText(snippet.getLanguage());
        holder.tvSnippetOwner.setText(snippet.getOwner());
    }

    @Override
    public int getItemCount() {
        return snippetList.size();
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
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView,position);
                        }
                    }
                }
            });
        }
    }
}
