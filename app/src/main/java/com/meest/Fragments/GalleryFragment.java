package com.meest.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Adapters.OwnStatusAdapter;
import com.meest.R;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private ArrayList<String> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    OwnStatusAdapter adapter;
    private Cursor cursor;
    private ImageView img_close;
    private  int columnIndex;
    GridView gridView1;
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.gallery_fragment, viewGroup, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        arrayList = getAllShownImagesPath(getActivity());

        adapter = new OwnStatusAdapter(arrayList);
        //RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //recyclerView.setLayoutParams(lp);
        // recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);



        // int numberOfColumns = 2;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 4);

//        adapter.setHasStableIds(true);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(mLayoutManager);


        return view;
    }


    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }


}
