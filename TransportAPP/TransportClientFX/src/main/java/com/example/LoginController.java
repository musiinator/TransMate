package com.example;

import com.example.domain.User;
import com.example.service.TransportServiceInterface;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private AnchorPane loginAnchorPane;
    @FXML
    private TextField usernameText;
    @FXML
    private TextField passwordText;
    @FXML
    private Label loginError;

    private TransportServiceInterface service;
    private MenuController menuController;
    private Parent menuParent;
    public void setService(TransportServiceInterface service) {
        this.service = service;
    }
    public void setMenuController(MenuController menuController){
        this.menuController = menuController;
    }
    public void setParent(Parent p){
        menuParent = p;
    }

    public void verifyLogin(){
        String username = usernameText.getText();
        String password = passwordText.getText();
        User ctrlUser = new User(username, password);;
        try {
            service.login(ctrlUser, menuController);
            Stage s = (Stage) loginAnchorPane.getScene().getWindow();
            s.close();
            Stage stage = new Stage();
            Scene scene = new Scene(menuParent, 810, 515);
            stage.setTitle("TransMate");
            stage.setScene(scene);
            stage.show();
            menuController.setUsername(username);
            menuController.initializeMenu();
        } catch (Exception e) {
            loginError.setText("Incorrect username or password!");
        }
    }
}