package kr.ac.chungbuk.harmonize.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.item.MusicListItemView;
import kr.ac.chungbuk.harmonize.model.MusicSearchResult;
import kr.ac.chungbuk.harmonize.service.TokenService;

public class BookmarkFragment extends Fragment {

    RequestQueue queue;
    LinearLayout emptyView;
    ListView musicListView;
    MusicListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(getContext());

        emptyView = view.findViewById(R.id.emptyView);
        musicListView = view.findViewById(R.id.musicListView);

        adapter = new MusicListAdapter();
        musicListView.setAdapter(adapter);

        loadBookmarkedMusics();
    }

    private void loadBookmarkedMusics() {

        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/bookmark/list"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                            List<MusicSearchResult> searchResults = gson.fromJson(response, new TypeToken<List<MusicSearchResult>>(){}.getType());

                            adapter.clear();
                            adapter.setItems(searchResults);

                            if (searchResults.size() < 1) hideResultListView();
                            else showResultListView();;
                        }
                        catch (Exception e) {
                            Toast.makeText(getContext(), "찜 목록 요청 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "찜 목록 요청 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("uid", String.valueOf(TokenService.uid_load()));
                return params;
            }
        };

        queue.add(request);
    }


    private void showResultListView() {
        musicListView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    private void hideResultListView() {
        emptyView.setVisibility(View.VISIBLE);
        musicListView.setVisibility(View.GONE);
    }

    class MusicListAdapter extends BaseAdapter {
        List<MusicSearchResult> musics = new ArrayList<>();

        @Override
        public int getCount() {
            return musics.size();
        }

        public void addItem(MusicSearchResult music) {
            musics.add(music);
        }

        public void setItems(List<MusicSearchResult> musics) {
            this.musics = musics;
        }

        public void clear() {
            musics.clear();
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
            MusicSearchResult searchResult = musics.get(position);

            MusicListItemView view = new MusicListItemView(getActivity().getApplicationContext());
            view.setNameAndArtist(searchResult.id, searchResult.name, searchResult.artist, searchResult.level,
                    searchResult.matchRate, searchResult.isFavorite);

            if (searchResult.thumbnail != null) {
                ImageView thumbnailView = view.findViewById(R.id.thumbnailView);
                Glide
                        .with(getActivity())
                        .load(searchResult.thumbnail)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(new ColorDrawable(Color.parseColor("#eeeeee")))
                        .into(thumbnailView);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity main = (MainActivity)getActivity();
                    main.loadMusicDetail(searchResult.id);
                }
            });

            return view;
        }
    }
}