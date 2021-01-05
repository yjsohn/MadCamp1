package com.example.basicapplicationfunction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.basicapplicationfunction.Gallery.Gallery;
import com.example.basicapplicationfunction.Temp.Alarm;
import com.example.basicapplicationfunction.Temp.AlarmRecyclerViewAdapter;
import com.example.basicapplicationfunction.Temp.AlarmsListFragment;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    private ArrayList <String> tabNames = new ArrayList<>();
    FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

    Fragment1 fragment1 = adapter.getFragment1();
    Fragment gallery = adapter.getItem(1);
    Fragment3 fragment3 = adapter.getFragment3();

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        AlarmsListFragment alarmsListFragment = (AlarmsListFragment) getSupportFragmentManager().findFragmentById(R.id.alarmsListFragment);

        if(alarmsListFragment != null) {
            alarmsListFragment.hideButton();
        }

        AlarmRecyclerViewAdapter alarmRecyclerViewAdapter = (AlarmRecyclerViewAdapter) AlarmsListFragment.getAlarmRecyclerViewAdapter();
        if(alarmRecyclerViewAdapter != null && alarmRecyclerViewAdapter.getButtonShow() == true){
            alarmRecyclerViewAdapter.notifyDataSetChanged();
            alarmRecyclerViewAdapter.setButtonShow(false);
        }

        Log.d("null", "null");
        return super.dispatchTouchEvent(ev);
    } */

    @Override
    public void onBackPressed(){
        AlarmRecyclerViewAdapter alarmRecyclerViewAdapter = (AlarmRecyclerViewAdapter) AlarmsListFragment.getAlarmRecyclerViewAdapter();
        if(AlarmsListFragment.getNavController() != null){
            AlarmsListFragment.getNavController().navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            AlarmsListFragment.setNavController(null);
        }
        else if(alarmRecyclerViewAdapter != null && alarmRecyclerViewAdapter.getButtonShow() == true){
            alarmRecyclerViewAdapter.setButtonShow(false);
            alarmRecyclerViewAdapter.getDeleteAlarms().clear();
            alarmRecyclerViewAdapter.notifyDataSetChanged();
            AlarmsListFragment.deleteAlarms.setVisibility(View.GONE);
        }
        else {

            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
        loadTabName();
        setTabLayout();
        setViewPager();

    }

    String[] permission_list = {
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.CALL_PHONE
    };

    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //허용됐다면
                if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                }
                else {
                    Toast.makeText(getApplicationContext(),"앱권한설정하세요",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //fragment1.refresh();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);

    }

    @TargetApi(Build.VERSION_CODES.N)
    private void setTabLayout(){
        tabLayout = findViewById(R.id.tabs);
        tabNames.stream().forEach(name ->tabLayout.addTab(tabLayout.newTab().setText(name)));
    }

    private void loadTabName(){
        tabNames.add("Contact");
        tabNames.add("Gallery");
        tabNames.add("Alarm");
    }
    private void setViewPager() {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}