package kr.ac.chungbuk.harmonize.item;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import kr.ac.chungbuk.harmonize.R;
import kr.ac.chungbuk.harmonize.config.Domain;

public class MusicRecordingView extends LinearLayout {

    String musicId;
    MediaRecorder recorder;
    ImageView btnRecord, btnRecordStop, btnUpload;
    TextView tvStatus;
    
    /* 파일 전송 관련 */
    String filename;
    byte[] fileBytes;
    RequestQueue queue;

    public MusicRecordingView(Context context) {
        super(context);
        init(context);
    }

    public MusicRecordingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.music_recording, this, true);

        btnRecord = findViewById(R.id.btnRecord);
        btnRecordStop = findViewById(R.id.btnRecordStop);
        btnUpload = findViewById(R.id.btnUpload);

        btnRecord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                recordAudio();
            }
        });

        btnRecordStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();

                File file = new File(filename);
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

                send();
            }
        });


    }

    private void recordAudio() {
        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        filename = getFilePath(musicId + "record");
        recorder.setOutputFile(filename);

        try {
            recorder.prepare();
            recorder.start();
            Toast.makeText(getContext(), "녹음 시작", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "녹음 시작 오류", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
                Toast.makeText(getContext(), "녹음 중지", Toast.LENGTH_SHORT).show();
                System.out.println("녹음 중지");
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "녹음 중지 오류", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 파일 경로 생성 메서드
    private String getFilePath(String fileName) {
        File sdcard = getContext().getExternalFilesDir(null);
        File file = new File(sdcard, fileName + ".m4a");
        return file.getAbsolutePath();
    }


    private void send(){
        StringRequest request = new StringRequest(Request.Method.POST, Domain.url("/api/test/compare"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        Toast.makeText(getContext(), "업로드 성공", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Toast.makeText(getContext(), "업로드 실패", Toast.LENGTH_SHORT).show();
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
