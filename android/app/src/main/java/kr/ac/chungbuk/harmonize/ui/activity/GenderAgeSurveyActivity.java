package kr.ac.chungbuk.harmonize.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.enums.Gender;
import kr.ac.chungbuk.harmonize.model.Token;
import kr.ac.chungbuk.harmonize.service.TokenService;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;

public class GenderAgeSurveyActivity extends AppCompatActivity {

    RequestQueue queue;

    ThemedButton genderFemale, genderMale, age10, age20, age30, age40, age50, age60;
    Button btnNext, btnCancel;

    private String selectedGenderBtn;
    private String selectedAgeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_gender_age_survey);

        queue = Volley.newRequestQueue(this);

        genderFemale = findViewById(R.id.genderFemale);
        genderMale = findViewById(R.id.genderMale);

        age10 = findViewById(R.id.age10);
        age20 = findViewById(R.id.age20);
        age30 = findViewById(R.id.age30);
        age40 = findViewById(R.id.age40);
        age50 = findViewById(R.id.age50);
        age60 = findViewById(R.id.age60);

        //gender 버튼 클릭 리스너
        View.OnClickListener genderListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectedGenderBtn = ((ThemedButton) v).getText().toString(); //버튼을 클릭 했을때 내부에서 view 객체로 해당 text를 가져오는 방법
            }
        };
        //리스너 변수를 버튼에 등록
        genderFemale.setOnClickListener(genderListener);
        genderMale.setOnClickListener(genderListener);

        //age 버튼 클릭 리스너
        View.OnClickListener ageListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectedAgeBtn = ((ThemedButton) v).getText().toString(); //버튼을 클릭 했을때 내부에서 view 객체로 해당 text를 가져오는 방법
            }
        };
        //리스너 변수를 버튼에 등록 - 하나의 리스너를 여러 버튼에 등록 가능
        age10.setOnClickListener(ageListener);
        age20.setOnClickListener(ageListener);
        age30.setOnClickListener(ageListener);
        age40.setOnClickListener(ageListener);
        age50.setOnClickListener(ageListener);
        age60.setOnClickListener(ageListener);

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenderAgeSurvey(selectedGenderBtn, selectedAgeBtn);
                //startActivity(new Intent(GenderAgeSurveyActivity.this, {옮겨갈액티비티}.class));
            }
        });

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(GenderAgeSurveyActivity.this, {옮겨갈액티비티}.class));
            }
        });
    }

    private void GenderAgeSurvey(String selectedGenderBtn, String selectedAgeBtn)
    {
        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/GenderAgeSurvey"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(GenderAgeSurveyActivity.this, "성별/연령대 저장에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(GenderAgeSurveyActivity.this, {옮겨갈액티비티}.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);

                        Toast.makeText(GenderAgeSurveyActivity.this, "성별/연령대 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("id", String.valueOf(TokenService.uid_load()));
                if(selectedGenderBtn.equals("남성"))
                    params.put("gender", String.valueOf(1));
                if(selectedGenderBtn.equals("여성"))
                    params.put("gender", String.valueOf(2));

                if(selectedAgeBtn.equals("10대"))
                    params.put("age", String.valueOf(10));
                else if(selectedAgeBtn.equals("20대"))
                    params.put("age", String.valueOf(20));
                else if(selectedAgeBtn.equals("30대"))
                    params.put("age", String.valueOf(30));
                else if(selectedAgeBtn.equals("40대"))
                    params.put("age", String.valueOf(40));
                else if(selectedAgeBtn.equals("50대"))
                    params.put("age", String.valueOf(50));
                else if(selectedAgeBtn.equals("60대 이상"))
                    params.put("age", String.valueOf(60));

                return params;
            }
        };

        queue.add(request);
    }
}