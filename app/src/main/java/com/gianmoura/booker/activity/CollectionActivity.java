package com.gianmoura.booker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.gianmoura.booker.R;
import com.gianmoura.booker.helper.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionActivity extends Activity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        ButterKnife.bind(this);

        //Configura Toolbar
        toolbar.setTitle("Booker");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.icons));
        setActionBar(toolbar);
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
                Utils.redirectTo(new LoginActivity(), this);
                return true;
            case R.id.item_create_account:
                Utils.redirectTo(new SigninActivity(), this);
                return true;
            case R.id.item_about:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
