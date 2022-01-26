package com.example.co3102_cw2;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.co3102_cw2.Model.Option;
import com.example.co3102_cw2.Model.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class AddNewOption extends BottomSheetDialogFragment {

    public static  final String TAG = "BottomDialogBox";

    private EditText newOptionText;
    private Button newOptionSaveButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference tmp = db.collection("tmp");
    CollectionReference quest = db.collection("questions");

    public static AddNewOption newInstance(){
        return new AddNewOption();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogBoxStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_option, container, false);
////        getDialog().getWindow().setDecorFitsSystemWindows(false);
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        newOptionText = getView().findViewById(R.id.newOptionText);
        newOptionSaveButton = getView().findViewById(R.id.newOptionButton);

        boolean editMode = false;
        final Bundle bundle = getArguments();
        if(bundle != null) {
            editMode = true;
            String option = bundle.getString("option");
            newOptionText.setText(option);
            if (option.length() > 0) {
                //TODO: Make button available if there is text
                //newOptionSaveButton.setTextColor(Colour.);

            }
        }
            newOptionText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().equals("")){
                        newOptionSaveButton.setEnabled(false);
                        newOptionSaveButton.setTextColor(Color.GRAY);
                    } else {
                        newOptionSaveButton.setEnabled(true);
                        newOptionSaveButton.setTextColor(Color.GREEN);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Nothing
                }
            });

            boolean finalEditMode = editMode;
            newOptionSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String OptionText = newOptionText.getText().toString();
                    if(finalEditMode){
                        // Update existing Option in The List

                        //Update The Database on new Values
                        tmp.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                    if(documentSnapshot.get("text").toString().equals(bundle.getString("option"))){
                                        documentSnapshot.getReference().update("text",OptionText);
                                    }
                                }
                                // Updates existing database options
                                quest.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for(QueryDocumentSnapshot documentSnapshotQuest : queryDocumentSnapshots){
                                            if(documentSnapshotQuest.get("question") != null)
                                                if(documentSnapshotQuest.get("question").equals(bundle.getString("parent"))){
                                                    Question question = documentSnapshotQuest.toObject(Question.class);
                                                    for(Option o : question.getOptions()){
                                                        if(o.getText().equals(bundle.getString("option"))){
                                                            o.setText(OptionText);
                                                        }
                                                    }
                                                    documentSnapshotQuest.getReference().update("options",question.getOptions());
                                                }
                                        }
                                    }
                                });
                            }
                        });

                    } else {
                        Option option = new Option();
                        option.setStatus(false);
                        option.setText(OptionText);
                        addTempToDatabase(option);
                    }
                    dismiss();
                }
            });

        }


    @Override
    public void onDismiss(DialogInterface dialogInterface){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialogInterface);
        }
    }

    private void addTempToDatabase(Option option){
        db.collection("tmp").add(option).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Successfully added option to the Database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error adding document: " + e);
            }
        });
    }


}
