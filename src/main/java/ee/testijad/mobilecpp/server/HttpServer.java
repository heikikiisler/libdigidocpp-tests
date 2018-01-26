package ee.testijad.mobilecpp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class HttpServer implements Runnable {

    private ServerSocket serverSocket;
    private int port;


    public HttpServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("Starting HTTP server");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HTTP server accepting connections on port " + port);
        while (!serverSocket.isClosed()) {
            try {
                Socket connection = serverSocket.accept();
                System.out.println(connection.getInetAddress().getHostName());
                System.out.println("Accepted connection");
                Thread thread = new Thread(new HttpRequestHandler(connection));
                thread.start();
                System.out.println("Started HttpRequestHandler");
            } catch (SocketException e) {
                System.out.println("Closed serverSocket");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
