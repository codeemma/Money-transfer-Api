package com.codeemma.revolut;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class MainApp {

    public static void main(String[] args) throws IOException {
        int serverPort = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        createContext(server);
        server.setExecutor(Executors.newCachedThreadPool()); // set executor for multithreading
        server.start();
        System.out.println("Server started on port "+serverPort);

        }

    private static void createContext(HttpServer server) {
        server.createContext("/api/hello", (var exchange) -> {
            String threadName  = Thread.currentThread().getName();
            String respText = "Hello! "+threadName;
            exchange.sendResponseHeaders(200, respText.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.getBytes());
            output.flush();
            exchange.close();
        });
        System.out.println("context created"+"/api/hello");

    }
}
