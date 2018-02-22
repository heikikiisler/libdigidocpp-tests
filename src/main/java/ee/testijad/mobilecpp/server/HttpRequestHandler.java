package ee.testijad.mobilecpp.server;

import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.Log;
import ee.testijad.mobilecpp.util.Utils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class HttpRequestHandler implements Runnable {

    private Socket socket;

    HttpRequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Log.info("[HTTP Server] Processing request on thread " + Thread.currentThread().getName());
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            OutputStream out = new BufferedOutputStream(socket.getOutputStream());
            PrintStream printStream = new PrintStream(out);

            String request = in.readLine();
            if (request == null) return;

            boolean bufferIsEmpty = false;
            String contentType = null;
            int contentLength = 0;
            String misc;
            while (true) {
                misc = in.readLine();
                Log.info(String.format("[HTTP header] %s", misc));
                if (misc.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(misc.split(":")[1].trim());
                    if (contentLength == 0) {
                        bufferIsEmpty = true;
                    }
                }
                if (misc.startsWith("Content-Type:")) {
                    Log.info(misc);
                    contentType = misc.split(":")[1].trim().toLowerCase();
                }

                if (misc == null || misc.length() == 0) {
                    break;
                }
            }

            if (!request.startsWith("GET") || request.length() < 14 || !request.endsWith("HTTP/1.1")) {
                if (request.startsWith("PUT") & request.length() > 14 & request.endsWith("HTTP/1.1")) {
                    Log.info("[HTTP server] Processing PUT request");
                    String file = "a.b";
                    if (contentType.startsWith("text/plain")) {
                        file = "digidocpp.log";
                    }
                    if (contentType.startsWith("application/json")) {
                        file = "result.json";
                    }
                    String targetFile = targetFilePath(file);
                    // String fileRequest = request.substring(4, request.length() - 9).trim();
                    if (!bufferIsEmpty) {
                        Log.info(String.format("[HTTP server] Source file: %s", file));
                        int fileSaveResult = writeFile(in, targetFile, contentLength);

                        if (fileSaveResult == 200) {
                            printStream.print("HTTP/1.0 " + fileSaveResult + "\r\nDate:" + Utils.getTimeStamp() + "\r\n\r\nUpload succeeded<br>");
                        } else {
                            generateError(printStream, socket, "500", "Internal server error",
                                    "Upload error");
                        }
                    } else {
                        generateError(printStream, socket, "400", "Bad Request", "Upload error, request is empty.");
                    }
                } else {
                    generateError(printStream, socket, "400", "Bad Request", "Your browser sent a request that this server could not understand.");
                }
            } else {
                Log.info("[HTTP server] Processing GET request");
                String fileRequest = request.substring(4, request.length() - 9).trim();
                if (fileRequest.contains("/diag")) {
                    Log.info("[HTTP server] Handling diagnostic request");
                    printStream.print(String.format("HTTP/1.0 200 OK\r\nDate: %s\r\nServer: HTTP Server 1.0\r\n\r\nServer is visible!\r\n\r\n%s", Utils.getTimeStamp(), Utils.getLocalTimeStamp()));
                } else {
                    String fileName = "./" + Config.ZIP_FILE_DIRECTORY;
                    Log.info(String.format("[HTTP server] Zip file name: %s", fileName));
                    File fileToSend = new File(fileName);
                    if (fileToSend.isDirectory() && !fileRequest.endsWith("/")) {
                        printStream.print(String.format("HTTP/1.0\r\n 301 Moved Permanently<br> Location: http://%s:%s/%s/<br><br>"
                                , socket.getLocalAddress().getHostAddress()
                                , socket.getLocalPort(), fileRequest));
                    } else {
                        try {
                            InputStream file = new FileInputStream(fileToSend);
                            printStream.print(String.format("HTTP/1.0 200 OK\r\nContent-Type: %s\r\nDate: %s\r\nServer: HTTP Server 1.0\r\n\r\n", selectMimeType(fileName), Utils.getTimeStamp()));

                            sendFile(file, out);
                        } catch (FileNotFoundException e) {
                            generateError(printStream, socket, "404", "Not Found", "The requested URL was not found on this server.");
                        }
                    }
                }
            } // GET request end
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.info("[HTTP server] Processing finished on thread " + Thread.currentThread().getName());
    }

    private static void sendFile(InputStream file, OutputStream out) {
        Log.info(String.format("[HTTP Server] Starting file sending %s", Utils.getLocalTimeStamp()));
        try {
            byte[] buffer = new byte[1024 * 1024];
            int i = 0;
            while (file.available() > 0) {
                out.write(buffer, 0, file.read(buffer));
                Log.info(String.format("[HTTP Server] Send file: index %s", String.valueOf(i)));
                i++;
            }
            Log.info(String.format("[HTTP Server] File sending is complete %s", Utils.getLocalTimeStamp()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String selectMimeType(String path) {
        if (path.endsWith(".zip")) {
            return "application/zip";
        } else if (path.endsWith(".json")) {
            return "application/json";
        } else {
            return "text/plain";
        }
    }

    private void generateError(PrintStream printStream, Socket connection,
                               String code, String title, String message) {
        printStream.print("HTTP/1.0 " + code + " " + title + "\r\n" +
                          "\r\n" +
                          "<!DOCTYPE HTML>\r\n" +
                          "<TITLE>Server error</TITLE>\r\n" +
                          "</HEAD><BODY>\r\n" +
                          "<H1>" + title + "</H1>\r\n" + message + "<P>\r\n" +
                          "<HR><ADDRESS>HTTP Server 1.0 at " +
                          connection.getLocalAddress().getHostName() +
                          " Port " + connection.getLocalPort() + "</ADDRESS>\r\n" +
                          "</BODY></HTML>\r\n");
    }

    private static String targetFilePath(String fileName) {
        long epoch = System.currentTimeMillis() / 1000;
        String timePart = Long.toString(epoch);
        return String.format("%s/%s-%s", Config.RESULT_FILES_DIRECTORY, timePart, fileName);
    }

    private static int writeFile(BufferedReader in, String targetFilePath, int contentLength) {
        Log.info(String.format("[HTTP Server] Writing output file: %s", targetFilePath));
        try {
            BufferedWriter bufWriter = Files.newBufferedWriter(Paths.get(targetFilePath), StandardCharsets.UTF_8);
            Log.info(String.format("File copy started: %s %s", targetFilePath, Utils.getLocalTimeStamp()));
            char[] buffer = new char[(1024 * 64)];
            int len;
            int total = 0;
            int counter = 0;
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while ((len = in.read(buffer)) >= 0) {
                Log.info(String.format("Write started: %s, size: %s", String.valueOf(counter), String.valueOf(len)));
                bufWriter.write(buffer, 0, len);
                total += len;
                Log.info(String.format("Write ended: %s,  copied: %s", String.valueOf(counter), String.valueOf(total)));
                counter++;
                if (total == contentLength || !in.ready()) {
                    break;
                }
            }
            bufWriter.flush();
            bufWriter.close();
            Log.info(String.format("File copy ended: %s %s", targetFilePath, Utils.getLocalTimeStamp()));
        } catch (IOException e) {
            System.err.println(e);
            return 304;
        }
        Log.info(String.format("[HTTP server] File %s writing succeeded %s", targetFilePath, Utils.getLocalTimeStamp()));
        return 200;
    }
}
