package com.example.vizsgaremek_asztali.dogs;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class DogsController extends Controller {
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableColumn<Dogs, String> szulIdoCol;
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
    @FXML
    private Button modifiction;

    public void initialize(){
        nevCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nemCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        szulIdoCol.setCellValueFactory(new PropertyValueFactory<>("bdayFormated"));
        fajCol.setCellValueFactory(new PropertyValueFactory<>("species"));
        kulsoCol.setCellValueFactory(new PropertyValueFactory<>("external_property"));
        erdeklodesCol.setCellValueFactory(new PropertyValueFactory<>("interest"));
        adoptionIdCol.setCellValueFactory(new PropertyValueFactory<>("adoption_id"));
        virtualAdoptionIdCol.setCellValueFactory(new PropertyValueFactory<>("virtual_adoption_id"));


        kutyakListaFeltolt();
    }

    private void kutyakListaFeltolt() {
        try {
            List<Dogs> filmList = DogApi.getDogs();
            kutyakTable.getItems().clear();
            for(Dogs film: filmList){
                kutyakTable.getItems().add(film);
            }
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onHozzaadKutya(ActionEvent actionEvent) {
    }

    @FXML
    public void onModositKutya(ActionEvent actionEvent) {
    }

    @FXML
    public void onKutyaTorol(ActionEvent actionEvent) {
        int selectedIndex = kutyakTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Dogs torlendoKutya = kutyakTable.getSelectionModel().getSelectedItem();
        if (!confirm("Biztos hogy törölni szeretné az alábbi kutya adatait: "+torlendoKutya.getName())){
            return;
        }
        try {
            boolean sikeres= DogApi.kutyaTorlese(torlendoKutya.getId());
            alert(sikeres? "Sikeres törlés": "Sikertelen törlés");
            kutyakListaFeltolt();
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onPrintKutyakTabla(ActionEvent actionEvent) {
    }
}