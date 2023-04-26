package kr.ac.chungbuk.harmonize.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import kr.ac.chungbuk.harmonize.R;

public class SearchHistoryItemView extends LinearLayout {

    TextView historyTextView;

    public SearchHistoryItemView(Context context) {
        super(context);
        init(context);
    }

    public SearchHistoryItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.search_history_item, this, true);

        historyTextView = (TextView) findViewById(R.id.historyTextView);
    }

    public void setHistoryTextView(String history) {
        historyTextView.setText(history);
    }
}
