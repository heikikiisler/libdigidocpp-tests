package ee.testijad.mobilecpp.server;

import ee.testijad.mobilecpp.util.Log;
import ee.testijad.mobilecpp.util.Utils;

public class HttpServerRunner {

    public static void main(String[] args) {
        int httpServerPort = 60001;
        HttpServer httpServer = new HttpServer(httpServerPort);
        String baseUrl = Utils.getBaseUrl(httpServerPort);
        Log.info(String.format("\r\n[HttpServerRunner] Service url: %ssync\r\n", baseUrl));
        Log.info(String.format("[HttpServerRunner] Diagnostic url: %sdiag\r\n", baseUrl));
        Log.info("[HttpServerRunner] If default IP address is not visible for Android device, please add correct IP address to properties.conf file. \r\n");
        Utils.myIPAddress();
        Thread thread = new Thread(httpServer);
        thread.start();

        try {
            Thread.sleep(90000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            httpServer.stopServer();
        }
    }
}
