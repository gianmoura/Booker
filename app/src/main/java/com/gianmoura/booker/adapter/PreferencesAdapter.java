package com.gianmoura.booker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gianmoura.booker.R;
import com.gianmoura.booker.helper.FragmentCustomModal;
import com.gianmoura.booker.model.Preference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreferencesAdapter extends
        RecyclerView.Adapter<PreferencesAdapter.InnerViewHolder>
{
    private final List<Preference> preferences;
    private Context context;

    public PreferencesAdapter(
            @NonNull final List<Preference> list )
    {
        this.preferences = list;
    }

    public static class InnerViewHolder
            extends
            RecyclerView.ViewHolder
    {
        @BindView(R.id.list_preferences_category)
        TextView preferenceCategoryView;
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
        final View view = LayoutInflater.from( context ).inflate( R.layout.list_preferences, viewGroup,
                false );
        final InnerViewHolder innerViewHolder = new InnerViewHolder( view );
        return innerViewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull final InnerViewHolder innerViewHolder,
            final int position )
    {
        if(preferences.size() > 0){
            final Preference preference = preferences.get(position);
            innerViewHolder.preferenceCategoryView.setText(preference.getCid());

            innerViewHolder.view.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(
                        final View v )
                {
                    final FragmentCustomModal removeModal = FragmentCustomModal.getInstance(context, R.layout.dialog_confirmaton);
                    TextView message = removeModal.getView().findViewById(R.id.dialog_confirmation_message);
                    ((TextView)removeModal.getView().findViewById(R.id.dialog_confirmation_title)).setText("Remover");
                    message.setText("Deseja remover esta categoria de suas preferências?");

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
                    message.setText("Confirma remoção ?");
                    Button noButton = confirmModal.getView().findViewById(R.id.dialog_confirmation_button_no);
                    noButton.setText("Cancelar");
                    noButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmModal.hide();
                        }
                    });
                    Button yesButton = confirmModal.getView().findViewById(R.id.dialog_confirmation_button_yes);
                    yesButton.setText("Remover");
                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmModal.hide();
                            preferences.remove(position);
//                            preference.delete(context);
                            notifyDataSetChanged();
                        }
                    });
                    confirmModal.show();
                }
            } );
        }
    }

    @Override
    public int getItemCount()
    {
        return preferences.size();
    }
}