package kr.ac.chungbuk.harmonize.ui.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.databinding.FragmentMoreBinding;
import kr.ac.chungbuk.harmonize.item.MoreItemView;
import kr.ac.chungbuk.harmonize.item.SearchHistoryItemView;

public class MoreFragment extends Fragment {

    private FragmentMoreBinding binding;

    ListView menuListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MoreViewModel moreViewModel =
                new ViewModelProvider(this).get(MoreViewModel.class);

        binding = FragmentMoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MoreMenuAdapter adapter = new MoreMenuAdapter();
        adapter.addItem("관심 장르 수정");
        adapter.addItem("성별/연령대 수정");
        adapter.addItem("도움말");
        adapter.addItem("로그아웃");

        menuListView = (ListView) root.findViewById(R.id.menuListView);
        menuListView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class MoreMenuAdapter extends BaseAdapter {
        ArrayList<String> menus = new ArrayList<>();

        @Override
        public int getCount() {
            return menus.size();
        }

        public void addItem(String text) {
            menus.add(text);
        }

        @Override
        public Object getItem(int position) {
            return menus.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MoreItemView view = new MoreItemView(getActivity().getApplicationContext());
            view.setMenuName(menus.get(position));
            return view;
        }
    }
}