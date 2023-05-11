package kr.ac.chungbuk.harmonize.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;

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
                        // TO-DO : 토큰과 user 정보를 저장하기
                        btnSignup.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btnSignup.setText(error.toString());
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