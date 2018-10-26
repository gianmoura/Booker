package com.gianmoura.booker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gianmoura.booker.R;
import com.gianmoura.booker.helper.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        verifyConnection();
    }

    @OnClick(R.id.button)
    public void refreshAlert(){
        verifyConnection();
    }

    private void verifyConnection(){
        if (Utils.isOnline(this)){
            startActivity( new Intent( this, BookFilterActivity.class) );
            finish();
        }
    }
}
