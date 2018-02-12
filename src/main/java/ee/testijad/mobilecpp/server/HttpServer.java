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
        System.out.println("[HTTP server] Starting ...");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[HTTP server] Accepting connections on port " + port);
        while (!serverSocket.isClosed()) {
            try {
                System.out.println("[HTTP server] Waiting connection");
                Socket connection = serverSocket.accept();
                System.out.println("[HTTP server] Accepted connection");
                Thread thread = new Thread(new HttpRequestHandler(connection));
                thread.start();
                System.out.println("[HTTP server] Started HttpRequestHandler");
            } catch (SocketException e) {
                System.out.println("[HTTP server] Closed serverSocket on port " + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
