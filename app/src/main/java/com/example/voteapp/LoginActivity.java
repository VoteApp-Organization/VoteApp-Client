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
import com.example.voteapp.utils.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private TextView notRegisteredButton;
    private EditText email;
    private EditText password;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
       // currentUser = mAuth.getCurrentUser();
      //  updateUI(currentUser);

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
             /*   try {
                    getInfo();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FirebaseAuthException e) {
                    e.printStackTrace();
                }*/
            }
        });
    }
/*
    private void login(){
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("LoginActivity", "signInWithEmail:success");
                            currentUser = mAuth.getCurrentUser();
                            try {
                                getInfo();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (FirebaseAuthException e) {
                                e.printStackTrace();
                            }
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("LoginActivity", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //  updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void getInfo() throws IOException, FirebaseAuthException {
     /*   currentUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();

                            Log.e("LoginActivity",  idToken);
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });

        String idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjNlNTQyN2NkMzUxMDhiNDc2NjUyMDhlYTA0YjhjYTZjODZkMDljOTMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdm90ZWFwcC0xMzk1NiIsImF1ZCI6InZvdGVhcHAtMTM5NTYiLCJhdXRoX3RpbWUiOjE2MDU5ODM2NjEsInVzZXJfaWQiOiJKSXNXSXAwRFdOWDFuTDYzWDhDdWdjTGM1QlAyIiwic3ViIjoiSklzV0lwMERXTlgxbkw2M1g4Q3VnY0xjNUJQMiIsImlhdCI6MTYwNTk4MzY2MSwiZXhwIjoxNjA1OTg3MjYxLCJlbWFpbCI6Im1hcmNpbkBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsibWFyY2luQGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.F1n6DGO-9iEhOLAmDQGiY2srnxR4SYgPE2nWLUZcYSFlPn7dOJUH2BPTQ3gxpwaJkDdNuNNyt4rZ2rjC9cja5RxI1ptli4uXkY6sVv1Ea6ZKhnamxVn3RzVIk4oCiBtV-AMkXORGzUdSU1pUytB3hrRp01w3AOkvZdhM4FYHBsFBV2rpal7E3b1maK1a3jo3oenfdrks6nonZzYA-g2M0dzHiOAN6rxaXpdk4acolDk0QWRdUfRRx_j97P67XxEwAkrMAeih94aPyTu0BTbeA3FLiw-2qk46apIAMNTfU3KuacFbZVxrgeb3FlPdJhXWTZhl__NFnCWAysBBrg50vg";
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
        // idToken comes from the client app (shown above)
        FirebaseToken decodedToken = mAuth.verifyIdToken(idToken);

        String uid = decodedToken.getUid();


        Log.e("LoginActivity",  uid);
    }
*/
    private void sendPostRequestToCheckUserExist() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("email", email.getText().toString());
        obj.put("password", password.getText().toString());

        String URL = "https://voteaplication.herokuapp.com/loginUser";
        Log.w("LoginActivity", obj.toString());

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