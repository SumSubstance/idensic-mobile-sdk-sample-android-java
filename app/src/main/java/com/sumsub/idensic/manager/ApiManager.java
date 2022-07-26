package com.sumsub.idensic.manager;

import com.sumsub.idensic.BuildConfig;
import com.sumsub.idensic.model.AccessTokenResponse;
import com.sumsub.idensic.model.FlowItem;
import com.sumsub.idensic.model.FlowListResponse;
import com.sumsub.idensic.model.LevelListResponse;
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

    public ApiManager(String apiUrl, ISandboxProvider sandboxProvider, IClientIdProvider clientIdProvider) {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new DemoHeadersInterceptor(sandboxProvider, clientIdProvider))
                .addInterceptor(logInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(ApiService.class);
    }

    public Call<PayloadResponse> login(String username, String password) {
        return service.login(Credentials.basic(username, password), Integer.MAX_VALUE);
    }

    public Call<AccessTokenResponse> getAccessTokenForLevel(String token, String userId, String levelName) {
        return service.getAccessToken("Bearer " + token, levelName, userId, null, Integer.MAX_VALUE);
    }

    public Call<AccessTokenResponse> getAccessTokenForAction(String token, String userId, String actionId, String levelName) {
        return service.getAccessToken("Bearer " + token, levelName, userId, actionId, Integer.MAX_VALUE);
    }

    public Call<FlowListResponse> getFlows(String authorizationToken) {
        return service.getFlows("Bearer " + authorizationToken);
    }

    public Call<FlowItem> getFlow(String authorizationToken, String flowId) {
        return service.getFlow("Bearer " + authorizationToken, flowId); }

    public Call<LevelListResponse> getLevels(String authorizationToken) {
        return service.getLevels("Bearer " + authorizationToken);
    }

}
