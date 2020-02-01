package com.anime.cloud;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HttpConnection {

    public HttpConnection(){}

    public Retrofit connect(String url){
        OkHttpClient.Builder httpConn = new OkHttpClient.Builder();
        Retrofit connection = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpConn.build())
                .build();
        return connection;
    }
}
