package cse.netsys.drftclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {
    public static final String TAG = "DRFTClient";
    static final String API_BASE_URL = "http://10.0.2.2:8000/";  // Not 127.0.0.1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_base, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menuLogin:
                Intent sIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(sIntent);
                return true;
            case R.id.menuLogout:
//                mApiService = APIServiceGenerator.createService(DRFTAPIService.class, API_BASE_URL);
//                mApiService = null;
                doLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void doLogout() { }
}