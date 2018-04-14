package com.example.sahni.cinemato.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sahni.cinemato.Constant;
import com.example.sahni.cinemato.DataBase.DatabaseClient;
import com.example.sahni.cinemato.Fragments.ListDisplayFragment;
import com.example.sahni.cinemato.Fragments.StartActivityCallBack;
import com.example.sahni.cinemato.R;

public class DisplayActivity extends AppCompatActivity implements StartActivityCallBack {

    private Bundle bundle;
    int type;
    Toolbar toolbar;
    DatabaseClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        client=DatabaseClient.getInstance(this);

        bundle=getIntent().getExtras();
        type=bundle.getInt(Constant.TYPE_KEY);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        createPage(type);
    }

    private void createPage(int type) {
        switch (type){
            case Constant.LIST:
                setList();
                break;
        }
    }

    private void setList() {
        ListDisplayFragment fragment=new ListDisplayFragment();
        fragment.setArguments(bundle);
        fragment.supportActionBar =getSupportActionBar();
        fragment.callback=this;
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return true;
    }

    @Override
    public void start(Class<?> cls, Bundle bundle) {
        Intent intent=new Intent(this,cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
