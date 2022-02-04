package com.meest.metme.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.databinding.ChatRequestListAdapterBinding;
import com.meest.metme.ChatRequestActivity;
import com.meest.metme.model.ChatListModel;
import com.meest.metme.model.ChatRequestListModel;

import java.util.List;


public class ChatRequestViewModel extends ViewModel {

    public String username, image, msg, time, msgCount;

    public ChatRequestListModel chatListModel;
    List<ChatRequestListModel> chatListModelList;
    static Activity activity;
    ChatRequestListAdapterBinding chatListAdapterBinding;



    public ObservableBoolean isChecked = new ObservableBoolean(false);
//    public ObservableBoolean isClickable = new ObservableBoolean(true);

    public ChatRequestViewModel(Activity activity, ChatRequestListAdapterBinding chatListAdapterBinding, ChatRequestListModel chatListModel, List<ChatRequestListModel> chatListModelList) {

        this.activity = activity;
        this.chatListModel = chatListModel;
        this.username = chatListModel.getUsername();
        this.msg = chatListModel.getMsg();
        this.image = chatListModel.getDisplayPicture();
        this.time = chatListModel.getCreatedAt();
        this.msgCount = chatListModel.getMsgCount();
        this.chatListModelList = chatListModelList;
        this.chatListAdapterBinding = chatListAdapterBinding;
        
        chatListAdapterBinding.layoutMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(activity, "hi", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    


    public void onClick(){
        if (!isChecked.get()) {
            Intent intent = new Intent(activity, ChatRequestActivity.class);
            intent.putExtra("userId", chatListModel.getUserId());
            intent.putExtra("firstName", "" + chatListModel.getFirstname());
            intent.putExtra("lastName", "" + chatListModel.getLastname());
            activity.startActivity(intent);
        }else {
            if (chatListModel.isSelected()){
                chatListModel.setSelected(false);
            }else {
                chatListModel.setSelected(true);
            }

            if (listContain()){
                isChecked.set(true);
            }else {
                isChecked.set(false);
            }

        }
    }


    boolean listContain(){
        for(int i=0;i<chatListModelList.size();i++){
            if (chatListModelList.get(i).isSelected()){
                return true;
            }
        }
        return false;
    }

    @BindingAdapter("userImage")
    public static void userImage(ImageView imageView, String imageUrl) {

        if (imageUrl != null) {

            if (!imageUrl.isEmpty()) {
                Glide.with(imageView.getContext()).load(imageUrl)
                        .into(imageView);
            } else {
                if (activity != null)
                    imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.image_placeholder));
            }
        }
    }
}
