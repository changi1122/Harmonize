package kr.ac.chungbuk.harmonize.item;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.model.MusicDetail;

public class MusicPlayingItemView extends LinearLayout {

    ImageView thumbnailView;
    TextView tvName, tvArtist;
    ImageButton ibClose;

    public MusicPlayingItemView(Context context) {
        super(context);
        init(context);
    }

    public MusicPlayingItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.music_playing_item, this, true);

        thumbnailView = findViewById(R.id.thumbnailView);
        tvName = findViewById(R.id.tvName);
        tvArtist = findViewById(R.id.tvArtist);
        ibClose = findViewById(R.id.ibClose);
        ibClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main = (MainActivity) context;
                main.hideMusicPlayingView();
            }
        });
    }

    public void setExpandedMode(boolean on) {
        if (on) {
            thumbnailView.animate().alpha(0.0f);
            tvName.animate().alpha(0.0f);
            tvArtist.animate().alpha(0.0f);
        }
        else {
            thumbnailView.animate().alpha(1.0f);
            tvName.animate().alpha(1.0f);
            tvArtist.animate().alpha(1.0f);
        }
    }

    public void setMusic(MusicDetail music) {
        tvName.setText(music.music_name);
        tvArtist.setText(music.artist);
        Glide
                .with(getContext())
                .load(Domain.url("/api/music/img/" + music.img_link))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(new ColorDrawable(Color.parseColor("#eeeeee")))
                .into(thumbnailView);
    }
}
