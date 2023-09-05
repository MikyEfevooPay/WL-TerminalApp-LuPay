package com.efevoopay.demoui.utils;

import android.annotation.SuppressLint;

import java.util.concurrent.CompletableFuture;

interface resolveCallback<T> {
    void onResolve(CompletableFuture<T> resolve);
}

public class Promise {

    @SuppressLint("NewApi")
    public static <T> CompletableFuture<T> resolveAsync(CompletableFuture<T> resolve, resolveCallback<T> callback) {
        new Thread(() -> {
            try {
                callback.onResolve(resolve);
            } catch (Exception e) {
                resolve.completeExceptionally(e);
            }
        }).start();
        return resolve;
    }
}
