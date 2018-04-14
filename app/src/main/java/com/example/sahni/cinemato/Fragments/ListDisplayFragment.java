package com.example.sahni.cinemato.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.sahni.cinemato.APIData.ApiClient;
import com.example.sahni.cinemato.APIData.MovieResult;
import com.example.sahni.cinemato.Activities.DisplayActivity;
import com.example.sahni.cinemato.Adapters.GenresAdapter;
import com.example.sahni.cinemato.Adapters.ItemClickCallback;
import com.example.sahni.cinemato.Adapters.MovieListAdapter;
import com.example.sahni.cinemato.Constant;
import com.example.sahni.cinemato.DataBase.DatabaseClient;
import com.example.sahni.cinemato.DataClasses.Movie;
import com.example.sahni.cinemato.DataClasses.MovieGenre;
import com.example.sahni.cinemato.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListDisplayFragment extends Fragment {


    private View rootView;
    ProgressBar progressBar;
    RecyclerView list;
    DatabaseClient client;
    Context context;
    public StartActivityCallBack callback;

    MovieListAdapter movieListAdapter;
    ArrayList<Movie> movies;
    ArrayList<MovieGenre> genres;
    GenresAdapter genresAdapter;
    int page=1;
    public ActionBar supportActionBar;

    public ListDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_display_list, container, false);
        list=rootView.findViewById(R.id.list);
        client=DatabaseClient.getInstance(context);
        progressBar=rootView.findViewById(R.id.progress);
        list.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        setData();
        return rootView;
    }

    private void setData() {
        int type=getArguments().getInt(Constant.MOVIE_TV_GENRE);
        switch (type){
            case Constant.MOVIE:
                movies=new ArrayList<>();
                int listType =getArguments().getInt(Constant.LIST_TYPE);
                movieListAdapter=new MovieListAdapter(context,movies,listType);
                list.setAdapter(movieListAdapter);
                switch (listType){
                    case Constant.FAVOURITE_LIST:
                        supportActionBar.setTitle("Favourite Movies");
                        List<Long> favIds=client.data().getAllFavourites();
                        for(int i=0;i<favIds.size();i++)
                            movies.add(client.data().selectedMovie(favIds.get(i)));
                        movieListAdapter.notifyDataSetChanged();
                        break;
                    case Constant.TOP_LIST:
                        supportActionBar.setTitle("Top Movies");
                        List<Long> topIds=client.data().getTopRated();
                        for(int i=0;i<topIds.size();i++)
                            movies.add(client.data().selectedMovie(topIds.get(i)));
                        movieListAdapter.notifyDataSetChanged();
                        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if(!recyclerView.canScrollVertically(1)){
                                    progressBar.setVisibility(View.VISIBLE);
                                    page++;
                                    Call<MovieResult> call= ApiClient.getInstance().getAPI().TopRated(page);
                                    call.enqueue(new Callback<MovieResult>() {
                                        @Override
                                        public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                                            movies.addAll(response.body().movies);
                                            movieListAdapter.notifyDataSetChanged();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onFailure(Call<MovieResult> call, Throwable t) {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }
                        });
                        break;
                    case Constant.NOW_LIST:
                        supportActionBar.setTitle("In Cinemas");
                        List<Long> nowIds=client.data().getNow();
                        for(int i=0;i<nowIds.size();i++)
                            movies.add(client.data().selectedMovie(nowIds.get(i)));
                        movieListAdapter.notifyDataSetChanged();
                        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if(!recyclerView.canScrollVertically(1)){
                                    progressBar.setVisibility(View.VISIBLE);
                                    page++;
                                    Call<MovieResult> call= ApiClient.getInstance().getAPI().NowPlaying(page);
                                    call.enqueue(new Callback<MovieResult>() {
                                        @Override
                                        public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                                            movies.addAll(response.body().movies);
                                            movieListAdapter.notifyDataSetChanged();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onFailure(Call<MovieResult> call, Throwable t) {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }
                        });
                        break;
                    case Constant.POPULAR_LIST:
                        supportActionBar.setTitle("Most Popular Movies");
                        List<Long> popIds=client.data().getPopular();
                        for(int i=0;i<popIds.size();i++)
                            movies.add(client.data().selectedMovie(popIds.get(i)));
                        movieListAdapter.notifyDataSetChanged();
                        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if(!recyclerView.canScrollVertically(1)){
                                    progressBar.setVisibility(View.VISIBLE);
                                    page++;
                                    Call<MovieResult> call= ApiClient.getInstance().getAPI().PopularMovies(page);
                                    call.enqueue(new Callback<MovieResult>() {
                                        @Override
                                        public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                                            movies.addAll(response.body().movies);
                                            movieListAdapter.notifyDataSetChanged();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onFailure(Call<MovieResult> call, Throwable t) {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }
                        });
                        break;
                    case Constant.GENRE_LIST:
                        MovieGenre genre;
                        final long genreId=getArguments().getLong(Constant.GENRE_ID);
                        genre=client.data().selectedGenre(genreId);
                        supportActionBar.setTitle(genre.name);
                        List<Long> movieIds=client.data().getMovieIds(genreId);
                        for(int i=0;i<movieIds.size();i++)
                            movies.add(client.data().selectedMovie(movieIds.get(i)));
                        movieListAdapter.notifyDataSetChanged();
                        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if(!recyclerView.canScrollVertically(1)){
                                    progressBar.setVisibility(View.VISIBLE);
                                    page++;
                                    Call<MovieResult> call= ApiClient.getInstance().getAPI().GenreMovies(genreId,page);
                                    call.enqueue(new Callback<MovieResult>() {
                                        @Override
                                        public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                                            movies.addAll(response.body().movies);
                                            movieListAdapter.notifyDataSetChanged();
                                            progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onFailure(Call<MovieResult> call, Throwable t) {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }
                        });
                        break;
                }
                break;
            case Constant.GENRE:
                genres=new ArrayList<>();
                supportActionBar.setTitle("Favourite Genres");
                genres.addAll(client.data().getLiked(true));
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
                },R.layout.display_genre_item);
                list.setAdapter(genresAdapter);
                list.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                break;
            case Constant.TV:
                Log.e("LIST", "setData: TV ");
                break;
        }
    }

}
