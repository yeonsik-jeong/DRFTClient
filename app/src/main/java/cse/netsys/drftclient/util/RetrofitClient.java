package cse.netsys.drftclient.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import cse.netsys.drftclient.MainActivity;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    @SuppressWarnings("SpellCheckingInspection")
    public static Retrofit getRetrofitClient(String url) {
        final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
        Gson gson = new GsonBuilder()
                     .setDateFormat(ISO_FORMAT)
                     .create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor basicAuthInterceptor = new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                                        .header("Authorization", Credentials.basic("user2", "admin!@#"))
                                        .build();
                return chain.proceed(newRequest);
            }
        };

        Interceptor tokenAuthInterceptor = new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", MainActivity.getToken())
                        .build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient httpClient = new OkHttpClient.Builder()
                                    .addInterceptor(loggingInterceptor)
//                                    .addInterceptor(basicAuthInterceptor)
//                                    .addInterceptor(tokenAuthInterceptor)
                                    .build();

        return new Retrofit.Builder()
                .client(httpClient)  // Necessary for Interceptors
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
