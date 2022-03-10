package com.example.vizsgaremek_asztali.dogs;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class HozzaadController extends Controller {


    @FXML
    private TextField nevInput;
    @FXML
    private DatePicker szulidoInput;
    @FXML
    private Spinner<Integer> kedvelésInput;
    @FXML
    private TextField fajInput;
    @FXML
    private TextArea leirasInput;
    @FXML
    private TextArea kultulInput;
    @FXML
    private ComboBox<String> nemInput;
    @FXML
    private ComboBox<Integer> orokbefogadasInput;


   @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
        String nev=nevInput.getText().trim();
        String faj=fajInput.getText().trim();
        LocalDate szuldatum=szulidoInput.getValue();
        String formazottSzuldatum;
        String kultul=kultulInput.getText().trim();
        String leiras=leirasInput.getText().trim();
        int erdeklodes=0;
        Integer  orokbefogadasid= null; //Integer osztály kell és kezelni kell az api hivásban hogy ha beállitom az alap értéket pl -1-re akkor azt az SQL-ben NULL-nak vegye
        int nemIndex = nemInput.getSelectionModel().getSelectedIndex();
        //String nem=nemInput.getValue();

       
       if (nev.isEmpty()){
           alert("A név megadása kötelező");
           nevInput.getStyleClass().add("error");
           return;
       }else {
           nevInput.getStyleClass().remove("error");
       }


       if (nemIndex == -1){
           alert("A nem kiválasztása köztelező");
           return;
       }
       String nem = nemInput.getValue();

       if (szuldatum==null){
           alert("A dátum megadása kötelező");
           return;
       }
       formazottSzuldatum=szuldatum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


       if (faj.isEmpty()){
           alert("A faj megadása kötelező");
           return;
       }

       if (kultul.isEmpty()){
           alert("A külső tulajdonság megadása kötelező");
           return;
       }
       if (formazottSzuldatum.isEmpty()){
           alert("A születési dátum megadása kötelező");
           return;
       }

       if (leiras.isEmpty()){
           alert("A leírás megadása kötelező");
           return;
       }

        try {
            erdeklodes =  kedvelésInput.getValue();
        } catch (Exception ex){
            System.out.println(ex);
            alert("Az kedmelés mértéke csak 1 és 10 közötti szám lehet");
            return;
        }
        if (erdeklodes < 1 || erdeklodes > 10) {
            alert("Az kedmelés mértéke csak 1 és 10 közötti szám lehet");
            return;
        }



        try {
            Dogs ujKutya = new Dogs(0,nev,nem,formazottSzuldatum,faj,kultul,leiras,erdeklodes,orokbefogadasid);
            Dogs letrehozott = DogApi.post(ujKutya);
            if (letrehozott != null){
                alert("Sikeres hozzáadás");
            } else {
                alert("Sikeretelen hozzáadás");
            }
        } catch (Exception e) {
            hibaKiir(e);
        }
    }


}
