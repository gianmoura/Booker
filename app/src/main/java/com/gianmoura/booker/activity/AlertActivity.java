package com.gianmoura.booker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gianmoura.booker.R;
import com.gianmoura.booker.helper.BackgroundTask;
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
        new ConnectionTask(this).execute();
    }

    private class ConnectionTask extends BackgroundTask {

        public ConnectionTask(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            verifyConnection();
            super.doInBackground(params);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    private void verifyConnection(){
        if (Utils.isOnline(this)){
            startActivity( new Intent( this, MainActivity.class) );
            finish();
        }
    }
}
