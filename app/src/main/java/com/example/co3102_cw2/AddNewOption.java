//package com.example.co3102_cw2;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//
//public class AddNewOption extends BottomSheetDialogFragment {
//
//    public static  final String TAG = "BottomDialogBox";
//
//    private EditText newOptionText;
//    private Button newOptionSaveButton;
//    // Possible needs a database
//
//    public static AddNewOption newInstance(){
//        return new AddNewOption();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setStyle(STYLE_NORMAL, R.style.Base_Theme_AppCompat_Dialog);
//    }
//
//    @Override
//    public View OnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        View view = inflater.inflate(R.layout.new_option, container, false);
//        getDialog().getWindow().setDecorFitsSystemWindows(true);
//        return view;
//    }
//}
