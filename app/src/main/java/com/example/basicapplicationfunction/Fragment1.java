package com.example.basicapplicationfunction;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import static android.provider.ContactsContract.Directory.DISPLAY_NAME;

public class Fragment1 extends Fragment{

    MyListAdapter myListAdapter;

    View view;
    public Fragment1() {
        // Required empty public constructor
    }


    public static Fragment1 newInstance() {
        Fragment1 fragment1 = new Fragment1();
        return fragment1;
    }

    public void refresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment1, container, false);
        /*ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        progressBar.setIndeterminate(true);

        LinearLayout progressBarContainer = new LinearLayout(super);
        progressBarContainer.setLayoutParams( new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        progressBarContainer.setGravity(Gravity.CENTER);
        progressBarContainer.addView(progressBar);

        getListView().setEmptyView(progressBarContainer);
        ViewGroup root = (ViewGroup)findViewById(android.R.id.content);
        root.addView(progressBarContainer);

        String[] fromColumns = { ContactsContract.Data.DISPLAY_NAME };

        int[] toViews = { android.R.id.text1 };

        mAdapter = new SimpleCursorAdapter( this, // 컨텍스트
                android.R.layout.simple_list_item_1, // 리스트 아이템 레이아웃
                null, // 커서
                fromColumns, // 뷰로 뿌릴 데이터
                toViews, // 데이터를 뿌릴 타겟 뷰
                0 // 플래그
                );
        setListAdapter(mAdapter);
        //LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = this;
        //LoaderManager loaderManager = getLoaderManager();
        //loaderManager.initLoader(0, null, loaderCallbacks);
        */
        ListView list = (ListView) view.findViewById(R.id.list);
        ArrayList<list_item> list_itemArrayList = new ArrayList<list_item>();
        HashMap<String, String> item;

/*        Cursor c =getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (c.moveToNext()) {
            String contactName = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            long Photo_id = c.getLong(c.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
            long person_id = c.getLong(c.getColumnIndex(ContactsContract.Contacts._ID));
            String email = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
            Log.d("email", email);
            //int image = c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.)
            list_itemArrayList.add(new list_item(Photo_id, person_id, contactName, phonenumber, email));
        }
        c.close();*/

        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            do {
                // get the contact's information
                long person_id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                long photo_id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex("DISPLAY_NAME"));
                Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                // get the user's email address
                String email = null;
                Cursor ce = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                if (ce != null && ce.moveToFirst()) {
                    email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    ce.close();
                }

                // get the user's phone number
                String phone = null;
                if (hasPhone > 0) {
                    Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
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

        return view;
    }



    /*public Loader<Cursor> onCreateLoader(int id, Bundle args) { // 커서로더를 만들어 리턴해주면, 로딩이 시작된다.
         return new CursorLoader( this, ContactsContract.Data.CONTENT_URI, PROJECTION, SELECTION, null, null );
    }*/

    /*public void onLoadFinished(Loader<Cursor> loader, Cursor data) { // 커서어댑터의 커서를 업데이트 해주면, // fromColumns과 toView를 참고하여 리스트뷰가 업데이트된다.
        mAdapter.swapCursor(data);
    } // Called when a previously created loader is reset, making the data // unavailable
    public void onLoaderReset(Loader<Cursor> loader) { // This is called when the last Cursor provided to onLoadFinished() // above is about to be closed. We need to make sure we are no // longer using it.
        mAdapter.swapCursor(null);
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) { // do something when a list item is clicked
        Log.i("GUN", ((TextView)v).getText().toString());
    }*/
}
