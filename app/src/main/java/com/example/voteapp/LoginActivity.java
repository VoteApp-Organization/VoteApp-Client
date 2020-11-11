package com.example.voteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private TextView notRegisteredButton;
    private EditText email;
    private EditText password;
    private Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        notRegisteredButton = findViewById(R.id.notRegisteredButton);
        notRegisteredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendPostRequestToCheckUserExist();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendPostRequestToCheckUserExist() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("email", email.getText().toString());
        obj.put("password", password.getText().toString());

        String URL = "https://voteaplication.herokuapp.com/loginUser";

        RequestManager requestManager = RequestManager.getInstance(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                        try {
                            intent.putExtra("userId", response.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("LoginActivity", "signInWithEmail:failure", error.getCause());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        requestManager.addToRequestQueue(jsObjRequest);
    }
}