package com.meest.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Faq.FaqQuestionDetailsActivity;
import com.meest.R;
import com.meest.responses.UserFAQResponse;

import java.io.Serializable;
import java.util.List;

public class FaqQuestionAdapter extends RecyclerView.Adapter<FaqQuestionAdapter.FaqQuestionViewHolder> {

    private List<UserFAQResponse.Row> FabQuestionList;

    public FaqQuestionAdapter(List<UserFAQResponse.Row> rows) {
        FabQuestionList = rows;
    }

    @NonNull
    @Override
    public FaqQuestionAdapter.FaqQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_question_list, parent, false);
        return new FaqQuestionAdapter.FaqQuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqQuestionAdapter.FaqQuestionViewHolder holder, int position) {
        holder.tvFaqQuestion.setText(FabQuestionList.get(position).getQuestion());

        holder.tvFaqQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(v.getContext(), FaqQuestionDetailsActivity.class);
                it.putExtra("data", (Serializable) FabQuestionList.get(position));
                v.getContext().startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return FabQuestionList.size();
    }

    public class FaqQuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvFaqQuestion;

        public FaqQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFaqQuestion = itemView.findViewById(R.id.tvFaqQuestion);

        }
    }
}
