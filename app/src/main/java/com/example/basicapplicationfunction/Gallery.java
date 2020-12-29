package com.example.basicapplicationfunction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Gallery extends Fragment {
    View view;
    public Gallery() {
        // Required empty public constructor
    }


    public static Gallery newInstance() {
        Gallery gallery = new Gallery();
        return gallery;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gallery, container, false);
        return view;
    }
}
