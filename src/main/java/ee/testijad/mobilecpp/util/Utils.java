package ee.testijad.mobilecpp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

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

    private static boolean isMac() {
        return getOSName().contains("mac");
    }

    private static String getOSName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static void downloadFileFromAndroid(String fileName) {
        long epoch = System.currentTimeMillis() / 1000;
        String timePart = Long.toString(epoch);
        String sourcePath = String.format("/sdcard/%s", fileName);
        String targetPath = String.format("%s/%s-%s", Config.RESULT_FILES_DIRECTORY, timePart, fileName);
        String commandString = String.format("adb pull %s %s", sourcePath, targetPath);
        execCommand(commandString);
    }

    private static void execCommand(String commandString) {
        Log.info(String.format("[COMMAND] %s", commandString));
        try {
            Process process;
            if (isMac()) {
                List<String> commands = new ArrayList<>();
                commands.add("/bin/sh");
                commands.add("-c");
                commands.add(commandString);
                process = new ProcessBuilder(commands).start();
            } else {
                process = Runtime.getRuntime().exec(commandString);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result;
            while ((result = reader.readLine()) != null) {
                Log.info(String.format("[COMMAND RESULT] %s", result));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void execMacCommand(String commandString) {
        List<String> commands = new ArrayList<>();
        commands.add("/bin/sh");
        commands.add("-c");
        commands.add(commandString);
        try {
            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result;
            while ((result = reader.readLine()) != null) {
                Log.info(String.format("[COMMAND] %s", result));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFileFromAndroid(String fileName) {
        String commandString = String.format("adb shell rm -f /sdcard/%s", fileName);
        execCommand(commandString);
    }

    private static void copyFileToAndroid(String fileName) {
        String commandString = String.format("adb push \"%s/%s\" \"/sdcard\"", Config.DATA_FILES_DIRECTORY, fileName);
        if (isMac()) {
            //fileName = fileName.replace(" ", "\\ ");
            commandString = String.format("adb push './%s/%s' /sdcard", Config.DATA_FILES_DIRECTORY, fileName);
            Log.info(commandString);
            execMacCommand(commandString);
        } else {
            Log.info(commandString);
            execCommand(commandString);
        }

    }

    public static void copyAllDataFiles(String directory) {
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                copyFileToAndroid(file.getName());
            }
        }
    }

    public static String readFileIntoString(String fileName, Charset encoding) {
        StringBuilder contents = new StringBuilder();
        try {
            Files.readAllLines(Paths.get(fileName), encoding).forEach(contents::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(contents);
    }

    public static String getHostAddress() {
        String hostAddress = null;
        if (Config.HTTP_SERVER_HOST != null) {
            hostAddress = Config.HTTP_SERVER_HOST;
        } else {
            try {
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return hostAddress;
    }

    public static String getBaseUrl(int port) {
        return String.format("http://%s:%s/", getHostAddress(), String.valueOf(port));
    }

    public static void createResultsFolderIfNotExists() {
        String path = System.getProperty("user.dir") + File.separator + Config.RESULT_FILES_DIRECTORY;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        return sdf.format(date);
    }

    public static String getLocalTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        return sdf.format(date);
    }

    public static Set<String> getWarningSetFromString(String warningString, String separator) {
        Set<String> warnings = new HashSet<>();
        if (!warningString.equals("")) {
            Arrays.stream(warningString.split(separator))
                    .filter(warning -> !warning.equals(""))
                    .map(warning -> warning.replaceAll("(ERROR|WARNING): [0-9]+ - ", ""))
                    .map(String::trim)
                    .forEach(warnings::add);
        }
        return warnings;
    }

    public static boolean verifyHttpServer(String urlString) {
        boolean result = false;
        HttpURLConnection urlConnection;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = urlConnection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
                result = true;
            }
            in.close();
            urlConnection.disconnect();
        } catch (Exception e) {
        }
        return result;
    }

    public static void myIPAddress() {
        Log.info("***************** Up and running network interfaces *****************");
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                // drop inactive
                if (!networkInterface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    Log.info(String.format("Interface name: [ %s ] --> ip: %s",
                            networkInterface.getDisplayName(), addr.getHostAddress()));
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Log.info("***************** *****************");
    }

}
