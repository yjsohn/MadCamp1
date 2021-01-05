package com.example.basicapplicationfunction;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.io.InputStream;
import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter{
    Context context;
    ArrayList<list_item> list_itemArrayList;

    public MyListAdapter(Context context, ArrayList<list_item> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    TextView nickname_textView;
    TextView content_textView;
    ImageView profile_imageView;

    @Override
    public int getCount() {
        return this.list_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
         convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
        }
        nickname_textView = (TextView)convertView.findViewById(R.id.nickname_textview);
        content_textView = (TextView)convertView.findViewById(R.id.content_textview);
        profile_imageView = (ImageView)convertView.findViewById(R.id.profile_imageview);

        nickname_textView.setText(list_itemArrayList.get(position).getNickname());
        Log.d("name", nickname_textView.getText().toString());
        content_textView.setText(list_itemArrayList.get(position).getContent());
        Bitmap image = loadContactPhoto(context.getContentResolver(), list_itemArrayList.get(position).getPerson_id(), list_itemArrayList.get(position).getPhoto_id());
        //profile_imageView.setImageBitmap(list_itemArrayList.get(position).getProfile_image());
        if(image != null)
            profile_imageView.setImageBitmap(image);
        else {    //설정된 사진 없음
            profile_imageView.setImageResource(R.drawable.profile_icon);
            BitmapDrawable images = (BitmapDrawable) profile_imageView.getDrawable();
            profile_imageView.setImageBitmap(resizingBitmap(images.getBitmap()));
            DrawableCompat.setTint(
                    DrawableCompat.wrap(profile_imageView.getDrawable()),
                    ContextCompat.getColor(context, R.color.highlight)
            );
        }

        //set Rounding
        GradientDrawable drawable= (GradientDrawable) context.getDrawable(R.drawable.background_rounding2);
        profile_imageView.setBackground(drawable);
        profile_imageView.setClipToOutline(true);
        return convertView;
    }

    public Bitmap loadContactPhoto(ContentResolver cr, long id, long photo_id){
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if(input != null)
            return resizingBitmap(BitmapFactory.decodeStream(input));
        else
            Log.d("PHOTO", "first try failed to load photo");

        byte[] photoBytes = null;
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);
        Cursor c = cr.query(photoUri, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);
        try{
            if(c.moveToFirst())
                photoBytes = c.getBlob(0);
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            c.close();
        }

        if(photoBytes != null)
            return resizingBitmap(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length));

        else
            Log.d("PHOTO", "second try also failed");
        return null;
    }

    public Bitmap resizingBitmap(Bitmap oBitmap){
        if(oBitmap == null)
            return null;
        float width = oBitmap.getWidth();
        float height = oBitmap.getHeight();
        float resizing_size = 120;
        Bitmap rBitmap = null;
        if(width > resizing_size){
            float mWidth = (float) (width /100);
            float fScale = (float) (resizing_size/mWidth);
            width *= (fScale/100);
            height *= (fScale/100);

        }else if (height > resizing_size){
            float mHeight = (float) (height/100);
            float fScale = (float)(resizing_size/mHeight);
            width *= (fScale/100);
            height *= (fScale/100);
        }
        rBitmap = Bitmap.createScaledBitmap(oBitmap, (int)width, (int)height, true);
        return rBitmap;

    }
}

