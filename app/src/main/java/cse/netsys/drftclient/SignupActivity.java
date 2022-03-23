package cse.netsys.drftclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cse.netsys.drftclient.api.DRFTAPIService;
import cse.netsys.drftclient.model.SignupReq;
import cse.netsys.drftclient.model.SignupResp;
import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.model.Snippets;
import cse.netsys.drftclient.util.APIServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);

        Button btSignup = findViewById(R.id.btSignup);
        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupReq signupReq = new SignupReq(etUsername.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString());
                doSignup(signupReq);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    public void doSignup(SignupReq signupReq) {
        DRFTAPIService apiService = APIServiceGenerator.createService(DRFTAPIService.class, API_BASE_URL);

        Call<SignupResp> call = apiService.signup(signupReq);
        call.enqueue(new Callback<SignupResp>() {
            @Override
            public void onResponse(Call<SignupResp> call, Response<SignupResp> response) {
                if(response.isSuccessful()) {
                    SignupResp signupResp = response.body();
                    Log.i(TAG,signupResp.getMessage() + ": " + signupResp.getUsername());
                }
            }

            @Override
            public void onFailure(Call<SignupResp> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}