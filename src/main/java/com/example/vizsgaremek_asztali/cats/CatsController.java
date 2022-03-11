package com.example.vizsgaremek_asztali.cats;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.dogs.Dogs;
import com.example.vizsgaremek_asztali.dogs.DogsController;
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

public class CatsController extends Controller {
    @FXML
    private TextArea leirasKulTulTextArea;
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableColumn<Cats,String> szulIdoCol;
    @FXML
    private TableColumn<Cats,Integer> adoptionIdCol;
    @FXML
    private Button catModosit;
    @FXML
    private TableColumn<Cats,String> kulsoCol;
    @FXML
    private Button catTorol;
    @FXML
    private TableView<Cats> macskakTable;
    @FXML
    private TableColumn<Cats,Integer> idCol;
    @FXML
    private TableColumn<Cats,String>  leirasCol;
    @FXML
    private TableColumn<Cats,String>  nevCol;
    @FXML
    private TableColumn<Cats,String>  nemCol;
    @FXML
    private TableColumn<Cats,Integer> erdeklodesCol;
    private DogsController dogsController;
    private ObservableList<Cats> macskakLista = FXCollections.observableArrayList();


    public void initialize(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nevCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nemCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        szulIdoCol.setCellValueFactory(new PropertyValueFactory<>("likely_bday"));
        kulsoCol.setCellValueFactory(new PropertyValueFactory<>("external_property"));
        leirasCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        erdeklodesCol.setCellValueFactory(new PropertyValueFactory<>("interest"));
        adoptionIdCol.setCellValueFactory(new PropertyValueFactory<>("adoption_id"));
        macskakListaFeltolt();
        //kereses();
    }

    public void macskakListaFeltolt() {
        catTorol.setDisable(true);
        catModosit.setDisable(true);
        try {
            macskakLista.clear();
            macskakLista.addAll(CatApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }
    private void kereses() {
        FilteredList<Cats> filteredList = new FilteredList<>(macskakLista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(dog -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (dog.getName().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }
                else if(dog.getGender().toLowerCase().indexOf(kereses) > -1){
                    return true;
                }
                else if(dog.getLikely_bday().toLowerCase().indexOf(kereses) > -1){
                    return true;
                }
                else if(dog.getExternal_property().toLowerCase().indexOf(kereses) > -1){
                    return true;
                }
                else {
                    return false;
                }

            });
        });
        SortedList<Cats> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(macskakTable.comparatorProperty());
        macskakTable.setItems(sortedList);
    }

    @FXML
    public void onMacskakClick(ActionEvent actionEvent) {

    }

    @FXML
    public void onModositMacska(ActionEvent actionEvent) {
    }

    @FXML
    public void onSelectCat(Event event) {
    }

    @FXML
    public void onKutyakClick(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/dogs/dogs-view.fxml", "Kutyák tábla",
                    1100, 600);
            //hozzadas.getStage().setOnCloseRequest(event -> dogsController.kutyakListaFeltolt());
            hozzadas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onMacskaTorol(ActionEvent actionEvent) {
    }

    @FXML
    public void onHozzaadMacska(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/cats/hozzaad-view.fxml", "Macska hozzáadása",
                    600, 400);
            hozzadas.getStage().setOnCloseRequest(event -> macskakListaFeltolt());
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onExportMacskaTabla(ActionEvent actionEvent) {
    }
}
