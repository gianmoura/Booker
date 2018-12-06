package com.gianmoura.booker.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gianmoura.booker.R;
import com.gianmoura.booker.activity.BookRegisterActivity;
import com.gianmoura.booker.activity.CollectionActivity;
import com.gianmoura.booker.activity.MainActivity;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.helper.FragmentCustomModal;
import com.gianmoura.booker.helper.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import okhttp3.internal.Util;

/*
-BookOwners
    -uid
        -bid
            -quantity
            -description
            -value
 */
public class Book {
    private String uid;
    private String bid;
    private String description;
    private int quantity;
    private double value;

    public Book() {
    }

    public void save(final Activity activity){
        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference();
        databaseReference.child("books").child(getUid()).child(getBid()).setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    final FragmentCustomModal customModal = FragmentCustomModal.getInstance(activity, R.layout.diolog_alert);
                    ((TextView) customModal.getView().findViewById(R.id.dialog_alert_title)).setText("Sucesso");
                    ((TextView) customModal.getView().findViewById(R.id.dialog_alert_message)).setText("Seu livro foi salvo com sucesso, acesse sua coleção para visualiza-lo.");
                    customModal.show();
                    (customModal.getView().findViewById(R.id.dialog_alert_button_ok)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.finish();
                            customModal.hide();
                        }
                    });
                }else {
                    Utils.showAlertModal(activity, "Não foi possivel salvar as informações, tente novamente por favor.", null);
                    return;
                }
            }
        });
    }

    public void delete(final Context context){
        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference();
        databaseReference.child("books").child(getUid()).child(getBid()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Utils.showAlertModal(context, "Livro removido com sucesso.", "Confirmação");
                }else{
                    Utils.showAlertModal(context, "Erro: "+databaseError.getMessage(), null);
                }
            }
        });
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
