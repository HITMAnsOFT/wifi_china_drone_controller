package org.cyanotic.cx10.io.video;

import java.io.IOException;

/**
 * Created by cyanotic on 25/11/2016.
 */
public class FFPlayProcessVideoPlayer implements IVideoPlayer {
    private static final int PORT = 8889;
    private Process ffplay;

    public void start() {
        if (ffplay != null) {
            stop();
        }
        try {
            ffplay = new ProcessBuilder("gst-launch-1.0", "tcpserversrc", "port=" + PORT, "!", "h264parse", "!", "avdec_h264", "!", "xvimagesink")
                        .start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (ffplay != null) {
            ffplay.destroy();
            ffplay = null;
        }
    }
}
