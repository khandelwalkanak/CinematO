package com.example.sahni.cinemato.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.sahni.cinemato.Adapters.ViewPagerAdapter;
import com.example.sahni.cinemato.Constant;
import com.example.sahni.cinemato.Fragments.StartActivityCallBack;
import com.example.sahni.cinemato.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, StartActivityCallBack {
    ViewPager tabbedView;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        DrawerLayout drawer= findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tabLayout = findViewById(R.id.tabLayout);
        tabbedView = findViewById(R.id.tabbedView);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
        tabbedView.setAdapter(adapter);
        tabLayout.setupWithViewPager(tabbedView);
        tabLayout.getTabAt(0).setText("Movies");
        tabLayout.getTabAt(1).setText("Tv");
        tabLayout.getTabAt(2).setText("Celebs");
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.Content));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabbedView.setOffscreenPageLimit(adapter.getCount());
    }

    public void searchActivity(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        Bundle bundle;
        switch (id){
            case R.id.favourite_m :
                bundle=new Bundle();
                bundle.putInt(Constant.TYPE_KEY,Constant.LIST);
                bundle.putInt(Constant.LIST_TYPE,Constant.FAVOURITE_LIST);
                bundle.putInt(Constant.MOVIE_TV_GENRE,Constant.MOVIE);
                start(DisplayActivity.class,bundle);
                break;
            case R.id.favourite_g :
                bundle=new Bundle();
                bundle.putInt(Constant.TYPE_KEY,Constant.LIST);
                bundle.putInt(Constant.LIST_TYPE,Constant.FAVOURITE_LIST);
                bundle.putInt(Constant.MOVIE_TV_GENRE,Constant.GENRE);
                start(DisplayActivity.class,bundle);
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.reload();
    }

    @Override
    public void start(Class<?> cls, Bundle bundle) {
        Intent intent=new Intent(this,cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
