package rango.tool.androidtool.http.lib.call;


import androidx.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public final class CallAdapterFactory extends CallAdapter.Factory {

    private CallAdapterFactory() {
    }

    public static CallAdapterFactory getInstance() {
        return ClassHolder.INSTANCE;
    }

    private static class ClassHolder {
        private static final CallAdapterFactory INSTANCE = new CallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        if (getRawType(returnType) != Callable.class) {
            return null;
        }

        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("return type must be parameterized as Callable<Foo> or Callable<? extends Foo>");
        }

        final Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);

        return new CallAdapter<Object, Callable<?>>() {
            @Override
            public Type responseType() {
                return responseType;
            }

            @Override
            public Callable<Object> adapt(@NonNull Call<Object> call) {
                return new RealCall<>(retrofit.callbackExecutor(), call);
            }
        };
    }
}
