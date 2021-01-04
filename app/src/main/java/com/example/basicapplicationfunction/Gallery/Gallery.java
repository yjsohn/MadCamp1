package com.example.basicapplicationfunction.Gallery;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicapplicationfunction.Gallery.DBHelper;
import com.example.basicapplicationfunction.Gallery.Picture;
import com.example.basicapplicationfunction.Gallery.PictureAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.basicapplicationfunction.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import static android.app.Activity.RESULT_OK;
//!!!!!파일 경로:  file:///storage/emulated/0/Pictures/building.jpg


public class Gallery extends Fragment {
    View view;
    Context context;
    DBHelper MyDB;

    private static final String TAG = "blackjin";

    private Boolean isPermission = true;

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private ArrayList<Picture> mArrayList;
    static private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentManager fragmentManager;
    private MenuItem.OnMenuItemClickListener listener;

    private File tempFile;

    public Gallery() {
        // Required empty public constructor
    }


    public static Gallery newInstance() {
        Gallery gallery = new Gallery();
        return gallery;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gallery, container, false);
        context = view.getContext();
        tedPermission();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyDB = new DBHelper(view.getContext());
        //MyDB.insertPicture("/storage/emulated/0/Pictures/duck.jpg", "", "", "");
        mArrayList = MyDB.getAllImages();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.galleryView);
        mAdapter = new PictureAdapter(context, mArrayList, this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(context, 3);
        mRecyclerView.setLayoutManager(layoutManager);

        listener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.gallery:  //click gallery
                        if(isPermission)
                            goToAlbum();
                        break;
                    case R.id.camera:   //click camera
                        if(isPermission)
                            takePhoto();
                        break;
                }
                return true;
            }
        };

        view.findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {  //추가버튼 클릭
            @Override
            public void onClick(View view) {

                PopupMenu popup= new PopupMenu(context, view);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.gallery:  //click gallery
                                if(isPermission)
                                    goToAlbum();
                                break;
                            case R.id.camera:   //click camera
                                if(isPermission)
                                    takePhoto();
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        view.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {   //삭제버튼 클릭
            @Override
            public void onClick(View view) {
                MyDB.deleteAll();//db삭제
                refreshFragment();
            }
        });
    }

    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tempFile != null) {

            /**
             *  안드로이드 OS 누가 버전 이후부터는 file:// URI 의 노출을 금지로 FileUriExposedException 발생
             *  Uri 를 FileProvider 도 감싸 주어야 합니다.
             *
             *  참고 자료 http://programmar.tistory.com/4 , http://programmar.tistory.com/5
             */
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(context,
                        "com.example.basicapplicationfunction", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            } else {
                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
        }
    }
    private File createImageFile() throws IOException {
        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "basicapp_" + timeStamp + "_";

        // 이미지가 저장될 파일 주소 ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/");   //저장 위치 확인해보기
        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

        return image;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.popup_menu, menu);
    }

    void refreshFragment(){
        //refresh fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    //사진첩에서 사진 받아오기
    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

            Cursor cursor = null;

            try {
                //Uri 스키마를 content:/// 에서 file:/// 로  변경
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContext().getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            //refresh fragment
            refreshFragment();
            addImage();
        }
        else if(requestCode == PICK_FROM_CAMERA){
            refreshFragment();
            addImage();
        }
    }

    private void addImage() {
        String path = tempFile.getPath();
        MyDB.insertPicture(path, "", "", "");
        mArrayList.add(new Picture(path));
    }

    //권한 설정
    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;
                Log.e("ERROR", "권한 설정 오류");
            }
        };


        //Todo: 반복 문구 수정
        TedPermission.with(context)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }
}