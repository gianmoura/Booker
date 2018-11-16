package com.gianmoura.booker.model;

import com.gianmoura.booker.config.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

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

    public Preference() {
    }

    public void save(){
        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference();
        databaseReference.child("preferences").child(getUid()).child(getCid()).setValue(this);
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
}
