package com.example.basicapplicationfunction;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

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

        Cursor c =getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (c.moveToNext()) {
            String contactName = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            long Photo_id = c.getLong(c.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
            long person_id = c.getLong(c.getColumnIndex(ContactsContract.Contacts._ID));
            //int image = c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.)
            list_itemArrayList.add(new list_item(Photo_id, person_id, contactName, phonenumber));
        }
        c.close();
        myListAdapter = new MyListAdapter(getContext(), list_itemArrayList);
        list.setAdapter(myListAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "미구현", Toast.LENGTH_SHORT).show();
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
