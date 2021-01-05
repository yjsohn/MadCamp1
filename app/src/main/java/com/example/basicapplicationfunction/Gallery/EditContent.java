package com.example.basicapplicationfunction.Gallery;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.basicapplicationfunction.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//수정->저장
public class EditContent extends AppCompatActivity {
    DBHelper MyDB;
    TextView date_tv;
    TextView loc_tv;
    TextView description_tv;

    EditText date_edit;
    EditText loc_edit;
    EditText desc_edit;

    ImageView imageView;
    ImageButton save_btn;
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

        //TextView
        date_tv = findViewById(R.id.date_tv);
        loc_tv = findViewById(R.id.location_tv);
        description_tv = findViewById(R.id.description_tv);
        //EditText
        date_edit = findViewById(R.id.date_edit);
        loc_edit = findViewById(R.id.location_edit);
        desc_edit = findViewById(R.id.description_edit);

        imageView = findViewById(R.id.detailed_image2);
        GradientDrawable drawable= (GradientDrawable) getDrawable(R.drawable.background_rounding);
        imageView.setBackground(drawable);
        imageView.setClipToOutline(true);

        File tempFile = new File(ImageInfo[0]);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        imageView.setImageBitmap(originalBm);
        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(view -> {
            String date = date_edit.getText().toString();
            String loc = loc_edit.getText().toString();
            String description = desc_edit.getText().toString();
            String updatedInfo[] = {date, loc, description};

            values.put("date", date);
            values.put("location", loc);
            values.put("description", description);
            db.update("images", values,"path=?", new String[]{ImageInfo[0]});

            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", updatedInfo);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        if(ImageInfo[1].equals(""))
            date_edit.setHint("날짜를 입력해주세요");
        if(ImageInfo[2].equals(""))
            loc_edit.setHint("위치를 입력해주세요");
        if(ImageInfo[3].equals(""))
            desc_edit.setHint("내용을 입력해주세요");

        date_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                } else {
                    SelectDate(v);
                }
            }
        });

        date_edit.setText(ImageInfo[1]);
        loc_edit.setText(ImageInfo[2]);
        desc_edit.setText(ImageInfo[3]);
    }
    public void SelectDate(View v) {
        String date = date_edit.getText().toString();
        DatePickerDialog dialog = new DatePickerDialog(this, R.style.DialogTheme);
        dialog.show();
        dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                date_edit.setText(date);
            }
        });
        dialog.getDatePicker().setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = "" + year + "." + (monthOfYear + 1) + "." + dayOfMonth;
                date_edit.setText(date);
            }
        });
    }
}