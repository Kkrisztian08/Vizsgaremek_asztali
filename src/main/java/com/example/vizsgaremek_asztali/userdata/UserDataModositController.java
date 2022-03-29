package com.example.vizsgaremek_asztali.userdata;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.ElethangApp;
import com.example.vizsgaremek_asztali.user.User;
import com.example.vizsgaremek_asztali.user.UserApi;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserDataModositController extends Controller {
    @FXML
    private DatePicker szulidoInput;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField phoneInput;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField addressInput;
    @FXML
    private TextField emailInput;
    private User modositando;

    @FXML
    public void onModosit(ActionEvent actionEvent) {
        String nev=nameInput.getText().trim();
        String felhasznalonev=usernameInput.getText().trim();
        LocalDate szuldatum=szulidoInput.getValue();
        String formazottSzuldatum;
        String telefonszam=phoneInput.getText().trim();
        String lakcim=addressInput.getText().trim();
        String email=emailInput.getText().trim();

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();


        if (nev.isEmpty()){
            //alert("A név megadása kötelező");
            nameInput.getStyleClass().add("error");
            alertBuilder.append("A név megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }

        if (felhasznalonev.isEmpty()){
            //alert("A név megadása kötelező");
            usernameInput.getStyleClass().add("error");
            alertBuilder.append("A felhasználónév megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }



        if (szuldatum==null){
            //alert("A dátum megadása kötelező");
            szulidoInput.getStyleClass().add("error");
            alertBuilder.append("A dátum megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }

        if (telefonszam.isEmpty()){
            //alert("A faj megadása kötelező");
            phoneInput.getStyleClass().add("error");
            alertBuilder.append("A telefonszám megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }


        if (lakcim.isEmpty()){
            //alert("A külső tulajdonság megadása kötelező");
            addressInput.getStyleClass().add("error");
            alertBuilder.append("A lakcím megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }


        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (email.isEmpty()){
            //alert("A leírás megadása kötelező");
            emailInput.getStyleClass().add("error");
            alertBuilder.append("Az email megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }else if(!(matcher.matches()) ){
            emailInput.getStyleClass().add("error");
            alertBuilder.append("Helytelen email formátum").append(System.lineSeparator());
            hiba=true;
        }else if(!(email.endsWith(".com"))){
            emailInput.getStyleClass().add("error");
            alertBuilder.append("Helytelen email formátum (Hiányzik a '.com' a végéről)").append(System.lineSeparator());
            hiba=true;
        }

        if (hiba) {
            alertinput(alertBuilder.toString());
            return;
        }
        formazottSzuldatum=szuldatum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        modositando.setName(nev);
        modositando.setUsername(felhasznalonev);
        modositando.setBirthday(formazottSzuldatum);
        modositando.setPhone_number(formazottSzuldatum);
        modositando.setAddress(lakcim);
        modositando.setEmail(email);

        try {
            User modositott = UserApi.put(modositando);
            if (modositott != null) {
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

    public User getModositando() {
        return modositando;
    }

    public void setModositando(User modositando) {
        this.modositando = modositando;
        ertekekBeallitasa();
    }

    private void ertekekBeallitasa() {
        nameInput.setText(ElethangApp.BEJELENTKEZETT.getName());
        usernameInput.setText(ElethangApp.BEJELENTKEZETT.getUsername());
        emailInput.setText(ElethangApp.BEJELENTKEZETT.getEmail());
        phoneInput.setText(ElethangApp.BEJELENTKEZETT.getPhone_number());
        addressInput.setText(ElethangApp.BEJELENTKEZETT.getAddress());
        LocalDate datum=LocalDate.parse(ElethangApp.BEJELENTKEZETT.getBirthday());
        szulidoInput.setValue(datum);
    }
}
