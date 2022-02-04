package com.meest.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.UserFAQCategoryResponse;

import java.util.List;

public class FAQCategoriesAdapter extends RecyclerView.Adapter<FAQCategoriesAdapter.FaqButtonViewHolder> {
    FAQCategoriesAdapter.onItemClickListener onItemClickListener;
    TextView oldTextView;
    boolean isFirst;


    private List<UserFAQCategoryResponse.Row> FabButtonList;
    public FAQCategoriesAdapter(List<UserFAQCategoryResponse.Row> fabBtList){
        FabButtonList = fabBtList;

isFirst=true;
    }

    @NonNull
    @Override
    public FaqButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_btn_list,parent,false);
        return new FaqButtonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqButtonViewHolder holder, int position) {
        holder.btPay.setText(FabButtonList.get(position).getCatagoryName());

        if (isFirst && position==0){
            holder.btPay.setBackgroundResource(R.drawable.ic_btn_gradient_curved);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirst=false;
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(position,FabButtonList.get(position));

                if (oldTextView!=null){
                    oldTextView.setBackgroundResource(R.drawable.ic_bg_gray_faq);
                }
                holder.btPay.setBackgroundResource(R.drawable.ic_btn_gradient_curved);
                oldTextView=holder.btPay;
            }

        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
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

    public interface onItemClickListener {
        void onItemClick(int position,UserFAQCategoryResponse.Row rowItem);
    }

    public void setItemClickListener(final FAQCategoriesAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
