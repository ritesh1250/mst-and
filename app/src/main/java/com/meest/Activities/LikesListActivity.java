package com.meest.Activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Adapters.ListItemAdapter;
import com.meest.R;

import java.util.ArrayList;

public class LikesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> imageModelArrayList = new ArrayList<>();
    private ListItemAdapter adapter;
    private Paint p = new Paint();
    private ImageView back_arrow;
    private String[] myImageNameList = new String[]{"Code1", "Code2",
            "Code3","Code4"
            ,"Code5","Code6",
            "Code7","Code8",
            "Code9","Code10"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_fragment);

        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        for (int i =0; i < 10;i++){
            imageModelArrayList.add("");

        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new ListItemAdapter(getApplicationContext(),imageModelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

    }





}
