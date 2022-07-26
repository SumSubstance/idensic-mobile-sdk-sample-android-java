package com.sumsub.idensic.manager;

import androidx.annotation.NonNull;

import com.sumsub.idensic.BuildConfig;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class DemoHeadersInterceptor implements Interceptor {

    ISandboxProvider sandboxProvider;
    IClientIdProvider clientIdProvider;

    private static final String HEADER_COOKIE = "Cookie";
    private static final String HEADER_CLIENT_ID = "X-Client-Id";
    private static final String HEADER_APPLICATION = "X-Mob-App";
    private static final String HEADER_APPLICATION_VERSION = "X-Mob-App-Ver";
    private static final String HEADER_OS = "X-Mob-OS";
    private static final String HEADER_X_IMPERSONATE = "X-Impersonate";

    DemoHeadersInterceptor(ISandboxProvider sandboxProvider, IClientIdProvider clientIdProvider) {
        super();
        this.sandboxProvider = sandboxProvider;
        this.clientIdProvider = clientIdProvider;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder()
                .addHeader(HEADER_CLIENT_ID, "msdkDemo")
                .addHeader(HEADER_APPLICATION, "com.sumsub.IdensicMobileSDK-Android-Public_Demo_Java")
                .addHeader(HEADER_APPLICATION_VERSION, BuildConfig.DEMO_VERSION)
                .addHeader(HEADER_OS, "Android");

        if (sandboxProvider.isSandBox()) {
            requestBuilder.addHeader(HEADER_COOKIE, "_ss_route=sbx");
        }

        String clientId = clientIdProvider.getClientID();
        if (clientId != null && !clientId.isEmpty()) {
            requestBuilder.addHeader(HEADER_X_IMPERSONATE, URLEncoder.encode(clientId, "utf-8"));
        }

        return chain.proceed(requestBuilder.build());
    }
}
