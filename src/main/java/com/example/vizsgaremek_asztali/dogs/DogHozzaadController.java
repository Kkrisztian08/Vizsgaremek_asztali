package com.example.vizsgaremek_asztali.dogs;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DogHozzaadController extends Controller {


    @FXML
    private TextField nevInput;
    @FXML
    private DatePicker szulidoInput;
    @FXML
    private Spinner<Integer> kedvelesInput;
    @FXML
    private TextField fajInput;
    @FXML
    private TextArea leirasInput;
    @FXML
    private TextArea kultulInput;
    @FXML
    private ComboBox<String> nemInput;


    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
        String nev=nevInput.getText().trim();
        String faj=fajInput.getText().trim();
        LocalDate szuldatum=szulidoInput.getValue();
        String formazottSzuldatum;
        String kultul=kultulInput.getText().trim();
        String leiras=leirasInput.getText().trim();
        int erdeklodes=0;
        Integer  orokbefogadasid= null;
        int nemIndex = nemInput.getSelectionModel().getSelectedIndex();

       boolean hiba =false;
       StringBuilder alertBuilder=new StringBuilder();


       if (nev.isEmpty()){
           //alert("A név megadása kötelező");
           nevInput.getStyleClass().add("error");
           alertBuilder.append("A név megadása kötelező").append(System.lineSeparator());
           hiba=true;
       }


       if (nemIndex == -1){
           //alert("A nem kiválasztása köztelező");
           nemInput.getStyleClass().add("error");
           alertBuilder.append("A nem kiválasztása köztelező").append(System.lineSeparator());
           hiba=true;
       }
       String nem = nemInput.getValue();

       if (szuldatum==null){
           //alert("A dátum megadása kötelező");
           szulidoInput.getStyleClass().add("error");
           alertBuilder.append("A dátum megadása kötelező").append(System.lineSeparator());
           hiba=true;
       }



       if (faj.isEmpty()){
           //alert("A faj megadása kötelező");
           fajInput.getStyleClass().add("error");
           alertBuilder.append("A faj megadása kötelező").append(System.lineSeparator());
           hiba=true;
       }

       if (kultul.isEmpty()){
           //alert("A külső tulajdonság megadása kötelező");
           kultulInput.getStyleClass().add("error");
           alertBuilder.append("A külső tulajdonság megadása kötelező").append(System.lineSeparator());
           hiba=true;
       }


       if (leiras.isEmpty()){
           //alert("A leírás megadása kötelező");
           leirasInput.getStyleClass().add("error");
           alertBuilder.append("A leírás megadása kötelező").append(System.lineSeparator());
           hiba=true;
       }

        try {
            erdeklodes =  kedvelesInput.getValue();
        } catch (Exception ex){
            System.out.println(ex);
            alert("Az érdeklődés mértéke csak 1 és 10 közötti szám lehet");
            return;
        }
        if (erdeklodes < 1 || erdeklodes > 10) {
            alert("Az érdeklődés mértéke csak 1 és 10 közötti szám lehet");
            return;
        }

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        formazottSzuldatum=szuldatum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try {
            Dog ujKutya = new Dog(0,nev,nem,formazottSzuldatum,faj,kultul,leiras,erdeklodes,orokbefogadasid);
            Dog letrehozott = DogApi.post(ujKutya);
            if (letrehozott != null){
                alert("Sikeres hozzáadás");
            } else {
                alert("Sikeretelen hozzáadás");
            }
        } catch (Exception e) {
            hibaKiir(e);
        }
    }



    @FXML
    public void hibaVege(Event event) {
        Control control = (Control) event.getSource();
        control.getStyleClass().remove("error");
    }


    @FXML
    public void hibakMegszuntet(ActionEvent actionEvent) {
        Control control = (Control) actionEvent.getSource();
        control.getStyleClass().remove("error");
    }
}
