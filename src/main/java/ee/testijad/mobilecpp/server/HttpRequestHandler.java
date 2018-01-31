package ee.testijad.mobilecpp.server;

import ee.testijad.mobilecpp.util.Config;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HttpRequestHandler implements Runnable {

    private Socket socket;

    HttpRequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Processing request on thread " + Thread.currentThread().getName());
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = new BufferedOutputStream(socket.getOutputStream());
            PrintStream printStream = new PrintStream(out);

            String request = in.readLine();
            if (request == null) return;

            boolean bufferIsEmpty = false;
            String contentType = null;
            String misc;
            while (true) {
                misc = in.readLine();
                System.out.println(String.format("[HTTP header] %s", misc));
                if (misc.startsWith("Content-Length:")) {
                   if (Integer.parseInt(misc.split(":")[1].trim()) == 0) {
                       bufferIsEmpty = true;
                   }
                }
                if (misc.startsWith("Content-Type:")) {
                    System.out.println(misc);
                    contentType = misc.split(":")[1].trim().toLowerCase();
                }

                    if (misc == null || misc.length() == 0) {
                    break;
                }
            }

            if (!request.startsWith("GET") || request.length() < 14 || !request.endsWith("HTTP/1.1")) {
                if (request.startsWith("PUT") & request.length() > 14 & request.endsWith("HTTP/1.1")) {
                    System.out.println("[HTTP server] Processing PUT request");
                    String file ="a.b";
                    if(contentType.startsWith("text/plain")) {
                        file = "digidocpp.log";
                    }
                    if(contentType.startsWith("application/json")) {
                        file = "result.json";
                    }
                    String targetFile = targetFilePath(file);
                    String fileRequest = request.substring(4, request.length() - 9).trim();
                    if (!bufferIsEmpty) {
                        System.out.println(String.format("[HTTP server] Source file: %s", file));
                        int fileSaveResult = writeFile(in, targetFile);

                        if (fileSaveResult == 200) {
                            printStream.print("HTTP/1.0 " + fileSaveResult + "\r\nDate:" + getTimeStamp() + "\r\n\r\nUpload succeeded<br>");
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
                System.out.println("[HTTP server] Processing GET request");
                String fileRequest = request.substring(4, request.length() - 9).trim();
                String fileName = "./" + Config.ZIP_FILE_DIRECTORY;
                System.out.println(String.format("[HTTP server] Zip file name: %s", fileName));
                File fileToSend = new File(fileName);
                if (fileToSend.isDirectory() && !fileRequest.endsWith("/")) {
                    printStream.print(String.format("HTTP/1.1\r\n 301 Moved Permanently<br> Location: http://%s:%s/%s/<br><br>"
                            , socket.getLocalAddress().getHostAddress()
                            , socket.getLocalPort(), fileRequest));
                } else {
                    try {
                        InputStream file = new FileInputStream(fileToSend);
                        printStream.print(String.format("HTTP/1.0 200 OK\r\nContent-Type: %s\r\nDate: %s\r\nServer: HTTP Server 1.0\r\n\r\n", selectMimeType(fileName), getTimeStamp()));

                        sendFile(file, out);
                    } catch (FileNotFoundException e) {
                        generateError(printStream, socket, "404", "Not Found", "The requested URL was not found on this server.");
                    }
                }
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[HTTP server] Processing finished on thread " + Thread.currentThread().getName());
    }

    private static void sendFile(InputStream file, OutputStream out) {
        try {
            byte[] buffer = new byte[1024*1024];
            while (file.available() > 0)
                out.write(buffer, 0, file.read(buffer));
        } catch (SocketException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
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
        String targetPath = String.format("%s/%s-%s", Config.RESULT_FILES_DIRECTORY, timePart, fileName);
        return targetPath;
    }

    private static int writeFile(BufferedReader in, String targetFilePath) {
        File myFile = new File(targetFilePath);
        BufferedWriter bufWriter;
        System.out.println(String.format("Writing output file: %s", targetFilePath));
        try {
            bufWriter = new BufferedWriter(new FileWriter(myFile));
            IOUtils.copy(in, bufWriter);
            bufWriter.flush();
            bufWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return 304;
        }
        System.out.println(String.format("[HTTP server] File %s writing succeeded", targetFilePath));
        return 200;
    }

    private static String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

}
