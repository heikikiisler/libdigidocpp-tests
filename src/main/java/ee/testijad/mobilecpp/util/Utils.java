package ee.testijad.mobilecpp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static int getFreePort() {
        int port = 0;
        try {
            ServerSocket socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            port = socket.getLocalPort();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return port;
    }

    public static void downloadFileFromAndroid(String fileName){
        long epoch = System.currentTimeMillis() / 1000;
        String timePart = Long.toString(epoch);

        String commandString = String.format("adb pull /sdcard/%s results/%s-%s", fileName, timePart, fileName);
        List<String> output = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(commandString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result;
            while ((result = reader.readLine()) != null) {
                output.add(result);
                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
