package com.example.voteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voteapp.model.Group;
import com.example.voteapp.model.GroupsToExploreWrapper;
import com.example.voteapp.utils.CustomSpinner;
import com.example.voteapp.utils.RequestManager;
import com.example.voteapp.utils.StaticResources;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.voteapp.utils.CommonUtils.sendPostJoinGroup;

public class DashboardActivity extends AppCompatActivity {

    private List<TextView> textViewList = new ArrayList<>();
    private List<ImageView> imageViewList = new ArrayList<>();
    private List<LinearLayout> cardGroupsList = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<GroupsToExploreWrapper> publicGroups = new ArrayList<>();
    private String userId;
    private Button buttonCreate;
    private Button logoutBtn;
    private Button buttonCreate2;
    private Dialog customDialog;
    private Switch isPublic;
    private EditText createGroupName;
    private EditText groupDescriptionEditText;
    private SearchView searchView;
    private ListView searchingListView;
    private List<String> searchingListOfNames = new ArrayList<>();
    private List<String> searchingListOfIds = new ArrayList<>();
    private TextView yourGroupsTextView;
    private TextView noGroupsTextView;
    private TextView viewAllGroups;
    private TextView viewAllExploreGroups;
    private Context context;
    private Spinner spinnerIcons;
    private String selectedIcon;

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
        viewAllExploreGroups = findViewById(R.id.viewAllExploreGroups);
        context = this;

        checkVisibility();

        viewAllGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AllGroupsActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("groups", (Serializable) groups);
                intent.putExtra("BUNDLE", args);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        viewAllExploreGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ExploreActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("groups", (Serializable) groups);
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
                groupDescriptionEditText = customDialog.findViewById(R.id.groupDescriptionEditText);
                isPublic = customDialog.findViewById(R.id.isPublic);
                buttonCreate2 = customDialog.findViewById(R.id.buttonCreate2);
                spinnerIcons = customDialog.findViewById(R.id.spinner);

