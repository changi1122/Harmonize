package kr.ac.chungbuk.harmonize.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;
import kr.ac.chungbuk.harmonize.service.TokenService;

public class TuneCheckPageActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 101;
    private ImageButton btnRecord;
    private ImageView pianoImage;
    private TextView t1, t2, t3, t4, t5, t6, t7;

    //이미지 버튼 초기 상태 - 녹음 수행
    boolean isRecording = false;
    boolean sucRecording = true;
    int recordingCount = 0;
    int WholeCount=0;
    Handler handler = new Handler();
    int MAX_RECORDINGS = 30;

    int RecordingScalePosition =0;

    private MediaRecorder recorder;
    private String filename;

    RequestQueue queue;

    byte[] fileBytes;

    String [] scale = new String[30];

    final String baseFileName = String.valueOf(TokenService.uid_load()); // 기본 파일 이름

    boolean next=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tune_check_page);

        queue = Volley.newRequestQueue(getApplicationContext());

        MakeBasicScale();

        btnRecord = findViewById(R.id.btnRecord);
        pianoImage = findViewById(R.id.pianoImage);
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        t4=findViewById(R.id.t4);
        t5=findViewById(R.id.t5);
        t6=findViewById(R.id.t6);
        t7=findViewById(R.id.t7);

        Runnable recordingRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRecording) {

                    if(next){
                        next=false;

                        stopRecording();
                        btnRecord.setImageResource(R.drawable.baseline_mic_none_24);


                        String rootSD =  getExternalFilesDir("/").getAbsolutePath();
                        File file2 = new File(rootSD);
                        File list[]  = file2.listFiles();
                        Uri audioUrl = Uri.parse(rootSD+"/"+list[1].getName());

                        System.out.println(audioUrl);

                        /*File file = new File(filename);
                        System.out.println(filename);*/
                        String uriName = String.valueOf(audioUrl);
                        File file = new File(uriName);

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();

                        try {
                            FileInputStream fis = new FileInputStream(file);
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = fis.read(buffer)) != -1) {
                                bos.write(buffer, 0, len);
                            }
                            fis.close();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (fileBytes == null || fileBytes.length == 0) {
                            fileBytes = new byte[0];
                        }
                        fileBytes = bos.toByteArray();

                        // uid + scale Name
                        System.out.println("Let's send message");
                        get(baseFileName + scale[RecordingScalePosition] + "_" + recordingCount);
                    }else{
                        System.out.println("Next is false" + next);
                    }


                } else {

                    //알파벳+"숫자" : 2~6
                    char noteNum = scale[RecordingScalePosition].charAt(1);
                    t1.setText("C"+noteNum); t2.setText("D"+noteNum);
                    t3.setText("E"+noteNum); t4.setText("F"+noteNum);
                    t5.setText("G"+noteNum); t6.setText("A"+noteNum);
                    t7.setText("B"+noteNum);

                    //음계 피아노 변환(디자인) 코드 (단, 재녹음 여부 판단 하에)
                    //"알파벳"+숫자 : C~G,A,B
                    char note = scale[RecordingScalePosition].charAt(0);
                    if (note == 'C') {
                        pianoImage.setImageResource(R.drawable.pianoc4);
                        t1.setTextColor(Color.RED);
                        t7.setTextColor(Color.BLACK); t2.setTextColor(Color.BLACK);
                    } else if (note == 'D') {
                        pianoImage.setImageResource(R.drawable.pianod4);
                        t2.setTextColor(Color.RED);
                        t1.setTextColor(Color.BLACK); t3.setTextColor(Color.BLACK);
                    } else if (note == 'E') {
                        pianoImage.setImageResource(R.drawable.pianoe4);
                        t3.setTextColor(Color.RED);
                        t2.setTextColor(Color.BLACK); t4.setTextColor(Color.BLACK);
                    } else if (note == 'F') {
                        pianoImage.setImageResource(R.drawable.pianof4);
                        t4.setTextColor(Color.RED);
                        t3.setTextColor(Color.BLACK); t5.setTextColor(Color.BLACK);
                    } else if (note == 'G') {
                        pianoImage.setImageResource(R.drawable.pianog4);
                        t5.setTextColor(Color.RED); t4.setTextColor(Color.BLACK);
                    } else if (note == 'A') {
                        pianoImage.setImageResource(R.drawable.pianoa4);
                        t6.setTextColor(Color.RED);
                        t5.setTextColor(Color.BLACK); t7.setTextColor(Color.BLACK);
                    } else if (note == 'B') {
                        pianoImage.setImageResource(R.drawable.pianob4);
                        t7.setTextColor(Color.RED);
                        t6.setTextColor(Color.BLACK); t1.setTextColor(Color.BLACK);
                    } else {
                        System.out.println("Invalid note");
                    }

                    //@김성욱, 여기다가 음악 재생시켜서 들려주면 됨

                    recordAudio();
                    btnRecord.setImageResource(R.drawable.baseline_mic_24);

                    isRecording = !isRecording;
                }
                if (WholeCount < MAX_RECORDINGS) {
                    // 새로운 파일 이름 생성
                    //String numberedFileName = baseFileName +"_"+ recordingCount;
                    //filename = getFilePath(numberedFileName); // 파일 경로 업데이트

                    handler.postDelayed(this, 5000); // 5초 후에 다시 녹음 시작
                }
            }
        };

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
                    }, 1000); // 5초 후에 녹음 시작
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

