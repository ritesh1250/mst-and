package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.ShowAdvtResponse;

import java.util.ArrayList;

public class ShowAdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<ShowAdvtResponse.Datum> showAdvtResponses;


    public ShowAdAdapter(Context context, ArrayList<ShowAdvtResponse.Datum> showAdvtResponses) {
        this.context = context;
        this.showAdvtResponses = showAdvtResponses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.show_advt_layout, parent, false);
        return new ShowAdAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ShowAdAdapter.ViewHolder userViewHolder = (ShowAdAdapter.ViewHolder) holder;
        final ShowAdvtResponse.Datum model = showAdvtResponses.get(position);

        userViewHolder.tvAdDetails.setText(model.getCaption());

    }

    @Override
    public int getItemCount() {
        return showAdvtResponses==null?0 :showAdvtResponses.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvAdDetails;
        public ViewHolder(View itemView) {
            super(itemView);
        tvAdDetails=itemView.findViewById(R.id.tvAdDetails);

        }
    }
}