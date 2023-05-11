package kr.ac.chungbuk.harmonize.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.item.MoreItemView;
import kr.ac.chungbuk.harmonize.ui.activity.CategorySurveyActivity;
import kr.ac.chungbuk.harmonize.ui.activity.GenderAgeSurveyActivity;

public class MoreFragment extends Fragment {

    ListView menuListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MoreMenuAdapter adapter = new MoreMenuAdapter();
        adapter.addItem("관심 장르 수정");
        adapter.addItem("성별/연령대 수정");
        adapter.addItem("도움말");
        adapter.addItem("로그아웃");

        menuListView = (ListView) view.findViewById(R.id.menuListView);
        menuListView.setAdapter(adapter);

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // 관심 장르 수정 클릭시
                        startActivity(new Intent(getActivity(), CategorySurveyActivity.class));
                        break;
                    case 1:
                        // 성별/연령대 수정 클릭시
                        startActivity(new Intent(getActivity(), GenderAgeSurveyActivity.class));
                        break;
                    case 2:
                        // 도움말 클릭시
                        break;
                    case 3:
                        // 로그아웃 클릭시
                        break;
                    default:
                        break;
                }
            }
        });
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