package com.example.vizsgaremek_asztali.adoptionType;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.dogs.DogApi;
import com.example.vizsgaremek_asztali.dogs.Dogs;
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

public class AdoptionTypeController extends Controller {
    @FXML
    private TextField keresesTextField;
    @FXML
    private Button typeModosit;
    @FXML
    private Button typeTorol;
    @FXML
    private TableView<AdoptionType> tipusTable;
    @FXML
    private TableColumn<AdoptionType,Integer> typeidCol;
    @FXML
    private TableColumn<AdoptionType,String> typeCol;
    private ObservableList<AdoptionType> typeLista = FXCollections.observableArrayList();

    public void initialize(){
        typeidCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        kutyakListaFeltolt();
        kereses();
    }

    public  void kutyakListaFeltolt() {
        typeModosit.setDisable(true);
        typeTorol.setDisable(true);
        try {
            typeLista.clear();
            typeLista.addAll(AdoptionTypeApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    private void kereses(){
        FilteredList<AdoptionType> filteredList = new FilteredList<>(typeLista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(type -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (type.getType().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }
                else {
                    return false;
                }

            });
        });
        SortedList<AdoptionType> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tipusTable.comparatorProperty());
        tipusTable.setItems(sortedList);
    }

    @FXML
    public void onHozzaadType(ActionEvent actionEvent) {
    }

    @FXML
    public void onModositType(ActionEvent actionEvent) {
    }

    @FXML
    public void onTypeTorol(ActionEvent actionEvent) {
    }

    @FXML
    public void onExportTypeTabla(ActionEvent actionEvent) {
    }

    @FXML
    public void onSelectType(Event event) {
    }

    @FXML
    public void onMacskakClick(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/cats/cats-view.fxml", "Macskák tábla",
                    1100, 600);
            hozzadas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onKutyakClick(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/dogs/dogs-view.fxml", "Kutyák tábla",
                    1100, 600);
            hozzadas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }

    }

    @FXML
    public void onProgramApplicationClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onEvenetClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onProgramtypeClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onAdoptionTypeClick(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/adoptionTypes/adoptionTypes-view.fxml", "Örökbefogadási Típus tábla",
                    1100, 600);
            hozzadas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onProgramHourAndDayClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onAdoptionClick(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/adoptions/adoptions-view.fxml", "Örökbefogadás tábla",
                    1100, 600);
            hozzadas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }
}
