package kr.ac.chungbuk.harmonize.ui.fragment;

import android.content.Intent;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.LateinitKt;
import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.model.Music;
import kr.ac.chungbuk.harmonize.model.User;
import kr.ac.chungbuk.harmonize.service.TokenService;
import kr.ac.chungbuk.harmonize.ui.activity.LoginActivity;

public class HomeFragment extends Fragment{
    ImageButton ibtnSearch;
    ViewPager pagerMusicList;
    TabLayout tabTitle;

    List<String> UserPrefer;

    RequestQueue queue;

    List<String> Last;

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


        queue = Volley.newRequestQueue(getContext());

        getC(String.valueOf(TokenService.uid_load()), view);
        System.out.println("2323"+Last);


        System.out.println("hello");

        tabTitle = view.findViewById(R.id.tabTitle);
    }

    private void Deduplication(View view){
        Last = new ArrayList<>();
        List<String> category = new ArrayList<String>(Arrays.asList("전체" ,"가요", "팝송", "일본곡", "랩/힙합", "R&B", "발라드", "댄스", "OST", "인디뮤직", "트로트", "어린이곡"));
        Last.add("전체");
        for(int i=0; i<UserPrefer.size(); i++){
            Last.add(UserPrefer.get(i));
        }
        System.out.println(Last);

        for(int i=0; i<category.size(); i++){
            if(!Last.contains(category.get(i))){
                Last.add(category.get(i));
            }
        }

        pagerMusicList = view.findViewById(R.id.pagerMusicList);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        List<MusicListFragment> genre = new ArrayList<>();

        for(int i=0; i<Last.size(); i++){
            genre.add(new MusicListFragment());
            genre.get(i).SetCategory(String.valueOf(category.indexOf(Last.get(i))+1));
            adapter.addItem(Last.get(i), genre.get(i));
        }

       /* MusicListFragment all = new MusicListFragment();
        all.SetCategory("1");
        adapter.addItem(Last.get(0), all);

        MusicListFragment genre1 = new MusicListFragment();
        genre1.SetCategory("2");
        adapter.addItem("가요", genre1);

        MusicListFragment genre2 = new MusicListFragment();
        genre2.SetCategory("3");
        adapter.addItem("팝송", genre2);

        MusicListFragment genre3 = new MusicListFragment();
        genre3.SetCategory("4");
        adapter.addItem("일본곡", genre3);

        MusicListFragment genre4 = new MusicListFragment();
        genre4.SetCategory("5");
        adapter.addItem("랩/힙합", genre4);

        MusicListFragment genre5 = new MusicListFragment();
        genre5.SetCategory("6");
        adapter.addItem("R&B", genre5);

        MusicListFragment genre6 = new MusicListFragment();
        genre6.SetCategory("7");
        adapter.addItem("발라드", genre6);

        MusicListFragment genre7 = new MusicListFragment();
        genre7.SetCategory("8");
        adapter.addItem("댄스", genre7);

        MusicListFragment genre8 = new MusicListFragment();
        genre8.SetCategory("9");
        adapter.addItem("OST", genre8);

        MusicListFragment genre9 = new MusicListFragment();
        genre9.SetCategory("10");
        adapter.addItem("인디뮤직", genre9);

        MusicListFragment genre10 = new MusicListFragment();
        genre10.SetCategory("11");
        adapter.addItem("트로트", genre10);

        MusicListFragment genre11 = new MusicListFragment();
        genre11.SetCategory("12");
        adapter.addItem("어린이곡", genre11);
*/
        pagerMusicList.setAdapter(adapter);

        System.out.println(Last);
    }

    private void getC(String uid, View view){

        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/prefer/get/categories"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("response" + response);

                            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                            UserPrefer = gson.fromJson(response, TypeToken.getParameterized(ArrayList.class, String.class).getType());

                            System.out.println(UserPrefer);
                            System.out.println(UserPrefer.get(0));

                            Deduplication(view);

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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/json; charset=UTF-8");
                //params.put("token", "welkfjlwejflwe");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                return params;
            }
        };
        queue.add(request);
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