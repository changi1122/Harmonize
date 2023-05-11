package kr.ac.chungbuk.harmonize.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.item.MusicListItemView;
import kr.ac.chungbuk.harmonize.item.SearchHistoryItemView;
import kr.ac.chungbuk.harmonize.model.Music;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    ListView historyListView, musicListView;
    TextInputLayout tilSearch;
    TextInputEditText etSearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SearchHistoryAdapter historyAdapter = new SearchHistoryAdapter();
        historyAdapter.addItem("사건의지평선");
        historyAdapter.addItem("응급실(쾌걸춘향OST)");
        historyAdapter.addItem("Monologue");
        historyAdapter.addItem("그대라는사치");
        historyAdapter.addItem("어디에도");
        historyAdapter.addItem("내가아니라도");
        historyAdapter.addItem("Marry Me");
        historyAdapter.addItem("Monologue");
        historyAdapter.addItem("그대라는사치");

        historyListView = (ListView) view.findViewById(R.id.historyListView);
        historyListView.setAdapter(historyAdapter);


        tilSearch = view.findViewById(R.id.tilSearch);
        tilSearch.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        etSearch = view.findViewById(R.id.etSearch);
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    search();
                    return true;
                }
                return false;
            }
        });


        musicListView = view.findViewById(R.id.musicListView);

        MusicListAdapter adapter = new MusicListAdapter();
        adapter.addItem(new Music(
                "사건의 지평선", "윤하(YOUNHA)", 1, 45, false,
                "https://search.pstatic.net/common?type=n&size=174x174&quality=95&direct=true&src=https%3A%2F%2Fmusicmeta-phinf.pstatic.net%2Falbum%2F007%2F434%2F7434553.jpg%3Ftype%3Dr204Fll%26v%3D20230109102326"));
        adapter.addItem(new Music(
                "좋니", "윤종신", 2, 88, false,
                "https://search.pstatic.net/common?type=n&size=174x174&quality=95&direct=true&src=https%3A%2F%2Fmusicmeta-phinf.pstatic.net%2Falbum%2F002%2F067%2F2067347.jpg%3Ftype%3Dr204Fll%26v%3D20220513012522"));
        adapter.addItem(new Music(
                "Tears", "소찬휘", 2, 45, true,
                "https://search.pstatic.net/common?type=n&size=174x174&quality=95&direct=true&src=https%3A%2F%2Fmusicmeta-phinf.pstatic.net%2Falbum%2F000%2F001%2F1408.jpg%3Ftype%3Dr204Fll%26v%3D20230103162533"));
        adapter.addItem(new Music(
                "어디에도", "엠씨더맥스(M.C the MAX)", 3, 66, false,
                "https://search.pstatic.net/common?type=n&size=174x174&quality=95&direct=true&src=https%3A%2F%2Fmusicmeta-phinf.pstatic.net%2Falbum%2F000%2F614%2F614753.jpg%3Ftype%3Dr204Fll%26v%3D20230106154012"));
        adapter.addItem(new Music(
                "사건의 지평선", "윤하(YOUNHA)", 1, 34, false));
        adapter.addItem(new Music(
                "좋니", "윤종신", 3, 86, true));
        adapter.addItem(new Music(
                "Tears", "소찬휘", 1, 76, false));
        adapter.addItem(new Music(
                "어디에도", "엠씨더맥스(M.C the MAX)", 2, 32, false));
        adapter.addItem(new Music(
                "사건의 지평선", "윤하(YOUNHA)", 3, 56, false));
        adapter.addItem(new Music(
                "좋니", "윤종신", 2, 23, true));
        adapter.addItem(new Music(
                "Tears", "소찬휘", 1, 99, false));
        adapter.addItem(new Music(
                "어디에도", "엠씨더맥스(M.C the MAX)", 3, 45, false));

        musicListView.setAdapter(adapter);
    }


    private void search() {
        Toast.makeText(getActivity(), "검색 버튼 눌림", Toast.LENGTH_LONG).show();
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
        ArrayList<Music> musics = new ArrayList<>();

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
            view.setNameAndArtist(music.name, music.artist, music.level, music.matchRate);

            if (music.thumbnail != null) {
                ImageView thumbnailView = view.findViewById(R.id.thumbnailView);
                Glide
                        .with(getActivity())
                        .load(music.thumbnail)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(new ColorDrawable(Color.parseColor("#eeeeee")))
                        .into(thumbnailView);
            }

            return view;
        }
    }
}