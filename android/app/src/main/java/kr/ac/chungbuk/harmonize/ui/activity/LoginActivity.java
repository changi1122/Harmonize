package kr.ac.chungbuk.harmonize.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import kr.ac.chungbuk.harmonize.MainActivity;
import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.model.Token;
import kr.ac.chungbuk.harmonize.service.TokenService;

public class LoginActivity extends AppCompatActivity {

    RequestQueue queue;

    Button btnSignup, btnLogin;
    TextInputEditText editTextId, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        queue = Volley.newRequestQueue(this);

        editTextId = findViewById(R.id.editTextId);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });


        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                login(editTextId.getText().toString(), editTextPassword.getText().toString());
            }
        });

    }

    private void login(String username, String password)
    {
        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/login"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //btnSignup.setText(response);
                        System.out.println(response);

                        int uid = 0;
                        //response에서 uid 값 가져오기
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            uid = jsonObject.getInt("uid");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("uid를 가져오지 못했습니다.");
                        }

                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        Token token = gson.fromJson(response, Token.class);
                        token.setCreatedAt(OffsetDateTime.now());

                        TokenService.save(token, uid);

                        if ((token = TokenService.load()) != null) {
                            System.out.println(token.getToken());
                        }

                        int gender = 0;
                        //response에서 gender 값 가져오기
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            gender = jsonObject.getInt("gender");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("gender를 가져오지 못했습니다.");
                        }

                        /* 첫 로그인 시 flow 설정
                        gender == 0 : 첫 로그인 -> HelloworldActivity
                        gender != 0 : 초기 조사 완료 user -> MainActivity
                         */
                        if(gender==0){
                            startActivity(new Intent(LoginActivity.this, HelloworldActivity.class));
                        } else{
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/json; charset=UTF-8");
                //params.put("token", "welkfjlwejflwe");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        queue.add(request);
    }
}