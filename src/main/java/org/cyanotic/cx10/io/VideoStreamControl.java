package org.cyanotic.cx10.io;

import org.cyanotic.cx10.utils.ByteUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by orfeo.ciano on 23/11/2016.
 */
public class VideoStreamControl {

    private int position = 0;
    private byte[] window;
    private int toSkip = 0;

    public VideoStreamControl() {
    }

    public byte[] feed(byte[] bytes) {
        System.out.println("Feeding the VideoStream");
        ByteBuffer out = ByteBuffer.allocate(bytes.length);
        if (position > 0) {
            position = 0;
        } else {
            System.out.println("It's the first feed, initializing the window buffer");
            window = new byte[]{bytes[0], bytes[1], bytes[2]};
            position = 2;
        }

        for (; position < bytes.length; position++) {
            if (toSkip > 0) {
                pushInWindow(bytes[position]);
                toSkip--;
                continue;
            }

            if (isHeader()) {
                System.out.println("Found a new NAL");
                byte[] src = fixNAL(bytes);
                out.put(src);
            } else {
                byte oldByte = pushInWindow(bytes[position]);
                out.put(oldByte);
            }
        }

        byte[] ret = new byte[out.position() + 1];
        out.rewind();
        out.get(ret);
        ret[ret.length - 1] = window[2];
        System.out.println("Return is " + ByteUtils.bytesToHex(ret));
        return ret;
    }

    private byte[] fixNAL(byte[] bytes) {

        byte[] nal = Arrays.copyOfRange(bytes, position - 2, position + 38);
        System.out.println("The old NAL is " + ByteUtils.bytesToHex(nal));

        byte[] header = new byte[5];
        header[0] = 0x00;
        header[1] = 0x00;
        header[2] = 0x00;
        header[3] = 0x01 & 0xFF;
        header[4] = nal[3];
        System.out.println("The new NAL is " + ByteUtils.bytesToHex(header));

        toSkip = 37;
        return header;
    }

    private byte pushInWindow(byte b) {
        byte ret = window[0];
        window[0] = window[1];
        window[1] = window[2];
        window[2] = b;
        return ret;
    }

    private boolean isHeader() {
        return window[0] == 0x00 && window[1] == 0x00 && window[2] == (0x01 & 0xFF);
    }
}