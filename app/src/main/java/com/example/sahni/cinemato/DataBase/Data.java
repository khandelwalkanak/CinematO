package com.example.sahni.cinemato.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.sahni.cinemato.DataClasses.FavouriteMovies;
import com.example.sahni.cinemato.DataClasses.Movie;
import com.example.sahni.cinemato.DataClasses.MovieGenre;
import com.example.sahni.cinemato.DataClasses.MovieGenreRelationship;
import com.example.sahni.cinemato.DataClasses.NowPlayingMovies;
import com.example.sahni.cinemato.DataClasses.PopularMovies;
import com.example.sahni.cinemato.DataClasses.TopRatedMovies;
import com.example.sahni.cinemato.DataClasses.UpcomingMovies;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by sahni on 8/4/18.
 */
@Dao
public interface Data {
    //Movies
    @Insert(onConflict = IGNORE)
    void insertMovies(ArrayList<Movie> movies);
    @Insert(onConflict = REPLACE)
    void updateMovie(Movie movie);
    @Query("SELECT * from Movie where id = :id")
    Movie selectedMovie(long id);


    //MovieGenreRelationship
    @Insert(onConflict = IGNORE)
    void InsertMovieGenreRelation(MovieGenreRelationship relationship);
    @Query("SELECT * from MovieGenreRelationship where movieId=:movieId AND genreId=:genreId")
    List<MovieGenreRelationship> getRelation(long genreId,long movieId);
    @Query("SELECT genreId from MovieGenreRelationship where movieId=:movieId")
    List<Long> getGenreIds(long movieId);
    @Query("SELECT movieId from MovieGenreRelationship where genreId=:genreId")
    List<Long> getMovieIds(long genreId);
    @Query("Select * from moviegenrerelationship")
    List<MovieGenreRelationship> getRelations();

    //Genre
    @Insert(onConflict = IGNORE)
    void insertGenre(ArrayList<MovieGenre> genres);
    @Query("SELECT * from MovieGenre where id = :id")
    MovieGenre selectedGenre(long id);
    @Query("UPDATE MovieGenre SET isLiked=:isLiked where id=:id")
    void  updateLiked(long id,boolean isLiked);
    @Query("SELECT * from MovieGenre where isLiked=:isLiked")
    List<MovieGenre> getLiked(boolean isLiked);
    @Query("SELECT * from MovieGenre order by name ASC")
    List<MovieGenre> getGenre();

    //FavouriteMovies
    @Insert(onConflict = IGNORE)
    void insertFavourite(FavouriteMovies movies);
    @Query("SELECT * from FavouriteMovies where movieId = :id Order by id ASC")
    FavouriteMovies getFavourite(long id);
    @Query("SELECT movieId from FavouriteMovies")
    List<Long> getAllFavourites();
    @Delete
    void deleteFavourite(FavouriteMovies movies);

    //UpcomingMovies
    @Insert(onConflict = IGNORE)
    void insertUpcoming(UpcomingMovies upcomingMovies);
    @Query("SELECT movieId from UpcomingMovies Order by id ASC")
    List<Long> getUpcoming();
    @Query("DELETE from UpcomingMovies")
    void deleteAllUpcomingMovies();

    //PopularMovies
    @Insert(onConflict = IGNORE)
    void insertPopular(PopularMovies popularMovies);
    @Query("SELECT movieId from PopularMovies Order by id ASC")
    List<Long> getPopular();
    @Query("DELETE from PopularMovies")
    void deleteAllPopularMovies();

    //NowPlayingMovies
    @Insert(onConflict = IGNORE)
    void insertNow(NowPlayingMovies nowPlayingMovies);
    @Query("SELECT movieId from NowPlayingMovies Order by id ASC")
    List<Long> getNow();
    @Query("DELETE from NowPlayingMovies")
    void deleteAllNowPlaying();

    //TopRated
    @Insert(onConflict = IGNORE)
    void insertTop(TopRatedMovies topRatedMovies);
    @Query("SELECT movieId from TopRatedMovies Order by id ASC")
    List<Long> getTopRated();
    @Query("DELETE from TopRatedMovies")
    void deleteAllTopRated();
}
