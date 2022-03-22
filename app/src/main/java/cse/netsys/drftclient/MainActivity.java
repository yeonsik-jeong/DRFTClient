package cse.netsys.drftclient;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cse.netsys.drftclient.api.DRFTAPIService;
import cse.netsys.drftclient.model.ObservableToken;
import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.model.Snippets;
import cse.netsys.drftclient.util.APIClient;
import cse.netsys.drftclient.util.APIServiceGenerator;
import cse.netsys.drftclient.util.SnippetAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
//    public static final String TAG = "DRFTClient";
//    static final String API_BASE_URL = "http://10.0.2.2:8000/";  // Not 127.0.0.1
//    static final Snippet TMP_SNIPPET = new Snippet("Hello World in Android", "public class MainActivity extends AppCompatActivity {}", false, "java", "friendly");

//    private static String mToken = null;
/*
    private static ObservableToken mToken = new ObservableToken();
    private static String mCurrentUsername = null;
*/

//    SnippetAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mApiService = APIClient.getAPIClient(BASE_URL);  // Older version
        doListSnippets();

        List<Snippet> snippetList = new ArrayList<>();
        RecyclerView rvSnippets = findViewById(R.id.rvSnippets);

        rvSnippets.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvSnippets.addItemDecoration(itemDecoration);

        mAdapter = new SnippetAdapter(snippetList);
        rvSnippets.setAdapter(mAdapter);

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
    }

/*    public static ObservableToken getToken() {
        return mToken;
    }

*//*    public static void setToken(ObservableToken token) {
        MainActivity.mToken = token;
    }*//*

    public static String getCurrentUsername() {
        return mCurrentUsername;
    }

    public static void setCurrentUsername(String currentUsername) {
        MainActivity.mCurrentUsername = currentUsername;
    }*/

    public void doListSnippets() {
        DRFTAPIService apiService = APIServiceGenerator.createService(DRFTAPIService.class, API_BASE_URL);

        Call<Snippets> call = apiService.listSnippets();
        call.enqueue(new Callback<Snippets>() {
            @Override
            public void onResponse(Call<Snippets> call, Response<Snippets> response) {
                if(response.isSuccessful()) {
                    Snippets snippets = response.body();
                    List<Snippet> snippetList = snippets.getResults();
                    for(int i=0; i<snippetList.size(); i++) {
                        Log.i(TAG,"[ID: " + snippetList.get(i).getId() + "] " + snippetList.get(i).getTitle());
                    }
                    mAdapter.setSnippetList(snippetList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Snippets> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}