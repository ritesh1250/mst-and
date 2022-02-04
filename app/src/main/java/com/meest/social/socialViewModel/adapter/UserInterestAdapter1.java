package com.meest.social.socialViewModel.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Paramaters.UpdateTopicsParams;
import com.meest.R;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.responses.UserUpdatedSelectedTopics2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.meest.meestbhart.register.fragment.choosetopic.model.TopicsResponse;

public class UserInterestAdapter1 extends RecyclerView.Adapter<UserInterestAdapter1.ViewHolder>{

    private final Context mContext;
    private List<String> mData = new ArrayList<>();
    UserInterestAdapter1.OnItemClickListener mItemClickListener;

    public OnItemClickListener getmItemClickListener() {
        return mItemClickListener;
    }

    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int position, List<String> mData, ViewHolder holder);
    }

    public UserInterestAdapter1(Context context, List<String> mData, OnItemClickListener mItemClickListener){
        mContext = context;
        this.mData = mData;
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public UserInterestAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tagged_users_items_row, null);



        return new UserInterestAdapter1.ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserInterestAdapter1.ViewHolder holder, int position) {
        holder.tv_name.setText(mData.get(position));
//        holder.img_remove.setOnClickListener(v -> {
//            int newPosition = holder.getBindingAdapterPosition();
//            Constant.rowList.remove(newPosition);
//            Constant.selectedTopicIDs.clear();
//            for(int j = 0 ; j < Constant.rowList.size() ; j++){
//                Constant.selectedTopicIDs.add(Constant.rowList.get(j).getTopic().getId());
//            }
//            Log.e("TAG", "onBindViewHolder: \n" + Constant.selectedTopicIDs);
//            updateApi(Constant.selectedTopicIDs);
////            String temp = Constant.allTopics.get(newPosition);
////            Constant.allTopics.remove(newPosition);
////            Constant.selectedTopics.add(temp);
//
//            notifyItemRemoved(newPosition);
//            notifyItemRangeChanged(newPosition, Constant.allTopics.size());
//
////            Toast.makeText(mContext, String.valueOf(Constant.rowList.size()), Toast.LENGTH_SHORT).show();
//        });

        holder.img_remove.setOnClickListener(v -> mItemClickListener.onItemClick(position,mData,holder));
//        holder.tv_name.setOnClickListener(v -> Toast.makeText(mContext, String.valueOf(Constant.mData.size()), Toast.LENGTH_SHORT).show());
//        Log.e("TAG", "*********onBindViewHolder: "+ mData.get(position).getTopic().get_topic());

    }

    private void updateApi(ArrayList<String> ids) {
        Map<String, String> map = new HashMap<>();
//        HashMap<String, ArrayList<String>> map2 = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(mContext, SharedPrefreances.AUTH_TOKEN));

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < ids.size(); i++) {
            jsonArray.put(ids.get(i));
        }
        JSONObject obj = new JSONObject();
        try {
            // Add the JSONArray to the JSONObject
            obj.put("topic", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String xtoken = SharedPrefreances.getSharedPreferenceString(mContext, SharedPrefreances.AUTH_TOKEN);
//        map2.put("topic", ids);


//        ChooseTopicResponse updateProfileParam = new ChooseTopicResponse(ids);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

//        ChooseTopicResponse updateProfileParam = new ChooseTopicResponse(ids);

//        Call<UserUpdatedSelectedTopics> call = webApi.getUpdatedSelectedTopics(xtoken, ids);
        UpdateTopicsParams updateProfileParam = new UpdateTopicsParams();
        updateProfileParam.setUpdatedTopic(ids);
        Call<UserUpdatedSelectedTopics2> call = webApi.getUpdatedSelectedTopics(map, updateProfileParam);

        call.enqueue(new Callback<UserUpdatedSelectedTopics2>() {
            @Override
            public void onResponse(Call<UserUpdatedSelectedTopics2> call, Response<UserUpdatedSelectedTopics2> response) {
                Log.e("UserInterestAdapter", "onResponse: "+ "API run");
                Toast.makeText(mContext, response.body().getSuccess().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserUpdatedSelectedTopics2> call, Throwable t) {
                Log.e("UserInterestAdapter", "onFailure: " + "API Didn't Run");
                Utilss.showToast(mContext, mContext.getString(R.string.error_msg), R.color.grey);
            }

//            @Override
//            public void onResponse(Call<UserUpdatedSelectedTopics> call, Response<UserUpdatedSelectedTopics> response) {
//                Log.e("UserInterestAdapter", "onResponse: "+ "API run");
//                Toast.makeText(mContext, response.body().getSuccess().toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<UserUpdatedSelectedTopics> call, Throwable t) {
//                Log.e("UserInterestAdapter", "onFailure: " + "API Didn't Run");
//                Utilss.showToast(mContext, mContext.getString(R.string.error_msg), R.color.grey);
//            }
        });


    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView img_remove;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            img_remove = itemView.findViewById(R.id.img_remove);
        }
    }


    public interface dataChange {
        void itemChange();
    }
}
