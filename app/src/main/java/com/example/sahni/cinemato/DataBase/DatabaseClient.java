package com.example.sahni.cinemato.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.sahni.cinemato.Constant;
import com.example.sahni.cinemato.DataClasses.FavouriteMovies;
import com.example.sahni.cinemato.DataClasses.Movie;
import com.example.sahni.cinemato.DataClasses.MovieGenre;
import com.example.sahni.cinemato.DataClasses.MovieGenreRelationship;
import com.example.sahni.cinemato.DataClasses.NowPlayingMovies;
import com.example.sahni.cinemato.DataClasses.PopularMovies;
import com.example.sahni.cinemato.DataClasses.TopRatedMovies;
import com.example.sahni.cinemato.DataClasses.UpcomingMovies;

/**
 * Created by sahni on 8/4/18.
 */

@Database(entities = {  Movie.class,
                        MovieGenre.class,
                        MovieGenreRelationship.class,
                        PopularMovies.class,
                        UpcomingMovies.class,
                        FavouriteMovies.class,
                        NowPlayingMovies.class,
                        TopRatedMovies.class
                    }, version = 1)
public abstract class DatabaseClient extends RoomDatabase {
    private static DatabaseClient INSTANCE;
    public abstract Data data();
    public static DatabaseClient getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), DatabaseClient.class, Constant.DATABASE)
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
