package kr.ac.chungbuk.harmonize.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import org.json.JSONException;
import org.json.JSONObject;

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.item.MusicListItemView;
import kr.ac.chungbuk.harmonize.item.SearchHistoryItemView;
import kr.ac.chungbuk.harmonize.model.Music;
import kr.ac.chungbuk.harmonize.model.MusicSearchResult;
import kr.ac.chungbuk.harmonize.model.Token;
import kr.ac.chungbuk.harmonize.service.TokenService;
import kr.ac.chungbuk.harmonize.ui.activity.LoginActivity;

import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {

    RequestQueue queue;
    LinearLayout emptyView;
    ListView historyListView, musicListView;
    MusicListAdapter adapter;
    TextInputLayout tilSearch;
    TextInputEditText etSearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(getContext());

        SearchHistoryAdapter historyAdapter = new SearchHistoryAdapter();

        historyListView = (ListView) view.findViewById(R.id.historyListView);
        historyListView.setAdapter(historyAdapter);
        emptyView = view.findViewById(R.id.emptyView);
        tilSearch = view.findViewById(R.id.tilSearch);
        etSearch = view.findViewById(R.id.etSearch);
        musicListView = view.findViewById(R.id.musicListView);


        tilSearch.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(etSearch.getText().toString());
            }
        });

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    search(etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });

        adapter = new MusicListAdapter();
        musicListView.setAdapter(adapter);
    }


    private void search(String query) {
        if (query.equals("")) {
            hideResultListView();
            adapter.clear();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/music/search"),
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
                            Toast.makeText(getContext(), "검색 요청 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "검색 요청 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
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
                params.put("search", query);
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

    class SearchHistoryAdapter extends BaseAdapter {
        ArrayList<String> history = new ArrayList<>();

        @Override
        public int getCount() {
            return history.size();
        }

        public void addItem(String text) {
            history.add(text);
        }

        @Override
        public Object getItem(int position) {
            return history.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SearchHistoryItemView view = new SearchHistoryItemView(getActivity().getApplicationContext());
            view.setHistoryTextView(history.get(position));
            return view;
        }
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
                        .load(Domain.url("/api/music/img/" + searchResult.thumbnail))
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