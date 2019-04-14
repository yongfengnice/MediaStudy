package com.suyf.mediastudy.audio.track;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PcmStreamTracker {
    private int mSampleRateInHz;
    private int mChannelConfig;
    private int mAudioFormat;
    private int mBufferSizeInBytes;
    private AudioTrack mAudioTrack;
    private DataInputStream mAudioStream;

    /**
     * 流式模式播放，边播放边写入数据，数据块大小没有限制，存在一点点延迟。这种模式使用比较多。
     *
     * @param pcmFileName
     */
    public PcmStreamTracker(String pcmFileName) {
        this(AudioManager.STREAM_MUSIC, 22050, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, AudioTrack.MODE_STREAM, pcmFileName);
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
    public PcmStreamTracker(int streamType, int sampleRateInHz, int channelConfig,
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
     * MODE_STREAM模式播放时，先播放，再一次一次写入数据
     */
    public void startTrack() {
        if (mAudioTrack == null || mAudioStream == null) {
            return;
        }
        byte[] buffer = new byte[mBufferSizeInBytes];
        mAudioTrack.play();
        try {
            while (mAudioStream.available() > 0) {
                int length = mAudioStream.read(buffer, 0, mBufferSizeInBytes);
                mAudioTrack.write(buffer, 0, length);
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
