package com.example.sahni.cinemato.DataClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by sahni on 24/3/18.
 */
@Entity
public class Movie {
    public int vote_count;
    public boolean adult;
    @PrimaryKey @NonNull
    public long id;
    public float vote_average;
    public boolean video;
    public String title;
    public String original_language;
    public String original_title;
    @Ignore
    public ArrayList<Long> genre_ids;
    @Ignore
    public ArrayList<MovieCollection> belongs_to_collection;
    @Ignore
    public ArrayList<MovieGenre> genres;
    public String backdrop_path;
    public String poster_path;
    public String overview;
    public String release_date;
}
