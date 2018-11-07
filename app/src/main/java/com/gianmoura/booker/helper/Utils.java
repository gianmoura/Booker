package com.gianmoura.booker.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.gianmoura.booker.config.FirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;

public class Utils {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        else
            return false;
    }

    public static boolean isLoggedIn() {
        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        return firebaseAuth.getCurrentUser() != null;
    }

    public static void showBlockedAccessMassage(Context context){
        Toast.makeText(context, "Por favor, se identifique para ter acesso total.", Toast.LENGTH_LONG).show();
    }
}
