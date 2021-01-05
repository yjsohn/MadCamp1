package com.example.basicapplicationfunction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicapplicationfunction.Gallery.DBHelper;
import com.example.basicapplicationfunction.Gallery.Picture;
import com.example.basicapplicationfunction.Gallery.PictureAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.basicapplicationfunction.Temp.Alarm;
import com.example.basicapplicationfunction.Temp.AlarmRecyclerViewAdapter;
import com.example.basicapplicationfunction.Temp.AlarmsListFragment;
import com.example.basicapplicationfunction.Temp.DeleteMode;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
//!!!!!파일 경로:  file:///storage/emulated/0/Pictures/building.jpg


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Fragment3 extends Fragment {

    View view;
    Context context;
    DBHelper MyDB;
    private static final String TAG = "blackjin";

    private Boolean isPermission = true;

    private static final int PICK_FROM_ALBUM = 1;
    private ArrayList<Picture> mArrayList;
    static private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentManager fragmentManager;

    private File tempFile;

    public Fragment3() {
        // Required empty public constructor
    }

    public static Fragment3 newInstance() {
        Fragment3 fragment3 = new Fragment3();
        return fragment3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Fragment", "Fragment3");
        view = inflater.inflate(R.layout.fragment3, container, false);
        context = view.getContext();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AlarmRecyclerViewAdapter alarmRecyclerViewAdapter = (AlarmRecyclerViewAdapter) AlarmsListFragment.getAlarmRecyclerViewAdapter();

        if (alarmRecyclerViewAdapter.getButtonShow() == true) {
            alarmRecyclerViewAdapter.setButtonShow(false);
            alarmRecyclerViewAdapter.getDeleteAlarms().clear();
            alarmRecyclerViewAdapter.notifyDataSetChanged();
            AlarmsListFragment.deleteAlarms.setVisibility(View.GONE);
        }
    }
}

