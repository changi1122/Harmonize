package kr.ac.chungbuk.harmonize.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import kr.ac.chungbuk.harmonize.R;

public class MusicDetailItemView extends LinearLayout {

    TextView tvName;

    public MusicDetailItemView(Context context) {
        super(context);
        init(context);
    }

    public MusicDetailItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.music_detail_item, this, true);
    }

    public void loadMusicDetail(Long id)
    {
        tvName = findViewById(R.id.tvName);
        tvName.setText(String.valueOf(id));
    }

}
