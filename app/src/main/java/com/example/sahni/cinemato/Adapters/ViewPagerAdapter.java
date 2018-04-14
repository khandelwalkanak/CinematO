package com.example.sahni.cinemato.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sahni.cinemato.Fragments.CelebsFragment;
import com.example.sahni.cinemato.Fragments.MovieFragment;
import com.example.sahni.cinemato.Fragments.StartActivityCallBack;
import com.example.sahni.cinemato.Fragments.TVFragment;

import java.util.List;

/**
 * Created by sahni on 23/3/18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final StartActivityCallBack callBack;
    FragmentManager fm;
    public ViewPagerAdapter(FragmentManager fm, StartActivityCallBack callBack) {
        super(fm);
        this.fm=fm;
        this.callBack=callBack;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position)
        {
            case 0: fragment= MovieFragment.newInstance(callBack);
                break;
            case 1: fragment= TVFragment.newInstance(callBack);
                break;
            case 2: fragment= CelebsFragment.newInstance(callBack);
                break;
        }
        return fragment;
    }
    @Override
    public int getCount() {
        return 3;
    }

    public void reload() {
        List<Fragment> fragments=fm.getFragments();
        for(int i=0;i<fragments.size();i++)
        {
            if(fragments.get(i) instanceof MovieFragment)
                ((MovieFragment) fragments.get(i)).updateAll();
        }
    }
}
