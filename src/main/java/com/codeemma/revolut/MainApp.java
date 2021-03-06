package com.codeemma.revolut;

import com.codeemma.revolut.account.AccountServiceImpl;
import com.codeemma.revolut.endpoint.AccountEndpointHandler;
import com.codeemma.revolut.seeder.AccountSeeder;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class MainApp {
    int serverPort = 8080;
    HttpServer server;

    public static void main(String[] args) throws IOException {
        AccountSeeder.seedData();
        MainApp mainApp = new MainApp();
        mainApp.startServer();

    }

    public void setServerPort(int port){
        serverPort = port;
    }

    public void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        createContext(server);
        server.setExecutor(Executors.newCachedThreadPool()); // set executor for multithreading
        server.start();
        System.out.println("Server started on port " + serverPort);
    }

    private static void createContext(HttpServer server) {
        AccountEndpointHandler accountEndpointHandler = new AccountEndpointHandler(new AccountServiceImpl());
        server.createContext("/api/transfer", accountEndpointHandler::handleTransfer);
        System.out.println("context created"+"/api/transfer");

        server.createContext("/api/account", accountEndpointHandler::handleGetAccount);
        System.out.println("context created"+"/api/account");

    }

    public void stopServer(){
        server.stop(1);
    }
}
