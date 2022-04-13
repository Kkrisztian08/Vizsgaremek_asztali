package com.example.vizsgaremek_asztali.login;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.ElethangApp;
import com.example.vizsgaremek_asztali.user.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.ConnectException;

public class LoginController extends Controller {
    @FXML
    private TextField felhasznaloInput;
    @FXML
    private PasswordField jelszoInput;

    @FXML
    public void onBejelentkezes(ActionEvent actionEvent) {
        String felhasznalo = felhasznaloInput.getText().trim();
        String jelszo = jelszoInput.getText().trim();

        boolean hiba = false;
        StringBuilder alertBuilder = new StringBuilder();


        if (felhasznalo.isEmpty()) {
            //alert("A név megadása kötelező");
            felhasznaloInput.getStyleClass().add("error");
            alertBuilder.append("A felhasználonév megadása kötelező").append(System.lineSeparator());
            hiba = true;
        }
        if (jelszo.isEmpty()) {
            //alert("A név megadása kötelező");
            jelszoInput.getStyleClass().add("error");
            alertBuilder.append("A jelszó megadása kötelező").append(System.lineSeparator());
            hiba = true;
        }

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }

        Login login = new Login(felhasznalo, jelszo);
            try {
                Token token = LoginApi.postLogin(login);
                User felhAdatai = LoginApi.getLoginData(token.getToken());
                ElethangApp.BEJELENTKEZETT = felhAdatai;
                if (felhAdatai.getAdmin() >= 1) {
                    Controller oldalvaltas = ujAblak("FXML/users/users-view.fxml", "Élethang alapitvány",
                            1100, 600);
                    oldalvaltas.getStage().show();
                    this.stage.close();
                } else {
                    alert("Nem rendelkezik admin jogosultsággal!");
                }
            } catch (IOException e) {
                hibaKiir(e);
                alertBejelentkezés("Nem megfelelő felhasználónév vagy jelszó!");
            }
    }

    @FXML
    public void hibaVege(Event event) {
        Control control = (Control) event.getSource();
        control.getStyleClass().remove("error");
    }
}
