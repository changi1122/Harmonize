package kr.ac.chungbuk.harmonize.item;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.model.MusicDetail;
import kr.ac.chungbuk.harmonize.utility.PitchConverter;

public class MusicDetailItemView extends LinearLayout {

    TextView tvName, tvArtist, tvMaxPitch, tvMinPitch, tvDifficulty;
    ImageView thumbnailView;

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

        tvName = findViewById(R.id.tvName);
        tvArtist = findViewById(R.id.tvArtist);
        thumbnailView = findViewById(R.id.thumbnailView);
        tvMaxPitch = findViewById(R.id.tvMaxPitch);
        tvMinPitch = findViewById(R.id.tvMinPitch);
        tvDifficulty = findViewById(R.id.tvDifficulty);
    }

    public void loadMusicDetail(MusicDetail music)
    {
        tvName.setText(music.music_name);
        tvArtist.setText(music.artist);
        tvMaxPitch.setText(PitchConverter.doubleToPitch(music.max));
        tvMinPitch.setText(PitchConverter.doubleToPitch(music.min));
        tvDifficulty.setText((music.level == 0) ? "쉬움" : ((music.level == 1) ? "보통" : "어려움"));

        Glide
                .with(getContext())
                .load(Domain.url("/api/music/img/" + music.img_link))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(new ColorDrawable(Color.parseColor("#eeeeee")))
                .into(thumbnailView);
    }

}
