package com.meest.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.FeelingResponse;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.meest.meestbhart.utilss.Constant;

public class FeelingAdapter extends RecyclerView.Adapter<FeelingAdapter.FeelingHolder> {

    private Context context;
    private FeelingResponse feelingResponse;

    public FeelingAdapter(Context context, FeelingResponse feelingResponse) {
        this.context = context;
        this.feelingResponse = feelingResponse;
    }

    @NonNull
    @Override
    public FeelingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.feeling_child,parent,false);
        return new FeelingHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FeelingHolder holder, int position) {
        FeelingResponse.Row model = feelingResponse.getData().getRows().get(position);
        holder.feeling_name.setText(model.getTitle());
        Glide.with(context).load(model.getIcon()).into(holder.feeling_icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feelingData = new Gson().toJson(model);
                Intent intent = new Intent();
                intent.putExtra("isFeelingType", true);
                intent.putExtra("feelingData", feelingData);
                ((Activity)context).setResult(Constant.REQUEST_FOR_FEELING, intent);
                ((Activity)context).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return feelingResponse.getData().getRows().size();
    }

    public class FeelingHolder extends RecyclerView.ViewHolder{

        public ImageView feeling_icon;
        public TextView feeling_name;

        public FeelingHolder(@NonNull View itemView) {
            super(itemView);
            feeling_name = itemView.findViewById(R.id.feeling_text_child);
            feeling_icon = itemView.findViewById(R.id.feeling_image_child);
        }
    }
}
