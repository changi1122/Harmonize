package kr.ac.chungbuk.harmonize.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import kr.ac.chungbuk.harmonize.R;

public class MusicListItemView extends LinearLayout {

    TextView tvName, tvArtist, tvMatchRate;
    ImageView ivLevel;

    public MusicListItemView(Context context) {
        super(context);
        init(context);
    }

    public MusicListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.music_list_item, this, true);

        tvName = findViewById(R.id.tvName);
        tvArtist = findViewById(R.id.tvArtist);
        ivLevel = findViewById(R.id.ivLevel);
        tvMatchRate = findViewById(R.id.tvMatchRate);
    }

    public void setNameAndArtist(String name, String artist, Integer level, Integer matchRate, Boolean is_prefer) {
        tvName.setText(name);
        tvArtist.setText(artist);
        if (level == 1) {
            ivLevel.setImageResource(R.drawable.circle_green);
        } else if (level == 2) {
            ivLevel.setImageResource(R.drawable.circle_yellow);
        }
        tvMatchRate.setText(matchRate.toString() + "%");
    }

}
