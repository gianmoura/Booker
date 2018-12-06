package com.gianmoura.booker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gianmoura.booker.R;
import com.gianmoura.booker.helper.BackgroundTask;
import com.gianmoura.booker.helper.FragmentCustomModal;
import com.gianmoura.booker.helper.Utils;
import com.gianmoura.booker.model.VolumeInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionAdapter extends
        RecyclerView.Adapter<CollectionAdapter.InnerViewHolder>
{
    private final List<VolumeInfo> collection;
    private Context context;

    public CollectionAdapter(
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
    public InnerViewHolder onCreateViewHolder(
            @NonNull final ViewGroup viewGroup,
            final int i )
    {
        context = viewGroup.getContext();
        final View view = LayoutInflater.from( context ).inflate( R.layout.list_book_collection, viewGroup,
                false );
        final InnerViewHolder innerViewHolder = new InnerViewHolder( view );
        return innerViewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull final InnerViewHolder innerViewHolder,
            final int position )
    {
        if(collection.size() > 0){
            Utils.checkNullFields(collection);
            final VolumeInfo book = collection.get(position);
            Picasso.get().load(book.getImageLinks().getThumbnail()).into(innerViewHolder.bookImageView);
            innerViewHolder.bookTitleView.setText(book.getTitle());
            innerViewHolder.bookAuthorsView.setText("Autores: " + book.getAuthors().toString());
            innerViewHolder.bookCategoriesView.setText("Categorias: " + book.getCategories().toString());
            innerViewHolder.bookQuantityView.setText("Quantidade: " + String.valueOf(book.getOwner().getQuantity()));
            innerViewHolder.bookValueView.setText("Valor da Oferta: R$" + String.valueOf(book.getOwner().getValue()));

            innerViewHolder.view.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(
                        final View v )
                {
                    final FragmentCustomModal removeModal = FragmentCustomModal.getInstance(context, R.layout.dialog_confirmaton);
                    TextView message = removeModal.getView().findViewById(R.id.dialog_confirmation_message);
                    ((TextView)removeModal.getView().findViewById(R.id.dialog_confirmation_title)).setText("Remover");
                    message.setText("Deseja excluir esta obra da sua coleção?");

                    (removeModal.getView().findViewById(R.id.dialog_confirmation_button_no)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeModal.hide();
                        }
                    });
                    (removeModal.getView().findViewById(R.id.dialog_confirmation_button_yes)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeModal.hide();
                            confirmDelete();
                        }
                    });
                    removeModal.show();
                }

                private void confirmDelete() {
                    final FragmentCustomModal confirmModal = FragmentCustomModal.getInstance(context, R.layout.dialog_confirmaton);
                    TextView message = confirmModal.getView().findViewById(R.id.dialog_confirmation_message);
                    message.setText("Confirma exclusão ?");
                    Button noButton = confirmModal.getView().findViewById(R.id.dialog_confirmation_button_no);
                    noButton.setText("Cancelar");
                    noButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmModal.hide();
                        }
                    });
                    Button yesButton = confirmModal.getView().findViewById(R.id.dialog_confirmation_button_yes);
                    yesButton.setText("Excluir Obra");
                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmModal.hide();
                            book.getOwner().delete(context);
                            collection.remove(position);
                            new DeleteTask(context).execute();
                            notifyDataSetChanged();
                        }
                    });
                    confirmModal.show();
                }

                class DeleteTask extends BackgroundTask {

                    public DeleteTask(Context context) {
                        super(context);
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        super.onPostExecute(result);
                    }
                }
            } );
        }
    }





    @Override
    public int getItemCount()
    {
        return collection.size();
    }
}

