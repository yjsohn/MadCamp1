package com.example.basicapplicationfunction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        EditText phone = (EditText) findViewById(R.id.editTextPhone);
        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }
    public void mOnContactAdd2(View v){
        if(v.getId() != R.id.btnContactAdd2)
            return;

        ContactAdd2();
    }

    public void ContactAdd2(){

        EditText person_name = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText phone = (EditText) findViewById(R.id.editTextPhone);
        EditText email = (EditText) findViewById(R.id.editTextTextEmailAddress);

        Intent resultIntent = new Intent();

        resultIntent.putExtra("name", person_name.getText().toString());
        resultIntent.putExtra("number", phone.getText().toString());
        resultIntent.putExtra("email", email.getText().toString());

        setResult(Code.resultCode, resultIntent);
        finish();
    }
}