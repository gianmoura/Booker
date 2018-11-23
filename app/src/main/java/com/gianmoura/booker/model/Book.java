package com.gianmoura.booker.model;

import com.gianmoura.booker.config.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.List;

/*
-Books
    -bid
        -title
        -smallThumbnail
        -thumbnail
        -isbn10
        -isbn13
        -authors
            -authorName
        -categories
            -cid
            -cid1
 */
public class Book {
    private String bid;
    private String title;
    private String smallThumbnail;
    private String thumbnail;
    private String isbn10;
    private String isbn13;
    private List<String> authors;
    private List<String> categories;
    private BookOwner owner;

    public Book() {
    }

    public void save(){
        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference().child("books");
        if(getBid() == null){
            setBid(databaseReference.push().getKey());
        }
        databaseReference.child(getBid()).setValue(this);
    }

    @Exclude
    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Exclude
    public BookOwner getOwner() {
        return owner;
    }

    public void setOwner(BookOwner owner) {
        this.owner = owner;
    }
}
