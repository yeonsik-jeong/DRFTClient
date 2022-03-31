package cse.netsys.drftclient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cse.netsys.drftclient.api.DRFTAPIService;
import cse.netsys.drftclient.model.ObservableToken;
import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.api.APIServiceGenerator;
import cse.netsys.drftclient.util.SnippetAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SnippetDetailActivity extends BaseActivity {
//    private String mToken = null;
//    private ObservableToken mToken = null;
//    private String mCurrentUsername = null;
    private static ObservableToken mToken = new ObservableToken();
    private SnippetAdapter mAdapter;

    private TextView mTvURL;
    private TextView mTvID;
    private TextView mTvOwner;
    private TextView mTvHighlight;
    private EditText mEtTitle;
    private EditText mEtCode;
    private CheckBox mCbLinenos;
    private EditText mEtLanguage;
    private EditText mEtStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snippetdetail);

        mAdapter = MainActivity.getAdapter();

//        Snippet snippet = getIntent().getParcelableExtra("Snippet");
        int position = getIntent().getIntExtra("position", 0);
        Snippet snippet = mAdapter.getSnippet(position);
        doDetailSnippet(snippet.getId(), position);  // Unchangeable element

        mTvURL = findViewById(R.id.tvURL);
        mTvID = findViewById(R.id.tvID);
        mTvOwner = findViewById(R.id.tvOwner);
        mTvHighlight = findViewById(R.id.tvHighlight);
        mEtTitle = findViewById(R.id.etTitle);
        mEtCode = findViewById(R.id.etCode);
        mCbLinenos = findViewById(R.id.cbLinenos);
        mEtLanguage = findViewById(R.id.etLanguage);
        mEtStyle = findViewById(R.id.etStyle);

//        showDetailSnippet(snippet);

        mTvHighlight.setTextColor(Color.BLUE);
        mTvHighlight.setMovementMethod(LinkMovementMethod.getInstance());
        mTvHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // doHighlightSnippet()
                Intent sIntent = new Intent(SnippetDetailActivity.this, SnippetHighlightActivity.class);
                sIntent.putExtra("highlightURL", snippet.getHighlight());
                startActivity(sIntent);
            }
        });

        Button btUpdate = findViewById(R.id.btUpdate);
        Button btDelete = findViewById(R.id.btDelete);

//        mToken = MainActivity.getToken();
//        mCurrentUsername = MainActivity.getCurrentUsername();
//        Toast.makeText(getApplicationContext(), "mCurrentUsername: " + mCurrentUsername + "mToken.get(): " + mToken.get(), Toast.LENGTH_LONG).show();
        if(mToken.get() == null || !MainActivity.getCurrentUsername().equals(snippet.getOwner())) {
            btUpdate.setEnabled(false);
            btDelete.setEnabled(false);
        }

        mToken.setOnStringChangeListener(new ObservableToken.OnStringChangeListener() {
            @Override
            public void onStringChanged(String newValue) {
                Log.i(TAG, "SnippetDetailActivity: onStringChanged() called");
                Log.i(TAG, "SnippetDetailActivity: mToken.get(): " + mToken.get() + ", mCurrentUsername: " + MainActivity.getCurrentUsername() + ", owner: " + snippet.getOwner());
                invalidateOptionsMenu();
                if(mToken.get() != null && MainActivity.getCurrentUsername().equals(snippet.getOwner())) {
                    btUpdate.setEnabled(true);
                    btDelete.setEnabled(true);
                } else {
                    btUpdate.setEnabled(false);
                    btDelete.setEnabled(false);
                }
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snippet modifiedSnippet = new Snippet(
                                            mEtTitle.getText().toString(),
                                            mEtCode.getText().toString(),
                                            mCbLinenos.isChecked(),
                                            mEtLanguage.getText().toString(),
                                            mEtStyle.getText().toString());
                doUpdateSnippet(snippet.getId(), modifiedSnippet, position);
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDeleteSnippet(snippet.getId(), position);
            }
        });

