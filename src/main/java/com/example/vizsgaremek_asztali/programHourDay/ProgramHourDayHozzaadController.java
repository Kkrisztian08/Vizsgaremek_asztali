package com.example.vizsgaremek_asztali.programHourDay;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProgramHourDayHozzaadController extends Controller {
    @FXML
    private DatePicker datumInput;
    @FXML
    private TextField idoInput;

    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
        String idopont=idoInput.getText().trim();
        LocalDate datum=datumInput.getValue();
        String formazottdatum;

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();


        if (idopont.isEmpty()){
            //alert("A név megadása kötelező");
            idoInput.getStyleClass().add("error");
            alertBuilder.append("Az időpont megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }

        if (datum==null){
            //alert("A dátum megadása kötelező");
            datumInput.getStyleClass().add("error");
            alertBuilder.append("A dátum megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }


        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }

        formazottdatum=datum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try {
            ProgramHourDay ujEvent = new ProgramHourDay(0,formazottdatum,idopont);
            ProgramHourDay letrehozott = ProgramHourDayApi.post(ujEvent);
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
    public void hibakMegszuntet(ActionEvent actionEvent) {
        Control control = (Control) actionEvent.getSource();
        control.getStyleClass().remove("error");
    }

    @FXML
    public void hibaVege(Event event) {
        Control control = (Control) event.getSource();
        control.getStyleClass().remove("error");
    }

}
