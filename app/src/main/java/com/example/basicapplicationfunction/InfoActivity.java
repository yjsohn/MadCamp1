package com.example.basicapplicationfunction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ImageView image = findViewById(R.id.imageView);
        TextView name = findViewById(R.id.textView6);
        TextView phone = findViewById(R.id.textView7);
        TextView email = findViewById(R.id.textView8);

        Intent Intent = getIntent();
        name.setText(Intent.getStringExtra("name"));
        phone.setText(Intent.getStringExtra("phone"));
        email.setText(Intent.getStringExtra("email"));
        image.setImageBitmap(getIntent().getParcelableExtra("image"));
    }

    public void mOnCall(View v){
        TextView phone = findViewById(R.id.textView7);
        Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText().toString()));
        startActivity(call);
    }
}