                final CustomSpinner customAdapter = new CustomSpinner(getApplicationContext());
                spinnerIcons.setAdapter(customAdapter);
                spinnerIcons.setSelection(0);
                spinnerIcons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedIcon = String.valueOf(parent.getAdapter().getItem(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                buttonCreate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Group grp = new Group(createGroupName.getText().toString(), groupDescriptionEditText.getText().toString(), isPublic.isChecked(), selectedIcon, Long.valueOf(userId));
                            sendPostCreateGroup(grp);
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
                new AlertDialog.Builder(DashboardActivity.this)
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
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void sendPostCreateGroup(Group grp) throws JSONException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(grp);
        JSONObject obj = new JSONObject(jsonString);

        String URL = "https://voteaplication.herokuapp.com/createNewGroup";
        Log.e("Dashboard", obj.toString());

        RequestManager requestManager = RequestManager.getInstance(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                        getUserInfoApiRequest(userId);
                        getAllPublicGroups();
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
        getAllPublicGroups();
    }

    private void parseUserGroups(Object response, boolean yourGroups) throws JSONException {
        if (yourGroups) {
            groups.clear();
            for (TextView textView : textViewList) {
                textView.setText("");
            }

            JSONArray jsonArray = ((JSONArray) response);
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                groups.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Group.class));
                if (i < 4) {
                    textViewList.get(i).setText(groups.get(i).getName());
                    imageViewList.get(i).setImageResource(StaticResources.mapOfIcons.get(groups.get(i).getPicture_name()));
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
        } else {
            publicGroups.clear();
            JSONArray jsonArray = ((JSONArray) response);
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length() && i < 2; i++) {
                publicGroups.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), GroupsToExploreWrapper.class));
            }
            refreshExplore();
        }
    }

    private void refreshExplore() {
        View.OnClickListener alert = new View.OnClickListener() {
            public void onClick(View v) {
                final int i = (int) v.getTag();
                Log.e("asd", String.valueOf(i));

                new AlertDialog.Builder(DashboardActivity.this)
                        .setTitle("Join to group")
                        .setMessage("Are you sure you want to join to this group?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    sendPostJoinGroup(context, userId, publicGroups.get(i).group.getId(), "");
                                    Intent intent = new Intent(DashboardActivity.this, GroupViewActivity.class);
                                    intent.putExtra("group", publicGroups.get(i).group);
                                    intent.putExtra("userId", userId);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        };

        LinearLayout cardPublicGroup1 = findViewById(R.id.cardPublicGroup1);
        cardPublicGroup1.setOnClickListener(alert);
        cardPublicGroup1.setTag(0);
        TextView publicGroup1title = findViewById(R.id.publicGroup1title);
        TextView publicGroup1members = findViewById(R.id.publicGroup1members);
        TextView publicGroup1surveys = findViewById(R.id.publicGroup1surveys);
        ImageView publicGroup1image = findViewById(R.id.publicGroup1image);

        LinearLayout cardPublicGroup2 = findViewById(R.id.cardPublicGroup2);
        cardPublicGroup2.setOnClickListener(alert);
        cardPublicGroup2.setTag(1);
        TextView publicGroup2title = findViewById(R.id.publicGroup2title);
        TextView publicGroup2members = findViewById(R.id.publicGroup2members);
        TextView publicGroup2surveys = findViewById(R.id.publicGroup2surveys);
        ImageView publicGroup2image = findViewById(R.id.publicGroup2image);

        if (publicGroups.size() > 0) {
            publicGroup1title.setText(publicGroups.get(0).group.getName());
            publicGroup1members.setText(publicGroups.get(0).numberOfUsers + " members");
            publicGroup1surveys.setText(publicGroups.get(0).numberOfSurveys + " surveys");
            publicGroup1image.setImageResource(StaticResources.mapOfIcons.get(publicGroups.get(0).group.getPicture_name()));
            if (publicGroups.size() > 1) {
                publicGroup2title.setText(publicGroups.get(1).group.getName());
                publicGroup2members.setText(publicGroups.get(1).numberOfUsers + " members");
                publicGroup2surveys.setText(publicGroups.get(1).numberOfSurveys + " surveys");
                publicGroup2image.setImageResource(StaticResources.mapOfIcons.get(publicGroups.get(0).group.getPicture_name()));
            }else{
                cardPublicGroup2.setVisibility(View.GONE);
            }
        }else{
            cardPublicGroup1.setVisibility(View.GONE);
            cardPublicGroup2.setVisibility(View.GONE);
        }


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

        imageViewList.add((ImageView) findViewById(R.id.group1image));
        imageViewList.add((ImageView) findViewById(R.id.group2image));
        imageViewList.add((ImageView) findViewById(R.id.group3image));
        imageViewList.add((ImageView) findViewById(R.id.group4image));

        groupCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, GroupViewActivity.class);
                intent.putExtra("group", groups.get(0));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        groupCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, GroupViewActivity.class);
                intent.putExtra("group", groups.get(1));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        groupCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, GroupViewActivity.class);
                intent.putExtra("group", groups.get(2));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        groupCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, GroupViewActivity.class);
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
                    parseUserGroups(response, true);
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
                searchingListOfNames.clear();
                searchingListOfIds.clear();
                Log.e("Response", response.toString());
                for (int i = 0; i < response.length() && i < 4; i++) {
                    String name = null;
                    long id = 0;
                    try {
                        name = response.getJSONObject(i).getString("name");
                        id = response.getJSONObject(i).getLong("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    searchingListOfNames.add(name);
                    searchingListOfIds.add(String.valueOf(id));
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
        final ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(DashboardActivity.this, android.R.layout.simple_list_item_1, searchingListOfNames);
        searchingListView.setAdapter(itemsAdapter);

        searchingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                for (Group grp : groups) {
                    if (grp.getId().equals(searchingListOfIds.get(position))) {
                        Intent intent = new Intent(DashboardActivity.this, GroupViewActivity.class);
                        intent.putExtra("group", grp);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        return;
                    }
                }
                new AlertDialog.Builder(DashboardActivity.this)
                        .setTitle("Join to group")
                        .setMessage("Are you sure you want to join to this group?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    sendPostJoinGroup(context, userId, searchingListOfIds.get(position), "");
                                    searchView.setQuery("", false);
                                    searchView.setIconified(true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void getAllPublicGroups() {
        RequestManager requestManager = RequestManager.getInstance(this);

        String URL = "https://voteaplication.herokuapp.com/exploreGroupsByName/" + userId + "/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.e("Response", response.toString());
                try {
                    parseUserGroups(response, false);
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