package com.example.vizsgaremek_asztali.dogs;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.adoption.Adoption;
import com.example.vizsgaremek_asztali.adoption.AdoptionApi;
import com.example.vizsgaremek_asztali.adoptionType.AdoptionType;
import com.example.vizsgaremek_asztali.adoptionType.AdoptionTypeApi;
import com.example.vizsgaremek_asztali.user.User;
import com.example.vizsgaremek_asztali.user.UserApi;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DogModositController extends Controller {
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
    private ComboBox<Adoption> orokbefogadasInput;
    private Dog modositando;
    private List<Adoption> adoptionList;


    public void initialize(){
        adoptionList = new ArrayList<>();
        try {
            adoptionList= AdoptionApi.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Adoption adoption : adoptionList){
            orokbefogadasInput.getItems().add(adoption);
        }
        orokbefogadasInput.getSelectionModel();
    }

    @FXML
    public void onModositas(ActionEvent actionEvent) {
        String nev=nevInput.getText().trim();
        String faj=fajInput.getText().trim();
        LocalDate szuldatum=szulidoInput.getValue();
        String formazottSzuldatum;
        String kultul=kultulInput.getText().trim();
        String leiras=leirasInput.getText().trim();
        int erdeklodes=0;
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


        Integer orobefogadasIndex=orokbefogadasInput.getSelectionModel().getSelectedItem().getId();
        Integer orobefogadasid=null;

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        formazottSzuldatum=szuldatum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        modositando.setName(nev);
        modositando.setSpecies(faj);
        modositando.setLikely_bday(formazottSzuldatum);
        modositando.setGender(nem);
        modositando.setExternal_property(kultul);
        modositando.setDescription(leiras);
        modositando.setInterest(erdeklodes);
        if (orobefogadasIndex == -1){
            modositando.setAdoption_id(orobefogadasid);
        }else {
            modositando.setAdoption_id(orobefogadasIndex);
        }

        try {
            Dog modositott=DogApi.put(modositando);
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
        control.getStyleClass().remove("error");
    }

    public Dog getModositando() {
        return modositando;
    }

    public void setModositando(Dog modositando) {
        this.modositando = modositando;
        ertekekBeallitasa();
    }

    private void ertekekBeallitasa() {
        nevInput.setText(modositando.getName());
        fajInput.setText(modositando.getSpecies());
        kultulInput.setText(modositando.getExternal_property());
        leirasInput.setText(modositando.getDescription());
        kedvelesInput.getValueFactory().setValue(modositando.getInterest());
        nemInput.setValue(modositando.getGender());
        LocalDate datum=LocalDate.parse(modositando.getLikely_bday());
        szulidoInput.setValue(datum);
    }
}