//    public void uploadFileToServer(String filePath) throws FileNotFoundException {
//
//    }

    public void MakeBasicScale(){
        int k=0;
        for(int i=4; i<=5; i++){
            scale[k++] = "C"+i;
            scale[k++] = "D"+i;
            scale[k++] = "E"+i;
            scale[k++] = "F"+i;
            scale[k++] = "G"+i;
            scale[k++] = "A"+i;
            scale[k++] = "B"+i;
        }
        scale[k++] = "C6";
        scale[k++] = "D6";

        for(int i=3; i>=2; i--){
            scale[k++] = "B"+i;
            scale[k++] = "A"+i;
            scale[k++] = "G"+i;
            scale[k++] = "F"+i;
            scale[k++] = "E"+i;
            scale[k++] = "D"+i;
            scale[k++] = "C"+i;
        }
    }

    synchronized private void get(String scale){
        CompletableFuture<String> future = new CompletableFuture<>();
        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/catch/file/"+ scale),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        System.out.println("Response" + response);
                        if(response.equals("true")){
                            sucRecording = true;
                            System.out.println("sucRecording : " + sucRecording);

                            Toast.makeText(getApplicationContext(), "음정 일치", Toast.LENGTH_SHORT).show();

                            System.out.println("RRR " + RecordingScalePosition);
                            System.out.println(isRecording);
                            System.out.println(recordingCount);
                            System.out.println(WholeCount);
                            RecordingScalePosition++;
                            recordingCount=0; //새로운 녹음은 녹음 횟수 초기화
                            WholeCount++;     //전체에 대한 녹음 완료 횟수 카운트

                            isRecording = !isRecording;
                        }else{
                            sucRecording= false;
                            System.out.println("sucRecording : " + sucRecording);

                            Toast.makeText(getApplicationContext(), "음정 불일치", Toast.LENGTH_SHORT).show();

                            recordingCount++; //재녹음 시 녹음 횟수 +1
                            System.out.println("여기는 false");
                            System.out.println(recordingCount);
                            System.out.println(WholeCount);

                            isRecording = !isRecording;
                        }
                        next=true;
                        System.out.println("next" + next);
                        future.complete(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        future.completeExceptionally(error);
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

        try {
            String response = future.get(15, TimeUnit.SECONDS); // 응답을 5초 동안 기다림
            // 응답 처리 로직 ...
        } catch (Exception e) {
            // 예외 처리 또는 타임아웃 처리
        }
    }
}