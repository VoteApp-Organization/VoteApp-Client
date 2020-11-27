package com.example.voteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.example.voteapp.utils.RequestManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;

public class Dashboard extends AppCompatActivity {

    private List<TextView> textViewList = new ArrayList<>();
    private List<LinearLayout> cardGroupsList = new ArrayList<>();
    private Map<Integer, String> groupMap = new HashMap<Integer, String>();
    private List<Group> groups = new ArrayList<>();
    private String userId;
    private Button buttonCreate;
    private Button logoutBtn;
    private Button buttonCreate2;
    private Dialog customDialog;
    private Switch isPublic;
    private EditText createGroupName;
    private EditText createGroupPassword;
    private TextView createGroupPasswordLabel;
    private ImageView picture;
    private SearchView searchView;
    private ListView searchingListView;
    private ListAdapter listAdapter;
    private List<String> searchingList = new ArrayList<String>();
    private TextView yourGroupsTextView;
    private TextView noGroupsTextView;
    private TextView viewAllGroups;


    private String mAuth;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        buttonCreate = findViewById(R.id.buttonCreate);
        searchView = findViewById(R.id.searchView);
        searchingListView = findViewById(R.id.searchingListView);
        logoutBtn = findViewById(R.id.logoutBtn);
        yourGroupsTextView = findViewById(R.id.yourGroupsTextView);
        noGroupsTextView = findViewById(R.id.noGroupsTextView);
        viewAllGroups = findViewById(R.id.viewAllGroups);

        checkVisibility();

        viewAllGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, AllGroupsActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("groups",(Serializable)groups);
                intent.putExtra("BUNDLE", args);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        customDialog = new Dialog(this);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
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

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Dashboard.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("queryText", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    getGroupByName(newText);
                } else {
                    searchingListView.setVisibility(View.GONE);
                }
                Log.e("queryText", newText);
                return false;
            }
        });
    }

    public void logout() {
        System.out.println(FirebaseAuth.getInstance());
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(Dashboard.this, LoginActivity.class);
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

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        mAuth = intent.getStringExtra("fire");
        Log.e("Dashboard", "user Id= " + userId);
        getUserInfoApiRequest(userId);
    }

    private void parseUserGroups(Object response) throws JSONException {
        groups.clear();
        for (TextView textView : textViewList) {
            textView.setText("");
        }

        JSONArray jsonArray = ((JSONArray) response);
        Gson gson = new Gson();
        for (int i = 0; i < jsonArray.length(); i++) {
            groups.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Group.class));
            if(i<4){
                textViewList.get(i).setText(groups.get(i).getName());
            }
        }

        if (groups.isEmpty()) {
            noGroupsTextView.setVisibility(View.VISIBLE);
            viewAllGroups.setVisibility(View.INVISIBLE);
            yourGroupsTextView.setVisibility(View.INVISIBLE);
        } else {
            noGroupsTextView.setVisibility(View.INVISIBLE);
            viewAllGroups.setVisibility(View.VISIBLE);
            yourGroupsTextView.setVisibility(View.VISIBLE);
        }
        checkVisibility();
    }

    private void checkVisibility() {
        for (int i = 0; i < textViewList.size(); i++) {
            if (textViewList.get(i).getText().toString().isEmpty()) {
                cardGroupsList.get(i).setVisibility(View.GONE);
            } else {
                cardGroupsList.get(i).setVisibility(View.VISIBLE);
            }
        }

        LinearLayout groupCard1 = findViewById(R.id.cardGroup1);
        LinearLayout groupCard2 = findViewById(R.id.cardGroup2);
        LinearLayout groupCard3 = findViewById(R.id.cardGroup3);
        LinearLayout groupCard4 = findViewById(R.id.cardGroup4);

        cardGroupsList.add(groupCard1);
        cardGroupsList.add(groupCard2);
        cardGroupsList.add(groupCard3);
        cardGroupsList.add(groupCard4);

        textViewList.add((TextView) findViewById(R.id.group1));
        textViewList.add((TextView) findViewById(R.id.group2));
        textViewList.add((TextView) findViewById(R.id.group3));
        textViewList.add((TextView) findViewById(R.id.group4));

        groupCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("group", groups.get(0));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        groupCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("group", groups.get(1));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        groupCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("group", groups.get(2));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        groupCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("group", groups.get(3));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
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

    private void getGroupByName(String name) {
        RequestManager requestManager = RequestManager.getInstance(this);
        String URL = "https://voteaplication.herokuapp.com/getGroupsByName/" + "?searchName=" + name;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                searchingList.clear();
                Log.e("Response", response.toString());
                for (int i = 0; i < response.length() && i < 4; i++) {
                    String name = null;
                    try {
                        name = response.getJSONObject(i).getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    searchingList.add(name);
                }
                refreshList();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API error: ", "#onErrorResponse in Dashboard");
            }
        });
        requestManager.addToRequestQueue(request);
    }

    private void refreshList() {
        searchingListView.setVisibility(View.VISIBLE);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(Dashboard.this, android.R.layout.simple_list_item_1, searchingList);
        searchingListView.setAdapter(itemsAdapter);
    }
}