package com.example.basicapplicationfunction;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.provider.ContactsContract.Directory.DISPLAY_NAME;

public class Fragment1 extends Fragment{

    MyListAdapter myListAdapter;
    List<list_item> copylist;

    View view;
    public Fragment1() {
        // Required empty public constructor
    }


    public static Fragment1 newInstance() {
        Fragment1 fragment1 = new Fragment1();
        return fragment1;
    }

    public void refresh() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment1, container, false);

        ListView list = (ListView) view.findViewById(R.id.list);
        ArrayList<list_item> list_itemArrayList = new ArrayList<list_item>();
        HashMap<String, String> item;

        String[] projection = new String[]{
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        
        while (getActivity().checkCallingOrSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED);

            ContentResolver cr = getActivity().getContentResolver();
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {

                do {
                    // get the contact's information
                    long person_id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    long photo_id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex("DISPLAY_NAME"));
                    Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    // get the user's email address

                    String[] projection1 = new String[]{
                            ContactsContract.CommonDataKinds.Email.DATA
                    };
                    String email = null;
                    Cursor ce = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection1,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (ce != null && ce.moveToFirst()) {
                        email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        ce.close();
                    }

                    // get the user's phone number
                    String phone = null;
                    String[] projection2 = new String[]{
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    };
                    if (hasPhone > 0) {
                        Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection2,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (cp != null && cp.moveToFirst()) {
                            phone = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            cp.close();
                        }
                    }

                    // if the user user has an email or phone then add it to contacts
                    if ((!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                            && !email.equalsIgnoreCase(name)) || (!TextUtils.isEmpty(phone))) {
                        list_itemArrayList.add(new list_item(photo_id, person_id, name, phone, email));
                    }

                } while (cursor.moveToNext());

                // clean up cursor
                cursor.close();
            }

        copylist = new ArrayList<>();
        copylist.addAll(list_itemArrayList);
        myListAdapter = new MyListAdapter(getContext(), list_itemArrayList);
        list.setAdapter(myListAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                list_item item = (list_item) parent.getItemAtPosition(position);
                //intent.putExtra("photo_id", item.getPhoto_id());
                //intent.putExtra("person_id",item.getPerson_id());
                ImageView image = (ImageView) view.findViewById(R.id.profile_imageview);
                TextView nameview = (TextView) view.findViewById(R.id.nickname_textview);
                TextView phonenum = (TextView) view.findViewById(R.id.content_textview);
                Log.d("check", image.getDrawable().getClass().getName());
                if(image.getDrawable().getClass() == AdaptiveIconDrawable.class){
                    AdaptiveIconDrawable icon = (AdaptiveIconDrawable) image.getDrawable();
                    int w = icon.getIntrinsicWidth();
                    int h = icon.getIntrinsicHeight();
                    Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(result);
                    icon.setBounds(0, 0, w, h);
                    icon.draw(canvas);
                    intent.putExtra("image", result);
                }
                else {
                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    intent.putExtra("image", drawable.getBitmap());
                }
                intent.putExtra("name", nameview.getText().toString());
                intent.putExtra("phone", phonenum.getText().toString());
                intent.putExtra("email", item.getEmail());
                startActivity(intent);
                //Toast.makeText(getContext(), "미구현", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btnContactAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnContactAdd(v);
            }
        });

        EditText editSearch = view.findViewById(R.id.editTextTextPersonName2);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = editSearch.getText().toString();
                search(text);
            }
        });
        return view;
    }

    public void mOnContactAdd(View v){
        Log.d(null, "ContactAdd: start fin");

        if(v.getId() != R.id.btnContactAdd)
            return;

        ContactAdd();
        //fragment1.myListAdapter.notifyDataSetChanged();
    }

    public void ContactAdd(){
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        startActivityForResult(intent, Code.requestCode);
    }

    public void search(String charText){
        myListAdapter.list_itemArrayList.clear();

        if(charText.length() == 0){
            myListAdapter.list_itemArrayList.addAll(copylist);
        }
        else{
            copylist.forEach(item -> {
                if(item.getNickname().toLowerCase().contains(charText)){
                    myListAdapter.list_itemArrayList.add(item);
                }
            });
        }

        myListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);
        if(requestCode == Code.requestCode && resultCode == Code.resultCode){

            /*new Thread(){
            @Override
            public void run() {*/

            ArrayList<ContentProviderOperation> list = new ArrayList<>();
            try{
                list.add(
                        ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                .build()
                );

                list.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, resultIntent.getStringExtra("name"))   //이름

                                .build()
                );

                list.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, resultIntent.getStringExtra("number"))           //전화번호
                                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE  , ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)   //번호타입(Type_Mobile : 모바일)

                                .build()
                );

                list.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Email.DATA  , resultIntent.getStringExtra("email"))  //이메일
                                .withValue(ContactsContract.CommonDataKinds.Email.TYPE  , ContactsContract.CommonDataKinds.Email.TYPE_WORK)     //이메일타입(Type_Work : 직장)

                                .build()
                );

                getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, list);  //주소록추가
                list.clear();   //리스트 초기화
                refresh();
            }catch(RemoteException e){
                e.printStackTrace();
            }catch(OperationApplicationException e){
                e.printStackTrace();
            }
        }
    }

}
