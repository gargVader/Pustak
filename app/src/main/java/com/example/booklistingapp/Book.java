package com.example.booklistingapp;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Book {

    private String title;
    private ArrayList<String> authorArrayList;
    private Bitmap imageBitmap;
    private String infoLink;

    public Book(String title, ArrayList<String> authorArrayList, Bitmap imageBitmap, String infoLink) {
        setTitle(title);
        setAuthor(authorArrayList);
        setImageBitmap(imageBitmap);
        setInfoLink(infoLink);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(ArrayList<String> authorArrayList) {
        this.authorArrayList = authorArrayList;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getAuthorArrayList() {
        return authorArrayList;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public String getInfoLink() {
        return infoLink;
    }
}
