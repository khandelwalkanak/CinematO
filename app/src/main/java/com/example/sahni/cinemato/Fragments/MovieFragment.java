package com.example.sahni.cinemato.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sahni.cinemato.APIData.ApiClient;
import com.example.sahni.cinemato.APIData.GenreResult;
import com.example.sahni.cinemato.APIData.MovieResult;
import com.example.sahni.cinemato.Activities.DisplayActivity;
import com.example.sahni.cinemato.Adapters.GenresAdapter;
import com.example.sahni.cinemato.Adapters.ItemClickCallback;
import com.example.sahni.cinemato.Adapters.MoviesAdapter;
import com.example.sahni.cinemato.Adapters.UpcomingMoviesPagerAdapter;
import com.example.sahni.cinemato.Adapters.UpdateAllLists;
import com.example.sahni.cinemato.Constant;
import com.example.sahni.cinemato.DataBase.DatabaseClient;
import com.example.sahni.cinemato.DataClasses.Movie;
import com.example.sahni.cinemato.DataClasses.MovieGenre;
import com.example.sahni.cinemato.DataClasses.MovieGenreRelationship;
import com.example.sahni.cinemato.DataClasses.NowPlayingMovies;
import com.example.sahni.cinemato.DataClasses.PopularMovies;
import com.example.sahni.cinemato.DataClasses.TopRatedMovies;
import com.example.sahni.cinemato.DataClasses.UpcomingMovies;
import com.example.sahni.cinemato.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sahni on 23/3/18.
 */

public class MovieFragment extends Fragment implements UpdateAllLists {
    View rootView;
    boolean complete=true;
    //Genre
    RecyclerView Genres;
    ArrayList<MovieGenre> genres;
    GenresAdapter genresAdapter;

    //UpcomingMovies
    ViewPager UpcomingMovies;
    ArrayList<Movie> upcoming;
    UpcomingMoviesPagerAdapter upcomingAdapter;

    //PopularMovies
    RecyclerView PopularMovies;
    ArrayList<Movie> popular;
    MoviesAdapter popularAdapter;

    //NowPlayingMovies
    RecyclerView NowPlaying;
    ArrayList<Movie> now;
    MoviesAdapter nowAdapter;

    //TopRatedMovies
    RecyclerView TopRated;
    ArrayList<Movie> top;
    MoviesAdapter topAdapter;
    //Database
    DatabaseClient client;
    //CallBacks
    private StartActivityCallBack callback;
    private boolean canEstablish=false;

