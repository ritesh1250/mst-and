package com.meest.metme.camera2.sticker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.meest.R;
import com.meest.metme.camera2.preview.PreviewImageActivity;
import com.meest.metme.camera2.sticker.adapter.StickerAdapter;
import com.meest.metme.camera2.sticker.adapter.TittleAdapter;
import com.meest.metme.camera2.utills.CameraUtil;


public class StickerBottomSheet extends BottomSheetDialogFragment {
    private static final String TAG = "galleryBottomSheet";

    RecyclerView recyclerView,stickerTittleRecyclerView;
    StickerAdapter stickerAdapter;
    TittleAdapter titleAdapter;
    ArrayList<String> images = new ArrayList<>();
    View view;
    String url = "https://picsum.photos/v2/list?page=2&limit=40";
    String newUrl = "http://65.1.89.89:3003/sticker/getAll";
    String basicAuth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJJZCI6IjA3ZmQ4OGE4LWJjY2EtNGQ4Yy1iZmUwLTlkMDg5YTY5YjlkZiIsImVtYWlsIjoiIiwiY3JlYXRlZEF0IjoiMjAyMS0wNi0wOVQwNjoxMTo1OC4yMDdaIn0sImlhdCI6MTYyMzIxOTExOH0.8q6v4_Z6fR4U0oksL6u-6kV1lFnou-t1KIYlwMYf5rw";



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.stciker_bottom_sheet,
                container, false);

        recyclerView = view.findViewById(R.id.stickerRecyclerView);
        stickerTittleRecyclerView = view.findViewById(R.id.stickerTittleRecyclerView);
        //new JsonTask().execute(newUrl);

        view.findViewById(R.id.closeBottomSheet).setOnClickListener(v1 -> PreviewImageActivity.stickerBottomSheet.dismiss());
        callStickerAPI();
        return view;
    }


    StringRequest stringRequest;
    RequestQueue mRequestQueue;
    public void callStickerAPI() {

        mRequestQueue = Volley.newRequestQueue(getActivity());

        stringRequest = new StringRequest(Request.Method.POST, newUrl,
                response -> {
                    JSONObject j;
                    try {

                        j = new JSONObject(response);

                        String data = j.getString("data");

                        handleJsonResponse(data);
//                        if (status.equals("1")) {
//
//
//                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSONException: "+e.getMessage() );

                    }
                },
                error -> {
                    Log.e(TAG, "error: "+error);

                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-token", basicAuth);
                return headers;
            }
        };
        stringRequest.setTag(TAG);
        mRequestQueue.add(stringRequest);
    }
    String Response;
    private final ArrayList<String> title = new ArrayList<>();
    private void handleJsonResponse(String response)
    {
        try {
            Response = response;
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                title.add(jsonObject.getString("title"));
            }
            setTitle();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setTitle() {
        stickerTittleRecyclerView.setHasFixedSize(true);
        stickerTittleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        titleAdapter = new TittleAdapter(getContext(), title, position -> {
            Log.e(TAG, "position: "+position );
            indexValue = position;
            setStickers();
        });

        stickerTittleRecyclerView.setAdapter(titleAdapter);
        setStickers();
    }
    private int indexValue = 0;
    private void setStickers(){

        try {

            if (images!=null)
            {
                images.clear();
            }

            JSONArray jsonArray = new JSONArray(Response);
           JSONObject jsonObject = jsonArray.getJSONObject(indexValue);
            JSONArray jsonArray1 = new JSONArray(jsonObject.getString("stickers"));
            for (int i = 0; i <jsonArray1.length() ; i++) {
                images.add(jsonArray1.getString(i));
            }
            loadImages();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadImages() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        stickerAdapter = new StickerAdapter(getContext(), images, path -> {
            Log.e(TAG, "path: "+path);
            CameraUtil.stickerURL = path;
            PreviewImageActivity.stickerBottomSheet.dismiss();
            if (getActivity()!=null)
            {
                ((PreviewImageActivity)getActivity()).addStickerView();
            }

        });

        recyclerView.setAdapter(stickerAdapter);

    }




}