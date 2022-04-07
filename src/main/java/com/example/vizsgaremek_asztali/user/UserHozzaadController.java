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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Runnable runnableAfterHozzaadas;


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


        if (jelszo.isEmpty()){
            //alert("A leírás megadása kötelező");
            passwordInput.getStyleClass().add("error");
            alertBuilder.append("A jelszó megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }else if(jelszo.length()<8){
            passwordInput.getStyleClass().add("error");
            alertBuilder.append("A jelszónak minimum 8 karakternek kell lennie").append(System.lineSeparator());
            hiba=true;
        }

        if (hiba) {
            alertinput(alertBuilder.toString());
            return;
        }
        formazottSzuldatum=szuldatum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try {
            User ujUser = new User(0,admin,nev,felhasznalonev,formazottSzuldatum,lakcim,telefonszam,email,jelszo);
            User letrehozott = UserApi.post(ujUser);
            if (letrehozott != null){
                if (runnableAfterHozzaadas!=null){
                    runnableAfterHozzaadas.run();
                }
                alert("Sikeres hozzáadás");
            } else {
                alert("Sikeretelen hozzáadás");
            }
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    public void setRunnableAfterHozzaadas(Runnable runnableAfterHozzaadas) {
        this.runnableAfterHozzaadas = runnableAfterHozzaadas;
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
