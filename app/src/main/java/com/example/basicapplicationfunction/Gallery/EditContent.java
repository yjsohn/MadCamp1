package com.example.basicapplicationfunction.Gallery;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basicapplicationfunction.R;

import java.io.File;

//수정->저장
public class EditContent extends AppCompatActivity {
    DBHelper MyDB;
    TextView date_tv;
    TextView loc_tv;
    TextView description_tv;
    ImageView imageView;
    Button save_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_edit);
        String ImageInfo[] = getIntent().getStringArrayExtra("DETAIL_INFO");
        SQLiteDatabase db;
        //가져온 value를  edit view 내용으로 저장
        MyDB = new DBHelper(this);
        db = MyDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        date_tv = findViewById(R.id.date_tv);
        loc_tv = findViewById(R.id.location_tv);
        description_tv = findViewById(R.id.description_tv);
        imageView = findViewById(R.id.detailed_image2);
        File tempFile = new File(ImageInfo[0]);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        imageView.setImageBitmap(originalBm);
        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(view -> {
            String date = date_tv.getText().toString();
            String loc = loc_tv.getText().toString();
            String description = description_tv.getText().toString();
            String updatedInfo[] = {date, loc, description};

            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", updatedInfo);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        date_tv.setText(ImageInfo[1]);
        loc_tv.setText(ImageInfo[2]);
        description_tv.setText(ImageInfo[3]);
    }
}
