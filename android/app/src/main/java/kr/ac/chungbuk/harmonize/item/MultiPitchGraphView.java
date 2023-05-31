package kr.ac.chungbuk.harmonize.item;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.utility.PitchConverter;

public class MultiPitchGraphView extends LinearLayout implements SeekBar.OnSeekBarChangeListener {

    RequestQueue queue;
    private LineChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    public MultiPitchGraphView(Context context) {
        super(context);
        init(context);
    }

    public MultiPitchGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pitch_graph, this, true);

        queue = Volley.newRequestQueue(getContext());

        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);
        seekBarX = findViewById(R.id.seekBar1);
        seekBarY = findViewById(R.id.seekBar2);

        chart = findViewById(R.id.chart);
        chart.setViewPortOffsets(0, 0, 0, 0);
        chart.setBackgroundColor(Color.WHITE);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleXEnabled(true);
        chart.setScaleYEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.getViewPortHandler().setMaximumScaleX(4f);

        chart.setDrawBorders(true);
        chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(300);

        XAxis x = chart.getXAxis();
        x.setEnabled(false);

        YAxis y = chart.getAxisLeft();
        //y.setTypeface(tfLight);
        y.setAxisMaximum(1.1f);
        y.setAxisMinimum(-0.1f);
        y.setLabelCount(4, false);
        y.setTextColor(Color.rgb(127, 127, 127));
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(true);
        y.setAxisLineColor(Color.WHITE);
        y.setDrawGridLines(true);
        y.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return PitchConverter.doubleToPitch(value);
            }

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return super.getAxisLabel(value, axis);
            }
        });


        chart.getAxisRight().setEnabled(false);

        // add data
        seekBarY.setOnSeekBarChangeListener(this);
        seekBarX.setOnSeekBarChangeListener(this);

        // Marker
        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);

        mv.setChartView(chart);
        chart.setMarker(mv);

        // lower max, as cubic runs significantly slower than linear
        seekBarX.setMax(700);

        seekBarX.setProgress(45);
        seekBarY.setProgress(100);

        chart.getLegend().setEnabled(false);

        chart.animateXY(2000, 2000);

        // don't forget to refresh the drawing
        chart.invalidate();
    }

    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random());
            values.add(new Entry(i, val));
        }

        LineDataSet set1, set2;

        ArrayList<Entry> values2 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random());
            values2.add(new Entry(i, val));
        }

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);

            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
            set2.setValues(values2);

            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.rgb(219, 153, 241));
            set1.setHighLightColor(Color.BLACK);
            set1.setColor(Color.rgb(219, 153, 241));
            set1.setFillColor(Color.rgb(219, 153, 241));
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            set2 = new LineDataSet(values, "DataSet 2");

            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set2.setCubicIntensity(0.2f);
            set2.setDrawFilled(true);
            set2.setDrawCircles(false);
            set2.setLineWidth(1.8f);
            set2.setCircleRadius(4f);
            set2.setCircleColor(Color.rgb(34, 104, 227));
            set2.setHighLightColor(Color.BLACK);
            set2.setColor(Color.rgb(34, 104, 227));
            set2.setFillColor(Color.rgb(34, 104, 227));
            set2.setFillAlpha(50);
            set2.setDrawHorizontalHighlightIndicator(false);
            set2.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });


            // create a data object with the data sets
            LineData data = new LineData(set1, set2);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        tvX.setText(String.valueOf(seekBarX.getProgress()));
        tvY.setText(String.valueOf(seekBarY.getProgress()));

        setData(seekBarX.getProgress(), seekBarY.getProgress());

        // redraw
        chart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
