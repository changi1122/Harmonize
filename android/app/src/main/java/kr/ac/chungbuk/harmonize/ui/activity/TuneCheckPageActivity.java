package kr.ac.chungbuk.harmonize.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import kr.ac.chungbuk.harmonize.R;

public class TuneCheckPageActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 101;
    private ImageButton btnRecord;

    //이미지 버튼 초기 상태
    boolean isImage1 = true;

    private MediaRecorder recorder;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tune_check_page);

        btnRecord = findViewById(R.id.btnRecord);
        // 이미지 버튼 클릭 이벤트 처리
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImage1) {
                    btnRecord.setImageResource(R.drawable.baseline_mic_24);
                    btnRecord.setBackgroundResource(R.drawable.btnrecord_click_effect);
                    recordAudio();
                } else {
                    btnRecord.setImageResource(R.drawable.baseline_mic_none_24);
                    btnRecord.setBackgroundResource(R.drawable.btnrecord_click_effect);
                    stopRecording();
                    try {
                        uploadFileToServer(filename);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

                isImage1 = !isImage1;
            }
        });

        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        checkPermission(permissions);

        File sdcard = getExternalFilesDir(null);
        File file = new File(sdcard, "recorded.mp4");
        filename = file.getAbsolutePath();
        Log.d("TuneCheckPageActivity", "저장할 파일명: " + filename);
    }

    public void checkPermission(String[] permissions) {
        ArrayList<String> targetList = new ArrayList<>();

        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, permission + " 권한 있음", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, permission + " 권한 없음", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    Toast.makeText(this, permission + " 권한 설명 필요함.", Toast.LENGTH_SHORT).show();
                } else {
                    targetList.add(permission);
                }
            }
        }

        String[] targets = new String[targetList.size()];
        targetList.toArray(targets);

        if (targets.length > 0) {
            ActivityCompat.requestPermissions(this, targets, PERMISSION_REQUEST_CODE);
            Toast.makeText(this, targets+"권한 있음", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한을 사용자가 승인함", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void recordAudio() {
        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        recorder.setOutputFile(filename);

        try {
            recorder.prepare();
            recorder.start();
            Toast.makeText(getApplicationContext(), "녹음 시작", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "녹음 시작 오류", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
                Toast.makeText(getApplicationContext(), "녹음 중지", Toast.LENGTH_SHORT).show();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "녹음 중지 오류", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void uploadFileToServer(String filePath) throws FileNotFoundException {
        try{
            File file=new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file); //파일 내용 바이트 단위로 출력
            System.out.println(fileInputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}