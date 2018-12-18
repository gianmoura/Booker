package com.gianmoura.booker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.gianmoura.booker.R;
import com.gianmoura.booker.adapter.SearchCollectionAdapter;
import com.gianmoura.booker.helper.BackgroundTask;
import com.gianmoura.booker.helper.Utils;
import com.gianmoura.booker.model.BookList;
import com.gianmoura.booker.model.Item;
import com.gianmoura.booker.model.VolumeInfo;
import com.gianmoura.booker.service.google.book.ApiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionActivity extends Activity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.createBookCollection_search_text)
    TextView searchText;
    @BindView(R.id.createBookCollection_list)
    RecyclerView cListView;
    private List<VolumeInfo> collection;
    private List<Item> itemList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        Utils.checkDeviceConnection(this);
        ButterKnife.bind(this);

        searchText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchText.setRawInputType(InputType.TYPE_CLASS_TEXT);

        //Configura Toolbar
        toolbar.setTitle("Booker");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.icons));
        setActionBar(toolbar);

        collection = new ArrayList<>();
        adapter = new SearchCollectionAdapter(collection);
        cListView.setLayoutManager( new LinearLayoutManager( this ) );
        cListView.setAdapter(adapter);
    }

    @OnClick(R.id.createBookCollection_search_button)
    public void onClickIsbnButton(){
        if (searchText.getText().length() == 0){
            Utils.showAlertModal(this, "Existem campos obrigatórios não preenchidos.", null);
            return;
        }else{
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
            new SearchTask(this).execute();
        }
    }

    private class SearchTask extends BackgroundTask {

        public SearchTask(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            searchBooks();
            super.doInBackground(params);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    private void searchBooks() {
        Call<BookList> call = new ApiHelper().getService().getBooks(searchText.getText().toString());
        call.enqueue(new Callback<BookList>() {
            @Override
            public void onResponse(Call<BookList> call, Response<BookList> response) {
                if(response.isSuccessful()){
                    itemList = response.body().getItems();
                    collection.clear();
                    for (int i = 0; i < itemList.size(); i++){
                        collection.add(itemList.get(i).getVolumeInfo());
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    Utils.showAlertModal(CollectionActivity.this, "Houve um erro durante a busca de livros no Google Books API.", null);
                    return;
                }
            }

            @Override
            public void onFailure(Call<BookList> call, Throwable t) {
                Utils.showAlertModal(CollectionActivity.this, "Houve um erro durante a busca de livros no Google Books API.", null);
                return;
            }
        });
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
