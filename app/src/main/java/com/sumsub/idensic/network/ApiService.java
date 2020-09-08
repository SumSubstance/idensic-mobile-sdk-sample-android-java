package com.sumsub.idensic.network;

import com.sumsub.idensic.model.AccessTokenResponse;
import com.sumsub.idensic.model.ApplicantRequest;
import com.sumsub.idensic.model.ApplicantResponse;
import com.sumsub.idensic.model.PayloadResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("resources/auth/login")
    Call<PayloadResponse> login(@Header("Authorization") String authorization, @Query("ttlInSecs") int ttlInSecs);

    @POST("resources/accessTokens")
    Call<AccessTokenResponse> getAccessToken(@Header("Authorization") String authorization, @Query("applicantId") String applicantId, @Query("userId") String userId, @Query("ttlInSecs") int ttlInSecs);

    @POST("resources/applicants")
    Call<ApplicantResponse> getApplicantId(@Header("Authorization")String authorization, @Body ApplicantRequest body);
}
