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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voteapp.utils.CustomAdapter;
import com.example.voteapp.utils.CustomAllGroupsAdapter;
import com.example.voteapp.utils.RequestManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllGroupsActivity extends AppCompatActivity {

    private List<Group> groups = new ArrayList<>();
    private String userId;
    private ListView list;
    private TextView groupTitle;
    private Button leaveButton;
    private Button createGroupButton;
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
        setContentView(R.layout.activity_group_view);
        list = findViewById(R.id.listView);
        groupTitle = findViewById(R.id.groupTitle);
        leaveButton = findViewById(R.id.buttonLeaveGroup);
        createGroupButton = findViewById(R.id.createSurveyButton);
        groupTitle.setText("Your groups");
        createGroupButton.setText("Create new group");

        customDialog = new Dialog(this);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.setContentView(R.layout.custom_dialog_create_group);
                createGroupName = customDialog.findViewById(R.id.groupNameEditText);
                createGroupPassword = customDialog.findViewById(R.id.groupPasswordEditText);
                createGroupPasswordLabel = customDialog.findViewById(R.id.createGroupPasswordLabel);
                isPublic = customDialog.findViewById(R.id.isPublic);
                picture = customDialog.findViewById(R.id.createGroupImage);
                buttonCreate2 = customDialog.findViewById(R.id.buttonCreate2);

                picture.setImageResource(R.drawable.tools);
                picture.setTag(R.drawable.tools);
                final String name = getResources().getResourceName((Integer) picture.getTag());

                isPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            createGroupPassword.setVisibility(View.GONE);
                            createGroupPasswordLabel.setVisibility(View.GONE);
                        } else {
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

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        Bundle args = intent.getBundleExtra("BUNDLE");
        groups = (List<Group>) args.getSerializable("groups");
        leaveButton.setVisibility(View.INVISIBLE);

        CustomAllGroupsAdapter adapter = new CustomAllGroupsAdapter(this, groups, userId);
        list.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AllGroupsActivity.this, Dashboard.class);
        //  setIntent.addCategory(Intent.CATEGORY_HOME);
        //   setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void sendPostCreateGroup(String name, boolean isPublic, String password, String pictureName) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("description", "");
        obj.put("groupPasword", password);
        obj.put("public", isPublic);
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
                        getUserInfoApiRequest(userId);
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

    private void parseUserGroups(Object response) throws JSONException {
        groups.clear();
        JSONArray jsonArray = ((JSONArray) response);
        Gson gson = new Gson();
        for (int i = 0; i < jsonArray.length(); i++) {
            groups.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Group.class));
        }
        CustomAllGroupsAdapter adapter = new CustomAllGroupsAdapter(this, groups, userId);
        list.setAdapter(adapter);
    }
}