package org.cyanotic.cx10;

import com.ivan.xinput.exceptions.XInputNotLoadedException;
import org.cyanotic.cx10.io.CX10InputStreamReader;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by cyanotic on 19/11/2016.
 */
public class Launcher {

    public static void main(String[] args) throws IOException, InterruptedException, XInputNotLoadedException {
//        IVideoPlayer player = new FFPlayProcessVideoPlayer();
//        player.start();
//        Thread.sleep(1000);
//
//        Connection c1 = new Connection("172.16.10.1", 8888);
//        Controller controller = new Controller(new XInput(), new CommandConnection("172.16.10.1", 8895), c1);
//        controller.start();
//
//        InetAddress address = InetAddress.getByName("172.16.10.1");
//        Socket socket = new Socket(address, 8888);
//        byte[] bytes = Files.readAllBytes(Paths.get("video.bin"));
//        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
//        outputStream.write(bytes);
//        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        FileInputStream inputStream = new FileInputStream("video_protocol");
        byte[] response = new byte[106];
        inputStream.read(response);

//        InetAddress ffplay = InetAddress.getByName("localhost");
//        Socket ffplaySocket = new Socket(ffplay, 8889);
//        BufferedOutputStream ffplayOutput = new BufferedOutputStream(ffplaySocket.getOutputStream());
        //BufferedOutputStream ffplayOutput = new BufferedOutputStream(new FileOutputStream("media_1t_t.h264"));
        CX10InputStreamReader control = new CX10InputStreamReader();
        byte[] buf = new byte[64];
        while (inputStream.read(buf) > 0) {
            byte[] cleanOutput = control.feed(buf);
            //ffplayOutput.write(cleanOutput);
        }
    }
}
