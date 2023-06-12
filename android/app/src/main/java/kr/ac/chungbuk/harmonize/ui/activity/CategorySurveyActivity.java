package kr.ac.chungbuk.harmonize.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.service.TokenService;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

public class CategorySurveyActivity extends AppCompatActivity {

    RequestQueue queue;

    ThemedToggleButtonGroup genres;

    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_category_survey);

        queue = Volley.newRequestQueue(this);

        genres = findViewById(R.id.genres);

        // 선택한 장르 리스트를 저장하기 위한 변수
        List<String> selectedGenres = new ArrayList<>();

        // 장르 선택 리스너
        View.OnClickListener genreClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemedButton button = (ThemedButton) view;
                String genre = button.getText().toString();

                if (selectedGenres.contains(genre)) {
                    selectedGenres.remove(genre);
                } else {
                    selectedGenres.add(genre);
                }
            }
        };

        // 장르 버튼에 클릭 리스너 등록
        for (int i = 0; i < genres.getChildCount(); i++) {
            View child = genres.getChildAt(i);
            if (child instanceof ThemedButton) {
                child.setOnClickListener(genreClickListener);
            }
        }

        btnSubmit = findViewById(R.id.btnNext);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 선택한 장르 배열을 서버로 보내는 함수 호출
                try {
                    sendSelectedGenres(selectedGenres);
                }
                catch (Exception e) { }
            }
        });
    }

    private void sendSelectedGenres(List<String> selectedGenres) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("uid", TokenService.uid_load());
        jsonBody.put("category", selectedGenres);
        final String mResquestBody = jsonBody.toString();


        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/prefer/save"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CategorySurveyActivity.this, "선호 장르 저장에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CategorySurveyActivity.this, "선호 장르 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try{
                    return mResquestBody == null ? null : mResquestBody.getBytes("utf-8");
                }
                catch(UnsupportedEncodingException uee){
                    return null;
                }
            }
        };

        queue.add(request);
    }
}
