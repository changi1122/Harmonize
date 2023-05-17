package kr.ac.chungbuk.harmonize.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
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

        //2초 후 text 반가워요 -> 목소리를 들려주세요
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text.setText("목소리를\n들려주세요");
            }
        }, 2000); // 2초 후에 실행
    }
}