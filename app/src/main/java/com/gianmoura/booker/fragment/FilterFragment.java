package com.gianmoura.booker.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gianmoura.booker.R;
import com.gianmoura.booker.helper.FragmentCustomModal;
import com.gianmoura.booker.helper.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterFragment extends Fragment {

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.filterCollectionFab)
    public void onClickFab(){
        if(!Utils.isLoggedIn()){
            Utils.showBlockedAccessMessage(getContext());
        }else{
            Utils.showAlertModal(getContext(), "Não existe nenhuma recomendação disponivel.", null);
        }
    }

}
