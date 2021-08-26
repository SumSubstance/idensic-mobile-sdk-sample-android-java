package com.sumsub.idensic.network;

import com.sumsub.idensic.model.AccessTokenResponse;
import com.sumsub.idensic.model.FlowItem;
import com.sumsub.idensic.model.LevelListResponse;
import com.sumsub.idensic.model.PayloadResponse;
import com.sumsub.idensic.model.FlowListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("resources/auth/login")
    Call<PayloadResponse> login(@Header("Authorization") String authorization, @Query("ttlInSecs") int ttlInSecs);

    @POST("resources/accessTokens")
    Call<AccessTokenResponse> getAccessToken(@Header("Authorization") String authorization, @Query("levelName") String levelName, @Query("userId") String userId, @Query("externalActionId") String externalActionId, @Query("ttlInSecs") int ttlInSecs);

    @GET("/resources/sdkIntegrations/flows")
    Call<FlowListResponse> getFlows(@Header("Authorization") String authorization);

    @GET("resources/applicants/-/levels")
    Call<LevelListResponse> getLevels(@Header("Authorization") String authorization);

    @GET("resources/sdkIntegrations/flows/{flowid}")
    Call<FlowItem> getFlow(@Header("Authorization")String authorization, @Path("flowid") String flowid);
}
