package com.gianmoura.booker.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.gianmoura.booker.activity.LoginActivity;
import com.gianmoura.booker.activity.MainActivity;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.helper.Utils;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Objects;

/*
-Preferences
    -uid
        -cid
            -activity
        -cid1
            -activity
    -uid1
        -cid1
            -activity
 */
public class Preference {
    private String uid;
    private String cid;
    private int activity;
    private String tag;

    public Preference() {
    }

    public void save(){
        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference();
        databaseReference.child("preferences").child(getUid()).child(getCid()).setValue(this);
    }

    public void save(@NonNull final List<Preference> preferences){
        if (preferences != null){
            DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference();
            for (Preference preference: preferences) {
                databaseReference.child("preferences").child(preference.getUid()).child(preference.getCid()).setValue(preference);
            }
        }
    }

    public void delete(final Context context){
        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference();
        databaseReference.child("preferences").child(getUid()).child(getCid()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Utils.showAlertModal(context, "Preferência removida", "Confirmação");
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
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    @Exclude
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
        Preference that = (Preference) o;
        return Objects.equals(getCid(), that.getCid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCid());
    }
}
