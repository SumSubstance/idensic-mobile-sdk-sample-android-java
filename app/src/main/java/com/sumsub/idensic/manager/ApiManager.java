package com.sumsub.idensic.manager;

import com.sumsub.idensic.BuildConfig;
import com.sumsub.idensic.model.AccessTokenResponse;
import com.sumsub.idensic.model.FlowListResponse;
import com.sumsub.idensic.model.PayloadResponse;
import com.sumsub.idensic.network.ApiService;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private final ApiService service;

    public ApiManager() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(ApiService.class);
    }

    public Call<PayloadResponse> login(String username, String password) {
        return service.login(Credentials.basic(username, password), Integer.MAX_VALUE);
    }

    public Call<AccessTokenResponse> getAccessTokenForFlow(String token, String userId) {
        return service.getAccessToken("Bearer " + token, userId, null, Integer.MAX_VALUE);
    }

    public Call<AccessTokenResponse> getAccessTokenForAction(String token, String userId, String actionId) {
        return service.getAccessToken("Bearer " + token, userId, actionId, Integer.MAX_VALUE);
    }

    public Call<FlowListResponse> getFlows(String authorizationToken) {
        return service.getFlows("Bearer " + authorizationToken);
    }
}
