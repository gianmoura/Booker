package com.gianmoura.booker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.gianmoura.booker.R;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.helper.BackgroundTask;
import com.gianmoura.booker.helper.CustomViewPager;
import com.gianmoura.booker.helper.FragmentCustomModal;
import com.gianmoura.booker.helper.Utils;
import com.gianmoura.booker.model.Book;
import com.gianmoura.booker.model.Category;
import com.gianmoura.booker.model.VolumeInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookRegisterActivity extends Activity {
    private String bid;
    private VolumeInfo volumeInfo;
    @BindView(R.id.createBookRegister_image)
    ImageView imageView;
    @BindView(R.id.createBookRegister_quantity)
    EditText quantityEditText;
    @BindView(R.id.createBookRegister_price)
    EditText priceEditText;
    @BindView(R.id.createBookRegister_description)
    EditText descriptionEditText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_register);
        Utils.checkDeviceConnection(this);
        ButterKnife.bind(this);

        //Configura Toolbar
        toolbar.setTitle("Booker");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.icons));
        setActionBar(toolbar);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                bid = null;
            } else {
                bid = extras.getString("bid");
            }
        } else {
            bid = (String) savedInstanceState.getSerializable("bid");
        }

        if (bid == null || bid == ""){
            final FragmentCustomModal customModal = FragmentCustomModal.getInstance(this, R.layout.diolog_alert);
            ((TextView) customModal.getView().findViewById(R.id.dialog_alert_message)).setText("Houve um problema na recuperação do livro selecionado, por favor tente selecionar novamente.");
            customModal.show();
            (customModal.getView().findViewById(R.id.dialog_alert_button_ok)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    customModal.hide();
                }
            });
        }

        descriptionEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        descriptionEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);

        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference()
                .child("volumesInfo").child(bid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue(VolumeInfo.class);
                volumeInfo = ((VolumeInfo) value);
                if (volumeInfo != null){
                    Picasso.get().load(volumeInfo.getImageLinks().getThumbnail()).into(imageView);
                }else{
                    Utils.showAlertModal(BookRegisterActivity.this, "Não foi possível recuperar as informações do livro selecionado.", null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (databaseError.getMessage() != null){
                    Utils.showAlertModal(BookRegisterActivity.this, databaseError.getMessage(), null);
                }
            }
        });
    }

    @OnClick(R.id.createBookRegister_button)
    public void onClickRegisterButton(){
        if(quantityEditText.getText().length() == 0 || priceEditText.getText().length() == 0 || descriptionEditText.getText().length() == 0){
            Utils.showAlertModal(this, "Existem campos obrigatórios não preenchidos.", null);
            return;
        }
        new SaveTask(this).execute();
    }

    private class SaveTask extends BackgroundTask {

        public SaveTask(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            saveCategories();
            super.doInBackground(params);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    private void saveCategories(){

        final List<Category> categories = new ArrayList<>();
        final DatabaseReference categoriesReference = FirebaseConfig.getDatabaseReference()
                .child("categories");

        for (final String tag: volumeInfo.getCategories()) {
            final Category category = new Category();
            category.setTag(tag);

            Query query = categoriesReference.orderByChild("tag").equalTo(tag);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null){
                        //pega cid existente e seta na categoria
                        HashMap<String,Category> value = (HashMap<String, Category>)dataSnapshot.getValue();
                        String cid = value.toString().split("\\=")[0].substring(1);
                        category.setCid(cid);
                        categories.add(category);
                    }else{
                        categories.add(category);
                    }
                    if (categories.size() == volumeInfo.getCategories().size()){
                        category.save(categories);
                        saveBook();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (databaseError.getMessage() != null){
                        Utils.showAlertModal(BookRegisterActivity.this, databaseError.getMessage(), null);
                    }
                }
            });
//            DatabaseReference volumesInfoReference = FirebaseConfig.getDatabaseReference()
//                    .child("volumesInfo");
//            final List<String> bids = new ArrayList<>();
//            Query query = volumesInfoReference.orderByValue().equalTo(tag);
//            query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.getChildren() != null){
//                        if (bids != null){
//                            bids.clear();
//                        }
//                        for (DataSnapshot data: dataSnapshot.getChildren()) {
//                            HashMap<String, VolumeInfo> value = (HashMap<String, VolumeInfo>) data.getValue();
//                            bids.add(value.toString().split("\\=")[0].substring(1));
//                        }
//                        category.setBooks(bids);
//                        getOrCreateCid();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    if (databaseError.getMessage() != null){
//                        Utils.showAlertModal(BookRegisterActivity.this, databaseError.getMessage(), null);
//                    }
//                }
//
//                private void getOrCreateCid() {
//
//                }
//            });
        }

    }

    private void saveBook() {
        Book book = new Book();
        book.setUid(Utils.getLoggedUser().getUid());
        book.setBid(bid);
        book.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
        book.setValue(Double.valueOf(priceEditText.getText().toString()));
        book.setDescription(descriptionEditText.getText().toString());
        book.save(this);
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
            case R.id.item_about:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
