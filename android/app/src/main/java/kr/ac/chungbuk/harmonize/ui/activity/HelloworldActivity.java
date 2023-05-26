package kr.ac.chungbuk.harmonize.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import kr.ac.chungbuk.harmonize.R;

public class HelloworldActivity extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloworld);

        text = findViewById(R.id.text);

        // 2초 후 "목소리를 들려주세요"로 텍스트 변경
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                text.setText("목소리를\n들려주세요");

                // 추가: 2초 후 LoginActivity로 이동
                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(HelloworldActivity.this, TuneCheckPageActivity.class));
                        finish(); // 현재 액티비티 종료
                    }
                }, 2000); // 2초 후에 실행
            }
        }, 2000); // 2초 후에 실행
    }
}