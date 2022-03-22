package cse.netsys.drftclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cse.netsys.drftclient.api.DRFTAPIService;
import cse.netsys.drftclient.model.LoginReq;
import cse.netsys.drftclient.model.LoginResp;
import cse.netsys.drftclient.model.SignupReq;
import cse.netsys.drftclient.model.SignupResp;
import cse.netsys.drftclient.util.APIServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);

        Button btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginReq loginReq = new LoginReq(etUsername.getText().toString(), etPassword.getText().toString());
                doLogin(loginReq);
            }
        });

        TextView tvSignup = findViewById(R.id.tvSignup);
        tvSignup.setMovementMethod(LinkMovementMethod.getInstance());
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sIntent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(sIntent);
            }
        });
    }

    public void doLogin(LoginReq loginReq) {
        DRFTAPIService apiService = APIServiceGenerator.createService(DRFTAPIService.class, API_BASE_URL);

        Call<LoginResp> call = apiService.login(loginReq);
        call.enqueue(new Callback<LoginResp>() {
            @Override
            public void onResponse(Call<LoginResp> call, Response<LoginResp> response) {
                if(response.isSuccessful()) {
                    LoginResp loginResp = response.body();
                    Log.i(TAG,loginResp.getMessage() + ": " + loginResp.getUsername() + "'s Token " + loginResp.getToken());
//                    MainActivity.setToken(loginResp.getToken());
//                    MainActivity.getToken().set(loginResp.getToken());
//                    MainActivity.setCurrentUsername(loginResp.getUsername());
                    setCurrentUsername(loginResp.getUsername());
                    mToken.set(loginResp.getToken());  // Order is important
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginResp> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}