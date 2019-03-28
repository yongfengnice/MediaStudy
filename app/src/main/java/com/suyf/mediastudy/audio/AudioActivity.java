package com.suyf.mediastudy.audio;

import android.Manifest;
import android.media.AudioFormat;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.suyf.mediastudy.R;
import com.suyf.mediastudy.audio.record.PcmRecorder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AudioActivity extends FragmentActivity {

  private PcmRecorder mPcmRecorder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_audio);
  }

  public void startRecord(View view) {
    AudioActivityPermissionsDispatcher.startRecordWithPermissionCheck(this);
  }

  @NeedsPermission({Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})
  void startRecord() {
    new Thread() {
      @Override
      public void run() {
        super.run();
        mPcmRecorder = new PcmRecorder(44800, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_8BIT);
        mPcmRecorder.startRecord();
      }
    }.start();
  }

  public void stopRecord(View view) {
    new Thread() {
      @Override
      public void run() {
        super.run();
        mPcmRecorder.stopRecord();
        mPcmRecorder.pcmToWav();
      }
    }.start();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    AudioActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
  }

}
