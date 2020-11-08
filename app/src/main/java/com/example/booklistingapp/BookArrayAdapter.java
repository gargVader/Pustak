package com.example.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookArrayAdapter extends ArrayAdapter<Book> {

    public BookArrayAdapter(@NonNull Context context,int resource,  ArrayList<Book> bookArrayList) {
        super(context, 0, bookArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        // Check if existing view is being reused, otherwise inflate the view
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
        }

        // Get the book
        Book book = getItem(position);

        // Find all Views
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.imageView);
        TextView title = (TextView) listItemView.findViewById(R.id.title);
        TextView author = (TextView) listItemView.findViewById(R.id.author);

        // Set Views
        imageView.setImageBitmap(book.getImageBitmap());
        //imageView.setImageResource(R.drawable.content2);
        title.setText(book.getTitle());

        ArrayList<String> authorArrayList = book.getAuthorArrayList();
        String authorsString = "";
        for(int i=0; i<authorArrayList.size(); i++){
            String currAuthor = authorArrayList.get(i);
            authorsString+=currAuthor + System.getProperty("line.separator");
        }
        author.setText(authorsString);

        return listItemView;

    }
}
