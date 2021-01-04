package com.example.basicapplicationfunction.Gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.basicapplicationfunction.R;

import java.io.File;
import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {
    Context context;
    List<Picture> data;
    private PopupWindow mPopupWindow ;
    DBHelper MyDB;
    SQLiteDatabase db;
    Fragment myFragment;

    public PictureAdapter(Context context, List<Picture> data, Fragment myFragment) {
        this.context = context;
        this.data = data;
        MyDB = new DBHelper(context);
        db = MyDB.getWritableDatabase();
        this.myFragment = myFragment;
    }

    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PictureViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.gallery_item, parent, false);
        viewHolder = new PictureViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        File tempFile = new File(data.get(position).getPath());
        Glide.with(context).load(tempFile)
                .placeholder(R.color.black)
                .centerCrop()
                .into(holder.mImg);
    }

    public class PictureViewHolder extends RecyclerView.ViewHolder{
        ImageView mImg;

        public PictureViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mImg = (ImageView) itemView.findViewById(R.id.detailed_image2);
            //set Rounding
            GradientDrawable drawable= (GradientDrawable) context.getDrawable(R.drawable.background_rounding);
            mImg.setBackground(drawable);
            mImg.setClipToOutline(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.
                        Picture picture = data.get(pos) ;
                        String path = picture.getPath();

                        Intent intent = new Intent(context, GalleryContent.class);
                        intent.putExtra("PATH", path);

                        ((Activity)context).startActivityForResult(intent, 1);
                        // TODO : use item.
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //popup -> Do you wanna delete?
                    //Y: DELETE -> close popup -> refresh fragment
                    //N: close popup

                    View popupView = LayoutInflater.from(context).inflate(R.layout.activity_delete_popup, null);
                    //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정
                    mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mPopupWindow.setFocusable(true);    // 외부 영역 선택시 PopUp 종료
                    mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, -100);

                    Button cancel = (Button) popupView.findViewById(R.id.Cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {  //취소
                        @Override
                        public void onClick(View v) {
                            mPopupWindow.dismiss();
                        }
                    });

                    Button ok = (Button) popupView.findViewById(R.id.Ok);
                    ok.setOnClickListener(new View.OnClickListener() {  //확인
                        @Override
                        public void onClick(View v) {

                            int pos = getAdapterPosition() ;
                            if (pos != RecyclerView.NO_POSITION) {
                                // 데이터 리스트로부터 아이템 데이터 참조.
                                Picture picture = data.get(pos) ;
                                String path = picture.getPath();

                                db.delete("images", "path=?", new String[]{path});
                            }
                            mPopupWindow.dismiss();
                            refreshFragment();
                            //delete image
                            //close popup
                            //refresh fragment
                        }
                    });
                    return true;
                }
            });
        }
    }


    void refreshFragment(){
        //refresh fragment
        FragmentTransaction ft = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
        ft.detach(myFragment).attach(myFragment).commit();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}