package com.example.co3102_cw2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.co3102_cw2.AdminActivity;
import com.example.co3102_cw2.Model.Question;
import com.example.co3102_cw2.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private List<Question> questionList;
    private AdminActivity activity;
    private OnQuestionListener onQuestionListener;

    public QuestionAdapter(AdminActivity activity, OnQuestionListener onQuestionListener){
        this.activity = activity;
        this.onQuestionListener = onQuestionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_layout,parent,false);
        return new ViewHolder(itemView, onQuestionListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox question;
        OnQuestionListener onQuestionListener;
        ViewHolder(View view, OnQuestionListener onQuestionListener){
            super(view);
            question = view.findViewById(R.id.QuestionCheckBox);
            this.onQuestionListener = onQuestionListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onQuestionListener.onQuestionClick(getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        Question item = questionList.get(position);
        holder.question.setText(item.getQuestion());
        holder.question.setChecked(item.isStatus());
    }

    @Override
    public int getItemCount() {
        return questionList == null ? 0 : questionList.size();
    }

    public void setQuestionList(List<Question> questionList){
        this.questionList = questionList;
        notifyDataSetChanged();
    }

    public interface OnQuestionListener{
        void onQuestionClick(int position);
    }

}
