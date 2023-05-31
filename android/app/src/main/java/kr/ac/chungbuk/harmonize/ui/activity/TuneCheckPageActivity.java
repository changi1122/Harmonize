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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.service.TokenService;

public class TuneCheckPageActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 101;
    private ImageButton btnRecord;

    //이미지 버튼 초기 상태 - 녹음 수행
    boolean isRecording = false;
    boolean sucRecording = true;
    int recordingCount = 0;
    Handler handler = new Handler();
    int MAX_RECORDINGS = 3;

    int RecordingScalePosition =14;

    private MediaRecorder recorder;
    private String filename;

    RequestQueue queue;

    byte[] fileBytes;

    String [] scale = new String[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tune_check_page);

        queue = Volley.newRequestQueue(getApplicationContext());

        MakeBasicScale();

        final String baseFileName = String.valueOf(TokenService.uid_load()); // 기본 파일 이름

        Runnable recordingRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    stopRecording();
                    try {
                        uploadFileToServer(filename);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    recordingCount++;

                    if (recordingCount < MAX_RECORDINGS) {
                        // 새로운 파일 이름 생성
                        //String numberedFileName = baseFileName +"_"+ recordingCount;
                        //filename = getFilePath(numberedFileName); // 파일 경로 업데이트

                        handler.postDelayed(this, 2000); // 2초 후에 다시 녹음 시작
                    } else {
                        isRecording = false;
                        btnRecord.setImageResource(R.drawable.baseline_mic_none_24);
                        btnRecord.setBackgroundResource(R.drawable.btnrecord_click_effect);
                    }
                } else {
                    recordAudio();
                    handler.postDelayed(this, 2000); // 2초 후에 녹음 중지

                    File file = new File(filename);
                    System.out.println(filename);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    try {
                        FileInputStream fis = new FileInputStream(file);
                        byte[] buffer = new byte[1024];
                        int len;
                        while((len=fis.read(buffer)) != -1){
                            bos.write(buffer, 0, len);
                        }
                        fis.close();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if(fileBytes == null || fileBytes.length == 0){
                        fileBytes = new byte[0];
                    }
                    fileBytes = bos.toByteArray();

                    // uid + scale Name
                    get(baseFileName+scale[RecordingScalePosition]+"_"+recordingCount);
                }
                //spring에서 녹음 성공(true)/실패(false) 가져와야함
                //sucRecording=false;
                if(sucRecording == true){
                    isRecording = !isRecording;
                    System.out.println("RRR " + RecordingScalePosition);
                    System.out.println(isRecording);
                    System.out.println(recordingCount);
                    RecordingScalePosition++;
                    recordingCount =0;
                }
            }
        };

        //녹음 이미지 버튼 클릭 시 녹음 처리
        btnRecord = findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    btnRecord.setImageResource(R.drawable.baseline_mic_24);
                    btnRecord.setBackgroundResource(R.drawable.btnrecord_click_effect);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recordingRunnable.run();
                        }
                    }, 5000); // 5초 후에 녹음 시작
                } else {
                    btnRecord.setImageResource(R.drawable.baseline_mic_none_24);
                    btnRecord.setBackgroundResource(R.drawable.btnrecord_click_effect);
                    handler.removeCallbacks(recordingRunnable); // 현재 예약된 녹음 작업 취소
                    isRecording = false;
                }
            }
        });

        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        checkPermission(permissions);

        // 초기 파일 이름 설정
        filename = getFilePath(baseFileName);
        Log.d("TuneCheckPageActivity", "저장할 파일명: " + filename);
    }

    // 파일 경로 생성 메서드
    private String getFilePath(String fileName) {
        File sdcard = getExternalFilesDir(null);
        File file = new File(sdcard, fileName + ".m4a");
        return file.getAbsolutePath();
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
            System.out.println("녹음 시작");
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
                System.out.println("녹음 중지");
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

    public void MakeBasicScale(){
        int k=0;
        for(int i=2; i<=5; i++){
            scale[k++] = "C"+i;
            scale[k++] = "D"+i;
            scale[k++] = "E"+i;
            scale[k++] = "F"+i;
            scale[k++] = "G"+i;
            scale[k++] = "A"+i;
            scale[k++] = "B"+i;
        }
        scale[k++] = "C6";
        scale[k] = "B6";
    }

    private void get(String scale){
        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/catch/file/"+ scale),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        System.out.println("Response" + response);
                        if(response.equals("true")){
                            sucRecording = true;
                            System.out.println("sucRecording : " + sucRecording);
                        }else{
                            sucRecording= false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return fileBytes;
            }

            @Override
            public String getBodyContentType() {
                return "audio/m4a";
            }
        };

        queue.add(request);
    }
}