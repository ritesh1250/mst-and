package com.meest.metme.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ChatRequestListAdapterBinding;
import com.meest.metme.ChatRequestActivity;
import com.meest.metme.model.ChatListModel;
import com.meest.metme.model.ChatRequestListModel;
import com.meest.metme.viewmodels.ChatRequestViewModel;
import com.meest.metme.viewmodels.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatRequestListAdapter extends RecyclerView.Adapter<ChatRequestListAdapter.ViewHolder> implements Filterable {


    ChatRequestListAdapterBinding chatRequestListAdapterBinding;
    Activity activity;
    public static List<ChatRequestListModel> responseList;
    public static List<ChatRequestListModel> filteredList;

    ChatRequestListAdapter.onChatListItemClickListener mItemClickListener;
    public boolean isChecked;
    ImageView search;
    LinearLayout delete;
    LinearLayout buttonLayout;
    private int row_index;


    public ChatRequestListAdapter(Activity activity, List<ChatRequestListModel> responseList, LinearLayout buttonLayout, ImageView search, LinearLayout delete) {
        this.activity = activity;
        this.responseList = responseList;
        this.filteredList = responseList;
        this.search = search;
        this.delete = delete;
        this.buttonLayout = buttonLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        chatRequestListAdapterBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.chat_request_list_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(chatRequestListAdapterBinding.getRoot());
        viewHolder.setBinding(chatRequestListAdapterBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setChatStoryList(responseList.get(position));
        holder.getChatListAdapterBinding().layoutMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!listContain()) {
                    isChecked = true;
                    row_index = holder.getAdapterPosition();
                    responseList.get(position).setSelected(true);
                    notifyDataSetChanged();
                }
                return false;
            }
        });
        if (isChecked) {
            search.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.VISIBLE);
            holder.getChatListAdapterBinding().checkbox.setVisibility(View.VISIBLE);
        } else {
            search.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);
            holder.getChatListAdapterBinding().checkbox.setVisibility(View.GONE);
        }
        if (responseList.get(position).isSelected()) {
            holder.getChatListAdapterBinding().checkbox.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chat_request_select));
            holder.getChatListAdapterBinding().txtUserName.setTextColor(Color.parseColor("#000000"));
            holder.getChatListAdapterBinding().txtMsg.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.getChatListAdapterBinding().checkbox.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chat_request_unselect));
            holder.getChatListAdapterBinding().txtUserName.setTextColor(Color.parseColor("#939393"));
            holder.getChatListAdapterBinding().txtMsg.setTextColor(Color.parseColor("#959BA8"));
        }


        holder.getChatListAdapterBinding().layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked) {
                    if (responseList.get(position).isSelected()) {
                        responseList.get(position).setSelected(false);
                    } else {
                        responseList.get(position).setSelected(true);
                    }

                    if (listContain()) {
                        isChecked = true;
                    } else {
                        isChecked = false;
                    }
                    notifyDataSetChanged();

                } else {
                    Intent intent = new Intent(activity, ChatRequestActivity.class);
                    intent.putExtra("username", responseList.get(position).getUsername());
                    intent.putExtra("userId", responseList.get(position).getUserId());
                    intent.putExtra("firstName", "" + responseList.get(position).getFirstname());
                    intent.putExtra("lastName", "" + responseList.get(position).getLastname());
                    intent.putExtra("date_time", "" + responseList.get(position).getCreatedAt());
                    activity.startActivity(intent);
                }
            }
        });

    }


    boolean listContain() {
        for (int i = 0; i < responseList.size(); i++) {
            if (responseList.get(i).isSelected()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredList = responseList;
                } else {
                    ArrayList<ChatRequestListModel> filteredList = new ArrayList<>();
                    for (ChatRequestListModel response : responseList) {
                        if (response.getUsername() == null) {
                            response.setUsername("");
                        }
                        if (response.getUsername().toLowerCase().contains(charString) /*|| androidVersion.getMobile().startsWith(charString)*/) {
                            filteredList.add(response);
                        }
                    }
                    ChatRequestListAdapter.filteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<ChatRequestListModel>) filterResults.values;
                if (filteredList == null) {
                    filteredList = new ArrayList<>();
                }
                responseList = filteredList;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ChatRequestListAdapterBinding chatListAdapterBinding;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public ChatRequestListAdapterBinding getChatListAdapterBinding() {
            return chatListAdapterBinding;
        }

        public void setBinding(ChatRequestListAdapterBinding chatListAdapterBinding) {

            this.chatListAdapterBinding = chatListAdapterBinding;

        }

        public void setChatStoryList(ChatRequestListModel chatListModel) {

            chatListAdapterBinding.setChatViewModel(new ChatRequestViewModel(activity, chatListAdapterBinding, chatListModel, responseList));
        }
    }

    public void setOnItemClickListener(final ChatRequestListAdapter.onChatListItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }


    public interface onChatListItemClickListener {

        void onItemClick(int position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}

