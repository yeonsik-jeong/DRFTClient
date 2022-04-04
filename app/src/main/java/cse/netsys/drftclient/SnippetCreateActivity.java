package cse.netsys.drftclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import cse.netsys.drftclient.api.DRFTAPIService;
import cse.netsys.drftclient.model.ObservableToken;
import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.api.APIServiceGenerator;
import cse.netsys.drftclient.util.SnippetAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SnippetCreateActivity extends BaseActivity {
//    private String mToken = null;
    private static ObservableToken mToken = new ObservableToken();
    private SnippetAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snippetcreate);

        mAdapter = MainActivity.getAdapter();

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etCode = findViewById(R.id.etCode);
        CheckBox cbLinenos = findViewById(R.id.cbLinenos);
        EditText etLanguage = findViewById(R.id.etLanguage);
        EditText etStyle = findViewById(R.id.etStyle);
        Button btCreate = findViewById(R.id.btCreate);

        etLanguage.setText("java");
        etStyle.setText("friendly");

//        mToken = MainActivity.getToken();
        if(mToken.get() == null) {
            btCreate.setEnabled(false);
            Intent sIntent = new Intent(SnippetCreateActivity.this, LoginActivity.class);
            startActivity(sIntent);
        }

        mToken.setOnStringChangeListener(new ObservableToken.OnStringChangeListener() {
            @Override
            public void onStringChanged(String newValue) {
                Log.i(TAG, "SnippetCreateActivity: onStringChanged() called");
                Log.i(TAG, "SnippetCreateActivity: mToken.get(): " + mToken.get() + ", mCurrentUsername: " + MainActivity.getCurrentUsername());
                invalidateOptionsMenu();
                if(mToken.get() != null) {
                    btCreate.setEnabled(true);
                } else {
                    btCreate.setEnabled(false);
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

//        Log.i(TAG, "SnippetCreateActivity: mToken: " + mToken);
    }

    public static ObservableToken getToken() {
        return mToken;
    }

    public static void setToken(ObservableToken token) {
        SnippetCreateActivity.mToken = token;
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

    public void doCreateSnippet(Snippet snippet) {
        DRFTAPIService apiService = APIServiceGenerator.createService(DRFTAPIService.class, API_BASE_URL, mToken.get());

        Call<Snippet> call = apiService.createSnippet(snippet);
        call.enqueue(new Callback<Snippet>() {
            @Override
            public void onResponse(Call<Snippet> call, Response<Snippet> response) {
                Snippet snippet = response.body();
                mAdapter.addItem(snippet, 0);
//                List<Snippet> snippetList = mAdapter.getSnippetList();
//                snippetList.add(0, snippet);
//               mAdapter.setSnippetList(snippetList);
//                mAdapter.getSnippetList().add(0, snippet);
//                mAdapter.notifyItemInserted(0);
                Toast.makeText(getApplicationContext(), "Create successful", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Snippet> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void doLogout() {
        Log.i(TAG, "SnippetCreateActivity: doLogout()");
        MainActivity.setCurrentUsername(null);
        mToken.set(null);
        MainActivity.getToken().set(null);
        SnippetDetailActivity.getToken().set(null);
    }
}