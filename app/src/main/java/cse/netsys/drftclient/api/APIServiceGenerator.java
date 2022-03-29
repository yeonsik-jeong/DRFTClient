package cse.netsys.drftclient.api;

import android.text.TextUtils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIServiceGenerator {
    public static <S> S createService(Class<S> serviceClass, String apiBaseUrl) {
        return createService(serviceClass, apiBaseUrl, null);
    }

    public static <S> S createService(Class<S> serviceClass, String apiBaseUrl, final String token) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        if(!TextUtils.isEmpty(token)) {
            AuthenticationInterceptor tokenAuthenticationInterceptor = new AuthenticationInterceptor("Token " + token);
            httpClientBuilder.addInterceptor(tokenAuthenticationInterceptor);
        }

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(loggingInterceptor);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                                            .baseUrl(apiBaseUrl)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                                            .client(httpClientBuilder.build());

        return retrofitBuilder.build().create(serviceClass);
    }
}
