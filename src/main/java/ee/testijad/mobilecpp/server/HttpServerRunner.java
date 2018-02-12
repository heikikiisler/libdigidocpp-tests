package ee.testijad.mobilecpp.server;

import ee.testijad.mobilecpp.util.Utils;

public class HttpServerRunner {
    public static void main(String[] args) {
        int httpServerPort = 60001;
        HttpServer httpServer = new HttpServer(httpServerPort);
        String baseUrl = Utils.getBaseUrl(httpServerPort);
        System.out.println(String.format("\r\n[HttpServerRunner] Service url: %ssync\r\n", baseUrl));
        System.out.println(String.format("[HttpServerRunner] Diagnostic url: %sdiag\r\n", baseUrl));
        System.out.println("[HttpServerRunner] If default IP address is not visible for Android device, please add correct IP address to properties.conf file. \r\n");
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
