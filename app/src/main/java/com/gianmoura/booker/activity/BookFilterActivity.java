package com.gianmoura.booker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gianmoura.booker.R;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.helper.Utils;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookFilterActivity extends Activity {
    @BindView(R.id.btnLogout)
    Button logout;
    @BindView(R.id.btnLogin)
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_filter);
        if (!Utils.isOnline(this)){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        ButterKnife.bind(this);
        if (Utils.isLoggedIn()){
            login.setVisibility(View.INVISIBLE);
        }else{
            logout.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.btnLogout)
    public void logout(){
        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.btnLogin)
    public void redirectToLogin(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
