package cse.netsys.drftclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cse.netsys.drftclient.api.DRFTAPIService;
import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.util.APIServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SnippetCreateActivity extends AppCompatActivity {
    private String mToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snippetcreate);


    }

    public void doCreateSnippet(Snippet snippet) {
        DRFTAPIService apiService = APIServiceGenerator.createService(DRFTAPIService.class, MainActivity.API_BASE_URL, mToken);

        Call<Snippet> call = apiService.createSnippet(snippet);
        call.enqueue(new Callback<Snippet>() {
            @Override
            public void onResponse(Call<Snippet> call, Response<Snippet> response) {
                Log.i(MainActivity.TAG, "createSnippet() succeeds.");
            }

            @Override
            public void onFailure(Call<Snippet> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}