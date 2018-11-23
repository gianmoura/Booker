package com.gianmoura.booker.helper;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gianmoura.booker.R;
import com.gianmoura.booker.config.FirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;

public class Utils {

    public static boolean isOnline(Context context) {
        return Connectivity.isConnected(context);
    }

    public static boolean isLoggedIn() {
        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        return firebaseAuth.getCurrentUser() != null;
    }

    public static void showBlockedAccessMessage(Context context){
        showAlertModal(context, "Por favor, se identifique para ter acesso total das funcionalidades.");
    }

    public static void  showAlertModal(
            final Context context,
            final String messageDescription){
        final FragmentCustomModal customModal = FragmentCustomModal.getInstance(context, R.layout.diolog_alert);
        TextView message = customModal.getView().findViewById(R.id.dialog_alert_message);
        message.setText(messageDescription);
        customModal.show();

        Button alertButton = (Button) customModal.getView().findViewById(R.id.dialog_alert_button_ok);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customModal.hide();
            }
        });
    }

}
