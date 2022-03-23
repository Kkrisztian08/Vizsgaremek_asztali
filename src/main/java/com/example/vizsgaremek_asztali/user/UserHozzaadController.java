package com.example.vizsgaremek_asztali.user;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserHozzaadController extends Controller {
    @FXML
    private DatePicker szulidoInput;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField phoneInput;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private TextField addressInput;
    @FXML
    private TextField emailInput;

    @FXML
    public void onHozzad(ActionEvent actionEvent) {
        String nev=nameInput.getText().trim();
        String felhasznalonev=usernameInput.getText().trim();
        LocalDate szuldatum=szulidoInput.getValue();
        String formazottSzuldatum;
        String telefonszam=phoneInput.getText().trim();
        String lakcim=addressInput.getText().trim();
        String email=emailInput.getText().trim();
        String jelszo=passwordInput.getText().trim();
        Integer admin= 1;

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
            alertBuilder.append("A név megadása kötelező").append(System.lineSeparator());
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
            alertBuilder.append("A faj megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }

        if (lakcim.isEmpty()){
            //alert("A külső tulajdonság megadása kötelező");
            addressInput.getStyleClass().add("error");
            alertBuilder.append("A külső tulajdonság megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }


        if (email.isEmpty()){
            //alert("A leírás megadása kötelező");
            emailInput.getStyleClass().add("error");
            alertBuilder.append("A leírás megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }

        if (jelszo.isEmpty()){
            //alert("A leírás megadása kötelező");
            passwordInput.getStyleClass().add("error");
            alertBuilder.append("A leírás megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        formazottSzuldatum=szuldatum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try {
            User ujUser = new User(0,admin,nev,felhasznalonev,formazottSzuldatum,telefonszam,lakcim,email,jelszo);
            User letrehozott = UserApi.post(ujUser);
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
