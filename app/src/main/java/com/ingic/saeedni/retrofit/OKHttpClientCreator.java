package com.ingic.saeedni.retrofit;


import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OKHttpClientCreator {

    final static ProgressResponseBody.ProgressListener progressListener = new ProgressResponseBody.ProgressListener() {
        @Override
        public void update(long bytesRead, long contentLength, boolean done) {
//            int percent = (int) ((100 * bytesRead) / contentLength);
        }
    };
    private static NotificationManager mNotifyManager;
    private static NotificationCompat.Builder mBuilder;


    public static OkHttpClient createCustomInterceptorClient(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Accept-Encoding", "identity").build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .addNetworkInterceptor(new CustomInterceptor(progressListener))
                .addInterceptor(interceptor)
                .build();


        return client;


    }
    public static OkHttpClient createDefaultInterceptorClient(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }
                })
                .addInterceptor(interceptor)
                .build();


        return client;

    }

}
