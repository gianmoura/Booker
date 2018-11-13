package com.gianmoura.booker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gianmoura.booker.R;
import com.gianmoura.booker.adapter.TabAdapter;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.fragment.CollectionFragment;
import com.gianmoura.booker.fragment.FilterFragment;
import com.gianmoura.booker.fragment.NegocationsFragment;
import com.gianmoura.booker.fragment.PreferencesFragment;
import com.gianmoura.booker.fragment.ProfileFragment;
import com.gianmoura.booker.helper.CustomViewPager;
import com.gianmoura.booker.helper.SlidingTabLayout;
import com.gianmoura.booker.helper.Utils;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.stl_main)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp_main)
    CustomViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (!Utils.isOnline(this)){
            startActivity(new Intent(this, AlertActivity.class));
            finish();
        }
        //Instancia TabLayout
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new FilterFragment(), R.drawable.ic_action_book);
        adapter.addFragment(new CollectionFragment(), R.drawable.ic_library_books);
        adapter.addFragment(new NegocationsFragment(), R.drawable.ic_message);
        adapter.addFragment(new PreferencesFragment(), R.drawable.ic_action_settings);
        adapter.addFragment(new ProfileFragment(), R.drawable.ic_action_user);

        viewPager.setAdapter(adapter);
        if (!Utils.isLoggedIn() && viewPager.isPagingEnabled()){
            Utils.showBlockedAccessMassage(this);
            viewPager.setPagingEnabled(false);
        }else if (Utils.isLoggedIn() && !viewPager.isPagingEnabled()){
            viewPager.setPagingEnabled(true);
        }
        //Configurar Tab Layout
        tabLayout.setViewPager(viewPager);
        tabLayout.setDistributeEvenly(true);
        tabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //Configura Toolbar
        toolbar.setTitle("Booker");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.icons));
        setSupportActionBar(toolbar);
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
                logoutUser();
                return true;
            case R.id.item_signin:
                redirectToLogin();
                return true;
            case R.id.item_settings:
                startActivity(new Intent(this, AddressActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void redirectToLogin(){
        startActivity(new Intent(this, LoginActivity.class));
    }
}
