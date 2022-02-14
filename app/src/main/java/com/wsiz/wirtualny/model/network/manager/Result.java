package com.wsiz.wirtualny.model.network.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class Result<T> {

    public static class Success<T> extends Result<T> {
        @NonNull
        private final T data;

        public Success(@NonNull T data) {
            this.data = data;
        }

        @NonNull
        public T getData() {
            return data;
        }
    }

    public static class Error<T> extends Result<T> {
        @Nullable
        private final Throwable error;

        public Error(@Nullable Throwable error) {
            this.error = error;
        }

        @Nullable
        public Throwable getError() {
            return error;
        }
    }

    public static <T> Success<T> success(T data) {
        return new Success<>(data);
    }

    public static <T> Error<T> error(Throwable throwable) {
        return new Error<>(throwable);
    }
}
