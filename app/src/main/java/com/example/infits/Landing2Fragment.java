package com.example.infits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Landing2Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ViewGroup rootView =(ViewGroup)inflater.inflate(R.layout.landing2,container,false);
        return rootView;
    }
}
