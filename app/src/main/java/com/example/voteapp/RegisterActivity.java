package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private TextView alreadyRegisterd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        alreadyRegisterd = findViewById(R.id.alreadyRegistered);
        alreadyRegisterd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }
}