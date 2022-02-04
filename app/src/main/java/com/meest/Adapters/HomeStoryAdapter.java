package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.Interfaces.FeedDataCallback;
import com.meest.R;
import com.meest.responses.UserFollowerStoryResponse;
import com.meest.meestbhart.utilss.SharedPrefreances;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.meest.meestbhart.utilss.SharedPrefreances.PROFILE_IMAGE;

public class HomeStoryAdapter extends RecyclerView.Adapter<HomeStoryAdapter.StoryViewHolder> {
    private Context context;
    private List<UserFollowerStoryResponse.Row> storyData;
    private FeedDataCallback feedDataCallback;
    final int VIEW_TYPE_ADD_STORY = 0;
    final int VIEW_TYPE_STORY = 1;
    private UserFollowerStoryResponse userOwnStoryResponse;

    public HomeStoryAdapter(Context context, List<UserFollowerStoryResponse.Row> storyData, FeedDataCallback feedDataCallback, UserFollowerStoryResponse userOwnStoryResponse) {
        this.context = context;
        this.storyData = storyData;
        this.feedDataCallback = feedDataCallback;
        this.userOwnStoryResponse = userOwnStoryResponse;
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_story_adapter, parent, false);
        return new StoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NotNull StoryViewHolder holder, final int position) {
        UserFollowerStoryResponse.Row data = storyData.get(position);
            if(data.getUser().getDisplayPicture()==null || data.getUser().getDisplayPicture().equals("")){
                String image=SharedPrefreances.getSharedPreferenceString(context,PROFILE_IMAGE);
                Glide.with(context).load(image).placeholder(R.drawable.chat_user_placeholder).into(holder.story_pic);
            }else {
                Glide.with(context).load(data.getUser().getDisplayPicture()).placeholder(R.drawable.chat_user_placeholder).into(holder.story_pic);
            }
            holder.txt_name.setText(data.getUser().getUsername());
        holder.itemView.setOnClickListener(v -> {
            feedDataCallback.storyClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return storyData.size();
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView story_pic;
        TextView txt_name;

        public StoryViewHolder(View itemView) {
            super(itemView);

            //  img_profile_gradient = itemView.findViewById(R.id.img_profile_gradient);
            txt_name = itemView.findViewById(R.id.txt_name);
            story_pic = itemView.findViewById(R.id.story_pic);
        }
    }
}

/*
public class HomeStoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    FragmentActivity activity;
    ArrayList<UserFollowerStoryResponse.Row> userStoryList ;
    HomeFragments homeFragments;

    private int VIEW_TYPE_ADD_STORY=0;
    private int VIEW_TYPE_STORY=1;



    public HomeStoryAdapter(FragmentActivity activity, ArrayList<UserFollowerStoryResponse.Row> userStoryList, HomeFragments homeFragments) {
        this.activity = activity;
        this.userStoryList = userStoryList;
        this.homeFragments = homeFragments;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ADD_STORY) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_story_item_add, parent, false);
            return new AddStoryViewHolder(view);
        }
        else {

            View storyView = LayoutInflater.from(activity).inflate(R.layout.home_story_item, parent, false);
            return new StoryViewHolder(storyView);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof AddStoryViewHolder)
        {

        }
        else
        {

        }

    }

    @Override
    public int getItemCount() {

        return 8+1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 ? VIEW_TYPE_ADD_STORY : VIEW_TYPE_STORY);
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }


    public class AddStoryViewHolder extends RecyclerView.ViewHolder{

        public AddStoryViewHolder(View itemView) {
            super(itemView);

        }
    }
}


 */
