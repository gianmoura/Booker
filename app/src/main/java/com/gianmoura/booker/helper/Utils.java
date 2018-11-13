package com.gianmoura.booker.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    private static String getAddressFromLocation(double latitude, double longitude, Context context) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        String address = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();
                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append(" ");
                }
                address = strAddress.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
}
