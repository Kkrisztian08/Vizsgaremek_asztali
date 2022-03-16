package com.example.vizsgaremek_asztali;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends Controller {
    @FXML
    private TextField felhasznaloInput;
    @FXML
    private PasswordField jelszoInput;

    @FXML
    public void onBejelentkezes(ActionEvent actionEvent) {
        String felhasznalo=felhasznaloInput.getText().trim();
        String jelszo=jelszoInput.getText().trim();

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();


        if (felhasznalo.isEmpty()){
            //alert("A név megadása kötelező");
            felhasznaloInput.getStyleClass().add("error");
            alertBuilder.append("A felhasználonév megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }
        if (jelszo.isEmpty()){
            //alert("A név megadása kötelező");
            jelszoInput.getStyleClass().add("error");
            alertBuilder.append("A jelszó megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }

        try {
            Controller oldalvaltas = ujAblak("FXML/dogs/dogs-view.fxml", "Kutyák Tábla",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void hibaVege(Event event) {
        Control control = (Control) event.getSource();
        control.getStyleClass().remove("error");
    }
}
