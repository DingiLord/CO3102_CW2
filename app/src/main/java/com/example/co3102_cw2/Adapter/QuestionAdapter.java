package com.example.co3102_cw2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.co3102_cw2.AdminActivity;
import com.example.co3102_cw2.Model.QuestionListItem;
import com.example.co3102_cw2.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private List<QuestionListItem> questionList;
    private AdminActivity activity;

    public QuestionAdapter(AdminActivity activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_layout,parent,false);
        return new ViewHolder(itemView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox question;

        ViewHolder(View view){
            super(view);
            question = view.findViewById(R.id.QuestionCheckBox);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        QuestionListItem item = questionList.get(position);
        holder.question.setText(item.getQuestion());
        holder.question.setChecked(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void setQuestionList(List<QuestionListItem> questionList){
        this.questionList = questionList;
        notifyDataSetChanged();
    }
}
