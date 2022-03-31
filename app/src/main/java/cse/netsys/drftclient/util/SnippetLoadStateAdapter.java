package cse.netsys.drftclient.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import cse.netsys.drftclient.R;
import cse.netsys.drftclient.databinding.LoadingStatesBinding;

public class SnippetLoadStateAdapter extends LoadStateAdapter<SnippetLoadStateAdapter.LoadStateViewHolder> {
    private View.OnClickListener mRetryCallback;

    public SnippetLoadStateAdapter(View.OnClickListener retryCallback) {
        this.mRetryCallback = retryCallback;
    }

    @NonNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull LoadState loadState) {
        return new LoadStateViewHolder(viewGroup, mRetryCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull LoadStateViewHolder loadStateViewHolder, @NonNull LoadState loadState) {
        loadStateViewHolder.bind(loadState);
    }

    public class LoadStateViewHolder extends RecyclerView.ViewHolder {
        private TextView tvErrorMsg;
        private ProgressBar pbLoading;
        private Button btRetry;

        public LoadStateViewHolder(@NonNull ViewGroup viewGroup, @NonNull View.OnClickListener retryCallback) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_states, viewGroup, false));

            LoadingStatesBinding binding = LoadingStatesBinding.bind(itemView);
            tvErrorMsg = binding.tvErrorMsg;
            pbLoading = binding.pbLoading;
            btRetry = binding.btRetry;
            btRetry.setOnClickListener(retryCallback);
        }

        public void bind(LoadState loadState) {
            if(loadState instanceof LoadState.Error) {
                LoadState.Error loadStateError = (LoadState.Error)loadState;
                tvErrorMsg.setText(loadStateError.getError().getLocalizedMessage());
            }
            tvErrorMsg.setVisibility((loadState instanceof LoadState.Error)? View.VISIBLE: View.GONE);
            pbLoading.setVisibility((loadState instanceof LoadState.Loading)? View.VISIBLE: View.GONE);
            btRetry.setVisibility((loadState instanceof LoadState.Error)? View.VISIBLE: View.GONE);
        }
    }
}
