package com.example.sahni.cinemato.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sahni.cinemato.R;

/**
 * Created by sahni on 24/3/18.
 */

public class CelebsFragment extends android.support.v4.app.Fragment {
    //CallBack
    private StartActivityCallBack callBack;

    public CelebsFragment() {

    }
    public static CelebsFragment newInstance(StartActivityCallBack callBack)
    {
        CelebsFragment fragment=new CelebsFragment();
        fragment.callBack=callBack;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_celebs,container,false);
        return rootView;
    }
}
