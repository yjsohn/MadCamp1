package com.example.basicapplicationfunction.Gallery;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.basicapplicationfunction.R;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {
    Context context;
    List<Picture> data;

    public PictureAdapter(Context context, List<Picture> data) {
        this.context = context;
        this.data = data;
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
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}