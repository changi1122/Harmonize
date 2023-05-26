package kr.ac.chungbuk.harmonize.item;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.model.Token;
import kr.ac.chungbuk.harmonize.service.TokenService;
import kr.ac.chungbuk.harmonize.ui.activity.HelloworldActivity;
import kr.ac.chungbuk.harmonize.ui.activity.LoginActivity;

public class MusicListItemView extends LinearLayout {

    RequestQueue queue;
    TextView tvName, tvArtist, tvMatchRate;
    ImageView ivLevel;
    ImageButton ibBookmark;

    Long musicId;
    Boolean isFavorite = false;

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

        queue = Volley.newRequestQueue(getContext());

        tvName = findViewById(R.id.tvName);
        tvArtist = findViewById(R.id.tvArtist);
        ivLevel = findViewById(R.id.ivLevel);
        tvMatchRate = findViewById(R.id.tvMatchRate);
        ibBookmark = findViewById(R.id.ibBookmark);
    }

    public void setNameAndArtist(Long musicId, String name, String artist, Integer level,
                                 Integer matchRate, Boolean isFavorite) {
        this.musicId = musicId;
        tvName.setText(name);
        tvArtist.setText(artist);
        if (level == 0) {
            ivLevel.setImageResource(R.drawable.circle_green);
        } else if (level == 1) {
            ivLevel.setImageResource(R.drawable.circle_yellow);
        }
        tvMatchRate.setText(matchRate.toString() + "%");
        if (isFavorite) {
            this.isFavorite = true;
            ibBookmark.setImageResource(R.drawable.ic_favorite_24);
        }

        ibBookmark.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(!isFavorite);
            }
        });
    }

    public void sendRequest(Boolean setFavorite)
    {
        String url = Domain.url((setFavorite) ? "/api/bookmark/set" : "/api/bookmark/delete");

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (setFavorite) {
                            isFavorite = true;
                            ibBookmark.setImageResource(R.drawable.ic_favorite_24);
                        }
                        else {
                            isFavorite = false;
                            ibBookmark.setImageResource(R.drawable.ic_favorite_border_24);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "❤️에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("mid", String.valueOf(musicId));
                params.put("uid", String.valueOf(TokenService.uid_load()));
                return params;
            }
        };

        queue.add(request);
    }
}
