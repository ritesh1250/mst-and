package com.meest.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Interfaces.CreateTextInterface;
import com.meest.R;
import com.meest.responses.ColorsResponseNew;
import com.meest.meestbhart.utilss.Constant;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorsHolder> {

    private final Context context;
    private ColorsResponseNew ColorsResponseNew;
    private CreateTextInterface createTextInterface;

    public ColorsAdapter(Context context, ColorsResponseNew ColorsResponseNew,
                         CreateTextInterface createTextInterface) {
        this.context = context;
        this.ColorsResponseNew = ColorsResponseNew;
        this.createTextInterface = createTextInterface;

    }


    @NonNull
    @Override
    public ColorsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_layout_child, parent, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(Constant.pxFromDp(context, 5), Constant.pxFromDp(context, 5),
                Constant.pxFromDp(context, 5), Constant.pxFromDp(context, 5));
        itemLayoutView.setLayoutParams(params);
        return new ColorsHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ColorsHolder holder, final int position) {
        ColorsResponseNew.Data.Row model = ColorsResponseNew.getData().getRows().get(position);
        try {
            if (model.getColorCode() != null) {
                holder.colorView.setBackgroundColor(Color.parseColor(model.getColorCode()));
            } else if (model.getColorCode().equalsIgnoreCase("")) {
                holder.colorView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

        } catch (Exception e) {
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTextInterface.colorsSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ColorsResponseNew.getData().getRows().size();
    }

    final class ColorsHolder extends RecyclerView.ViewHolder {

        View colorView;

        public ColorsHolder(View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.color_view_child);
        }
    }
}
