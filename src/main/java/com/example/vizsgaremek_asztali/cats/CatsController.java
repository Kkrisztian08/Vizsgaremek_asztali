package com.example.vizsgaremek_asztali.cats;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.dogs.Dogs;
import com.example.vizsgaremek_asztali.dogs.DogsController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    }

    @FXML
    public void onMacskaTorol(ActionEvent actionEvent) {
    }

    @FXML
    public void onHozzaadMacska(ActionEvent actionEvent) {
    }

    @FXML
    public void onExportMacskaTabla(ActionEvent actionEvent) {
    }
}
