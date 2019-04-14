package com.suyf.mediastudy.audio.wav;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WavHeader {
    public final char fileID[] = {'R', 'I', 'F', 'F'};
    public int mFileLength;
    public char mWavTag[] = {'W', 'A', 'V', 'E'};
    public char mFmtHeaderID[] = {'f', 'm', 't', ' '};
    public int mFmtHeaderLength;
    public short mFormatTag;
    public short mChannels;
    public int mSamplesPerSec;
    public int mAvgBytesPerSec;
    public short mBlockAlign;
    public short mBitsPerSample;
    public char mDataHeaderID[] = {'d','a','t','a'};
    public int mDataHeaderLength;

    public byte[] getHeader() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        WriteChar(bos, fileID);
        WriteInt(bos, mFileLength);
        WriteChar(bos, mWavTag);
        WriteChar(bos, mFmtHeaderID);
        WriteInt(bos, mFmtHeaderLength);
        WriteShort(bos, mFormatTag);
        WriteShort(bos, mChannels);
        WriteInt(bos, mSamplesPerSec);
        WriteInt(bos, mAvgBytesPerSec);
        WriteShort(bos, mBlockAlign);
        WriteShort(bos, mBitsPerSample);
        WriteChar(bos, mDataHeaderID);
        WriteInt(bos, mDataHeaderLength);
        bos.flush();
        byte[] r = bos.toByteArray();
        bos.close();
        return r;
    }

    private void WriteShort(ByteArrayOutputStream bos, int s) throws IOException {
        byte[] mybyte = new byte[2];
        mybyte[1] =(byte)( (s << 16) >> 24 );
        mybyte[0] =(byte)( (s << 24) >> 24 );
        bos.write(mybyte);
    }


    private void WriteInt(ByteArrayOutputStream bos, int n) throws IOException {
        byte[] buf = new byte[4];
        buf[3] =(byte)( n >> 24 );
        buf[2] =(byte)( (n << 8) >> 24 );
        buf[1] =(byte)( (n << 16) >> 24 );
        buf[0] =(byte)( (n << 24) >> 24 );
        bos.write(buf);
    }

    private void WriteChar(ByteArrayOutputStream bos, char[] id) {
        for (int i=0; i<id.length; i++) {
            char c = id[i];
            bos.write(c);
        }
    }
}