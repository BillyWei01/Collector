package com.horizon.collector.main;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.horizon.base.ui.BaseActivity;
import com.horizon.collector.R;
import com.horizon.collector.download.DownloadFragment;
import com.horizon.collector.huaban.ui.HuabanFragment;
import com.horizon.collector.setting.SettingActivity;
import com.horizon.collector.setting.model.UserSetting;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String mCurrentTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(savedInstanceState);
    }

    protected void initView(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            String tag = UserSetting.INSTANCE.getLastShowingFragment();
            showFragment(TextUtils.isEmpty(tag) ? HuabanFragment.TAG : tag);
        }
    }

    private void showFragment(String tag) {
        if (TextUtils.equals(tag, mCurrentTag)) {
            return;
        }

        UserSetting.INSTANCE.setLastShowingFragment(tag);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null) {
            if(DownloadFragment.TAG.equals(tag)){
                fragment = new DownloadFragment();
            }else {
                fragment = HuabanFragment.newInstance();
            }
            transaction.add(R.id.main_fragment_container, fragment, tag);
        } else {
            transaction.show(fragment);
        }
        if (!TextUtils.isEmpty(mCurrentTag)) {
            Fragment f = manager.findFragmentByTag(mCurrentTag);
            if(f != null){
                transaction.hide(f);
            }
        }
        transaction.commit();

        mCurrentTag = tag;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(SettingActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_download) {
            showFragment(DownloadFragment.TAG);
        } else if (id == R.id.nav_huaban) {
            showFragment(HuabanFragment.TAG);
        } else if (id == R.id.nav_setting) {
            startActivity(SettingActivity.class);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
