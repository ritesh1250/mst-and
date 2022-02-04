package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.meestbhart.register.fragment.username.model.VerifyUserNameResponse;

import java.util.ArrayList;

public class UserSggestionAdapter  extends RecyclerView.Adapter<UserSggestionAdapter.ViewHolder> {
    private final static int sBtnCloseSize = 16;
    private final static int sBtnCloseLeftMargin = 5;

    private final Context mContext;


    private ArrayList<VerifyUserNameResponse.Data> mData = new ArrayList<>();

    UserSggestionAdapter.OnItemClickListener mItemClickListener;
    public UserSggestionAdapter(Context context,ArrayList<VerifyUserNameResponse.Data> mData) {
        mContext = context;
        this.mData = mData;

    }


    @NonNull
    @Override
    public UserSggestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_name_suggestion, null);
        UserSggestionAdapter.ViewHolder viewHolder = new UserSggestionAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull final UserSggestionAdapter.ViewHolder holder, final int position) {


        holder.txt_name.setText(mData.get(position).getSuggestions().get(position)) ;

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);

        }


    }


    public void setOnItemClickListener(final UserSggestionAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }


}
