package com.gianmoura.booker.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gianmoura.booker.R;
import com.gianmoura.booker.activity.BookRegisterActivity;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.helper.BackgroundTask;
import com.gianmoura.booker.helper.FragmentCustomModal;
import com.gianmoura.booker.helper.Utils;
import com.gianmoura.booker.model.ImageLinks;
import com.gianmoura.booker.model.VolumeInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchCollectionAdapter extends
        RecyclerView.Adapter<CollectionAdapter.InnerViewHolder>
{
    private final List<VolumeInfo> collection;
    private Context context;

    public SearchCollectionAdapter(
            @NonNull final List<VolumeInfo> list )
    {
        this.collection = list;
    }

    public static class InnerViewHolder
            extends
            RecyclerView.ViewHolder
    {
        @BindView(R.id.list_book_image)
        ImageView bookImageView;
        @BindView(R.id.list_book_title)
        TextView bookTitleView;
        @BindView(R.id.list_book_authors)
        TextView bookAuthorsView;
        @BindView(R.id.list_book_categories)
        TextView bookCategoriesView;
        @BindView(R.id.list_book_quantity)
        TextView bookQuantityView;
        @BindView(R.id.list_book_value)
        TextView bookValueView;
        public View view;

        public InnerViewHolder(
                @NonNull final View itemView )
        {
            super( itemView );
            ButterKnife.bind(this, itemView);
            view = itemView;

        }
    }

    @NonNull
    @Override
    public CollectionAdapter.InnerViewHolder onCreateViewHolder(
            @NonNull final ViewGroup viewGroup,
            final int i )
    {
        context = viewGroup.getContext();
        final View view = LayoutInflater.from( context ).inflate( R.layout.list_book_collection, viewGroup,
                false );
        final CollectionAdapter.InnerViewHolder innerViewHolder = new CollectionAdapter.InnerViewHolder( view );
        return innerViewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull final CollectionAdapter.InnerViewHolder innerViewHolder,
            final int position )
    {
        if(collection.size() > 0){
            Utils.checkNullFields(collection);
            final VolumeInfo volumeInfo = collection.get(position);
            Picasso.get().load(volumeInfo.getImageLinks().getThumbnail()).into(innerViewHolder.bookImageView);
            innerViewHolder.bookTitleView.setText(volumeInfo.getTitle());
            innerViewHolder.bookAuthorsView.setText("Autores: " + volumeInfo.getAuthors().toString());
            innerViewHolder.bookCategoriesView.setText("Categorias: " + volumeInfo.getCategories().toString());

            innerViewHolder.view.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(
                        final View v )
                {
                    final FragmentCustomModal removeModal = FragmentCustomModal.getInstance(context, R.layout.dialog_confirmaton);
                    TextView message = removeModal.getView().findViewById(R.id.dialog_confirmation_message);
                    ((TextView)removeModal.getView().findViewById(R.id.dialog_confirmation_title)).setText("Adicionar");
                    message.setText("Deseja incluir esta obra em sua coleção?");

                    (removeModal.getView().findViewById(R.id.dialog_confirmation_button_no)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeModal.hide();
                        }
                    });
                    (removeModal.getView().findViewById(R.id.dialog_confirmation_button_yes)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new SearchTask(context, collection.get(position)).execute();
                            removeModal.hide();
                        }
                    });
                    removeModal.show();
                }
            } );
        }
    }

    private class SearchTask extends BackgroundTask {
        private VolumeInfo volumeInfo;

        public SearchTask(Context context, VolumeInfo volumeInfo) {
            super(context);
            this.volumeInfo = volumeInfo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            saveVolumeInfoAndRedirect(volumeInfo);
            super.doInBackground(params);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    private void saveVolumeInfoAndRedirect(final VolumeInfo volumeInfo) {
        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference()
                .child("volumesInfo");

        if (volumeInfo.getCategories().size() == 0){
            ArrayList<String> defaultCategory = new ArrayList<>();
            if (volumeInfo.getAuthors().size() > 0){
                for (String author : volumeInfo.getAuthors()){
                    defaultCategory.add(author);
                }
            }
            defaultCategory.add(volumeInfo.getTitle());
            volumeInfo.setCategories(defaultCategory);
        }

        Query query = databaseReference.orderByChild("title").equalTo(volumeInfo.getTitle());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String bid;
                if (dataSnapshot.getValue() != null){
                    HashMap<String,VolumeInfo> value = (HashMap<String, VolumeInfo>)dataSnapshot.getValue();
                    bid = value.toString().split("\\=")[0].substring(1);
                    volumeInfo.setBid(bid);
                    volumeInfo.save();
                }else{
                    bid = volumeInfo.save();
                }
                if (bid == null){
                    Utils.showAlertModal(context, "Não foi possível recuperar os dados do livro selecionado, tente novamente por favor.", null);
                }else{
                    Utils.redirectTo(new Intent(context, BookRegisterActivity.class).putExtra("bid", bid), context);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (databaseError.getMessage() != null){
                    Utils.showAlertModal(context, databaseError.getMessage(), null);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return collection.size();
    }
}

