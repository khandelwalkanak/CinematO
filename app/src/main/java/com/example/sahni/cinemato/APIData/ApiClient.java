package com.example.sahni.cinemato.APIData;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sahni on 25/3/18.
 */

public class ApiClient {
    private static ApiClient INSTANCE;
    private GetData API;
    private ApiClient()
    {
        Retrofit retrofit=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.themoviedb.org/3/")
                .build();
        API =retrofit.create(GetData.class);
    }
    public static ApiClient getInstance()
    {
        if(INSTANCE==null)
            INSTANCE=new ApiClient();
        return INSTANCE;
    }
    public GetData getAPI()
    {
        return API;
    }
}
