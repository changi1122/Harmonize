package kr.ac.chungbuk.harmonize.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.databinding.FragmentSearchBinding;
import kr.ac.chungbuk.harmonize.item.SearchHistoryItemView;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    ListView historyListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SearchHistoryAdapter adapter = new SearchHistoryAdapter();
        adapter.addItem("사건의지평선");
        adapter.addItem("응급실(쾌걸춘향OST)");
        adapter.addItem("Monologue");
        adapter.addItem("그대라는사치");
        adapter.addItem("어디에도");
        adapter.addItem("내가아니라도");
        adapter.addItem("Marry Me");
        adapter.addItem("Monologue");
        adapter.addItem("그대라는사치");
        adapter.addItem("어디에도");
        adapter.addItem("내가아니라도");
        adapter.addItem("Marry Me");
        adapter.addItem("어디에도");
        adapter.addItem("내가아니라도");
        adapter.addItem("Marry Me");
        adapter.addItem("Monologue");
        adapter.addItem("그대라는사치");
        adapter.addItem("어디에도");
        adapter.addItem("내가아니라도");
        adapter.addItem("Marry Me");

        historyListView = (ListView) root.findViewById(R.id.historyListView);
        historyListView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
}