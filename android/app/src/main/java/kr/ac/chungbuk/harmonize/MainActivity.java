package kr.ac.chungbuk.harmonize;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import kr.ac.chungbuk.harmonize.databinding.ActivityMainBinding;
import kr.ac.chungbuk.harmonize.item.MusicPlayingItemView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    SlidingUpPanelLayout slidingLayout;
    MusicPlayingItemView playingView;

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

        playingView = findViewById(R.id.playingView);
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
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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

}