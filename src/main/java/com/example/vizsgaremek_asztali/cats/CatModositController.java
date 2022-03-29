package com.example.vizsgaremek_asztali.cats;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CatModositController extends Controller {
    @FXML
    private TextField nevInput;
    @FXML
    private DatePicker szulidoInput;
    @FXML
    private TextArea leirasInput;
    @FXML
    private TextArea kultulInput;
    @FXML
    private Spinner<Integer> kedvelesInput;
    @FXML
    private ComboBox<String> nemInput;
    @FXML
    private ComboBox<Integer> orokbefogadasInput;
    private Cat modositando;

    @FXML
    public void onCatsModositas(ActionEvent actionEvent) {
        String nev=nevInput.getText().trim();
        LocalDate szuldatum=szulidoInput.getValue();
        String formazottSzuldatum;
        String kultul=kultulInput.getText().trim();
        String leiras=leirasInput.getText().trim();
        int erdeklodes=0;
        int nemIndex = nemInput.getSelectionModel().getSelectedIndex();
        Integer orobefogadasIndex=orokbefogadasInput.getSelectionModel().getSelectedIndex();// ez majd akkor kell ha már megvan adoption osztáyl

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
        }catch (NullPointerException ex){
            //alert("A hossz megadása kötelező");
            kedvelesInput.getStyleClass().add("error");
            alertBuilder.append("Az érdeklődés mértékének megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }
        catch (Exception ex){
            System.out.println(ex);
            //alert("Az kedvelés mértéke csak 1 és 10 közötti szám lehet");
            kedvelesInput.getStyleClass().add("error");
            alertBuilder.append("Az érdeklődés mértéke csak 1 és 10 közötti szám lehet").append(System.lineSeparator());
            hiba=true;
        }
        if (erdeklodes < 1 || erdeklodes > 10) {
            //alert("Az kedvelés mértéke csak 1 és 10 közötti szám lehet");
            kedvelesInput.getStyleClass().add("error");
            alertBuilder.append("Az érdeklődés mértéke csak 1 és 10 közötti szám lehet").append(System.lineSeparator());
            hiba=true;
        }

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        Integer orokbefogadasId = orokbefogadasInput.getValue();
        formazottSzuldatum=szuldatum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        modositando.setName(nev);
        modositando.setLikely_bday(formazottSzuldatum);
        modositando.setGender(nem);
        modositando.setExternal_property(kultul);
        modositando.setDescription(leiras);
        modositando.setInterest(erdeklodes);
        modositando.setAdoption_id(orokbefogadasId);

        try {
            Cat modositott= CatApi.put(modositando);
            if (modositott !=null){
                alertWait("Sikeres módosítás");
                this.stage.close();
            } else {
                alert("Sikertelen módosítás");
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public Cat getModositando() {
        return modositando;
    }

    public void setModositando(Cat modositando) {
        this.modositando = modositando;
        ertekekBeallitasa();
    }

    private void ertekekBeallitasa() {
        nevInput.setText(modositando.getName());
        kultulInput.setText(modositando.getExternal_property());
        leirasInput.setText(modositando.getDescription());
        kedvelesInput.getValueFactory().setValue(modositando.getInterest());
        nemInput.setValue(modositando.getGender());
        orokbefogadasInput.setValue(modositando.getAdoption_id());
        LocalDate datum=LocalDate.parse(modositando.getLikely_bday());
        szulidoInput.setValue(datum);
    }



}
