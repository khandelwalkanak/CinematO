package com.example.sahni.cinemato.DataClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by sahni on 8/4/18.
 */
@Entity( foreignKeys = {
        @ForeignKey(entity = Movie.class,
                parentColumns = "id",
                childColumns = "movieId",
                onDelete = CASCADE),
        @ForeignKey(entity = MovieGenre.class,
                parentColumns = "id",
                childColumns = "genreId",
                onDelete = CASCADE)
})
public class MovieGenreRelationship {
    public long movieId;
    public long genreId;
    @NonNull @PrimaryKey(autoGenerate = true)
    public long id;
    public MovieGenreRelationship(long genreId, long movieId){
        this.genreId=genreId;
        this.movieId=movieId;
    }
}
