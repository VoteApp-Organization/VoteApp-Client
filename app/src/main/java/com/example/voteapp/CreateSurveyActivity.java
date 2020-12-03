package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voteapp.utils.CommonUtils;
import com.example.voteapp.utils.RequestManager;
import com.example.voteapp.utils.StaticResources;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateSurveyActivity extends AppCompatActivity {
    private Button previewSurveyBtn;
    private Button cancelSurveyBtn;
    private Button shareSurveyButton;
    private Group group;
    private String userId;
    private String surveyName;
    private List<SingleQuestion> allQuestions = new ArrayList<>();
    private EditText addQuestionEditText;
    private LayoutInflater inflater;
    private LinearLayout linearLayoutMain;
    private LinearLayout linearLayoutSub;
    private TextView numberOfQuestion;
    private View convertView;
    private List<LinearLayout> subLayoutsList = new ArrayList<>();
    private List<View> addOptionViews = new ArrayList<>();
    private List<TextView> questionContentTextViews = new ArrayList<>();
    private List<TextView> questionNumberTextViews = new ArrayList<>();
    private List<TextView> typeOfQuestionTextViews = new ArrayList<>();
    private Context context;
    private View lineAdd;
    private ScrollView scroll;
    private Spinner spinner;
    private View.OnClickListener addAnswerListener;
    private View.OnClickListener showHideExpandedListListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayoutMain = findViewById(R.id.test2);
        scroll = findViewById(R.id.scroll);
        convertView = inflater.inflate(R.layout.custom_question_item, null);
        context = this;

        previewSurveyBtn = findViewById(R.id.buttonPreviewSurvey);
        cancelSurveyBtn = findViewById(R.id.buttonCancelSurvey);
        shareSurveyButton = findViewById(R.id.createSurveyShare);

        previewSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateSurveyActivity.this, "Survey will be previewed here",
                        Toast.LENGTH_SHORT).show();
            }
        });

        cancelSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        shareSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateSurveyActivity.this, "Survey will be shared here",
                        Toast.LENGTH_SHORT).show();
            }
        });
        refreshList();
    }

    private void refreshSubList(LinearLayout linearLayoutSub, int i) {
        linearLayoutSub.removeAllViews();
        final Drawable lay = getResources().getDrawable(R.drawable.custom_container_add_question);
        if (allQuestions.get(i).getPicklistValues() != null) {
            for (String answer : allQuestions.get(i).getPicklistValues()) {
                View line2 = linearLayoutSub.inflate(this, R.layout.custom_create_answer_item, null);
                TextView txt3 = line2.findViewById(R.id.singleAnswerContent);
                txt3.setText(answer);
                linearLayoutSub.addView(line2);
            }
        }
        final View line2 = linearLayoutSub.inflate(this, R.layout.custom_create_answer_item, null);
        line2.setBackground(lay);
        final TextView txt = line2.findViewById(R.id.singleAnswerContent);
        txt.setTextColor(Color.rgb(198, 198, 198));
        txt.setText("Click here to add new option");
        addOptionViews.add(line2);
        line2.setTag(i);
        line2.setOnClickListener(addAnswerListener);
        linearLayoutSub.addView(line2);
    }

    private void refreshList() {
        subLayoutsList.clear();
        questionNumberTextViews.clear();
        addOptionViews.clear();
        questionContentTextViews.clear();
        typeOfQuestionTextViews.clear();
        linearLayoutMain.removeAllViews();
        for (int i = 0; i < allQuestions.size(); i++) {
            final View line = convertView.inflate(this, R.layout.custom_question_item, null);
            linearLayoutSub = line.findViewById(R.id.test);
            linearLayoutSub.setTag(i);
            subLayoutsList.add(linearLayoutSub);
            TextView titleTextView = line.findViewById(R.id.questionTitleTextView);
            TextView numberTextView = line.findViewById(R.id.numberOfQuestion);
            TextView typeTextView = line.findViewById(R.id.typeOfQuestionTextView);
            questionContentTextViews.add(titleTextView);
            questionNumberTextViews.add(numberTextView);
            typeOfQuestionTextViews.add(typeTextView);
            typeTextView.setText("Question type: " + allQuestions.get(i).getQuestionType());
            numberTextView.setText(String.valueOf(i + 1));
            titleTextView.setText(allQuestions.get(i).getQuestionContent());
            linearLayoutMain.addView(line);
            linearLayoutSub.setVisibility(View.GONE);
            typeTextView.setVisibility(View.GONE);
            final Drawable lay = getResources().getDrawable(R.drawable.custom_container_add_question);
            if (allQuestions.get(i).getQuestionType().equals("Picklist")) {
                refreshSubList(linearLayoutSub, i);
            } else if (allQuestions.get(i).getQuestionType().equals("Checkbox")) {
                View line2 = linearLayoutSub.inflate(this, R.layout.custom_create_answer_item, null);
                TextView txt3 = line2.findViewById(R.id.singleAnswerContent);
                txt3.setText("True");
                linearLayoutSub.addView(line2);
                line2 = linearLayoutSub.inflate(this, R.layout.custom_create_answer_item, null);
                txt3 = line2.findViewById(R.id.singleAnswerContent);
                txt3.setText("False");
                linearLayoutSub.addView(line2);
            } else {
                View line2 = linearLayoutSub.inflate(this, R.layout.custom_create_answer_item, null);
                line2.setBackground(lay);
                TextView txt3 = line2.findViewById(R.id.singleAnswerContent);
                txt3.setTextColor(Color.rgb(170, 170, 170));
                txt3.setText("The answer will be filled by user");
                linearLayoutSub.addView(line2);
            }
        }

        showHideExpandedListListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (typeOfQuestionTextViews.get((Integer) v.getTag()).getVisibility() == View.VISIBLE) {
                    typeOfQuestionTextViews.get((Integer) v.getTag()).setVisibility(View.GONE);
                } else if (typeOfQuestionTextViews.get((Integer) v.getTag()).getVisibility() == View.GONE) {
                    typeOfQuestionTextViews.get((Integer) v.getTag()).setVisibility(View.VISIBLE);
                }
                if (subLayoutsList.get((Integer) v.getTag()).getVisibility() == View.GONE) {
                    subLayoutsList.get((Integer) v.getTag()).setVisibility(View.VISIBLE);
                } else if (subLayoutsList.get((Integer) v.getTag()).getVisibility() == View.VISIBLE) {
                    subLayoutsList.get((Integer) v.getTag()).setVisibility(View.GONE);
                }

            }
        };

        addAnswerListener = new View.OnClickListener() {
            public void onClick(View v) {
                final int tag = (int) v.getTag();
                final Drawable lay = getResources().getDrawable(R.drawable.custom_container_add_question);
                subLayoutsList.get(tag).removeView(v);

                lineAdd = linearLayoutSub.inflate(context, R.layout.custom_answer_item_add, null);
                final EditText singleAnswerContentEdit = lineAdd.findViewById(R.id.singleAnswerContentEdit);
                subLayoutsList.get(tag).addView(lineAdd);
                lineAdd.setBackground(lay);

                singleAnswerContentEdit.setEnabled(true);
                singleAnswerContentEdit.requestFocus();
                CommonUtils.showSoftKeyboard(CreateSurveyActivity.this, singleAnswerContentEdit);
                singleAnswerContentEdit.setTextColor(Color.rgb(117, 117, 117));
                singleAnswerContentEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            allQuestions.get(tag).getPicklistValues().add(singleAnswerContentEdit.getText().toString());
                            refreshSubList(subLayoutsList.get(tag), tag);
                            return true;
                        }
                        return false;
                    }
                });
            }
        };
        for (int i = 0; i < allQuestions.size(); i++) {
            questionContentTextViews.get(i).setOnClickListener(showHideExpandedListListener);
            questionContentTextViews.get(i).setTag(i);
            questionNumberTextViews.get(i).setOnClickListener(showHideExpandedListListener);
            questionNumberTextViews.get(i).setTag(i);
            typeOfQuestionTextViews.get(i).setOnClickListener(showHideExpandedListListener);
            typeOfQuestionTextViews.get(i).setTag(i);
        }

        renderAddQuestionButton();
    }

    private void renderAddQuestionButton() {
        final View line = linearLayoutMain.inflate(this, R.layout.custom_question_item, null);
        linearLayoutMain.addView(line);
        Drawable lay = this.getResources().getDrawable(R.drawable.custom_container_add_question);
        line.setBackground(lay);
        numberOfQuestion = line.findViewById(R.id.numberOfQuestion);
        numberOfQuestion.setText("+");
        numberOfQuestion.setTextColor(Color.rgb(198, 198, 198));
        TextView txt = line.findViewById(R.id.questionTitleTextView);
        txt.setText("Click here to add new option");
        txt.setTextColor(Color.rgb(198, 198, 198));
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutMain.removeView(line);
                lineAdd = linearLayoutMain.inflate(context, R.layout.custom_question_add_item, null);
                numberOfQuestion = lineAdd.findViewById(R.id.numberOfQuestion);
                numberOfQuestion.setText(String.valueOf(allQuestions.size() + 1));
                linearLayoutMain.addView(lineAdd);
                addQuestionEditText = lineAdd.findViewById(R.id.questionTitleEditText);
                addQuestionEditText.setTextColor(Color.rgb(117, 117, 117));
                addQuestionEditText.setHint("Write here question content");
                spinner = lineAdd.findViewById(R.id.typeSpinner);
                ArrayAdapter adapter
                        = new ArrayAdapter(
                        context,
                        android.R.layout.simple_spinner_item, StaticResources.questionTypes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                scroll.fullScroll(View.FOCUS_DOWN);
                CommonUtils.showSoftKeyboard(CreateSurveyActivity.this, addQuestionEditText);
                addQuestionEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            scroll.fullScroll(View.FOCUS_DOWN);
                            Log.e("asd", spinner.getSelectedItem().toString());
                            allQuestions.add(new SingleQuestion(addQuestionEditText.getText().toString(), spinner.getSelectedItem().toString(), new ArrayList<String>()));
                            refreshList();
                            subLayoutsList.get(subLayoutsList.size() - 1).setVisibility(View.VISIBLE);
                            typeOfQuestionTextViews.get(typeOfQuestionTextViews.size() - 1).setVisibility(View.VISIBLE);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        group = (Group) intent.getSerializableExtra("group");
        surveyName = intent.getStringExtra("surveyName");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateSurveyActivity.this, GroupView.class);
        intent.putExtra("userId", userId);
        intent.putExtra("group", group);
        intent.putExtra("surveyName", surveyName);
        startActivity(intent);
    }

    private void sendSurvey(final String idToken) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("voteTitle", surveyName);
        obj.put("description", "");
        obj.put("author_id", Long.valueOf(userId));

        String URL = "https://voteaplication.herokuapp.com/createNewSurvey";

        RequestManager requestManager = RequestManager.getInstance(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.w("CreateSurveyActivity", "createSurvey:SUCCESS" + response.toString());
                        onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("CreateSurveyActivity", "createSurvey:failure", error.getCause());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID-TOKEN", idToken);
                params.put("group_Id", group.getId());
                return params;
            }
        };
        requestManager.addToRequestQueue(jsObjRequest);
    }
}