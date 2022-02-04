package com.meest.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Faq.FaqActivity;
import com.meest.Faq.FaqButtonItem;
import com.meest.R;

import java.util.ArrayList;

public class FaqButtonAdapter extends RecyclerView.Adapter<FaqButtonAdapter.FaqButtonViewHolder> {

    private ArrayList<FaqButtonItem> FabButtonList;

    @NonNull
    @Override
    public FaqButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_btn_list,parent,false);
        FaqButtonAdapter.FaqButtonViewHolder SVH = new FaqButtonAdapter.FaqButtonViewHolder(v);
        return SVH;
    }

    @Override
    public void onBindViewHolder(@NonNull FaqButtonViewHolder holder, int position) {
        FaqButtonItem faqButtonItem=FabButtonList.get(position);
        holder.btPay.setText(faqButtonItem.getBtPay());
    }

    @Override
    public int getItemCount() {
        return FabButtonList.size();
    }

    public class FaqButtonViewHolder extends RecyclerView.ViewHolder {

        TextView btPay;

        public FaqButtonViewHolder(@NonNull View itemView) {
            super(itemView);

            btPay=itemView.findViewById(R.id.btPay);

        }
    }

    public FaqButtonAdapter(ArrayList<FaqButtonItem> fabBtList, FaqActivity faqActivity, FaqActivity activity){
        FabButtonList = fabBtList;
    }
}
