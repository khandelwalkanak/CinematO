package com.example.sahni.cinemato.DataClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by sahni on 15/4/18.
 */
@Entity(foreignKeys = {
        @ForeignKey(
                entity = MovieGenre.class,
                parentColumns = "id",
                childColumns = "genreId",
                onDelete=CASCADE
        )
})
public class LikedMovieGenre {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long genreId;
    public LikedMovieGenre(long genreId){
        this.genreId=genreId;
    }
}
