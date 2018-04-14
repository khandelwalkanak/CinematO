package com.example.sahni.cinemato.APIData;

import com.example.sahni.cinemato.Constant;
import com.example.sahni.cinemato.DataClasses.MovieGenre;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.example.sahni.cinemato.Constant.*;

/**
 * Created by sahni on 25/3/18.
 */

public interface GetData {
    @GET("movie/upcoming?"+ API_KEY+"="+ key)
    Call<MovieResult> UpcomingMovies(@Query(Constant.PAGE) int page);

    @GET("movie/popular?"+ API_KEY+"="+ key)
    Call<MovieResult> PopularMovies(@Query(Constant.PAGE) int page);

    @GET("movie/now_playing?"+ API_KEY+"="+ key)
    Call<MovieResult> NowPlaying(@Query(Constant.PAGE) int page);

    @GET("movie/top_rated?"+ API_KEY+"="+ key)
    Call<MovieResult> TopRated(@Query(Constant.PAGE) int page);

    @GET("genre/{genre_id}/movies?"+ API_KEY+"="+ key)
    Call<MovieResult> GenreMovies(@Path("genre_id") long id,@Query(Constant.PAGE) int page);

    @GET("genre/movie/list?"+ API_KEY+"="+ key)
    Call<GenreResult> Genre();
}
