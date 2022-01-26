package com.example.co3102_cw2.Adapter;

import static com.example.co3102_cw2.AddNewOption.TAG;
import static com.example.co3102_cw2.AddNewOption.newInstance;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.co3102_cw2.AddNewOption;
import com.example.co3102_cw2.AddNewQuestionActivity;
import com.example.co3102_cw2.AdminActivity;
import com.example.co3102_cw2.EditQuestionActivity;
import com.example.co3102_cw2.Model.Option;
import com.example.co3102_cw2.Model.Question;
import com.example.co3102_cw2.Model.QuestionListItem;
import com.example.co3102_cw2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {
    private List<Option> optionList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference tmp = db.collection("tmp");
    CollectionReference quest = db.collection("questions");


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
//        bundle.putInt("id",option.getId());
        bundle.putString("option",option.getText());
        AddNewOption fragment = new AddNewOption();
        fragment.setArguments(bundle);
        fragment.show(fragment.getParentFragmentManager(), TAG);

        Log.d(TAG, "Current Option Text: " + option.getText());



        // TODO: Edit Option within existing question
    }

    public void deleteOption(int position){
        Option option = optionList.get(position);
        // Delete From TMP if it is still within creation of a question, however on
        // fail it will assume that this is not a tmp value and an already existing question therefore it will remove from a question
          tmp.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots.isEmpty()){
                        quest.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                    if(documentSnapshot.get("question") != null)
                                        if(documentSnapshot.get("question").equals(option.getParent())){
                                            Question question = documentSnapshot.toObject(Question.class);
                                            question.getOptions().remove(position);
                                            documentSnapshot.getReference().update("options",question.getOptions());
                                        }
                                }
                            }
                        });

                    } else {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            if(documentSnapshot.get("text").toString().equals(option.getText())){
                                documentSnapshot.getReference().delete();
                            }
                        }
                    }
                }
            });


        // TODO: Remove Option From a question if it exists in a question


        //Delete From a LocalList
        optionList.remove(position);
        notifyItemRemoved(position);

    }

}
