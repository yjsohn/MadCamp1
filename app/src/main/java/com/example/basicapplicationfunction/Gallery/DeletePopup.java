package com.example.basicapplicationfunction.Gallery;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basicapplicationfunction.R;

public class DeletePopup extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_delete_popup);
    }
}
