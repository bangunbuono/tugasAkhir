package com.example.fixinventori.Activity.Report;

import android.content.Context;
import android.widget.TextView;

import com.example.fixinventori.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

public class LineChartMarkerView extends MarkerView {

    TextView tvContent, tvContent2;
    LineChart lineChart;
    String xValue;

    public LineChartMarkerView(Context context, int layoutResource, LineChart lineChart) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
        tvContent2 = findViewById(R.id.tvContent2);
        this.lineChart = lineChart;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                xValue = lineChart.getXAxis()
                        .getValueFormatter()
                        .getFormattedValue(e.getX(), lineChart.getXAxis());

            }

            @Override
            public void onNothingSelected() {

            }
        });

        tvContent.setText(String.format("%s",(int) e.getY()));
        tvContent2.setText(xValue);
    }

    public MPPointF mpPointF;

    @Override
    public MPPointF getOffset() {
        if(mpPointF==null){
            mpPointF = new MPPointF(-(getWidth()/2), -getHeight());
        }
        return mpPointF;

    }
}
