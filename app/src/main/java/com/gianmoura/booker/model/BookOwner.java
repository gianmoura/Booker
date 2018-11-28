package com.gianmoura.booker.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.helper.Utils;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

/*
-BookOwners
    -uid
        -bid
            -quantity
            -description
            -value
 */
public class BookOwner {
    private String uid;
    private String bid;
    private String description;
    private int quantity;
    private double value;

    public BookOwner() {
    }

    public void save(){
        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference();
        databaseReference.child("bookowners").child(getUid()).child(getBid()).setValue(this);
    }

    public void delete(final Context context){
        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference();
        databaseReference.child("bookowners").child(getUid()).child(getBid()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Utils.showAlertModal(context, "Livro removido", "Confirmação");
                }else{
                    Utils.showAlertModal(context, "Erro: "+databaseError.getMessage(), null);
                }
            }
        });
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
