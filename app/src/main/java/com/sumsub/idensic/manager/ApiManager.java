package com.sumsub.idensic.manager;

import com.sumsub.idensic.BuildConfig;
import com.sumsub.idensic.model.AccessTokenResponse;
import com.sumsub.idensic.model.ApplicantRequest;
import com.sumsub.idensic.model.ApplicantResponse;
import com.sumsub.idensic.model.DocSet;
import com.sumsub.idensic.model.DocSets;
import com.sumsub.idensic.model.InfoAttribute;
import com.sumsub.idensic.model.PayloadResponse;
import com.sumsub.idensic.network.ApiService;

import java.util.List;

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

    public Call<AccessTokenResponse> getAccessToken(String token, String applicantId, String userId) {
        return service.getAccessToken("Bearer " + token, applicantId, userId, Integer.MAX_VALUE);
    }

    public Call<ApplicantResponse> getApplicantId(String token, List<DocSet> docs, String userId) {
        ApplicantRequest request = new ApplicantRequest(new DocSets(docs), userId, null, null, null, null, new InfoAttribute());
        return service.getApplicantId("Bearer " + token, request);
    }
}
