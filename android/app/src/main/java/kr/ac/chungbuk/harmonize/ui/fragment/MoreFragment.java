package kr.ac.chungbuk.harmonize.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.item.MoreItemView;
import kr.ac.chungbuk.harmonize.model.Token;
import kr.ac.chungbuk.harmonize.service.TokenService;
import kr.ac.chungbuk.harmonize.ui.activity.CategorySurveyActivity;
import kr.ac.chungbuk.harmonize.ui.activity.GenderAgeSurveyActivity;

public class MoreFragment extends Fragment {

    ListView menuListView;
    TextView tvUsername, tvCategory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvUsername = view.findViewById(R.id.tvUsername);
        tvCategory = view.findViewById(R.id.tvCategory);

        // TO-DO : 삭제할 임시 코드
        // 토큰 로드 확인용 임시 코드
        Token token;
        if ((token = TokenService.load()) != null) {
            tvUsername.setText(token.getToken());
            tvCategory.setText(token.getCreatedAt().toString());
        }



        MoreMenuAdapter adapter = new MoreMenuAdapter();
        adapter.initItems(new ArrayList<>(Arrays.asList(
                "관심 장르 수정", "성별/연령대 수정", "도움말", "로그아웃"
        )));

        menuListView = (ListView) view.findViewById(R.id.menuListView);
        menuListView.setAdapter(adapter);

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0: // 관심 장르 수정 클릭시
                        startActivity(new Intent(getActivity(), CategorySurveyActivity.class)); break;
                    case 1: // 성별/연령대 수정 클릭시
                        startActivity(new Intent(getActivity(), GenderAgeSurveyActivity.class)); break;
                    case 2: break; // 도움말 클릭시
                    case 3: break; // 로그아웃 클릭시
                    default: break;
                }
            }
        });
    }

    class MoreMenuAdapter extends BaseAdapter {
        ArrayList<String> menus = new ArrayList<>();

        public void initItems(ArrayList<String> menus) { this.menus = menus; }

        @Override
        public int getCount() {
            return menus.size();
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