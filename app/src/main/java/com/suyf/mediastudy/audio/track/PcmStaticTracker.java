package com.suyf.mediastudy.audio.track;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PcmStaticTracker {
    private int mSampleRateInHz;
    private int mChannelConfig;
    private int mAudioFormat;
    private int mBufferSizeInBytes;
    private AudioTrack mAudioTrack;
    private DataInputStream mAudioStream;

    /**
     * 静态模式播放，也就是一次性写入数据块，要求数据块很小。我的手机没成功过，不知道啥原因。
     *
     * @param pcmFileName
     */
    public PcmStaticTracker(String pcmFileName) {
        this(AudioManager.STREAM_RING, 22050, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, AudioTrack.MODE_STATIC, pcmFileName);
    }

    /**
     * 播放时的参数需要时录制时的参数一致才能播放出原来的声音
     *
     * @param streamType
     * @param sampleRateInHz 音频采样率
     * @param channelConfig  音频频道
     * @param audioFormat    音频格式
     * @param mode
     */
    public PcmStaticTracker(int streamType, int sampleRateInHz, int channelConfig,
                            int audioFormat, int mode, String pcmFileName) {
        try {
            mAudioStream = new DataInputStream(new BufferedInputStream(new FileInputStream(pcmFileName)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        mSampleRateInHz = sampleRateInHz;
        mChannelConfig = channelConfig;
        mAudioFormat = audioFormat;
        mBufferSizeInBytes = AudioTrack.getMinBufferSize(mSampleRateInHz, mChannelConfig, mAudioFormat);
        mAudioTrack = new AudioTrack(streamType, mSampleRateInHz, mChannelConfig,
                mAudioFormat, mBufferSizeInBytes, mode);
    }

    /**
     * MODE_STATIC模式播放时，先写入数据，再播放。数据不能太大
     */
    public void startTrack() {
        if (mAudioTrack == null || mAudioStream == null) {
            return;
        }
        try {
            int available = mAudioStream.available();
            if (available > 0) {
                byte[] buffer = new byte[available];
                int length = mAudioStream.read(buffer, 0, available);
                int writeNum = mAudioTrack.write(buffer, 0, length);//最多写入了3544?
                if (writeNum != AudioRecord.ERROR_BAD_VALUE
                        && writeNum != AudioRecord.ERROR_DEAD_OBJECT
                        && writeNum != AudioRecord.ERROR_INVALID_OPERATION) {
                    mAudioTrack.play();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopTrack() {
        if (mAudioTrack != null) {
            mAudioTrack.stop();
            mAudioTrack.release();
            mAudioTrack = null;
        }
        if (mAudioStream != null) {
            try {
                mAudioStream.close();
                mAudioStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
