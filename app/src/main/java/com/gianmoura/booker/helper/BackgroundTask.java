package com.gianmoura.booker.helper;

import android.content.Context;
import android.os.AsyncTask;

import com.gianmoura.booker.R;

public abstract class BackgroundTask extends AsyncTask<Void, Void, Boolean> {
    private FragmentCustomModal customModal = null;

    public BackgroundTask(Context context) {
        if (customModal == null){
            customModal = FragmentCustomModal.getInstance(context, R.layout.progress_dialog);
        }
    }

    @Override
    protected void onPreExecute() {
        customModal.getView().findViewById(R.id.mProgressBar).animate();
        customModal.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        customModal.hide();
    }
}
