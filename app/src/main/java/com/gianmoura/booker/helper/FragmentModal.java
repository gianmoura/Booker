package com.gianmoura.booker.helper;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

public final class FragmentModal
{

    private final AlertDialog.Builder builder;
    private final Context context;
    private final StringBuilder message;

    public static final FragmentModal getInstance(
        @NonNull final Context context )
    {
        return new FragmentModal( context );
    }

    private FragmentModal(
        final Context context )
    {
        this.context = context;
        builder = new AlertDialog.Builder( context );
        message = new StringBuilder();
    }

    public FragmentModal show()
    {
        builder.create().show();
        return this;
    }

    public FragmentModal addTitle(
        @NonNull final String title )
    {
        builder.setTitle( title );
        return this;
    }

    public FragmentModal addTitle(
        @StringRes final int title )
    {
        builder.setTitle( recoveryStringById( title ) );
        return this;
    }

    public FragmentModal addMessage(
        @NonNull final CharSequence message )
    {
        builder.setMessage( message );
        return this;
    }

    public FragmentModal addMessage(
        @NonNull final List<String> messagesList )
    {
        for( final String messageExtract : messagesList ) {
            message.append( new String( messageExtract ).concat( "\n" ) );
        }
        builder.setMessage( message );
        return this;
    }

    public FragmentModal addMessage(
        @StringRes final int message )
    {
        builder.setMessage( recoveryStringById( message ) );
        return this;
    }

    public FragmentModal addPositiveButton(
        @NonNull final String label,
        @Nullable final DialogInterface.OnClickListener onClickListener )
    {
        builder.setPositiveButton( label, onClickListener );
        return this;
    }

    public FragmentModal addPositiveButton(
        @StringRes final int label,
        @Nullable final DialogInterface.OnClickListener onClickListener )
    {
        builder.setPositiveButton( recoveryStringById( label ), onClickListener );
        return this;
    }

    public FragmentModal addNegativeButton(
        @NonNull final String label,
        @Nullable final DialogInterface.OnClickListener onClickListener )
    {
        builder.setNegativeButton( label, onClickListener );
        return this;
    }

    public FragmentModal addNegativeButton(
        @StringRes final int label,
        @Nullable final DialogInterface.OnClickListener onClickListener )
    {
        builder.setNegativeButton( recoveryStringById( label ), onClickListener );
        return this;
    }

    public FragmentModal addNeutralButton(
        @NonNull final String label,
        @Nullable final DialogInterface.OnClickListener onClickListener )
    {
        builder.setNeutralButton( label, onClickListener );
        return this;
    }

    public FragmentModal addNeutralButton(
        @StringRes final int label,
        @Nullable final DialogInterface.OnClickListener onClickListener )
    {
        builder.setNeutralButton( recoveryStringById( label ), onClickListener );
        return this;
    }

    private String recoveryStringById(
        final int resourceStringId )
    {
        return context.getResources().getString( resourceStringId );
    }
}