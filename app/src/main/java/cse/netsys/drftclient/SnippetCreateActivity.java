package cse.netsys.drftclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import cse.netsys.drftclient.api.DRFTAPIService;
import cse.netsys.drftclient.model.ObservableToken;
import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.util.APIServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SnippetCreateActivity extends BaseActivity {
//    private String mToken = null;
//    private ObservableToken mToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snippetcreate);

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etCode = findViewById(R.id.etCode);
        CheckBox cbLinenos = findViewById(R.id.cbLinenos);
        EditText etLanguage = findViewById(R.id.etLanguage);
        EditText etStyle = findViewById(R.id.etStyle);
        Button btCreate = findViewById(R.id.btCreate);

//        mToken = MainActivity.getToken();
        if(mToken.get() == null) {
            btCreate.setEnabled(false);
            Intent sIntent = new Intent(SnippetCreateActivity.this, LoginActivity.class);
            startActivity(sIntent);
        }

        mToken.setOnStringChangeListener(new ObservableToken.OnStringChangeListener() {
            @Override
            public void onStringChanged(String newValue) {
                if(mToken.get() != null) {
                    Log.i(TAG, SnippetCreateActivity.class.getSimpleName() + ": onStringChanged() called");
                    btCreate.setEnabled(true);
                }
            }
        });

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snippet snippet = new Snippet(
                        etTitle.getText().toString(),
                        etCode.getText().toString(),
                        cbLinenos.isChecked(),
                        etLanguage.getText().toString(),
                        etStyle.getText().toString());

                doCreateSnippet(snippet);
            }
        });
    }

    public void doCreateSnippet(Snippet snippet) {
        DRFTAPIService apiService = APIServiceGenerator.createService(DRFTAPIService.class, API_BASE_URL, mToken.get());

        Call<Snippet> call = apiService.createSnippet(snippet);
        call.enqueue(new Callback<Snippet>() {
            @Override
            public void onResponse(Call<Snippet> call, Response<Snippet> response) {
                Snippet snippet = response.body();
                mAdapter.getSnippetList().add(0, snippet);
                mAdapter.notifyItemInserted(0);
                Toast.makeText(getApplicationContext(), "Create successful", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Snippet> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}