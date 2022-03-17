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

import java.util.ArrayList;
import java.util.List;

import cse.netsys.drftclient.api.DRFTAPIService;
import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.model.Snippets;
import cse.netsys.drftclient.util.APIClient;
import cse.netsys.drftclient.util.APIServiceGenerator;
import cse.netsys.drftclient.util.SnippetAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "DRFTClient";
    static final String API_BASE_URL = "http://10.0.2.2:8000/";  // Not 127.0.0.1
    static final Snippet TMP_SNIPPET = new Snippet("Hello World in Android", "public class MainActivity extends AppCompatActivity {}", false, "java", "friendly");

    private static String mToken = null;
    SnippetAdapter mAdapter;

    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Intent rIntent = result.getData();
                    setToken(rIntent.getStringExtra("token"));
//                    mApiService = APIServiceGenerator.createService(DRFTAPIService.class, API_BASE_URL, mToken);
//                    doCreateSnippet(TMP_SNIPPET);
                }
            }
        }
    );

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
                Snippet snippet = mAdapter.getSnippetList().get(position);
//                Toast.makeText(getApplicationContext(), "[ID: " + snippet.getId() + "] " + snippet.getTitle(), Toast.LENGTH_LONG).show();
                Intent sIntent = new Intent(MainActivity.this, SnippetDetailActivity.class);
                sIntent.putExtra("Snippet", snippet);
                startActivity(sIntent);
            }
        });

        Button btCreate = findViewById(R.id.btCreate);
        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mToken == null) {
                    Intent sIntent = new Intent(MainActivity.this, LoginActivity.class);
                    mActivityResultLauncher.launch(sIntent);
                } else {
                    Intent sIntent = new Intent(MainActivity.this, SnippetCreateActivity.class);
                    startActivity(sIntent);
                }
            }
        });
    }

    public static String getToken() {
        return mToken;
    }

    public static void setToken(String token) {
        MainActivity.mToken = token;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menuLogin:
                Intent sIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(sIntent);
                return true;
            case R.id.menuLogout:
//                mApiService = APIServiceGenerator.createService(DRFTAPIService.class, API_BASE_URL);
//                mApiService = null;
                mToken = null;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void doListSnippets() {
        DRFTAPIService apiService = APIServiceGenerator.createService(DRFTAPIService.class, MainActivity.API_BASE_URL);

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