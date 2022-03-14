package com.example.vizsgaremek_asztali.programType;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class ProgramTypeHozzaadController extends Controller {
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
            alertBuilder.append("A típus megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        try {
            ProgramTypes ujTipus = new ProgramTypes(0,tipus);
            ProgramTypes letrehozott = ProgramTypeApi.post(ujTipus);
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
