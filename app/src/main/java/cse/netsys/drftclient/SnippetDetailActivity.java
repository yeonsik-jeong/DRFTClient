package cse.netsys.drftclient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cse.netsys.drftclient.api.DRFTAPIService;
import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.util.APIServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Path;

public class SnippetDetailActivity extends AppCompatActivity {
    private String mToken = null;

    TextView mTvURL;
    TextView mTvID;
    TextView mTvOwner;
    TextView mTvHighlight;
    EditText mEtTitle;
    EditText mEtCode;
    CheckBox mCbLinenos;
    EditText mEtLanguage;
    EditText mEtStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snippetdetail);

        Snippet snippet = getIntent().getParcelableExtra("Snippet");
        mToken = MainActivity.getToken();

        mTvURL = findViewById(R.id.tvURL);
        mTvID = findViewById(R.id.tvID);
        mTvOwner = findViewById(R.id.tvOwner);
        mTvHighlight = findViewById(R.id.tvHighlight);
        mEtTitle = findViewById(R.id.etTitle);
        mEtCode = findViewById(R.id.etCode);
        mCbLinenos = findViewById(R.id.cbLinenos);
        mEtLanguage = findViewById(R.id.etLanguage);
        mEtStyle = findViewById(R.id.etStyle);

        showDetailSnippet(snippet);

        Button btModify = findViewById(R.id.btModify);
        btModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snippet modifiedSnippet = new Snippet(
                                            mEtTitle.getText().toString(),
                                            mEtCode.getText().toString(),
                                            mCbLinenos.isChecked(),
                                            mEtLanguage.getText().toString(),
                                            mEtStyle.getText().toString());
                doUpdateSnippet(modifiedSnippet);
            }
        });

        Button btDelete = findViewById(R.id.btDelete);
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void showDetailSnippet(Snippet snippet) {
        mTvURL.setText(snippet.getUrl());
        mTvID.setText(Integer.toString(snippet.getId()));
        mTvOwner.setText(snippet.getOwner());
        mTvHighlight.setText(snippet.getHighlight());
        mEtTitle.setText(snippet.getTitle());
        mEtCode.setText(snippet.getCode());
        mCbLinenos.setChecked(snippet.isLinenos());
        mEtLanguage.setText(snippet.getLanguage());
        mEtStyle.setText(snippet.getStyle());
    }

    public void doUpdateSnippet(Snippet snippet) {
        DRFTAPIService apiService = APIServiceGenerator.createService(DRFTAPIService.class, MainActivity.API_BASE_URL, mToken);

        Call<Snippet> call = apiService.updateSnippet(snippet.getId(), snippet);
        call.enqueue(new Callback<Snippet>() {
            @Override
            public void onResponse(Call<Snippet> call, Response<Snippet> response) {

            }

            @Override
            public void onFailure(Call<Snippet> call, Throwable t) {

            }
        });
    }

    public void doDeleteSnippet() {

    }

    public void doHighlightSnippet() {

    }
}
