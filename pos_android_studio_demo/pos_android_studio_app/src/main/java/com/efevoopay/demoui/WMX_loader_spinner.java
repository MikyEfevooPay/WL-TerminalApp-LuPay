package com.efevoopay.demoui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class WMX_loader_spinner extends Fragment {

    ProgressBar pb;

    public WMX_loader_spinner() {

    }

    public static WMX_loader_spinner getInstance() {
        WMX_loader_spinner fragment = new WMX_loader_spinner();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wmx_loader_spinner, container, false);
        return view;
    }
}