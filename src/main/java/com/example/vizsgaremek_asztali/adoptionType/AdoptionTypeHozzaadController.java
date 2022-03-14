package com.example.vizsgaremek_asztali.adoptionType;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.cats.Cats;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;


public class AdoptionTypeHozzaadController extends Controller {
    @FXML
    private TextField tipusInput;

    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
        String tipus=tipusInput.getText().trim();

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();


        if (tipus.isEmpty()){
            //alert("A név megadása kötelező");
            tipusInput.getStyleClass().add("error");
            alertBuilder.append("A név megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        try {
            AdoptionType ujTipus = new AdoptionType(0,tipus);
            AdoptionType letrehozott = AdoptionTypeApi.post(ujTipus);
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
}
