package ee.testijad.mobilecpp.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class HttpServer {

    public static void main(String[] args) throws InterruptedException {
    //    HttpServer server = new HttpServer(Utils.getFreePort());
        HttpServer server = new HttpServer(60974);
        server.startServer();

      //  server.stopServer();
    }

    private ServerSocket serverSocket = null;
    private Thread thread;
    private Runnable task;
    private int port;
    private static String  wwwhome = ".";


    public HttpServer(int port) {
        this.port = port;
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not start server: " + e);
            System.exit(-1);
        }
        System.out.println("FileServer accepting connections on port " + port);
        try {
            Socket connection = serverSocket.accept();
            task = new Runnable() {
                @Override
                public void run() {
                    HandleRequest(connection);
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread = new Thread(task);
        thread.start();
    }

    private void HandleRequest(Socket s) {
        System.out.println("Processing request");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            OutputStream out = new BufferedOutputStream(s.getOutputStream());
            PrintStream pout = new PrintStream(out);

            String request = in.readLine();

            log(s, request);
            while (true) {
                String misc = in.readLine();
                if (misc == null || misc.length() == 0)
                    break;
            }
            if (!request.startsWith("GET") || request.length() < 14 ||
                    !(request.endsWith("HTTP/1.0") || request.endsWith("HTTP/1.1"))) {
                if(request.startsWith("PUT") & request.length() > 14 & (request.endsWith("HTTP/1.0") || request.endsWith("HTTP/1.1"))) {
                    System.out.println("Processing PUT request");
                    String filePath = request.substring(5, request.length() - 9).trim();
                    // TODO Add PUT action here


                    errorReport(pout, s, "400", "Bad Request",
                            "PUT message body is missing");
                }
                // bad request
                errorReport(pout, s, "400", "Bad Request",
                        "Your browser sent a request that " +
                                "this server could not understand.");
            } else {
                String req = request.substring(4, request.length() - 9).trim();
                if (req.indexOf("..") != -1 ||
                        req.indexOf("/.ht") != -1 || req.endsWith("~")) {

                    errorReport(pout, s, "403", "Forbidden",
                            "You don't have permission to access the requested URL.");
                } else {
                    String path = wwwhome + "/" + req;
                    File f = new File(path);
                    if (f.isDirectory() && !path.endsWith("/")) {
                        // redirect browser if referring to directory without final '/'
                        pout.print("HTTP/1.0 301 Moved Permanently\r\n" +
                                "Location: http://" +
                                s.getLocalAddress().getHostAddress() + ":" +
                                s.getLocalPort() + "/" + req + "/\r\n\r\n");
                        log(s, "301 Moved Permanently");
                    } else {
                        if (f.isDirectory()) {
                            // if directory, implicitly add 'index.html'
                            path = path + "index.html";
                            f = new File(path);
                        }
                        try {
                            // send file
                            InputStream file = new FileInputStream(f);
                            pout.print("HTTP/1.0 200 OK\r\n" +
                                    "Content-Type: " + guessContentType(path) + "\r\n" +
                                    "Date: " + new Date() + "\r\n" +
                                    "Server: FileServer 1.0\r\n\r\n");
                            sendFile(file, out); // send raw file
                            log(s, "200 OK");
                        } catch (FileNotFoundException e) {
                            // file not found
                            errorReport(pout, s, "404", "Not Found",
                                    "The requested URL was not found on this server.");
                        }
                    }
                }
            }
            out.flush();
        } catch (IOException e) {
            System.err.println(e);
        }
        try {
            if (s != null) s.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        System.out.println("Processiong finished.");
        thread.interrupt();
    }


    private void receiveFile(String fileName) {

    }

    private static void sendFile(InputStream file, OutputStream out) {
        try {
            byte[] buffer = new byte[1000];
            while (file.available() > 0)
                out.write(buffer, 0, file.read(buffer));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static void log(Socket connection, String msg) {
        System.err.println(new Date() + " [" + connection.getInetAddress().getHostAddress() +
                ":" + connection.getPort() + "] " + msg);
    }

    private static String guessContentType(String path)
    {
        if (path.endsWith(".html") || path.endsWith(".htm"))
            return "text/html";
        else if (path.endsWith(".txt") || path.endsWith(".java"))
            return "text/plain";
        else if (path.endsWith(".zip"))
            return "application/zip";
        else
            return "text/plain";
    }

    private static void errorReport(PrintStream pout, Socket connection,
                                    String code, String title, String msg)
    {
        pout.print("HTTP/1.0 " + code + " " + title + "\r\n" +
                "\r\n" +
                "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" +
                "<TITLE>" + code + " " + title + "</TITLE>\r\n" +
                "</HEAD><BODY>\r\n" +
                "<H1>" + title + "</H1>\r\n" + msg + "<P>\r\n" +
                "<HR><ADDRESS>FileServer 1.0 at " +
                connection.getLocalAddress().getHostName() +
                " Port " + connection.getLocalPort() + "</ADDRESS>\r\n" +
                "</BODY></HTML>\r\n");
        log(connection, code + " " + title);
    }

    public void stopServer() {
        System.out.println("Stopping server");
        thread.interrupt();
    }
}
