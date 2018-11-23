package com.gianmoura.booker.model;

import com.gianmoura.booker.config.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.List;

/*
-Categories
    -cid
        -tag
        -books
            -isbn
 */
public class Category {
    private String cid;
    private String tag;
    private List<String> books;

    public Category() {
    }

    public void save(){
        DatabaseReference categoriesReference = FirebaseConfig.getDatabaseReference().child("categories");
        if(getCid() == null){
            setCid(categoriesReference.push().getKey());
        }
        categoriesReference.child(getCid()).setValue(this);
    }

    @Exclude
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }
}
