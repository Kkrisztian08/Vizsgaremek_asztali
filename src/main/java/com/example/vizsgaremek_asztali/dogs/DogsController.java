package com.example.vizsgaremek_asztali.dogs;

import com.example.vizsgaremek_asztali.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class DogsController extends Controller {
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableColumn<Dogs, Date> szulIdoCol;
    @FXML
    private TableColumn<Dogs, Integer> adoptionIdCol;
    @FXML
    private Label keresesLabel;
    @FXML
    private TableColumn<Dogs,String> fajCol;
    @FXML
    private TableColumn<Dogs,String> kulsoCol;
    @FXML
    private TableView<Dogs> kutyakTable;
    @FXML
    private Label tablaNevLabel;
    @FXML
    private TableColumn<Dogs, Integer> virtualAdoptionIdCol;
    @FXML
    private TableColumn<Dogs,String> nevCol;
    @FXML
    private TableColumn<Dogs,String> nemCol;
    @FXML
    private TableColumn<Dogs, Integer> erdeklodesCol;

    public void initialize(){
        nevCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        //a tárolt objektumban egy getCim függvényt fog keresni.
        nemCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        szulIdoCol.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        fajCol.setCellValueFactory(new PropertyValueFactory<>("species"));
        kulsoCol.setCellValueFactory(new PropertyValueFactory<>("external_property"));
        erdeklodesCol.setCellValueFactory(new PropertyValueFactory<>("interest"));
        adoptionIdCol.setCellValueFactory(new PropertyValueFactory<>("adoption_id"));
        virtualAdoptionIdCol.setCellValueFactory(new PropertyValueFactory<>("virtual_adoption_id"));
        KutyakListaFeltolt();
    }

    private void KutyakListaFeltolt() {
        try {
            List<Dogs> filmList = DogsApi.getDogs();
            kutyakTable.getItems().clear();
            for(Dogs film: filmList){
                kutyakTable.getItems().add(film);
            }
        } catch (IOException e) {
            hibaKiir(e);
        }
    }
}