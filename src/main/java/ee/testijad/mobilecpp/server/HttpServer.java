package ee.testijad.mobilecpp.server;

import ee.testijad.mobilecpp.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public static void main(String[] args) throws InterruptedException {
        HttpServer server = new HttpServer(Utils.getFreePort());
        server.startServer();
        System.out.println("Started server");
        Thread.sleep(5000);
        server.stopServer();
    }

    private ServerSocket socket = null;
    private Thread thread;
    private Runnable task;
    private int port;


    public HttpServer(int port) {
        this.port = port;
    }

    private void startServer() {
        try {
            socket = new ServerSocket(port);
            thread = new Thread(task);
            thread.run();
        } catch (IOException e) {
            System.err.println("Could not start server: " + e);
            System.exit(-1);
        }
        System.out.println("FileServer accepting connections on port " + port);
        Socket connection;
        try {
            connection = socket.accept();
            task = new Runnable() {
                @Override
                public void run() {
                    HandleRequest(connection);
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void HandleRequest(Socket s) {
        BufferedReader in;
        PrintWriter out;
        String request;

        try {
            s.close();
        } catch (IOException e) {
            System.out.println("Failed respond to client request: " + e.getMessage());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void receiveFile(String fileName) {

    }

    private void sendFile(String fileName) {

    }

    public void stopServer() {
        System.out.println("Stopping server");
        thread.interrupt();
    }
}
