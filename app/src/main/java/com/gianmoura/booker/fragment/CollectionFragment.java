package com.gianmoura.booker.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gianmoura.booker.R;
import com.gianmoura.booker.activity.CollectionActivity;
import com.gianmoura.booker.adapter.CollectionAdapter;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.helper.Utils;
import com.gianmoura.booker.model.Book;
import com.gianmoura.booker.model.VolumeInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CollectionFragment extends Fragment {

    @BindView(R.id.bookCollectionList)
    RecyclerView cListView;
    private Set<VolumeInfo> volumeInfoSet;
    private List<VolumeInfo> volumeInfoCollection;
    private List<Book> bookCollection;
    private RecyclerView.Adapter adapter;
    private DatabaseReference booksReference;

    public CollectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        ButterKnife.bind(this, view);

        volumeInfoSet = new ArraySet<>();
        volumeInfoCollection = new ArrayList<>();
        bookCollection =  new ArrayList<>();
        adapter = new CollectionAdapter(volumeInfoCollection);
        cListView.setLayoutManager( new LinearLayoutManager( getActivity() ) );
        cListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCollection();
    }

    private void getCollection(){
        final String uid = Utils.getLoggedUid();
        if (booksReference == null){
            booksReference = FirebaseConfig.getDatabaseReference()
                    .child("books").child(uid);
        }
        booksReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (bookCollection != null){
                    bookCollection.clear();
                }
                if (volumeInfoSet != null){
                    volumeInfoSet.clear();
                }
                for (DataSnapshot book: dataSnapshot.getChildren()) {
                    Book value = book.getValue(Book.class);
                    value.setBid(book.getKey());
                    value.setUid(uid);
                    bookCollection.add(value);
                }
                refreshVolumeCollection();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (databaseError.getMessage() != null){
                    Utils.showAlertModal(getActivity(), databaseError.getMessage(), null);
                }
            }
        });
    }

    private void refreshVolumeCollection() {
        DatabaseReference volumeInfoReference = FirebaseConfig.getDatabaseReference()
                .child("volumesInfo");
        for (final Book book: bookCollection) {
            Query query = volumeInfoReference.child(book.getBid());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Object value = dataSnapshot.getValue(VolumeInfo.class);
                    VolumeInfo volumeInfo = (VolumeInfo) value;
                    volumeInfo.setOwner(book);
                    volumeInfo.setBid(dataSnapshot.getKey());
                    volumeInfoSet.add(volumeInfo);
                    volumeInfoCollection.clear();
                    volumeInfoCollection.addAll(volumeInfoSet);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (databaseError.getMessage() != null){
                        Utils.showAlertModal(getActivity(), databaseError.getMessage(), null);
                    }
                }
            });
        }
    }

    @OnClick(R.id.bookCollectionFab)
    public void onClickFab(){
        startActivity(new Intent(getActivity(), CollectionActivity.class));
    }

}
