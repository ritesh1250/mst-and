package com.meest.metme.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.GetDocsMediaChatsResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatLinkDataAdapter extends RecyclerView.Adapter<ChatLinkDataAdapter.DoctumentViewHolder> implements Filterable {

    ArrayList<GetDocsMediaChatsResponse.Data.LastWeek> MediaDocumentList;
    ArrayList<GetDocsMediaChatsResponse.Data.LastWeek> mMediaDocumentList;
    Context context;

    public ChatLinkDataAdapter(ArrayList<GetDocsMediaChatsResponse.Data.LastWeek> mediaDocumentList, Context context) {
        MediaDocumentList = mediaDocumentList;
        this.mMediaDocumentList = new ArrayList<>(MediaDocumentList);
        this.context = context;
    }

    @NonNull
    @Override
    public ChatLinkDataAdapter.DoctumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.docyument_item_list, parent, false);
        ChatLinkDataAdapter.DoctumentViewHolder DVH = new ChatLinkDataAdapter.DoctumentViewHolder(v);
        return DVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatLinkDataAdapter.DoctumentViewHolder holder, int position) {

        GetDocsMediaChatsResponse.Data.LastWeek documents = mMediaDocumentList.get(position);

        Log.e("TAG", "maintaineddocuments=" + documents.getAttachmentType());

        if (documents.getAttachmentType().equals("File")) {
            holder.rl_doc.setVisibility(View.VISIBLE);
            holder.iv_document_images.setVisibility(View.GONE);
//            holder.name.setText(documents.getFileURL());
            holder.name.setText(documents.getJsonData().getFilename());
            holder.date.setText(Datetime(documents.getCreatedAt()));
        }
    }


    public String Datetime(String dated) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
        try {
            date = dateFormat.parse(dated);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
        String dateStr = formatter.format(date);
        Log.e("TAg", "merimasti=" + dateStr);
        return dateStr;
    }

    @Override
    public int getItemCount() {
        return mMediaDocumentList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String Key = constraint.toString();
            if (Key.isEmpty()) {

                mMediaDocumentList = MediaDocumentList;

            } else {
                ArrayList<GetDocsMediaChatsResponse.Data.LastWeek> lstFiltered = new ArrayList<>();
                for (GetDocsMediaChatsResponse.Data.LastWeek row : MediaDocumentList) {
                    final String filterString = constraint.toString().toLowerCase();
                    if (row.getAttachmentType().toLowerCase().startsWith(filterString)) {
                        lstFiltered.add(row);
                    }

                }
                mMediaDocumentList = lstFiltered;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = mMediaDocumentList;
            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mMediaDocumentList = (ArrayList<GetDocsMediaChatsResponse.Data.LastWeek>) results.values;
            notifyDataSetChanged();
        }
    };


    public class DoctumentViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView size;
        public TextView date;
        public ImageView iv_document_images;
        public RelativeLayout rl_doc;

        public DoctumentViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.doctument_name);
            date = (TextView) itemView.findViewById(R.id.doctument_date);
            rl_doc = itemView.findViewById(R.id.rl_doc);
            iv_document_images = (ImageView) itemView.findViewById(R.id.iv_document_images);
        }
    }

}
