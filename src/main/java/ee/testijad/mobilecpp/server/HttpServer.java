package ee.testijad.mobilecpp.server;

import ee.testijad.mobilecpp.util.Log;

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
        Log.info("[HTTP server] Starting ...");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.info("[HTTP server] Accepting connections on port " + port);
        while (!serverSocket.isClosed()) {
            try {
                Log.info("[HTTP server] Waiting connection");
                Socket connection = serverSocket.accept();
                Log.info("[HTTP server] Accepted connection");
                Thread thread = new Thread(new HttpRequestHandler(connection));
                thread.start();
                Log.info("[HTTP server] Started HttpRequestHandler");
            } catch (SocketException e) {
                Log.info("[HTTP server] Closed serverSocket on port " + port);
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
