package com.gianmoura.booker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gianmoura.booker.R;
import com.gianmoura.booker.helper.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlertActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        ButterKnife.bind(this);
        verifyConnection();
    }

    @OnClick(R.id.btnRefresh)
    public void refreshAlert(){
        verifyConnection();
    }

    private void verifyConnection(){
        if (Utils.isOnline(this)){
            startActivity( new Intent( this, MainActivity.class) );
            finish();
        }
    }
}
