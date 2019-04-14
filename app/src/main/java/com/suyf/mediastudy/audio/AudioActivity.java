package com.suyf.mediastudy.audio;

import android.Manifest;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.suyf.mediastudy.R;
import com.suyf.mediastudy.audio.record.PcmRecorder;

import androidx.fragment.app.FragmentActivity;

public class AudioActivity extends FragmentActivity {

    private PcmRecorder mPcmRecorder;
    private String mPcmFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
    }

    public void startRecord(View view) {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new BaseMultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                super.onPermissionsChecked(report);
                if (report.areAllPermissionsGranted()) {
                    startRecord();
                }
            }
        }).check();
    }

    void startRecord() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                mPcmFileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/" + "audio_record_pcm_file.pcm";
                mPcmRecorder = new PcmRecorder(11025, AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, mPcmFileName);
                mPcmRecorder.startRecord();
            }
        }.start();
    }

    public void stopRecord(View view) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (mPcmRecorder != null) {
                    mPcmRecorder.stopRecord();

                    String outFilename = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/" + "audio_record_wav_file.wav";
                    mPcmRecorder.pcmToWav(mPcmFileName, outFilename);
                }
            }
        }.start();
    }

}
