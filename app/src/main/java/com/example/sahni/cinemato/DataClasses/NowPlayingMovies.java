package com.example.sahni.cinemato.DataClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by sahni on 11/4/18.
 */
@Entity(foreignKeys = {
        @ForeignKey(
                entity = Movie.class,
                parentColumns = "id",
                childColumns = "movieId",
                onDelete=CASCADE
        )
})
public class NowPlayingMovies {
    @PrimaryKey @NonNull
    public long id;
    public long movieId;
    public NowPlayingMovies(long id, long movieId){
        this.id=id;
        this.movieId=movieId;
    }
}
