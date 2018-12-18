package com.gianmoura.booker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.gianmoura.booker.R;
import com.gianmoura.booker.adapter.CategoryAdapter;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.helper.FragmentCustomModal;
import com.gianmoura.booker.helper.Utils;
import com.gianmoura.booker.model.Category;
import com.gianmoura.booker.model.Preference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;

public class PreferencesActivity extends Activity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.preferenceSelectionList)
    RecyclerView cListView;
    private RecyclerView.Adapter adapter;
    private Set<Category> availableCategoriesSet;
    private List<Category> availableCategories;
    private List<Category> filteredCategories;
    private List<Preference> preferredCategories;
    private DatabaseReference categoriesReference;
    private DatabaseReference preferencesReference;
    private int preferencesIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        Utils.checkDeviceConnection(this);
        ButterKnife.bind(this);

        availableCategories = new ArrayList<>();
        preferredCategories =  new ArrayList<>();
        filteredCategories = new ArrayList<>();
        availableCategoriesSet = new ArraySet<>();
        adapter = new CategoryAdapter(availableCategories, preferredCategories);
        cListView.setLayoutManager( new LinearLayoutManager( this ) );
        cListView.setAdapter(adapter);

        //Configura Toolbar
        toolbar.setTitle("Booker");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.icons));
        setActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCategories();
    }

    private void getCategories() {
        final String uid = Utils.getLoggedUid();
        if (preferencesReference == null){
            preferencesReference = FirebaseConfig.getDatabaseReference()
                    .child("preferences").child(uid);
        }
        preferencesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (preferredCategories != null){
                    preferredCategories.clear();
                }
                if (availableCategoriesSet != null){
                    availableCategoriesSet.clear();
                }
                for (DataSnapshot category: dataSnapshot.getChildren()) {
                    Preference value = category.getValue(Preference.class);
                    value.setCid(category.getKey());
                    value.setUid(uid);
                    preferredCategories.add(value);
                }
                getAvailableCategories();
            }

            private void getAvailableCategories() {
                if (categoriesReference == null){
                    categoriesReference = FirebaseConfig.getDatabaseReference()
                            .child("categories");
                }
                categoriesReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot category: dataSnapshot.getChildren()) {
                            Category value = category.getValue(Category.class);
                            value.setCid(category.getKey());
                            if (preferredCategories.size() == 0){
                                availableCategoriesSet.add(value);
                            }else{
                                for (int i = 0; i < preferredCategories.size(); i++){
                                    if (preferredCategories.get(i).getCid().equals(value.getCid())){
                                        preferredCategories.get(i).setTag(value.getTag());
                                        break;
                                    }
                                    if (preferredCategories.size() == i+1){
                                        availableCategoriesSet.add(value);
                                    }
                                }
                            }
                            availableCategories.clear();
                            availableCategories.addAll(availableCategoriesSet);
                            preferencesIndex = preferredCategories.size();
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        if (databaseError.getMessage() != null){
                            Utils.showAlertModal(PreferencesActivity.this, databaseError.getMessage(), null);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (databaseError.getMessage() != null){
                    Utils.showAlertModal(PreferencesActivity.this, databaseError.getMessage(), null);
                }
            }
        });
    }

    @OnClick(R.id.preferenceSelectionFab)
    public void onClickSaveButton(){
        if (preferredCategories.size() == 0 || preferencesIndex == preferredCategories.size()){
            Utils.showAlertModal(this, "Nenhuma categoria foi selecionada.", null);
        }else{
            preferredCategories.get(0).save(preferredCategories);
            preferredCategories.clear();
            final FragmentCustomModal customModal = FragmentCustomModal.getInstance(this, R.layout.diolog_alert);
            ((TextView) customModal.getView().findViewById(R.id.dialog_alert_title)).setText("Sucesso");
            ((TextView) customModal.getView().findViewById(R.id.dialog_alert_message)).setText("Suas preferências foram salvas com sucesso.");
            customModal.show();
            (customModal.getView().findViewById(R.id.dialog_alert_button_ok)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    customModal.hide();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (Utils.isLoggedIn()){
            inflater.inflate(R.menu.menu_logged_in, menu);
        }else{
            inflater.inflate(R.menu.menu_logged_out, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_logout:
                Utils.logoutUser(this);
                return true;
            case R.id.item_signin:
                Utils.redirectTo(new Intent(this, LoginActivity.class), this);
                return true;
            case R.id.item_create_account:
                Utils.redirectTo(new Intent(this, SigninActivity.class), this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
