package cse.netsys.drftclient.util;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {
    private String mAuthToken;

    public AuthenticationInterceptor(String authToken) {
        this.mAuthToken = authToken;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request newRequest = originalRequest.newBuilder()
                                .header("Authorization", mAuthToken)
                                .build();
        return chain.proceed(newRequest);
    }
}
