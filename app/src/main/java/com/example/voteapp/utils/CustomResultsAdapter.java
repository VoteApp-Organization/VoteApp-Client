package com.example.voteapp.utils;

import android.annotation.SuppressLint;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.voteapp.R;
import com.example.voteapp.model.SurveyResultWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomResultsAdapter extends BaseAdapter {
    private Context context;
    private SurveyResultWrapper resultWrapper;

    public CustomResultsAdapter(Context context, SurveyResultWrapper resultWrapper) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.resultWrapper = resultWrapper;
    }

    @Override
    public int getCount() {
        return resultWrapper.questions.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
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
        convertView = inflater.inflate(R.layout.custom_chart_item, null);
        TextView textView = convertView.findViewById(R.id.textView2);
        textView.setText(position + 1 + ") " + resultWrapper.questions.get(position).questionContent);
        final Button displyAnswers = convertView.findViewById(R.id.displyAnswers);
        AnyChartView anyChartView = (AnyChartView) convertView.findViewById(R.id.any_chart_view);

        if (resultWrapper.questions.get(position).questionType.equals("String")) {
            anyChartView.setVisibility(View.GONE);
            displyAnswers.setVisibility(View.VISIBLE);
            final Dialog customDialog = new Dialog(context);
            displyAnswers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog.setContentView(R.layout.custom_dialog_string_answers);
                    Button button = customDialog.findViewById(R.id.button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customDialog.dismiss();
                        }
                    });
                    TextView answerContentTextView = customDialog.findViewById(R.id.answerContentTextView);
                    for (int i = 0; i < resultWrapper.questions.get(position).answers.size(); i++) {
                        answerContentTextView.append(resultWrapper.questions.get(position).answers.get(i).getAnswerContent().get(0)+"\n");
                    }
                    customDialog.show();
                }
            });
        } else {
            anyChartView.setVisibility(View.VISIBLE);
            displyAnswers.setVisibility(View.GONE);
            Pie pie = AnyChart.pie();
            pie.background("#FAFAFA");
            List<DataEntry> data = new ArrayList<>();
            if (!resultWrapper.questions.get(position).questionType.equals("String")) {
                if (resultWrapper.questions.get(position).numberOfAppearances != null) {
                    for (Map.Entry<String, Integer> entry : resultWrapper.questions.get(position).numberOfAppearances.entrySet()) {
                        data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
                        System.out.println(entry.getKey() + " = " + entry.getValue());
                    }
                }
                pie.data(data);
                anyChartView.setChart(pie);
            }
        }

        return convertView;
    }
}