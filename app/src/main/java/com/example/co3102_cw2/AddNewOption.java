package com.example.co3102_cw2;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNewOption extends BottomSheetDialogFragment {

    public static  final String TAG = "BottomDialogBox";

    private EditText newOptionText;
    private Button newOptionSaveButton;
    // Possible needs a database

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

      //  FirebaseFirestore db = FirebaseFirestore.getInstance();
        boolean editMode = false;
        final Bundle bundle = getArguments();
        if(bundle != null){
            editMode = true;
            String option = bundle.getString("option");
            newOptionText.setText(option);
            if(option.length()>0){
                //TODO: Make button available if there is text
                //newOptionSaveButton.setTextColor(Colour.);

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

                    } else {
                        // Pass it to the list and add it to the options

                    }
                    dismiss();
                }
            });

        }


    }

    @Override
    public void onDismiss(DialogInterface dialogInterface){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialogInterface);
        }
    }


}