//        Log.i(TAG, "SnippetDetailActivity: mToken: " + mToken);
    }

    public static ObservableToken getToken() {
        return mToken;
    }

    public static void setToken(ObservableToken token) {
        SnippetDetailActivity.mToken = token;
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

    public void showDetailSnippet(Snippet snippet) {
        mTvURL.setText(snippet.getUrl());
        mTvID.setText(Integer.toString(snippet.getId()));
        mTvOwner.setText(snippet.getOwner());
        mTvHighlight.setText(snippet.getHighlight());
//        Linkify.addLinks(mTvHighlight, Linkify.WEB_URLS);  // Works well
        mEtTitle.setText(snippet.getTitle());
        mEtCode.setText(snippet.getCode());
        mCbLinenos.setChecked(snippet.isLinenos());
        mEtLanguage.setText(snippet.getLanguage());
        mEtStyle.setText(snippet.getStyle());
    }

    public void doDetailSnippet(int snippetId, int position) {
        DRFTAPIService apiService = APIServiceGenerator.createService(DRFTAPIService.class, API_BASE_URL);

        Call<Snippet> call = apiService.detailSnippet(snippetId);
        call.enqueue(new Callback<Snippet>() {
            @Override
            public void onResponse(Call<Snippet> call, Response<Snippet> response) {
                if(response.isSuccessful()) {
                    Snippet snippet = response.body();
                    showDetailSnippet(snippet);
//                    mAdapter.snapshot().set(position, snippet);
//                    mAdapter.updateItem(position, snippet);  // Comments to reduce the amount of network req/resp traffic
//                    List<Snippet> snippetList = mAdapter.getSnippetList();
//                    snippetList.set(position, snippet);
//                    mAdapter.setSnippetList(snippetList);
//                    mAdapter.notifyItemChanged(position);
                    Toast.makeText(getApplicationContext(), "Detail successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Snippet> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void doUpdateSnippet(int snippetId, Snippet modifiedSnippet, int position) {
        DRFTAPIService apiService = APIServiceGenerator.createService(DRFTAPIService.class, API_BASE_URL, mToken.get());

        Call<Snippet> call = apiService.updateSnippet(snippetId, modifiedSnippet);
        call.enqueue(new Callback<Snippet>() {
            @Override
            public void onResponse(Call<Snippet> call, Response<Snippet> response) {
                if(response.isSuccessful()) {
                    Snippet snippet = response.body();
//                    showDetailSnippet(snippet);
//                    mAdapter.snapshot().set(position, snippet);
                    mAdapter.updateItem(position, snippet);
//                    List<Snippet> snippetList = mAdapter.getSnippetList();
//                   snippetList.set(position, snippet);
///                    mAdapter.setSnippetList(snippetList);
//                    mAdapter.getSnippetList().set(position, snippet);
//                    mAdapter.notifyItemChanged(position);
                    Toast.makeText(getApplicationContext(), "Modify successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Snippet> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void doDeleteSnippet(int snippetId, int position) {
        DRFTAPIService apiService = APIServiceGenerator.createService(DRFTAPIService.class, API_BASE_URL, mToken.get());

        Call<Void> call = apiService.deleteSnippet(snippetId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    mAdapter.removeItem(position);
//                    List<Snippet> snippetList = mAdapter.getSnippetList();
//                    snippetList.remove(position);
//                    mAdapter.setSnippetList(snippetList);
//                    mAdapter.notifyItemRemoved(position);
                    Toast.makeText(getApplicationContext(), "Delete successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void doLogout() {
        Log.i(TAG, "SnippetDetailActivity: doLogout()");
        MainActivity.setCurrentUsername(null);
        mToken.set(null);
        MainActivity.getToken().set(null);
        SnippetCreateActivity.getToken().set(null);
    }
}
