package com.meest.metme.camera2.sticker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.meest.R;

public class TittleAdapter  extends RecyclerView.Adapter<TittleAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "TittleAdapter";
    private final Context context;
    private final ArrayList<String> titleList;
    protected TitleListener titleListener;

    public TittleAdapter(Context context, ArrayList<String> titleList, TitleListener titleListener) {
        this.context = context;
        this.titleList = titleList;
        this.titleListener = titleListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.stickers_title_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = titleList.get(position);
        holder.title.setText(title);
        holder.itemView.setOnClickListener(view -> {
            titleListener.onTitleClick(position);
//            Toast.makeText(context, "Item Clicked", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    public interface TitleListener{
        void onTitleClick(int index);
    }
}
