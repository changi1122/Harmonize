package kr.ac.chungbuk.harmonize.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.item.MusicListItemView;
import kr.ac.chungbuk.harmonize.model.Music;
import kr.ac.chungbuk.harmonize.model.MusicSearchResult;
import kr.ac.chungbuk.harmonize.service.TokenService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView musicListView;

    RequestQueue queue;

    MusicListAdapter adapter;

    List<Music> musics;

    private String cid;

    public MusicListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicListFragment newInstance(String param1, String param2) {
        MusicListFragment fragment = new MusicListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void SetCategory(String category_id){
        cid = category_id;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        musicListView = view.findViewById(R.id.musicListView);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        get(String.valueOf(TokenService.uid_load()));
        adapter = new MusicListAdapter();
    }

    private void Set(){
        adapter.clear();
        adapter.setItems(musics);
        musicListView.setAdapter(adapter);
    }


    private void get(String uid){

        if(uid.equals("")){
            System.out.println("clear");
            adapter.clear();
            return;
        }
        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/music/get/list"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("response" + response);

                            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                            musics = gson.fromJson(response, TypeToken.getParameterized(ArrayList.class, Music.class).getType());

                            Set();

                            if (musics.isEmpty()) {
                                hideResultListView();
                            } else {
                                showResultListView();
                                System.out.println(musics);
                            }

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
                System.out.println(uid);
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                params.put("cid", cid);
                return params;
            }
        };

        queue.add(request);

    }

     private void showResultListView(){
        musicListView.setVisibility(View.VISIBLE);
     }

     private void hideResultListView(){
        musicListView.setVisibility(View.GONE);
     }


    class MusicListAdapter extends BaseAdapter {
        List<Music> musics = new ArrayList<>();

        @Override
        public int getCount() {
            return musics.size();
        }

        public void addItem(Music music) {
            musics.add(music);
        }

        @Override
        public Object getItem(int position) {
            return musics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Music music = musics.get(position);

            MusicListItemView view = new MusicListItemView(getActivity().getApplicationContext());
            view.setNameAndArtist(music.music_name, music.artist, music.level, music.range_avg, music.is_prefer);

            if (music.img_link != null) {
                ImageView thumbnailView = view.findViewById(R.id.thumbnailView);
                Glide
                        .with(getActivity())
                        .load(Domain.url("/api/music/img/" + music.img_link))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(new ColorDrawable(Color.parseColor("#eeeeee")))
                        .into(thumbnailView);
            }

            return view;
        }

        public void clear() {
            musics.clear();
        }

        public void setItems( List<Music> musics ){
            this.musics = musics;
        }

    }
}