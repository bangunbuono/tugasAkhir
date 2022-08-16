package com.example.fixinventori.Activity.Report;

import android.content.Context;
import android.widget.TextView;

import com.example.fixinventori.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class LineChartMarkerView extends MarkerView {

    TextView tvContent;

    public LineChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        int x = (int) e.getX();
//        String xValue = StatFrag.xValue.get(x);
        tvContent.setText(String.format("Rp %s \n %s",(int) e.getY(), x));
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
