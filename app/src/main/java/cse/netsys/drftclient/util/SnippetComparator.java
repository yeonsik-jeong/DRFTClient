package cse.netsys.drftclient.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import cse.netsys.drftclient.model.Snippet;

public class SnippetComparator extends DiffUtil.ItemCallback<Snippet> {
    @Override
    public boolean areItemsTheSame(@NonNull Snippet oldItem, @NonNull Snippet newItem) {
//        Log.i(BaseActivity.TAG, "areItemsTheSame() called");
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Snippet oldItem, @NonNull Snippet newItem) {
//        Log.i(BaseActivity.TAG, "areContentsTheSame() called");
        return (oldItem.getUrl().equals(newItem.getUrl())
                && oldItem.getOwner().equals(newItem.getOwner())
                && oldItem.getHighlight().equals(newItem.getHighlight())
                && oldItem.getTitle().equals(newItem.getTitle())
                && oldItem.getCode().equals(newItem.getCode())
                && oldItem.isLinenos() == newItem.isLinenos()
                && oldItem.getLanguage().equals(newItem.getLanguage())
                && oldItem.getStyle().equals(newItem.getStyle()));

    }
}
