package com.gianmoura.booker.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gianmoura.booker.R;
import com.gianmoura.booker.activity.AlertActivity;
import com.gianmoura.booker.activity.MainActivity;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.model.ImageLinks;
import com.gianmoura.booker.model.VolumeInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static boolean isOnline(Context context) {
        return Connectivity.isConnected(context);
    }

    public static boolean isLoggedIn() {
        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        return firebaseAuth.getCurrentUser() != null;
    }

    public static FirebaseUser getLoggedUser(){
        if (isLoggedIn()){
            FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
            return firebaseAuth.getCurrentUser();
        }
        return null;
    }

    public static String getLoggedUid(){
        if (getLoggedUser() != null){
            return getLoggedUser().getUid();
        }else {
            return "";
        }
    }

    public static void logoutUser(final Context context) {
        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.signOut();
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void saveMaxDistance(final int maxDistance, final Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(getLoggedUser().getUid(), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(String.valueOf(R.id.preferenceDistanceConfig), maxDistance);
        editor.apply();
    }

    public static int getMaxDistance(final Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(getLoggedUser().getUid(), context.MODE_PRIVATE);
        return sharedPreferences.getInt(String.valueOf(R.id.preferenceDistanceConfig), 1);
    }

    public static void showBlockedAccessMessage(Context context){
        showAlertModal(context, "Por favor, se identifique para ter acesso total das funcionalidades.", null);
    }

    public static void  showAlertModal(
            final Context context,
            final String messageDescription,
            @Nullable final String title){
        final FragmentCustomModal customModal = FragmentCustomModal.getInstance(context, R.layout.diolog_alert);
        ((TextView) customModal.getView().findViewById(R.id.dialog_alert_message)).setText(messageDescription);
        if (title != null){
            ((TextView) customModal.getView().findViewById(R.id.dialog_alert_title)).setText(title);
        }
        customModal.show();

        (customModal.getView().findViewById(R.id.dialog_alert_button_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customModal.hide();
            }
        });
    }

    public static void redirectTo(final Intent intent, final Context context){
        context.startActivity(intent);
    }

    public static void checkDeviceConnection(final Context context) {
        if (!Utils.isOnline(context)){
            context.startActivity(new Intent(context, AlertActivity.class));
        }
    }

    public static void checkNullFields(List<VolumeInfo> collection) {
        for (VolumeInfo volume : collection) {
            if (volume.getCategories() == null){
                volume.setCategories(new ArrayList<String>());
            }
            if (volume.getAuthors() == null){
                volume.setAuthors(new ArrayList<String>());
            }
            if (volume.getImageLinks() == null){
                ImageLinks imageLinks = new ImageLinks();
                imageLinks.setThumbnail(String.valueOf(R.drawable.books_render));
                volume.setImageLinks(imageLinks);
            }
        }
    }
}
