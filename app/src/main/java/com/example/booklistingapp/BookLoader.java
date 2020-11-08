package com.example.booklistingapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    String stringUrl;
    private String LOG_TAG = BookLoader.class.getName();

    public BookLoader(@NonNull Context context, String stringUrl) {
        super(context);
        this.stringUrl = stringUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Book> loadInBackground() {
        Log.e(LOG_TAG, "Loading in background");

        if(stringUrl==null || stringUrl.isEmpty()){
            return null;
        }

        ArrayList<Book> bookArrayList = QueryUtils.extractBooks(stringUrl);
        return bookArrayList;
    }
}
