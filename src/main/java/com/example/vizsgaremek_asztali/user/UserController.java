package com.example.vizsgaremek_asztali.user;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.programType.ProgramType;
import com.example.vizsgaremek_asztali.programType.ProgramTypeApi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class UserController extends Controller {
    @FXML
    private TextArea leirasKulTulTextArea;
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableColumn<User,String> szulIdoCol;
    @FXML
    private TableView<User> kutyakTable;
    @FXML
    private TableColumn<User,Integer> adminCol;
    @FXML
    private Button userModosit;
    @FXML
    private TableColumn<User,String> cimCol;
    @FXML
    private TableColumn<User,Integer> idCol;
    @FXML
    private Button userTorol;
    @FXML
    private TableColumn<User,String> emailCol;
    @FXML
    private TableColumn<User,String> nevCol;
    @FXML
    private TableColumn<User,String> jelszoCol;
    @FXML
    private TableColumn<User,String> telefonCol;
    private ObservableList<User> userLista = FXCollections.observableArrayList();


    /*public void initialize(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        megnevezesCol.setCellValueFactory(new PropertyValueFactory<>("megnevezes"));
        pTypeListaFeltolt();
        kereses();
    }

    public  void pTypeListaFeltolt() {
        userModosit.setDisable(true);
        userTorol.setDisable(true);
        try {
            userLista.clear();
            userLista.addAll(UserApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    private void kereses(){
        FilteredList<User> filteredList = new FilteredList<>(userLista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(users -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (users.getName().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }else if (users.getAddress().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }else if (users.getPhone_number().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }else if (users.getBirthday().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }else if (users.getEmail().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }
                else {
                    return false;
                }
            });
        });
        SortedList<ProgramType> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(pTypeTable.comparatorProperty());
        pTypeTable.setItems(sortedList);
    }

    @FXML
    public void onUserTorol(ActionEvent actionEvent) {
    }

    @FXML
    public void onExit(ActionEvent actionEvent) {
    }

    @FXML
    public void onProgramApplicationClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onKutyakClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onSelectUser(Event event) {
    }

    @FXML
    public void onHozzaadUser(ActionEvent actionEvent) {
    }

    @FXML
    public void onMacskakClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onProgramtypeClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onAdoptionTypeClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onModositUser(ActionEvent actionEvent) {
    }

    @FXML
    public void onProgramHourAndDayClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onUsersClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onEventClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onExportTabla(ActionEvent actionEvent) {
    }

    @FXML
    public void onAdoptionClick(ActionEvent actionEvent) {
    }*/
}
