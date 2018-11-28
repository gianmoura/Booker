package com.gianmoura.booker.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gianmoura.booker.R;
import com.gianmoura.booker.activity.PreferencesActivity;
import com.gianmoura.booker.adapter.CollectionAdapter;
import com.gianmoura.booker.adapter.PreferencesAdapter;
import com.gianmoura.booker.helper.Utils;
import com.gianmoura.booker.model.Book;
import com.gianmoura.booker.model.Preference;
import com.google.android.gms.flags.impl.SharedPreferencesFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;


public class PreferencesFragment extends Fragment {

    @BindView(R.id.preferenceList)
    RecyclerView preferenceListView;
    @BindView(R.id.preferenceDistanceConfig)
    SeekBar preferenceDistance;
    @BindView(R.id.preferenceDistanceText)
    TextView preferenceDistanceText;
    private List<Preference> preferences;
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
        adapter = new PreferencesAdapter(preferences);
        preferenceListView.setLayoutManager( new LinearLayoutManager( getActivity() ) );
        preferenceListView.setAdapter(adapter);
        getPreferences();
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

    private void getPreferences() {

        Preference preference = new Preference();
        preference.setUid("User");
        preference.setCid("Ficção");
        preference.setActivity(1);

        preferences.clear();
        preferences.add(preference);
        preferences.add(preference);
        preferences.add(preference);
        preferences.add(preference);
        preferences.add(preference);
        preferences.add(preference);
        preferences.add(preference);
        preferences.add(preference);
        preferences.add(preference);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.preferenceFab)
    public void onClickFab(){
        startActivity(new Intent(getActivity(), PreferencesActivity.class));
    }

}
