package com.example.voteapp.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class CommonUtils {

    public static void sendPostLeaveGroup(Context context, String userId, String groupId) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("user_Id", userId);
        obj.put("vote_Id", 10);
        obj.put("group_Id", groupId);

        String URL = "https://voteaplication.herokuapp.com/leaveGroup";
        Log.w("GroupView", obj.toString());

        RequestManager requestManager = RequestManager.getInstance(context);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                        // onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("GroupView", "leaveGroup:failure", error.getCause());
                    }
                });
        requestManager.addToRequestQueue(jsObjRequest);
    }

    public static void sendPostJoinGroup(Context context, String userId, String groupId, String password) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("user_Id", userId);
        obj.put("group_Id", groupId);
        obj.put("password", password);

        String URL = "https://voteaplication.herokuapp.com/joinGroup";
        Log.w("GroupView", obj.toString());

        RequestManager requestManager = RequestManager.getInstance(context);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                        // onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("GroupView", "leaveGroup:failure", error.getCause());
                    }
                });
        requestManager.addToRequestQueue(jsObjRequest);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Activity activity, EditText editText) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }
}
