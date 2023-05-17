package kr.ac.chungbuk.harmonize.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.ui.activity.LoginActivity;

public class HomeFragment extends Fragment {
    ImageButton ibtnSearch;
    ViewPager pagerMusicList;
    TabLayout tabTitle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ibtnSearch = view.findViewById(R.id.ibtnSearch);
        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        pagerMusicList = view.findViewById(R.id.pagerMusicList);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        MusicListFragment all = new MusicListFragment();
        adapter.addItem("전체", all);
        MusicListFragment genre1 = new MusicListFragment();
        adapter.addItem("가요", genre1);
        MusicListFragment genre2 = new MusicListFragment();
        adapter.addItem("팝송", genre2);
        MusicListFragment genre3 = new MusicListFragment();
        adapter.addItem("일본곡", genre3);
        MusicListFragment genre4 = new MusicListFragment();
        adapter.addItem("랩/힙합", genre4);
        MusicListFragment genre5 = new MusicListFragment();
        adapter.addItem("R&B", genre5);
        MusicListFragment genre6 = new MusicListFragment();
        adapter.addItem("발라드", genre6);
        MusicListFragment genre7 = new MusicListFragment();
        adapter.addItem("댄스", genre7);
        MusicListFragment genre8 = new MusicListFragment();
        adapter.addItem("OST", genre8);
        MusicListFragment genre9 = new MusicListFragment();
        adapter.addItem("인디뮤직", genre9);
        MusicListFragment genre10 = new MusicListFragment();
        adapter.addItem("트로트", genre10);
        MusicListFragment genre11 = new MusicListFragment();
        adapter.addItem("어린이곡", genre11);

        pagerMusicList.setAdapter(adapter);

        tabTitle = view.findViewById(R.id.tabTitle);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();
        ArrayList<String> titles = new ArrayList<String>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(String title, Fragment item) {
            titles.add(title);
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}