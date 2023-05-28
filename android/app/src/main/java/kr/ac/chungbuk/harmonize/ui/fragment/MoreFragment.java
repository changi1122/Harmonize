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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.item.MoreItemView;
import kr.ac.chungbuk.harmonize.model.Token;
import kr.ac.chungbuk.harmonize.model.User;
import kr.ac.chungbuk.harmonize.service.TokenService;
import kr.ac.chungbuk.harmonize.ui.activity.CategorySurveyActivity;
import kr.ac.chungbuk.harmonize.ui.activity.GenderAgeSurveyActivity;

public class MoreFragment extends Fragment {
    RequestQueue queue;
    List<String> userPrefer;
    ListView menuListView;
    TextView tvUsername, tvCategory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(getContext());

        tvUsername = view.findViewById(R.id.tvUsername);
        tvCategory = view.findViewById(R.id.tvCategory);

        loadUser();

        MoreMenuAdapter adapter = new MoreMenuAdapter();
        adapter.initItems(new ArrayList<>(Arrays.asList(
                "관심 장르 수정", "성별/연령대 수정", "로그아웃"
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
                    case 2: // 로그아웃 클릭시
                        logout(); break;
                    default: break;
                }
            }
        });
    }

    private void loadUser()
    {
        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/get/user"),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        User user = gson.fromJson(response, TypeToken.getParameterized(User.class, String.class).getType());

                        String categoryString = "";
                        if (user.categories != null && user.categories.size() > 0)
                            categoryString += user.categories.get(0);
                        if (user.categories != null && user.categories.size() > 1)
                            categoryString += ", " + user.categories.get(1);
                        if (user.categories != null && user.categories.size() > 2)
                            categoryString += ", " + user.categories.get(2);
                        tvCategory.setText(categoryString);
                        tvUsername.setText(user.username);

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error 발생");
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) { }
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", String.valueOf(TokenService.uid_load()));
                return params;
            }
        };
        queue.add(request);
    }


    /**
     * 로그아웃 : 저장된 토큰과 user id를 지우고, 앱을 종료합니다.
     */
    private void logout() {
        Toast.makeText(getContext(), "로그아웃하기 위해 앱을 종료합니다.", Toast.LENGTH_LONG).show();
        TokenService.clear();
        getActivity().finish();
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