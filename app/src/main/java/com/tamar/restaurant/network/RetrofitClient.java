package com.tamar.restaurant.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:1360/api/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }



}
