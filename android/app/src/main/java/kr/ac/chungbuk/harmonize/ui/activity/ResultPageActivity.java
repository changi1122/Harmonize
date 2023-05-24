package kr.ac.chungbuk.harmonize.ui.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import kr.ac.chungbuk.harmonize.R;

public class ResultPageActivity extends AppCompatActivity {

    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);
        TextView accuracyText = findViewById(R.id.accuracyText);
        TextView accuracyText2 = findViewById(R.id.accuracyText2);

// Simulating fetching accuracy from backend (replace with your actual implementation)
        int accuracyPercentage = 87;

// Update the accuracy text
        accuracyText.setText("정확도         ");
        accuracyText2.setText(accuracyPercentage + "%");

// Show/hide the accuracy circle based on the availability of accuracy percentage
        if (accuracyPercentage > 0) {
            accuracyText.setVisibility(View.VISIBLE);
        } else {
            accuracyText.setVisibility(View.GONE);
        }

        if (accuracyPercentage > 0) {
            accuracyText2.setVisibility(View.VISIBLE);
        } else {
            accuracyText2.setVisibility(View.GONE);
        }

        barChart = findViewById(R.id.chart);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 4));
        entries.add(new BarEntry(2, 3));
        entries.add(new BarEntry(3, 2));
        entries.add(new BarEntry(4, 1));
        entries.add(new BarEntry(5, 3));
        entries.add(new BarEntry(6, 2));
        entries.add(new BarEntry(7, 6));
        entries.add(new BarEntry(8, 8));
        entries.add(new BarEntry(9, 5));
        entries.add(new BarEntry(10, 3));
        entries.add(new BarEntry(11, 6));
        entries.add(new BarEntry(12, 5));
        entries.add(new BarEntry(13, 9));
        entries.add(new BarEntry(14, 10));
        entries.add(new BarEntry(15, 8));
        entries.add(new BarEntry(16, 12));

        // 그라데이션 색상 배열 설정
        int startColor = Color.GREEN;
        int centerColor = Color.YELLOW;
        int endColor = Color.RED;

        List<GradientColor> gradientColors = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            int color;
            if (i == 0) {
                color = startColor;
            } else if (i == entries.size() - 1) {
                color = endColor;
            } else {
                float ratio = (float) (i - 1) / (float) (entries.size() - 3);
                color = interpolateColor(centerColor, endColor, ratio);
            }
            gradientColors.add(new GradientColor(color, color));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setDrawValues(false); // 라벨 숨기기
        barDataSet.setGradientColors(gradientColors);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);

        BarData barData = new BarData(dataSets);
        barChart.setData(barData);

        // 범례(legend) 숨기기
        barChart.getLegend().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false); // x축 라벨 표시하지 않음
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis yLAxis = barChart.getAxisLeft();
        yLAxis.setDrawLabels(false); // y축 라벨 표시하지 않음
        yLAxis.setDrawGridLines(false);
        yLAxis.setDrawAxisLine(false);

        YAxis yRAxis = barChart.getAxisRight();
        yRAxis.setDrawLabels(false); // y축 라벨 표시하지 않음
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDescription(description);
        barChart.setRenderer(new RoundedBarChartRenderer(barChart, barChart.getAnimator(), barChart.getViewPortHandler()));
        barChart.invalidate();
    }

    // 그라데이션 색상 보간
    private int interpolateColor(int startColor, int endColor, float ratio) {
        int a = ave(Color.alpha(startColor), Color.alpha(endColor), ratio);
        int r = ave(Color.red(startColor), Color.red(endColor), ratio);
        int g = ave(Color.green(startColor), Color.green(endColor), ratio);
        int b = ave(Color.blue(startColor), Color.blue(endColor), ratio);
        return Color.argb(a, r, g, b);
    }

    // 두 색상 사이의 보간값 계산
    private int ave(int start, int end, float ratio) {
        return (int) (start + (ratio * (end - start)));
    }

    // 둥글게 처리된 BarChartRenderer 클래스 정의
    private static class RoundedBarChartRenderer extends BarChartRenderer {

        RoundedBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
            super(chart, animator, viewPortHandler);
        }

        protected void drawBar(Canvas c, IBarDataSet dataSet, int index, float left, float right, float top, float bottom) {
            // 막대 그래프의 모서리를 둥글게 처리하는 코드 추가
            Path path = new Path();
            float radius = 16f; // 모서리의 둥글기 정도를 조절할 수 있습니다.
            path.addRoundRect(left, top, right, bottom, radius, radius, Path.Direction.CW);
            c.drawPath(path, mRenderPaint);
        }
    }
}