package com.example.co3102_cw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Register Button
        Button registerButton = (Button) findViewById(R.id.RegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Wowie", Toast.LENGTH_SHORT).show();
                // Possibly pass over the entered email to the register field of email for Quality Of Life
                Intent intent = new Intent(getBaseContext(),RegisterActivity.class);
                startActivity(intent);

            }
        });
    }

}