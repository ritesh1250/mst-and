package com.meest.svs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.svs.interfaces.CategorySelectInterface;
import com.meest.svs.models.VideoCategoryResponse;

import java.util.List;

public class VideoCategoriesAdapter extends RecyclerView.Adapter<VideoCategoriesAdapter.CategoriesHolder> {

    private Context context;
    private  List<VideoCategoryResponse.Datum> categoriesList;
    private TextView previousCategory;
    private CategorySelectInterface categorySelectInterface;
    private boolean firstSelected;

    public VideoCategoriesAdapter(Context context,  List<VideoCategoryResponse.Datum> categoriesList,
                                  CategorySelectInterface categorySelectInterface) {
        this.context = context;
        this.categoriesList = categoriesList;
        this.categorySelectInterface = categorySelectInterface;

        firstSelected = true;
    }

    @NonNull
    @Override
    public VideoCategoriesAdapter.CategoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_category_child, parent, false);
        return new CategoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoCategoriesAdapter.CategoriesHolder holder, int position) {
        holder.category.setText(categoriesList.get(position).getTitle());

        if(position == 0 && firstSelected) {
            holder.category.setBackgroundResource(R.drawable.blue_background);
            holder.category.setTextColor(context.getResources().getColor(R.color.white));

            previousCategory = holder.category;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstSelected = false;

                if(previousCategory == holder.category) {
                    return;
                }
                if(previousCategory != null) {
                    previousCategory.setBackgroundResource(R.drawable.grey_border);
                    previousCategory.setTextColor(context.getResources().getColor(R.color.black));
                }

                holder.category.setBackgroundResource(R.drawable.blue_background);
                holder.category.setTextColor(context.getResources().getColor(R.color.white));

                previousCategory = holder.category;

                categorySelectInterface.categoryClicked(position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class CategoriesHolder extends RecyclerView.ViewHolder {

        TextView category;

        public CategoriesHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.vcc_category);
        }
    }
}
