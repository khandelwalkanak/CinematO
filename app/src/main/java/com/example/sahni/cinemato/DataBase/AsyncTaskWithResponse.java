package com.example.sahni.cinemato.DataBase;

import android.os.AsyncTask;
import android.service.media.MediaBrowserService;

/**
 * Created by sahni on 12/4/18.
 */

public abstract class AsyncTaskWithResponse<X,Y,Z> extends AsyncTask<X,Y,Z>{
    public interface ResultSet<T>{
        void onResult(T t);
    }
    ResultSet<Z> resultSet;
    AsyncTaskWithResponse(ResultSet<Z> resultSet){
        this.resultSet=resultSet;
    }
}
