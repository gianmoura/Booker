package com.gianmoura.booker.model;

import android.support.annotation.NonNull;

import com.gianmoura.booker.config.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Objects;

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

    public Category() {
    }

    public void save(){
        DatabaseReference categoriesReference = FirebaseConfig.getDatabaseReference().child("categories");
        if(getCid() == null){
            setCid(categoriesReference.push().getKey());
        }
        categoriesReference.child(getCid()).setValue(this);
    }

    public void save(@NonNull final List<Category> categories){
        if (categories != null){
            DatabaseReference categoriesReference = FirebaseConfig.getDatabaseReference().child("categories");
            for (Category category: categories) {
                if(category.getCid() == null){
                    category.setCid(categoriesReference.push().getKey());
                }
                categoriesReference.child(category.getCid()).setValue(category);
            }
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(getCid(), category.getCid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCid());
    }
}
