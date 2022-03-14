package com.example.vizsgaremek_asztali.adoptionType;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import java.io.IOException;


public class AdoptionTypeModositController extends Controller {
    @FXML
    private TextField tipusInput;
    private AdoptionType modositando;

    @FXML
    public void onModositas(ActionEvent actionEvent) {
        String tipus=tipusInput.getText().trim();

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();


        if (tipus.isEmpty()){
            //alert("A név megadása kötelező");
            tipusInput.getStyleClass().add("error");
            alertBuilder.append("A típus megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        modositando.setType(tipus);


        try {
            AdoptionType modositott= AdoptionTypeApi.put(modositando);
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

    public AdoptionType getModositando() {
        return modositando;
    }

    public void setModositando(AdoptionType modositando) {
        this.modositando = modositando;
        ertekekBeallitasa();
    }

    private void ertekekBeallitasa() {
        tipusInput.setText(modositando.getType());

    }
}
