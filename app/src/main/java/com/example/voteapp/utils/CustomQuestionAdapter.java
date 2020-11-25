package com.example.voteapp.utils;

import android.annotation.SuppressLint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.voteapp.R;
import com.example.voteapp.SingleQuestion;
import com.example.voteapp.Survey;

import java.util.ArrayList;
import java.util.List;

public class CustomQuestionAdapter extends BaseAdapter {
    private List<SingleQuestion> addedQuestions;

    private Context context;
    private TextView questionTitle;
    private TextView numberOfQuestion;
    private List<Integer> clickedQuestions = new ArrayList<>();
    private LinearLayout relativeLayout;

    public CustomQuestionAdapter(Context context, List<SingleQuestion> addedQuestions) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.addedQuestions = addedQuestions;
    }

    @Override
    public int getCount() {
        return addedQuestions.size();
    }

    @Override
    public Object getItem(int position) {

        return questionTitle;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_question_item, null);
        questionTitle = (TextView) convertView.findViewById(R.id.surveyTitle);
        numberOfQuestion = (TextView) convertView.findViewById(R.id.numberOfQuestion);
        final View finalConvertView = convertView;
        questionTitle.setText(addedQuestions.get(position).getQuestionContent());
        numberOfQuestion.setText(String.valueOf(position + 1));


        relativeLayout = convertView.findViewById(R.id.test);
        relativeLayout.removeAllViews();
        relativeLayout.setVisibility(View.INVISIBLE);
        Drawable lay = context.getResources().getDrawable(R.drawable.custom_container_add_question);
        if (clickedQuestions.contains(position)) {
            relativeLayout.setVisibility(View.VISIBLE);
            View line = relativeLayout.inflate(context, R.layout.custom_create_answer_item, null);
            if(addedQuestions.get(position).getQuestionType().equals("Picklist") && addedQuestions.get(position).getPicklistValues() != null){
                for (String answer : addedQuestions.get(position).getPicklistValues()) {
                    line = relativeLayout.inflate(context, R.layout.custom_create_answer_item, null);
                    TextView txt = line.findViewById(R.id.singleAnswerContent);
                    txt.setText(answer);
                    relativeLayout.addView(line);
                }
                line.setBackground(lay);
                TextView txt = line.findViewById(R.id.singleAnswerContent);
                txt.setTextColor(Color.rgb(198, 198, 198));
                txt.setText("Click here to add new option");
            }
        }else{
            relativeLayout.setVisibility(View.INVISIBLE);
        }
        notifyDataSetChanged();
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.groupItemLayout);
    /*    if (position == addedQuestions.size() - 1) {
            layout.setBackground(lay);
            questionTitle.setTextColor(Color.rgb(208, 208, 208));
            numberOfQuestion.setText("+");
            numberOfQuestion.setTextColor(Color.rgb(208, 208, 208));
        }*/
        Log.e("asd", String.valueOf(convertView.getId()));

        questionTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(clickedQuestions);
                System.out.println(position);
                if (clickedQuestions.contains(position)) {
                    int i=0;
                    for(i=0; i<clickedQuestions.size(); i++){
                        if(clickedQuestions.get(i) == position){
                            break;
                        }
                    }
                    clickedQuestions.remove(i);
                } else {
                    clickedQuestions.add(position);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}