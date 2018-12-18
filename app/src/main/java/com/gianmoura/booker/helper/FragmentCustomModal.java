package com.gianmoura.booker.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

public class FragmentCustomModal
{
    private final View view;
    private final AlertDialog alertDialog;

    public static FragmentCustomModal getInstance(
            @NonNull final Context context,
            @NonNull @LayoutRes final int resLayoutId )
    {
        return new FragmentCustomModal( context, resLayoutId );
    }

    private FragmentCustomModal(
            final Context context,
            final int resLayoutId )
    {
        view = LayoutInflater.from( context ).inflate( resLayoutId, null );
        alertDialog = new AlertDialog.Builder( context ).setView( view ).setCancelable( false ).create();
    }

    public View getView()
    {
        return view;
    }

    public void dismiss(){
        alertDialog.dismiss();
    }

    public void show()
    {
        alertDialog.show();
    }

    public void hide()
    {
        alertDialog.cancel();
    }
}