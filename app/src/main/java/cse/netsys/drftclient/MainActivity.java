package cse.netsys.drftclient;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cse.netsys.drftclient.model.ObservableToken;
import cse.netsys.drftclient.util.SnippetAdapter;
import cse.netsys.drftclient.util.SnippetComparator;
import cse.netsys.drftclient.paging.SnippetViewModel;
import cse.netsys.drftclient.util.SnippetLoadStateAdapter;

public class MainActivity extends BaseActivity {
//    private static String mToken = null;
    private static ObservableToken mToken = new ObservableToken();
    private static String mCurrentUsername = null;
    private static SnippetAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mApiService = APIClient.getAPIClient(BASE_URL);  // Older version
//        doListSnippets();

//        List<Snippet> snippetList = new ArrayList<>();

        RecyclerView rvSnippets = findViewById(R.id.rvSnippets);
        rvSnippets.setLayoutManager(new LinearLayoutManager(this));
//        rvSnippets.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvSnippets.addItemDecoration(itemDecoration);

        mAdapter = new SnippetAdapter(new SnippetComparator());
        rvSnippets.setAdapter(mAdapter.withLoadStateFooter(
                new SnippetLoadStateAdapter(v -> mAdapter.retry())));

        mAdapter.setOnItemClickListener(new SnippetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
//                Snippet snippet = mAdapter.getSnippetList().get(position);
//                Toast.makeText(getApplicationContext(), "[ID: " + snippet.getId() + "] " + snippet.getTitle(), Toast.LENGTH_LONG).show();
                Intent sIntent = new Intent(MainActivity.this, SnippetDetailActivity.class);
//                sIntent.putExtra("Snippet", snippet);
                sIntent.putExtra("position", position);
                startActivity(sIntent);
            }
        });

        FloatingActionButton fabCreate = findViewById(R.id.fabCreate);
        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sIntent = new Intent(MainActivity.this, SnippetCreateActivity.class);
                startActivity(sIntent);
            }
        });

        mToken.setOnStringChangeListener(new ObservableToken.OnStringChangeListener() {
            @Override
            public void onStringChanged(String newValue) {
                Log.i(TAG, "MainActivity: onStringChanged() called");
                Log.i(TAG, "MainActivity: mToken.get(): " + mToken.get() + ", mCurrentUsername: " + mCurrentUsername);
                invalidateOptionsMenu();
            }
        });

        doListSnippets();
//        Log.i(TAG, "MainActivity: mToken: " + mToken);
    }

    public static ObservableToken getToken() {
        return mToken;
    }

    public static String getCurrentUsername() {
        return mCurrentUsername;
    }

    public static void setCurrentUsername(String currentUsername) {
        MainActivity.mCurrentUsername = currentUsername;
    }

    public static SnippetAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        return super.onPrepareOptionsMenu(menu);
        Log.i(TAG, this.getClass().getSimpleName() + ": onPrepareOptionsMenu() called");
        if(mToken.get() == null) {
            menu.findItem(R.id.menuLogin).setVisible(true);
            menu.findItem(R.id.menuLogout).setVisible(false);
        } else {
            Log.i(TAG, this.getClass().getSimpleName() + ": onPrepareOptionsMenu(): mToken not null");
            menu.findItem(R.id.menuLogin).setVisible(false);
            menu.findItem(R.id.menuLogout).setVisible(true);
        }

        return true;
    }

    public void doListSnippets() {
        SnippetViewModel snippetViewModel = new ViewModelProvider(this).get(SnippetViewModel.class);
        snippetViewModel.getPagingDataFlowable()
//            .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe(snippetPagingData -> mAdapter.submitData(getLifecycle(), snippetPagingData), throwable -> Log.d(BaseActivity.TAG, throwable.getMessage()));


/*        DRFTAPIService apiService = APIServiceGenerator.createService(DRFTAPIService.class, API_BASE_URL);

        Call<Snippets> call = apiService.listSnippets();
//        Call<Snippets> call = apiService.listPagedSnippets(2);
        call.enqueue(new Callback<Snippets>() {
            @Override
            public void onResponse(Call<Snippets> call, Response<SnippetResp> response) {
                if(response.isSuccessful()) {
                    SnippetResp snippetResp = response.body();
                    List<Snippet> snippetList = snippetResp.getResults();
                    for(int i=0; i<snippetList.size(); i++) {
                        Log.i(TAG,"[ID: " + snippetList.get(i).getId() + "] " + snippetList.get(i).getTitle());
                    }
//                    mAdapter.submitList(snippetList);
//                    mAdapter.setSnippetList(snippetList);
//                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SnippetResp> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/
    }

    @Override
    public void doLogout() {
        Log.i(TAG, "MainActivity: doLogout()");
        setCurrentUsername(null);
        mToken.set(null);
        SnippetCreateActivity.getToken().set(null);
        SnippetDetailActivity.getToken().set(null);
    }
}