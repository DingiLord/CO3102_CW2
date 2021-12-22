package com.example.co3102_cw2.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.co3102_cw2.AddNewOption;
import com.example.co3102_cw2.AddNewQuestionActivity;
import com.example.co3102_cw2.AdminActivity;
import com.example.co3102_cw2.Model.Option;
import com.example.co3102_cw2.Model.QuestionListItem;
import com.example.co3102_cw2.R;

import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {
    private List<Option> optionList;
    private AddNewQuestionActivity activity;

    public OptionAdapter(AddNewQuestionActivity activity){
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
        CheckBox option;

        ViewHolder(View view){
            super(view);
            option = view.findViewById(R.id.QuestionCheckBox);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull OptionAdapter.ViewHolder holder, int position) {
        Option item = optionList.get(position);
        holder.option.setText(item.getText());
        holder.option.setChecked(item.isStatus());
    }

    @Override
    public int getItemCount() {
        return optionList == null ? 0 : optionList.size();
    }

    public void setOptionList(List<Option> optionList){
        this.optionList = optionList;
        notifyDataSetChanged();
    }
    public void editOption(int position){
        Option option = optionList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",option.getId());
        bundle.putString("option",option.getText());
        AddNewOption fragment = new AddNewOption();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewOption.TAG);
    }
}
