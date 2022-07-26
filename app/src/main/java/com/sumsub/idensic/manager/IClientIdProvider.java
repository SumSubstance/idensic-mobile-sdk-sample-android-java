package com.sumsub.idensic.manager;

import androidx.annotation.Nullable;

public interface IClientIdProvider {
    @Nullable
    String getClientID();
}