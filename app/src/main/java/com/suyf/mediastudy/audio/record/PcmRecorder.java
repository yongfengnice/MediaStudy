package com.suyf.mediastudy.audio.record;

import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;

import com.suyf.mediastudy.audio.utils.PcmToWavUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PcmRecorder {
    /**
     * 音频采样率
     * 我的手机支持44100，22050，16000，11025。如果不支持，录音可能是呲呲呲声音。
     */
    private int mSampleRateInHz;
    /**
     * 音频采样通道
     * AudioFormat.CHANNEL_IN_MONO：录音单通道
     * AudioFormat.CHANNEL_IN_STEREO：录音双通道，即左右声道
     */
    private int mChannelConfig;
    /**
     * 音频存储格式
     * AudioFormat.ENCODING_PCM_16BIT：16位存储(精度越高质量越好)
     * AudioFormat.ENCODING_PCM_8BIT：8位存储(我的手机失真)
     */
    private int mAudioFormat;

    /**
     * AudioRecord内部最小缓存，一般读取一次数据也按照这个大小即可
     */
    private int mBufferSizeInBytes;

    private byte[] mAudioData;
    private AudioRecord mAudioRecord;
    private boolean mRecording = false;
    private String mPcmFileName;

    public PcmRecorder(int sampleRateInHz, int channelConfig, int audioFormat, String pcmFileName) {
        mSampleRateInHz = sampleRateInHz;
        mChannelConfig = channelConfig;
        mAudioFormat = audioFormat;
        mPcmFileName = pcmFileName;

        mBufferSizeInBytes = AudioRecord
                .getMinBufferSize(mSampleRateInHz, mChannelConfig, mAudioFormat);
        mAudioData = new byte[mBufferSizeInBytes];

        mAudioRecord = new AudioRecord(AudioSource.MIC, mSampleRateInHz, mChannelConfig,
                mAudioFormat, mBufferSizeInBytes);
    }

    public void startRecord() {
        if (mRecording) {
            return;
        }
        mRecording = true;
        FileOutputStream outputStream = createOutputStream();
        if (outputStream == null) {
            mRecording = false;
            return;
        }

        mAudioRecord.startRecording();
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
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileOutputStream createOutputStream() {
        File file = new File(mPcmFileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return outputStream;
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

    public void pcmToWav(String pcmInputFile, String wavOutputFile) {
        PcmToWavUtil wavUtil = new PcmToWavUtil(mSampleRateInHz, mChannelConfig, mAudioFormat);
        wavUtil.pcmToWav(pcmInputFile, wavOutputFile);
    }
}
