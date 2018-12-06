package com.gianmoura.booker.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gianmoura.booker.R;
import com.gianmoura.booker.activity.PreferencesActivity;
import com.gianmoura.booker.adapter.PreferencesAdapter;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.helper.Utils;
import com.gianmoura.booker.model.Category;
import com.gianmoura.booker.model.Preference;
import com.gianmoura.booker.model.VolumeInfo;
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


public class PreferencesFragment extends Fragment {

    @BindView(R.id.preferenceList)
    RecyclerView preferenceListView;
    @BindView(R.id.preferenceDistanceConfig)
    SeekBar preferenceDistance;
    @BindView(R.id.preferenceDistanceText)
    TextView preferenceDistanceText;
    private DatabaseReference categoriesReference;
    private DatabaseReference preferencesReference;
    private List<Preference> preferences;
    private Set<Preference> preferencesSet;
    private RecyclerView.Adapter adapter;
    private int maxDistance;

    public PreferencesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preferences, container, false);

        ButterKnife.bind(this, view);

        preferences = new ArrayList<>();
        preferencesSet = new ArraySet<>();
        adapter = new PreferencesAdapter(preferences);
        preferenceListView.setLayoutManager( new LinearLayoutManager( getActivity() ) );
        preferenceListView.setAdapter(adapter);
        maxDistance = Utils.getMaxDistance(getContext());
        if (maxDistance == 200){
            preferenceDistanceText.setText("Distância Ilimitada");
        }else {
            preferenceDistanceText.setText(maxDistance+" Km");
        }
        preferenceDistance.setProgress(maxDistance);
        preferenceDistance.setMax(200);
        preferenceDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((maxDistance = progress) < 1)maxDistance = 1;
                if (maxDistance == 200){
                    preferenceDistanceText.setText("Distância Ilimitada");
                }else {
                    preferenceDistanceText.setText(maxDistance+" Km");
                }
                Utils.saveMaxDistance(maxDistance, getContext());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferences();
    }

    private void getPreferences() {
        final String uid = Utils.getLoggedUid();
        if (preferencesReference == null){
            preferencesReference = FirebaseConfig.getDatabaseReference()
                    .child("preferences").child(uid);
        }
        preferencesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (preferences != null){
                    preferences.clear();
                }
                for (DataSnapshot category: dataSnapshot.getChildren()) {
                    Preference value = category.getValue(Preference.class);
                    value.setCid(category.getKey());
                    value.setUid(uid);
                    getCategoryTag(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (databaseError.getMessage() != null){
                    Utils.showAlertModal(getActivity(), databaseError.getMessage(), null);
                }
            }

            private void getCategoryTag(final Preference preference) {
                categoriesReference = FirebaseConfig.getDatabaseReference()
                        .child("categories").child(preference.getCid());

                categoriesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null){
                            Object value = dataSnapshot.getValue(Category.class);
                            Category category = (Category) value;
                            preference.setTag(category.getTag());
                            preferencesSet.add(preference);
                            preferences.clear();
                            preferences.addAll(preferencesSet);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        if (databaseError.getMessage() != null){
                            Utils.showAlertModal(getActivity(), databaseError.getMessage(), null);
                        }
                    }
                });
            }
        });
    }

    @OnClick(R.id.preferenceFab)
    public void onClickFab(){
        startActivity(new Intent(getActivity(), PreferencesActivity.class));
    }

}
