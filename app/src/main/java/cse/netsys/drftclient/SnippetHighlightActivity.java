package cse.netsys.drftclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class SnippetHighlightActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snippethighlight);

        String highlightURL = getIntent().getStringExtra("highlightURL");

        WebView wvHighlight = findViewById(R.id.wvHighlight);
        wvHighlight.loadUrl(highlightURL);

        Button btClose = findViewById(R.id.btClose);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}