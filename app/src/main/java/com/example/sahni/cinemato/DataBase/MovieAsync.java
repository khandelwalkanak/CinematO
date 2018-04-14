package com.example.sahni.cinemato.DataBase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sahni.cinemato.DataClasses.Movie;
import com.example.sahni.cinemato.DataClasses.NowPlayingMovies;
import com.example.sahni.cinemato.DataClasses.PopularMovies;
import com.example.sahni.cinemato.DataClasses.TopRatedMovies;
import com.example.sahni.cinemato.DataClasses.UpcomingMovies;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahni on 12/4/18.
 */

public class MovieAsync {
    private static DatabaseClient client;
    public static void setClient(Context context){
        client=DatabaseClient.getInstance(context);
    }

    //MOVIE
    public static void InsertMovie(ArrayList<Movie> movies){
        new AsyncTask<ArrayList<Movie>,Void,Void>(){

            @Override
            protected Void doInBackground(ArrayList<Movie>[] arrayLists) {
                client.data().insertMovies(arrayLists[0]);
                return null;
            }

        }.execute(movies);
    }
    public static Movie GetMovie(long id){
        final Movie[] m = new Movie[1];
        new AsyncTask<Long,Void,Movie>(){

            @Override
            protected Movie doInBackground(Long... longs) {
                Movie movie=client.data().selectedMovie(longs[0]);
                return movie;
            }

            @Override
            protected void onPostExecute(Movie movie) {
                m[0] =movie;
            }
        }.execute(id);
        Log.e("TITLE", "GetMovie: "+m[0].title );
        return m[0];
    }

    //UPCOMING
    public static void InsertUpcoming(UpcomingMovies movies){
        new AsyncTask<UpcomingMovies, Void, Void>() {
            @Override
            protected Void doInBackground(UpcomingMovies... movies) {
                client.data().insertUpcoming(movies[0]);
                return null;
            }
        }.execute(movies);
        GetUpcoming();
    }
    public static ArrayList<Long> GetUpcoming(){
        final ArrayList<Long> ids=new ArrayList<>();
        new AsyncTask<Void, Void, List<Long>>() {
            @Override
            protected List<Long> doInBackground(Void... voids) {
                return client.data().getUpcoming();
            }

            @Override
            protected void onPostExecute(List<Long> longs) {
                ids.addAll(longs);
            }
        }.execute();
        Log.e("SIZE", "GetUpcoming: "+ids.size() );
        return ids;
    }
    public static void DeleteAllUpcoming(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                client.data().deleteAllUpcomingMovies();
                return null;
            }
        }.execute();
    }

    //POPULAR
    public static void InsertPopular(PopularMovies movies){
        new AsyncTask<PopularMovies, Void, Void>() {
            @Override
            protected Void doInBackground(PopularMovies... movies) {
                client.data().insertPopular(movies[0]);
                return null;
            }
        }.execute(movies);
    }
    public static ArrayList<Long> GetPopular(){
        final ArrayList<Long> ids=new ArrayList<>();
        new AsyncTask<Void, Void, List<Long>>() {
            @Override
            protected List<Long> doInBackground(Void... voids) {
                return client.data().getPopular();
            }

            @Override
            protected void onPostExecute(List<Long> longs) {
                ids.addAll(longs);
            }
        }.execute();
        return ids;
    }
    public static void DeleteAllPopular(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                client.data().deleteAllPopularMovies();
                return null;
            }
        }.execute();
    }

    //TOPRATED
    public static void InsertTop(TopRatedMovies movies){
        new AsyncTask<TopRatedMovies, Void, Void>() {
            @Override
            protected Void doInBackground(TopRatedMovies... movies) {
                client.data().insertTop(movies[0]);
                return null;
            }
        }.execute(movies);
    }
    public static ArrayList<Long> GetTop(){
        final ArrayList<Long> ids=new ArrayList<>();
        AsyncTaskWithResponse.ResultSet<List<Long>> resultSet= new AsyncTaskWithResponse.ResultSet<List<Long>>() {
            @Override
            public void onResult(List<Long> longs) {
                ids.addAll(longs);
            }
        };
        new AsyncTaskWithResponse<Void, Void, List<Long>>(resultSet) {
            @Override
            protected List<Long> doInBackground(Void... voids) {
                return client.data().getTopRated();
            }

            @Override
            protected void onPostExecute(List<Long> longs) {

                resultSet.onResult(longs);
            }
        }.execute();
        return ids;
    }
    public static void DeleteAllTop(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                client.data().deleteAllTopRated();
                return null;
            }
        }.execute();
    }

    //NOWPLAYING
    public static void InsertNow(NowPlayingMovies movies){
        new AsyncTask<NowPlayingMovies, Void, Void>() {
            @Override
            protected Void doInBackground(NowPlayingMovies... movies) {
                client.data().insertNow(movies[0]);
                return null;
            }
        }.execute(movies);
    }
    public static ArrayList<Long> GetNow(){
        final ArrayList<Long> ids=new ArrayList<>();
        new AsyncTask<Void, Void, List<Long>>() {
            @Override
            protected List<Long> doInBackground(Void... voids) {
                return client.data().getNow();
            }

            @Override
            protected void onPostExecute(List<Long> longs) {
                ids.addAll(longs);
            }
        }.execute();
        return ids;
    }
    public static void DeleteAllNow(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                client.data().deleteAllNowPlaying();
                return null;
            }
        }.execute();
    }
}
