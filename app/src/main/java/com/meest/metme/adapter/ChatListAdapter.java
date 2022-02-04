package com.meest.metme.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ChatListAdapterBinding;
import com.meest.metme.ChatBoatActivity;
import com.meest.metme.model.chat.NewChatListResponse;
import com.meest.metme.viewmodels.ChatViewModel;
import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> implements Filterable {

    ChatListAdapterBinding chatListAdapterBinding;
    Activity activity;
   public static ArrayList<NewChatListResponse> responseList;
    public static  ArrayList<NewChatListResponse> filteredList;
    public NewChatListResponse newChatListResponse;

    ChatListAdapter.onChatListItemClickListener mItemClickListener;
    private int row_index;
    public boolean isChecked;
    LinearLayout buttonLayout;
    ImageView search;
    LinearLayout delete;
    int totalCount=0;
    public ChatListAdapter(Activity activity, ArrayList<NewChatListResponse> responseList, LinearLayout buttonLayout, ImageView search, LinearLayout delete) {
        this.activity = activity;
        this.responseList = responseList;
        this.filteredList = responseList;
        this.buttonLayout = buttonLayout;
        this.search = search;
        this.delete = delete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        chatListAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.chat_list_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(chatListAdapterBinding.getRoot());
        viewHolder.setBinding(chatListAdapterBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setChatStoryList(responseList.get(position));
        holder.getChatListAdapterBinding().layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //           mItemClickListener.onItemClick(position);

             /*   Intent intent = new Intent(activity, ChatBoatActivity.class);
                activity.startActivity(intent);*/
            }
        });
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

        if (position==responseList.size()-1){
            holder.getChatListAdapterBinding().marginView.setVisibility(View.VISIBLE);
        }else {
            holder.getChatListAdapterBinding().marginView.setVisibility(View.GONE);
        }
        if (isChecked) {
            search.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.VISIBLE);
            holder.getChatListAdapterBinding().checkbox.setVisibility(View.VISIBLE);
            holder.getChatListAdapterBinding().rightLayout.setVisibility(View.GONE);
        } else {
            search.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            holder.getChatListAdapterBinding().checkbox.setVisibility(View.GONE);
            holder.getChatListAdapterBinding().rightLayout.setVisibility(View.VISIBLE);
        }
        if (responseList.get(position).isSelected()) {
            holder.getChatListAdapterBinding().checkbox.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chat_request_select));
        } else {
            holder.getChatListAdapterBinding().checkbox.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chat_request_unselect));
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
                }
                else {
                    Intent intent = new Intent(activity, ChatBoatActivity.class);
                    intent.putExtra("newChatListResponse", responseList.get(position));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_MULTIPLE_TASK /*| Intent.FLAG_ACTIVITY_CLEAR_TASK*/);
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
                    ArrayList<NewChatListResponse> filteredList = new ArrayList<>();

                    for (NewChatListResponse response : responseList) {
                        if (response.getUsername().toLowerCase().contains(charString) /*|| androidVersion.getMobile().startsWith(charString)*/) {
                            filteredList.add(response);
                        } /*else if (response.getUsername().toLowerCase().contains(charString) *//*|| androidVersion.getMobile().startsWith(charString)*//*) {
                            filteredList.add(response);
                        } else if (response.getLastname().toLowerCase().contains(charString) *//*|| androidVersion.getMobile().contains(charString)*//*) {
                            filteredList.add(response);
                        }*/
                    }
                    ChatListAdapter.filteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<NewChatListResponse>) filterResults.values;
                if (filteredList==null){
                    filteredList=new ArrayList<>();
                }
                responseList = filteredList;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ChatListAdapterBinding chatListAdapterBinding;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public ChatListAdapterBinding getChatListAdapterBinding() {
            return chatListAdapterBinding;
        }

        public void setBinding(ChatListAdapterBinding chatListAdapterBinding) {
            this.chatListAdapterBinding = chatListAdapterBinding;
        }

        public void setChatStoryList(NewChatListResponse chatListModel) {
            chatListAdapterBinding.setChatViewModel(new ChatViewModel(chatListModel, activity));
        }
    }

    public void setOnItemClickListener(final ChatListAdapter.onChatListItemClickListener onItemClickListener) {
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

