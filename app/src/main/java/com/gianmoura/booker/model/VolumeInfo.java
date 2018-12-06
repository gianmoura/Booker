package com.gianmoura.booker.model;

import com.gianmoura.booker.config.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
-VolumesInfo
    -bid
        -title
        -imageLinks
        -industryIdentifiers
        -authors
            -authorName
        -categories
            -cid
            -cid1
 */
public class VolumeInfo {
    private String bid;
    private Book owner;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("imageLinks")
    @Expose
    private ImageLinks imageLinks;
    @SerializedName("industryIdentifiers")
    @Expose
    private List<IndustryIdentifier> industryIdentifiers = new ArrayList<>();
    @SerializedName("authors")
    @Expose
    private List<String> authors;
    @SerializedName("categories")
    @Expose
    private List<String> categories = new ArrayList<>();

    public VolumeInfo() {
    }

    public String save(){
        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference().child("volumesInfo");
        if(getBid() == null){
            setBid(databaseReference.push().getKey());
        }
        databaseReference.child(getBid()).setValue(this);
        return getBid();
    }

    @Exclude
    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    @Exclude
    public Book getOwner() {
        return owner;
    }

    public void setOwner(Book owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }

    public List<IndustryIdentifier> getIndustryIdentifiers() {
        return industryIdentifiers;
    }

    public void setIndustryIdentifiers(List<IndustryIdentifier> industryIdentifiers) {
        this.industryIdentifiers = industryIdentifiers;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolumeInfo that = (VolumeInfo) o;
        return Objects.equals(getBid(), that.getBid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBid());
    }
}
