package com.gianmoura.booker.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;
import com.gianmoura.booker.config.FirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static boolean isOnline(Context context) {
        return Connectivity.isConnected(context);
    }

    public static boolean isLoggedIn() {
        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        return firebaseAuth.getCurrentUser() != null;
    }

    public static void showBlockedAccessMassage(Context context){
        Toast.makeText(context, "Por favor, se identifique para ter acesso total das funcionalidades.", Toast.LENGTH_LONG).show();
    }

}
