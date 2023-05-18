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
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.model.Music;
import kr.ac.chungbuk.harmonize.service.TokenService;
import kr.ac.chungbuk.harmonize.ui.activity.LoginActivity;

public class HomeFragment extends Fragment {
    ImageButton ibtnSearch;
    ViewPager pagerMusicList;
    TabLayout tabTitle;

    RequestQueue queue;

    ArrayList<Music> musics;

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

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        Integer uid = TokenService.uid_load();

        System.out.println("hello");
        GetMusicList(String.valueOf(1));

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

    private void GetMusicList(String uid){
        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/music/get/list"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("response" + response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Error 발생");
                        }
                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                        musics = gson.fromJson(response, TypeToken.getParameterized(ArrayList.class, Music.class).getType());

                        if (musics.isEmpty()) {
                            System.out.println(" ");
                        } else {
                            System.out.println(musics);
                            for (int i = 0; i < musics.size(); i++) {
                                System.out.println(musics.get(i).getMusic_id());
                            }
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/json; charset=UTF-8");
                //params.put("token", "welkfjlwejflwe");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                System.out.println(uid);
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                return params;
            }
        };

        queue.add(request);

    }
}