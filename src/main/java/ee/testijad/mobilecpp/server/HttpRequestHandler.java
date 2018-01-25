package ee.testijad.mobilecpp.server;

import java.io.*;
import java.net.Socket;
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

            while (true) {
                String misc = in.readLine();
                if (misc == null || misc.length() == 0)
                    break;
            }
            if (!request.startsWith("GET") || request.length() < 14 ||
                !(request.endsWith("HTTP/1.0") || request.endsWith("HTTP/1.1"))) {
                if(request.startsWith("PUT") & request.length() > 14 & (request.endsWith("HTTP/1.0") || request.endsWith("HTTP/1.1"))) {
                    System.out.println("PUT request");
                    // TODO Add PUT action here
                    generateError(printStream, socket, "400", "Bad Request",
                            "PUT message body is missing");
                }
                generateError(printStream, socket, "400", "Bad Request",
                        "Your browser sent a request that " +
                        "this server could not understand.");
            } else {
                String fileRequest = request.substring(4, request.length() - 9).trim();
                File fileToSend = new File("./" + fileRequest);
                if (fileToSend.isDirectory() && !fileRequest.endsWith("/")) {
                    printStream.print("HTTP/1.0 301 Moved Permanently<br>" +
                               "Location: http://" +
                               socket.getLocalAddress().getHostAddress() + ":" +
                               socket.getLocalPort() + "/" + fileRequest + "/<br><br>");
                } else {
                    try {
                        InputStream file = new FileInputStream(fileToSend);
                        printStream.print("HTTP/1.0 200 OK<br>" +
                                   "Content-Type: " + guessContentType(fileRequest) + "<br>" +
                                   "Date: " + new Date() + "<br>" +
                                   "Server: HTTP Server 1.0<br><br>");
                        sendFile(file, out);
                    } catch (FileNotFoundException e) {
                        generateError(printStream, socket, "404", "Not Found",
                                "The requested URL was not found on this server.");
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
        System.out.println("Processing finished on thread " + Thread.currentThread().getName());
    }

    private static void sendFile(InputStream file, OutputStream out) {
        try {
            byte[] buffer = new byte[2048];
            while (file.available() > 0)
                out.write(buffer, 0, file.read(buffer));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String guessContentType(String path) {
        if (path.endsWith(".zip")) {
            return "application/zip";
        }
        else {
            return "text/plain";
        }
    }

    private void generateError(PrintStream printStream, Socket connection,
                                      String code, String title, String message) {
        printStream.print("HTTP/1.0 " + code + " " + title + "\r\n" +
                          "\r\n" +
                          "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" +
                          "<TITLE>" + code + " " + title + "</TITLE>\r\n" +
                          "</HEAD><BODY>\r\n" +
                          "<H1>" + title + "</H1>\r\n" + message + "<P>\r\n" +
                          "<HR><ADDRESS>FileServer 1.0 at " +
                          connection.getLocalAddress().getHostName() +
                          " Port " + connection.getLocalPort() + "</ADDRESS>\r\n" +
                          "</BODY></HTML>\r\n");
    }

}
