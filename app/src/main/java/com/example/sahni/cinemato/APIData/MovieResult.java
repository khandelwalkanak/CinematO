package com.example.sahni.cinemato.APIData;

import com.example.sahni.cinemato.DataClasses.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sahni on 25/3/18.
 */

public class MovieResult {
    int page;
    long id;
    int total_results;
    int total_pages;
    @SerializedName("results")
    public
    ArrayList<Movie> movies;
}
