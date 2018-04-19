package com.example.sahni.cinemato.DataClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.sahni.cinemato.APIData.ApiClient;
import com.example.sahni.cinemato.APIData.MovieResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sahni on 8/4/18.
 */
@Entity
public class MovieGenre {
    @PrimaryKey @NonNull
    public long id;
    public String name;
    public String path;
    public boolean isLiked=false;

    public interface CallBack{
        void execute();
    }
    private interface InternalCall{
        void set(int i);
    }
    @Ignore
    public void setBackdropPath(final InternalCall internalCall, final int position){
        Call<MovieResult> result=ApiClient.getInstance().getAPI().GenreMovies(id,1);
        result.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                Log.e("RESPONSE", "onResponse: "+response.code());
                Log.e("RESPONSE", "onResponse: "+response.body().movies.size());
                if(response.body().movies!=null) {
                    ArrayList<Movie> movies = new ArrayList<>(response.body().movies);
                    path = movies.get(0).backdrop_path;
                }
                internalCall.set(position);
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }
        });
    }
    public static void setPaths(final ArrayList<MovieGenre> genres, final CallBack call){
        for(int i=0;i<genres.size();i++)
            genres.get(i).setBackdropPath(new InternalCall() {
                @Override
                public void set(int i) {
                    if(i==genres.size())
                        call.execute();
                }
            },i+1);
    }
}
