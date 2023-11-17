package com.prashant.zomatov2.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prashant.zomatov2.R;
import com.prashant.zomatov2.databinding.FragmentAccountBinding;
import com.prashant.zomatov2.databinding.FragmentHomeBinding;


public class AccountFragment extends Fragment {

    FragmentAccountBinding bind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = FragmentAccountBinding.inflate(inflater, container, false);

        return bind.getRoot();
    }
}