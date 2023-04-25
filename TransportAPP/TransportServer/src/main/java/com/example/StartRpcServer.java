package com.example;

import com.example.repository.Interfaces.CursaRepoInterface;
import com.example.repository.Interfaces.RezervareRepoInterface;
import com.example.repository.Interfaces.UserRepoInterface;
import com.example.repository.database.CursaDataBase;
import com.example.repository.database.RezervareDataBase;
import com.example.repository.database.UserDataBase;
import com.example.service.TransportServiceInterface;
import com.example.utils.AbstractServer;
import com.example.utils.TransportObjectConcurrentServer;
import com.example.utils.TransportRpcConcurrentServer;

import java.io.FileReader;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.Properties;

public class StartRpcServer {
    private static final int defaultPort = 55555;
    public static void main(String[] args){
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/transportserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch(IOException e){
            System.err.println("Cannot find transportserver.properties " + e);
            return;
        }
        UserRepoInterface userRepo = new UserDataBase(serverProps);
        CursaRepoInterface cursaRepo = new CursaDataBase(serverProps);
        RezervareRepoInterface rezervareRepo = new RezervareDataBase(serverProps);

        TransportServiceInterface service = new Service(userRepo, cursaRepo, rezervareRepo);
        int transportServerPort = defaultPort;
        try {
            transportServerPort = Integer.parseInt(serverProps.getProperty("com.example.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong Port Number " + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + transportServerPort);
        AbstractServer server = new TransportRpcConcurrentServer(transportServerPort, service);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
