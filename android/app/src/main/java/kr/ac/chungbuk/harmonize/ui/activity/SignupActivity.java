package kr.ac.chungbuk.harmonize.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.model.Token;
import kr.ac.chungbuk.harmonize.service.TokenService;

public class SignupActivity extends AppCompatActivity {

    RequestQueue queue;

    Button btnSignup;

    TextInputEditText editTextId, editTextPassword, editTextRetypePassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);

        queue = Volley.newRequestQueue(this);

        editTextId = findViewById(R.id.editTextId);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRetypePassword = findViewById(R.id.editTextRetypePassword);

        btnSignup = findViewById(R.id.btnSignup); //회원가입 id 없길래 xml에 내가 추가함
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(editTextId.getText().toString(), editTextPassword.getText().toString(), editTextRetypePassword.getText().toString());

            }
        });

    }

    private void signup(String username, String password, String retypepassword)
    {
        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/register"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SignupActivity.this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class)); //회원가입 -> 로그인 activity
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        Toast.makeText(SignupActivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            //Post 파라미터 보내기
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                if(password.equals(retypepassword)){
                    params.put("username", username);
                    params.put("password", password);
                }
                return params;
            }

        };



        queue.add(request);
    }
}