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
import com.gianmoura.booker.helper.FragmentCustomModal;
import com.gianmoura.booker.model.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendationAdapter extends
        RecyclerView.Adapter<RecommendationAdapter.InnerViewHolder>
{
    private final List<Book> collection;
    private Context context;

    public RecommendationAdapter(
            @NonNull final List<Book> list )
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
            final Book book = collection.get(position);
            Picasso.get().load(book.getSmallThumbnail()).into(innerViewHolder.bookImageView);
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
                    showOfferDialog(context, book);
                }
            } );
        }
    }

    private void showOfferDialog(final Context context, final Book book) {
        final FragmentCustomModal customModal = FragmentCustomModal.getInstance(context, R.layout.dialog_offer);
        View view = customModal.getView();
        ImageView imageView = view.findViewById(R.id.dialog_offer_image);
        TextView bookDescriptionView = view.findViewById(R.id.dialog_offer_book_description);
        bookDescriptionView.setText(book.getOwner().getDescription());
        Picasso.get().load(book.getThumbnail()).into(imageView);
        customModal.show();

        Button offer = customModal.getView().findViewById(R.id.dialog_offer_button_yes);
        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                createOffer();
                customModal.hide();
            }
        });
        Button cancel = customModal.getView().findViewById(R.id.dialog_offer_button_no);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customModal.hide();
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return collection.size();
    }
}


