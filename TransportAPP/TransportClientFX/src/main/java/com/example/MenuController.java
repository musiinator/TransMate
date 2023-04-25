package com.example;

import com.example.domain.*;
import com.example.service.TransportObserverInterface;
import com.example.service.TransportServiceInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MenuController implements TransportObserverInterface {

    private TransportServiceInterface service;
    public void setService(TransportServiceInterface service) {
        this.service = service;
    }
    @FXML
    ObservableList<CursaDTO> curse = FXCollections.observableArrayList();
    ObservableList<RezervareDTO> rezervari = FXCollections.observableArrayList();

    private Integer idLocDisponibil = null;

    private final Long idSelectatRezervare = null;
    @FXML
    private Label usernameLabel;

    @FXML
    private TableView<CursaDTO> curseTableView;

    @FXML
    private TableView<RezervareDTO> rezervariTableView;

    @FXML
    private TableColumn<Long, CursaDTO> idCursaColumn;

    @FXML
    private TableColumn<String, CursaDTO> destinatieColumn;

    @FXML
    private TableColumn<String, CursaDTO> dataOraPlecareColumn;

    @FXML
    private TableColumn<Integer, CursaDTO> locuriDisponibileColumn;

    @FXML
    private TableColumn<Long, RezervareDTO> idRezervareColumn;

    @FXML
    private TableColumn<String, RezervareDTO> numeClientColumn;

    @FXML
    private TableColumn<Integer, RezervareDTO> nrLocColumn;

    @FXML
    private TableColumn<Long, RezervareDTO> idCursaRezervareColumn;

    @FXML
    private TextField destinatieText;

    @FXML
    private TextField dataOraPlecareText;

    @FXML
    private TextField locuriDisponibileText;

    @FXML
    private TextField numeClientText;

    @FXML
    private TextField numarLocuriText;

    @FXML
    private TextField idCursaText;

    @FXML
    private Button cautareCursaButton;

    @FXML
    private Button addRezervareButton;

    @FXML
    private Button deleteRezervareButton;

    @FXML
    private Button updateRezervareButton;
    public User loggedUser;

    public void setUsername(String username) {
        usernameLabel.setText(username);
        try{
            loggedUser = service.findUserByUsername(username);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(loggedUser);
    }

    public void initializeMenu(){
        idCursaColumn.setCellValueFactory(new PropertyValueFactory<>("idCursaDTO"));
        destinatieColumn.setCellValueFactory(new PropertyValueFactory<>("destinatieDTO"));
        dataOraPlecareColumn.setCellValueFactory(new PropertyValueFactory<>("dataOraPlecareDTO"));
        locuriDisponibileColumn.setCellValueFactory(new PropertyValueFactory<>("nrLocuriDisponibileDTO"));
        idRezervareColumn.setCellValueFactory(new PropertyValueFactory<>("idRezervareDTO"));
        numeClientColumn.setCellValueFactory(new PropertyValueFactory<>("numeClientDTO"));
        nrLocColumn.setCellValueFactory(new PropertyValueFactory<>("nrLocDTO"));
        idCursaRezervareColumn.setCellValueFactory(new PropertyValueFactory<>("idCursaDTO"));
        try {
            for (Cursa c : service.findAllCursa()) {
                CursaDTO cursaDTO = new CursaDTO(c.getId(), c.getDestinatie(), c.getDataOraPlecare().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), c.getNrLocuriDisponibile());
                curse.add(cursaDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        curseTableView.setItems(curse);
    }

    public void refreshCurse(){
        curse.clear();
        try {
            for (Cursa c : service.findAllCursa()) {
                CursaDTO cursaDTO = new CursaDTO(c.getId(), c.getDestinatie(), c.getDataOraPlecare().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), c.getNrLocuriDisponibile());
                curse.add(cursaDTO);
            }
            curseTableView.setItems(curse);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeStage(){
        try{
            System.out.println(loggedUser.getUsername());
            service.logout(loggedUser, this);
            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Something went wrong!");
            alert.setContentText("Please try again!");
            alert.showAndWait();
        }

    }

    public void cautareCursa() {
        rezervari.clear();
        String destinatie = destinatieText.getText();
        String dataOraPlecare = dataOraPlecareText.getText();
        Long idCursaGasita = null;
        if (destinatie.equals("") || dataOraPlecare.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Nu ati introdus toate datele!");
            alert.setContentText("Va rugam sa completati destinatia, respectiv data si ora plecarii!");
            alert.showAndWait();
        } else {
            try {
                for (Cursa cursa : service.findAllCursa()) {
                    if (cursa.getDestinatie().equals(destinatieText.getText()) && cursa.getDataOraPlecare().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).equals(dataOraPlecareText.getText())) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Cursa gasita");
                        alert.setHeaderText("Cursa a fost gasita!");
                        alert.setContentText("Cursa cu destinatia " + cursa.getDestinatie() + " pleaca la " + cursa.getDataOraPlecare().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + " si are " + cursa.getNrLocuriDisponibile() + " locuri disponibile!");
                        alert.showAndWait();
                        locuriDisponibileText.setText(cursa.getNrLocuriDisponibile().toString());
                        idCursaGasita = cursa.getId();
                        idCursaText.setText(idCursaGasita.toString());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (idCursaGasita != null) {
                    idLocDisponibil = 1;
                    for (Rezervare rezervare : service.findRezervareByIdCursa(idCursaGasita)) {
                        int aux = rezervare.getNrLocuri();
                        while (aux != 0) {
                            RezervareDTO rezervareDTO = new RezervareDTO(rezervare.getId(), rezervare.getNumeClient(), idLocDisponibil, rezervare.getIdCursa());
                            rezervari.add(rezervareDTO);
                            idLocDisponibil++;
                            aux--;
                        }
                    }
                    rezervariTableView.setItems(rezervari);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cursa negasita");
                    alert.setHeaderText("Cursa nu a fost gasita!");
                    alert.setContentText("Cursa spre " + destinatieText.getText() + " nu a fost gasita!");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void populareCampuriRezervari() {
        if(rezervariTableView.getSelectionModel().getSelectedItem() != null) {
            numeClientText.setText(rezervariTableView.getSelectionModel().getSelectedItem().getNumeClientDTO());
            idCursaText.setText(rezervariTableView.getSelectionModel().getSelectedItem().getIdCursaDTO().toString());
        }
    }

    public void populareCampuriCurse(){
        if(curseTableView.getSelectionModel().getSelectedItem() != null) {
            destinatieText.setText(curseTableView.getSelectionModel().getSelectedItem().getDestinatieDTO());
            dataOraPlecareText.setText(curseTableView.getSelectionModel().getSelectedItem().getDataOraPlecareDTO());
            locuriDisponibileText.setText(curseTableView.getSelectionModel().getSelectedItem().getNrLocuriDisponibileDTO().toString());
        }
    }

    public void addRezervare(){
        Rezervare rezervare = null;
        String numeClient = null;
        Integer numarLocuri = null;
        Long idCursa = null;
        Long maxID = -1L;

        try {
            numeClient = numeClientText.getText();
            numarLocuri = Integer.parseInt(numarLocuriText.getText());
            idCursa = Long.parseLong(idCursaText.getText());
            maxID = 0L;
            rezervare = new Rezervare(numeClient, numarLocuri, idCursa);
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Nu ati introdus toate datele!");
            alert.setContentText("Va rugam sa completati toate campurile!");
            alert.showAndWait();
        }
        try {
            for (Rezervare r : service.findAllRezervare()) {
                if (r.getId() > maxID) {
                    maxID = r.getId();
                }
            }
            if(rezervare != null)
                rezervare.setId(maxID + 1);
        }catch (Exception e){
            e.printStackTrace();
        }
        Cursa cursa = null;
        try {
            if(rezervare != null) {
                for (Cursa c : service.findAllCursa()) {
                    if (c.getId().equals(rezervare.getIdCursa())) {
                        cursa = c;
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (cursa != null) {
                if (cursa.getNrLocuriDisponibile() - rezervare.getNrLocuri() >= 0) {
                    idLocDisponibil = 19 - cursa.getNrLocuriDisponibile();
                    locuriDisponibileText.setText(cursa.getNrLocuriDisponibile().toString());
                    service.saveRezervare(rezervare);
                    int aux = rezervare.getNrLocuri();
                    while (aux != 0) {
                        RezervareDTO rezervareDTO = new RezervareDTO(rezervare.getId(), rezervare.getNumeClient(), idLocDisponibil, rezervare.getIdCursa());
                        rezervari.add(rezervareDTO);
                        idLocDisponibil++;
                        aux--;
                    }
                    rezervariTableView.setItems(rezervari);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Nu sunt suficiente locuri disponibile!");
                    alert.setContentText("Incercati sa selectati mai putine locuri!");
                    alert.showAndWait();
                }
                numeClientText.clear();
                numarLocuriText.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void rezervareReceived(Rezervare rezervare){
        Platform.runLater(() -> {
            try {
                curseTableView.getItems().clear();
                refreshCurse();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

}
