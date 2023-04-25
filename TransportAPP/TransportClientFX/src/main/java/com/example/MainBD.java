package com.example;

import com.example.rpcprotocol.TransportServiceRpcProxy;
import com.example.service.Properties1;
import com.example.service.TransportServiceInterface;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class MainBD extends Application {

    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";
    private static Stage stg;

    /*Properties1 props = new Properties1();
    java.util.Properties properties = props.getProperties(new java.util.Properties());

    UserRepoInterface repoUser = new UserDataBase(properties);
    CursaRepoInterface repoCursa = new CursaDataBase(properties);
    RezervareRepoInterface repoRezervare = new RezervareDataBase(properties);
    TransportServiceInterface service = new Service(repoUser, repoCursa, repoRezervare);*/


    @Override
    public void start(Stage primaryStage) throws IOException {

        Properties clientProps = new Properties();
        try {
            clientProps.load(MainBD.class.getResourceAsStream("/transportclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find transportclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("com.example.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("com.example.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        TransportServiceInterface service = new TransportServiceRpcProxy(serverIP, serverPort);

        stg = primaryStage;
        primaryStage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(MainBD.class.getResource("/login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setService(service);

        FXMLLoader menuLoader = new FXMLLoader(MainBD.class.getResource("/menu.fxml"));
        Parent menuRoot = menuLoader.load();
        MenuController menuController = menuLoader.getController();
        menuController.setService(service);

        loginController.setMenuController(menuController);
        loginController.setParent(menuRoot);


        primaryStage.setTitle("TransportApp");
        primaryStage.setScene(new Scene(root, 643, 364));
        primaryStage.show();

    }
    public static void main(String[] args){
        Application.launch();
    }
}
