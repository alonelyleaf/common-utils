package com.alonelyleaf.netty.api.listener;

public interface Listener {
    
    void onSuccess(Object... args);

    void onFailure(Throwable cause);
}