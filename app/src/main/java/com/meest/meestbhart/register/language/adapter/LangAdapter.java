package com.meest.meestbhart.register.language.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.LanguagesResponse;
import com.bumptech.glide.Glide;
import com.meest.utils.SharedPrefsUtils;
import com.meest.chat_calling.utils.SharedPreferencesManager;
import com.meest.meestbhart.register.language.LangaugeActivity;
import java.util.ArrayList;

public class LangAdapter extends RecyclerView.Adapter<LangAdapter.ViewHolder> {

    ArrayList<LanguagesResponse.Row> productList;
    LangaugeActivity activity ;
    Context context;
    Resources resources;
    boolean one_time = true;
    OnItemClickListener mItemClickListener;
    public int selectedPosition;
    SharedPrefsUtils SharedPrefsUtils;

    public LangAdapter(LangaugeActivity activity, ArrayList<LanguagesResponse.Row> productList, int my_office_task) {
        this.activity = activity;
        this.productList = productList;
        this.selectedPosition = my_office_task;
        this.context = context;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lang_adapter, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        // holder.txt_lang_1.setText(productList.get(position).languageNameEnglish());
        holder.txt_lang_1.setText(productList.get(position).getLanguageNameNative());
        Glide.with(activity).load(productList.get(position).getImage()).into(holder.image);
        if (selectedPosition == position) {
            holder.parent_layout.setBackgroundResource(R.drawable.bg_lang_selected);
            holder.liComingSoon.setVisibility(View.GONE);
        } else {
            holder.parent_layout.setBackgroundResource(R.drawable.bg_lang_unselected);
            holder.liComingSoon.setVisibility(View.GONE);

        }

        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                notifyDataSetChanged();
                activity.changeColours(true);
                SharedPreferencesManager.save_Language(productList.get(position).getId());
                String lang_id = SharedPreferencesManager.getLanguage();
                Log.e("======Language IDs",""+lang_id);

            }
        });
    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout parent_layout;
        TextView tvTextComingSoon;
        ImageView ivBgSoon;
        RelativeLayout liComingSoon;
        CardView cardMain;
        TextView txt_lang_1;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_lang_1 = itemView.findViewById(R.id.txt_lang_1);
            //  txt_lang_2 = itemView.findViewById(R.id.txt_lang_2);
            cardMain = itemView.findViewById(R.id.cardMain);
            image = itemView.findViewById(R.id.image);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            ivBgSoon = itemView.findViewById(R.id.ivBgSoon);
            tvTextComingSoon = itemView.findViewById(R.id.tvTextComingSoon);
            liComingSoon = itemView.findViewById(R.id.liComingSoon);

        }
    }
}

