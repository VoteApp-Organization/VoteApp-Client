package com.example.voteapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voteapp.model.Group;
import com.example.voteapp.model.SingleQuestion;
import com.example.voteapp.model.Survey;
import com.example.voteapp.utils.CommonUtils;
import com.example.voteapp.utils.RequestManager;
import com.example.voteapp.utils.StaticResources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateSurveyActivity extends AppCompatActivity {
    private Button previewSurveyBtn;
    private Button cancelSurveyBtn;
    private Button shareSurveyButton;
    private Button backButton;
    private Group group;
    private String userId;
    private String surveyName;
    private String surveyDesc;
    private String surveyPicture;
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
    private Survey survey;

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
        backButton = findViewById(R.id.backButton);

        previewSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateSurveyActivity.this, PreviewSurveyContainerActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("group", group);
                Bundle args = new Bundle();
                Date cDate = new Date();
                String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                args.putSerializable("survey", (Serializable) new Survey(surveyName, surveyDesc, Long.valueOf(userId), fDate, true, false, false, allQuestions.size(), surveyPicture, true));
                args.putSerializable("questionsList", (Serializable) allQuestions);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);
            }
        });

        cancelSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        shareSurveyButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Date cDate = new Date();
                String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                try {
                    sendSurvey(new Survey(surveyName, surveyDesc, Long.valueOf(userId), fDate, true, false, false, allQuestions.size(), surveyPicture,true ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                            allQuestions.add(new SingleQuestion(addQuestionEditText.getText().toString(), false, false, 50, spinner.getSelectedItem().toString(), new ArrayList<String>()));
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
        Bundle args = intent.getBundleExtra("BUNDLE");
        userId = intent.getStringExtra("userId");
        group = (Group) intent.getSerializableExtra("group");
        if(args != null) {
            allQuestions = (List<SingleQuestion>) args.getSerializable("questionsList");
            survey = (Survey) args.getSerializable("survey");
            group = (Group) intent.getSerializableExtra("group");
            userId = intent.getStringExtra("userId");
            surveyName = survey.getVoteTitle();
            surveyDesc = survey.getSurveyDescription();
            surveyPicture = survey.getSurveyPicture();
            refreshList();
        }else{
            surveyName = intent.getStringExtra("surveyName");
            surveyDesc = intent.getStringExtra("surveyDesc");
            surveyPicture = intent.getStringExtra("surveyPicture");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateSurveyActivity.this, GroupViewActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("group", group);
        intent.putExtra("surveyName", surveyName);
        startActivity(intent);
    }

    private void sendSurvey(Survey survey) throws JSONException {
        Gson gsonPackage = new Gson();
        String jsonString = gsonPackage.toJson(survey);
        JSONObject obj = new JSONObject(jsonString);
        obj.put("group_id", group.getId());

        Gson gsonQuestions = new Gson();
        String element = gsonQuestions.toJson(
                allQuestions,
                new TypeToken<ArrayList<SingleQuestion>>() {
                }.getType());
        JSONArray list = new JSONArray(element);
        obj.put("questions", list);
        Log.e("CreateSurveyActivity", "" + obj);

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
                });
        requestManager.addToRequestQueue(jsObjRequest);
    }
}