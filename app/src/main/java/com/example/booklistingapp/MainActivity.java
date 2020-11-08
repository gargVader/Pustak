package com.example.booklistingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    GridView listView;
    BookArrayAdapter bookArrayAdapter;

    private String LOG_TAG = MainActivity.class.getName();

    private static int BOOK_LOADER_ID = 1;

    private static  String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?" +
            "q=";

    private StringBuilder queryUrl;
    LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (GridView) findViewById(R.id.listView);

        if(!internetConnected(this)){
            listView.setEmptyView(findViewById(R.id.no_internet));
        }else {
            bookArrayAdapter = new BookArrayAdapter(this, R.layout.list_item,
                    new ArrayList<Book>());
            listView.setAdapter(bookArrayAdapter);
            listView.setEmptyView(findViewById(R.id.search_for_books));


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Book currBook = (Book) bookArrayAdapter.getItem(position);
                    Uri bookUri = Uri.parse(currBook.getInfoLink());
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                    startActivity(webIntent);
                }
            });

            loaderManager = getSupportLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.e(LOG_TAG, "Initialising Loader Manager");
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        }


    }


    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {

        updateQueryUrl();
        bookArrayAdapter.clear();
        Log.e(LOG_TAG, "Creating Loader");
        return new BookLoader(this, queryUrl.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> data) {
        Log.e(LOG_TAG, "Loader creation finished");

        // Clear the adapter of previous book data
        bookArrayAdapter.clear();
        if(data!=null && !data.isEmpty()){
            bookArrayAdapter.addAll(data);
        }else{
            listView.setEmptyView(findViewById(R.id.no_books));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        Log.e(LOG_TAG, "Loader reset");
        bookArrayAdapter.clear();
    }

    private void updateQueryUrl(){
        EditText searchEditText = (EditText) findViewById(R.id.searchEditText);
        String searchTerm = searchEditText.getText().toString();

        queryUrl = new StringBuilder();
        queryUrl.append(GOOGLE_BOOKS_API_URL);

        if(searchTerm==null || searchTerm.isEmpty()){
            bookArrayAdapter.clear();
            listView.setEmptyView(findViewById(R.id.search_for_books));
//            queryUrl.append("coding");
//            queryUrl.append("&maxResults=5");
        }else{
            queryUrl.append(searchTerm);
            queryUrl.append("&maxResults=25");
        }

    }

    public void restartLoader(){
        loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
    }

    public void onClick(View view){
        bookArrayAdapter.clear();
        findViewById(R.id.search_for_books).setVisibility(View.GONE);
        listView.setEmptyView(findViewById(R.id.progressBar));
        updateQueryUrl();
        restartLoader();
    }

    private static boolean internetConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return  isConnected;
    }

}