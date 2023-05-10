package kr.ac.chungbuk.harmonize.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import kr.ac.chungbuk.harmonize.R;

public class MoreItemView extends LinearLayout {

    TextView tvMenuName;

    public MoreItemView(Context context) {
        super(context);
        init(context);
    }

    public MoreItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.more_item, this, true);

        tvMenuName = (TextView) findViewById(R.id.tvMenuName);
    }

    public void setMenuName(String name) {
        tvMenuName.setText(name);
    }
}
