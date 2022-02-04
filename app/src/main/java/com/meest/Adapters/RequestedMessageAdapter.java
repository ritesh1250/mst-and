package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.GetPendingUserResponse;

import java.util.List;

public class RequestedMessageAdapter extends RecyclerView.Adapter<RequestedMessageAdapter.MyViewHolder> {

    private Context mContext;
    private List<GetPendingUserResponse.Friends> responseList;

    public RequestedMessageAdapter(Context mContext, List<GetPendingUserResponse.Friends> responseList) {
        this.mContext = mContext;
        this.responseList = responseList;
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public RequestedMessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.msg_list_request, parent, false);
        return new RequestedMessageAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedMessageAdapter.MyViewHolder holder, final int position) {
       holder.txt_msg.setText(responseList.get(position).getMsg());
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        TextView txt_msg;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            txt_msg = itemView.findViewById(R.id.txt_msg);

        }
    }
}