    public MovieFragment() {

    }
    public static MovieFragment newInstance(StartActivityCallBack callback)
    {
        MovieFragment fragment=new MovieFragment();
        fragment.callback=callback;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_movies,container,false);
        final SwipeRefreshLayout scrollView=rootView.findViewById(R.id.scrollView);
        scrollView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollView.setRefreshing(true);
                if(!complete) {
                    complete=true;
                    setGenre();
                    setUpcoming();
                    setPopular();
                    setNow();
                    setTop();
                }
                scrollView.setRefreshing(false);
            }
        });
        client=DatabaseClient.getInstance(getActivity());
        setGenre();
        setUpcoming();
        setPopular();
        setNow();
        setTop();

        return rootView;
    }

    private void setGenre() {
        Genres=rootView.findViewById(R.id.Genres);
        genres= new ArrayList<>();

        genres.addAll(client.data().getGenre());
        genresAdapter=new GenresAdapter(getActivity(), genres, new ItemClickCallback() {
            @Override
            public void OnClick(long id) {
                Bundle bundle=new Bundle();
                bundle.putInt(Constant.TYPE_KEY,Constant.LIST);
                bundle.putInt(Constant.LIST_TYPE,Constant.GENRE_LIST);
                bundle.putInt(Constant.MOVIE_TV_GENRE,Constant.MOVIE);
                bundle.putLong(Constant.GENRE_ID,id);
                callback.start(DisplayActivity.class,bundle);
            }
        },R.layout.genre_item);
        Genres.setAdapter(genresAdapter);
        Genres.setLayoutManager(new GridLayoutManager(getActivity(),2,LinearLayoutManager.HORIZONTAL,false));

        Call<GenreResult> resultCall=ApiClient.getInstance().getAPI().Genre();
        resultCall.enqueue(new Callback<GenreResult>() {
            @Override
            public void onResponse(Call<GenreResult> call, Response<GenreResult> response) {
                genres.clear();
                genres.addAll(response.body().genres);
                MovieGenre.setPaths(genres, new MovieGenre.CallBack() {
                    @Override
                    public void execute() {
                        genresAdapter.notifyDataSetChanged();
                        client.data().insertGenre(genres);
                        EstablishRelations();
                    }
                });
            }

            @Override
            public void onFailure(Call<GenreResult> call, Throwable t) {
                complete=false;
            }
        });
    }

    private void EstablishRelations() {
        //Top
        for(int i=0;i<top.size();i++)
        {
            client.data().insertTop(new TopRatedMovies(i+1,top.get(i).id));
            for(int j=0;j<top.get(i).genre_ids.size();j++){
                long genreId=top.get(i).genre_ids.get(j);
                long movieId=top.get(i).id;
                if(client.data().getRelation(genreId,movieId).size()==0){
                    client.data().InsertMovieGenreRelation(new MovieGenreRelationship(genreId,movieId));
                }
            }

        }
        //Now
        for(int i=0;i<now.size();i++)
        {
            client.data().insertNow(new NowPlayingMovies(i+1,now.get(i).id));
            for(int j=0;j<now.get(i).genre_ids.size();j++){
                long genreId=now.get(i).genre_ids.get(j);
                long movieId=now.get(i).id;
                if(client.data().getRelation(genreId,movieId).size()==0){
                    client.data().InsertMovieGenreRelation(new MovieGenreRelationship(genreId,movieId));
                }
            }
        }
        //Popular
        for(int i=0;i<popular.size();i++)
        {
            client.data().insertPopular(new PopularMovies(i+1,popular.get(i).id));
            for(int j=0;j<popular.get(i).genre_ids.size();j++){
                long genreId=popular.get(i).genre_ids.get(j);
                long movieId=popular.get(i).id;
                if(client.data().getRelation(genreId,movieId).size()==0){
                    client.data().InsertMovieGenreRelation(new MovieGenreRelationship(genreId,movieId));
                }
            }
        }
        //Upcoming
        for(int i=0;i<upcoming.size();i++){
            client.data().insertUpcoming(new UpcomingMovies(upcoming.get(i).id));
            for(int j=0;j<upcoming.get(i).genre_ids.size();j++){
                long genreId=upcoming.get(i).genre_ids.get(j);
                long movieId=upcoming.get(i).id;
                if(client.data().getRelation(genreId,movieId).size()==0){
                    client.data().InsertMovieGenreRelation(new MovieGenreRelationship(genreId,movieId));
                }
            }
        }
        updateAll();
    }

    private void setTop() {
        TopRated=rootView.findViewById(R.id.TopRated);
        TextView seeAll=rootView.findViewById(R.id.seeTop);
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt(Constant.TYPE_KEY,Constant.LIST);
                bundle.putInt(Constant.LIST_TYPE,Constant.TOP_LIST);
                bundle.putInt(Constant.MOVIE_TV_GENRE,Constant.MOVIE);
                callback.start(DisplayActivity.class,bundle);
            }
        });

        List<Long> topIds = client.data().getTopRated();
        top=new ArrayList<>();
        if(topIds!=null){
            for(int i=0;i<topIds.size();i++)
                top.add(client.data().selectedMovie(topIds.get(i)));
        }
        topAdapter=new MoviesAdapter(getActivity(),top,this);
        TopRated.setAdapter(topAdapter);
        TopRated.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        Call<MovieResult> resultCall=ApiClient.getInstance().getAPI().TopRated(1);
        resultCall.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                top.clear();
                top.addAll(response.body().movies);
                topAdapter.notifyDataSetChanged();
                client.data().insertMovies(top);
                client.data().deleteAllTopRated();
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                complete=false;
            }
        });
    }

    private void setNow() {
        NowPlaying=rootView.findViewById(R.id.NowPlaying);
        TextView seeAll=rootView.findViewById(R.id.seeNow);
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt(Constant.TYPE_KEY,Constant.LIST);
                bundle.putInt(Constant.LIST_TYPE,Constant.NOW_LIST);
                bundle.putInt(Constant.MOVIE_TV_GENRE,Constant.MOVIE);
                callback.start(DisplayActivity.class,bundle);
            }
        });

        List<Long> nowIds = client.data().getNow();
        now=new ArrayList<>();
        if(nowIds.size()!=0) {
            for (int i=0;i<nowIds.size();i++)
                now.add(client.data().selectedMovie(nowIds.get(i)));
        }
        nowAdapter=new MoviesAdapter(getActivity(),now,this);
        NowPlaying.setAdapter(nowAdapter);
        NowPlaying.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        Call<MovieResult> resultCall=ApiClient.getInstance().getAPI().NowPlaying(1);
        resultCall.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                now.clear();
                now.addAll(response.body().movies);
                nowAdapter.notifyDataSetChanged();
                client.data().insertMovies(now);
                client.data().deleteAllNowPlaying();
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                complete=false;
            }
        });
    }

    private void setPopular() {
        PopularMovies=rootView.findViewById(R.id.PopularMovies);
        TextView seeAll=rootView.findViewById(R.id.seePopular);
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt(Constant.TYPE_KEY,Constant.LIST);
                bundle.putInt(Constant.LIST_TYPE,Constant.POPULAR_LIST);
                bundle.putInt(Constant.MOVIE_TV_GENRE,Constant.MOVIE);
                callback.start(DisplayActivity.class,bundle);
            }
        });
        List<Long> popularIds = client.data().getPopular();
        popular=new ArrayList<>();
        if(popularIds.size()!=0) {
            for (int i=0;i<popularIds.size();i++)
                popular.add(client.data().selectedMovie(popularIds.get(i)));
        }
        popularAdapter=new MoviesAdapter(getActivity(),popular,this);
        PopularMovies.setAdapter(popularAdapter);
        PopularMovies.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        Call<MovieResult> resultCall=ApiClient.getInstance().getAPI().PopularMovies(1);
        resultCall.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                popular.clear();
                popular.addAll(response.body().movies);
                popularAdapter.notifyDataSetChanged();
                client.data().insertMovies(popular);
                client.data().deleteAllPopularMovies();
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                complete=false;
            }
        });
    }

    private void setUpcoming() {
        UpcomingMovies=rootView.findViewById(R.id.UpcomingMovies);

        final List<Long> upcomingIds=client.data().getUpcoming();
        upcoming=new ArrayList<>();
        if(upcomingIds.size()!=0){
            for(int i=0;i<upcomingIds.size();i++)
                upcoming.add(client.data().selectedMovie(upcomingIds.get(i)));
        }

        upcomingAdapter= new UpcomingMoviesPagerAdapter(getActivity().getSupportFragmentManager(),upcoming,this);
        UpcomingMovies.setAdapter(upcomingAdapter);
        UpcomingMovies.setOffscreenPageLimit(0);
        Call<MovieResult> resultCall=ApiClient.getInstance().getAPI().UpcomingMovies(1);
        resultCall.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                upcoming.clear();
                upcoming.addAll(response.body().movies);
                upcomingAdapter.notifyDataSetChanged();
                client.data().insertMovies(upcoming);
                client.data().deleteAllUpcomingMovies();

            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                complete=false;
            }
        });
    }

    @Override
    public void updateAll() {
        popularAdapter.notifyDataSetChanged();
        nowAdapter.notifyDataSetChanged();
        topAdapter.notifyDataSetChanged();
        upcomingAdapter.reload();
        genresAdapter.notifyDataSetChanged();
    }
}
