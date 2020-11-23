package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voteapp.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity {

    private List<TextView> textViewList = new ArrayList<>();
    private List<LinearLayout> cardGroupsList = new ArrayList<>();
    private Map<Integer, String> groupMap = new HashMap<Integer, String>();
    private List<String> groupNames = new ArrayList<>();
    private String userId;
    private Button buttonCreate;
    private Button buttonCreate2;
    private Dialog customDialog;
    private Switch isPublic;
    private EditText createGroupName;
    private EditText createGroupPassword;
    private TextView createGroupPasswordLabel;
    private ImageView picture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        buttonCreate = findViewById(R.id.buttonCreate);

        final LinearLayout groupCard1 = findViewById(R.id.cardGroup1);
        final LinearLayout groupCard2 = findViewById(R.id.cardGroup2);
        final LinearLayout groupCard3 = findViewById(R.id.cardGroup3);
        final LinearLayout groupCard4 = findViewById(R.id.cardGroup4);

        cardGroupsList.add(groupCard1);
        cardGroupsList.add(groupCard2);
        cardGroupsList.add(groupCard3);
        cardGroupsList.add(groupCard4);


        final TextView group1 = findViewById(R.id.group1);
        final TextView group2 = findViewById(R.id.group2);
        final TextView group3 = findViewById(R.id.group3);
        final TextView group4 = findViewById(R.id.group4);

        textViewList.add(group1);
        textViewList.add(group2);
        textViewList.add(group3);
        textViewList.add(group4);
        checkVisibility();

        groupCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("groupId", groupMap.get(group1.getId()));
                intent.putExtra("groupTitle", groupNames.get(0));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        groupCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("groupId", groupMap.get(group2.getId()));
                intent.putExtra("groupTitle", groupNames.get(1));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        groupCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("groupId", groupMap.get(group3.getId()));
                intent.putExtra("groupTitle", groupNames.get(2));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        groupCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("groupId", groupMap.get(group4.getId()));
                intent.putExtra("groupTitle", groupNames.get(3));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        customDialog = new Dialog(this);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.setContentView(R.layout.custom_dialog);
                createGroupName = customDialog.findViewById(R.id.groupNameEditText);
                createGroupPassword = customDialog.findViewById(R.id.groupPasswordEditText);
                createGroupPasswordLabel = customDialog.findViewById(R.id.createGroupPasswordLabel);
                isPublic = customDialog.findViewById(R.id.isPublic);
                picture = customDialog.findViewById(R.id.createGroupImage);
                buttonCreate2 = customDialog.findViewById(R.id.buttonCreate2);

                picture.setImageResource(R.drawable.tools);
                picture.setTag(R.drawable.tools);
                final String name = getResources().getResourceName((Integer)picture.getTag());

                isPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            createGroupPassword.setVisibility(View.GONE);
                            createGroupPasswordLabel.setVisibility(View.GONE);
                        }
                        else{
                            createGroupPassword.setVisibility(View.VISIBLE);
                            createGroupPasswordLabel.setVisibility(View.VISIBLE);
                        }
                    }
                });

                buttonCreate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            sendPostCreateGroup(createGroupName.getText().toString(), isPublic.isChecked(), createGroupPassword.getText().toString(), name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        customDialog.dismiss();
                    }
                });

                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customDialog.show();
            }
        });
    }

    private void sendPostCreateGroup(String name, boolean isPublic, String password, String pictureName) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("description", "");
        obj.put("password", password);
        obj.put("isPublic", isPublic);
        obj.put("pictureName", pictureName);
        obj.put("owner_id", Long.valueOf(userId));

        String URL = "https://voteaplication.herokuapp.com/createNewGroup";
        Log.w("Dashboard", obj.toString());

        RequestManager requestManager = RequestManager.getInstance(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("Dashboard", "createGroup:failure", error.getCause());
                    }
                });
        requestManager.addToRequestQueue(jsObjRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        Log.e("Dashboard", "user Id= " + userId);
        getUserInfoApiRequest(userId);
    }

    private void parseUserGroups(Object response) throws JSONException {
        for (TextView textView : textViewList) {
            textView.setText("");
        }
        JSONArray jsonArray = ((JSONArray) response);
        for (int i = 0; i < jsonArray.length(); i++) {
            String name = jsonArray.getJSONObject(i).getString("name");
            String id = jsonArray.getJSONObject(i).getString("id");
            if (i < 4) {
                textViewList.get(i).setText(name);
            }
            groupMap.put(textViewList.get(i).getId(), id);
            groupNames.add(name);
        }
        checkVisibility();
    }

    private void checkVisibility() {
        for (int i = 0; i < textViewList.size(); i++){
            if (textViewList.get(i).getText().toString().isEmpty()) {
                cardGroupsList.get(i).setVisibility(View.INVISIBLE);
            } else {
                cardGroupsList.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    private void getUserInfoApiRequest(String userId) {
        RequestManager requestManager = RequestManager.getInstance(this);

        String URL = "https://voteaplication.herokuapp.com/getUserGroups/" + userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.e("Response", response.toString());
                try {
                    parseUserGroups(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API error: ", "#onErrorResponse in Dashboard");
            }
        });

        requestManager.addToRequestQueue(request);
    }


}