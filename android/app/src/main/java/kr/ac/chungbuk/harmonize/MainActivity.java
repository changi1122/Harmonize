package kr.ac.chungbuk.harmonize;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.databinding.ActivityMainBinding;
import kr.ac.chungbuk.harmonize.item.MusicDetailItemView;
import kr.ac.chungbuk.harmonize.item.MusicPlayingItemView;
import kr.ac.chungbuk.harmonize.model.Music;
import kr.ac.chungbuk.harmonize.model.MusicDetail;
import kr.ac.chungbuk.harmonize.model.Token;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    SlidingUpPanelLayout slidingLayout;
    MusicPlayingItemView playingView;
    MusicDetailItemView detailItemView;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Make light theme only */
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        /* Hide title bar */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_pitch,
                R.id.navigation_wishlist, R.id.navigation_more)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        queue = Volley.newRequestQueue(this);
        playingView = findViewById(R.id.playingView);
        detailItemView = findViewById(R.id.detailItemView);
        slidingLayout = findViewById(R.id.slidingLayout);

        playingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                    slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.DRAGGING) {
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
                else {
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            }
        });

        /* Sliding Layout */
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        slidingLayout.setTouchEnabled(false);

        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED ||
                    newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
                    playingView.setExpandedMode(true);
                }
                else {
                    playingView.setExpandedMode(false);
                }
            }
        });
    }

    public void hideMusicPlayingView()
    {
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    /* MusicDetailItem Event Handler */
    public void loadMusicDetail(Long id)
    {
        StringRequest request = new StringRequest(Request.Method.GET, Domain.url("/api/musics/" + String.valueOf(id)),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                            MusicDetail music = gson.fromJson(response, MusicDetail.class);

                            playingView.setMusic(music);
                            detailItemView.loadMusicDetail(music);
                            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Error 발생");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    // log error
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(request);
    }
}