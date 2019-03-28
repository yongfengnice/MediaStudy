package com.suyf.mediastudy.audio.record;

import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Environment;
import android.os.SystemClock;
import com.suyf.mediastudy.audio.utils.PcmToWavUtil;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PcmRecorder {

  private int mSampleRateInHz;
  private int mChannelConfig;
  private int mAudioFormat;
  private int mBufferSizeInBytes;
  private AudioRecord mAudioRecord;
  private final byte[] mAudioData;
  private boolean mRecording = false;
  private String mRecordFileName;

  public PcmRecorder(int sampleRateInHz, int channelConfig, int audioFormat) {
    this.mSampleRateInHz = sampleRateInHz;
    this.mChannelConfig = channelConfig;
    this.mAudioFormat = audioFormat;

    this.mBufferSizeInBytes = AudioRecord
        .getMinBufferSize(mSampleRateInHz, mChannelConfig, mAudioFormat);
    mAudioRecord = new AudioRecord(AudioSource.MIC, mSampleRateInHz, mChannelConfig,
        mAudioFormat, mBufferSizeInBytes);
    mAudioData = new byte[mBufferSizeInBytes];
  }

  public void startRecord() {
    if (mRecording) {
      return;
    }
    mRecordFileName = Environment.getExternalStorageDirectory().getAbsolutePath()
        + "/" + "audio_record_" + SystemClock.currentThreadTimeMillis() + ".pcm";

    mRecording = true;
    mAudioRecord.startRecording();

    FileOutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(mRecordFileName);
    } catch (FileNotFoundException e) {
      stopRecord();
      e.printStackTrace();
    }
    if (outputStream == null) {
      stopRecord();
      return;
    }

    while (mRecording) {
      int readSize = mAudioRecord.read(mAudioData, 0, mBufferSizeInBytes);
      if (readSize != AudioRecord.ERROR_INVALID_OPERATION) {
        try {
          outputStream.write(mAudioData, 0, readSize);
        } catch (IOException e) {
          e.printStackTrace();
          stopRecord();
          break;
        }
      }
    }
    try {
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void stopRecord() {
    if (!mRecording) {
      return;
    }
    mRecording = false;
    if (mAudioRecord != null) {
      mAudioRecord.stop();
      mAudioRecord.release();
      mAudioRecord = null;
    }
  }

  public void pcmToWav() {
    String outFilename = Environment.getExternalStorageDirectory().getAbsolutePath()
        + "/" + "audio_record_" + SystemClock.currentThreadTimeMillis() + ".wav";
    PcmToWavUtil wavUtil = new PcmToWavUtil(mSampleRateInHz, mChannelConfig, mAudioFormat);
    wavUtil.pcmToWav(mRecordFileName, outFilename);
  }
}
