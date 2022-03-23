package com.example.vizsgaremek_asztali.programHourDay;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.event.EventApi;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProgramHourDayModositController extends Controller {
    @FXML
    private DatePicker datumInput;
    @FXML
    private TextField idoInput;
    private ProgramHourDay modositando;

    @FXML
    public void onModositas(ActionEvent actionEvent) {
        String idopont = idoInput.getText().trim();
        LocalDate datum=datumInput.getValue();
        String formazottSzuldatum;

        boolean hiba = false;
        StringBuilder alertBuilder = new StringBuilder();

        if (idopont.isEmpty()) {
            //alert("A név megadása kötelező");
            idoInput.getStyleClass().add("error");
            alertBuilder.append("Az időpont megadása kötelező").append(System.lineSeparator());
            hiba = true;
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
        formazottSzuldatum=datum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        modositando.setIdo(idopont);
        modositando.setValasztottDatum(formazottSzuldatum);

        try {
            ProgramHourDay modositott = ProgramHourDayApi.put(modositando);
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
    public void hibakMegszuntet(ActionEvent actionEvent) {
        Control control = (Control) actionEvent.getSource();
        control.getStyleClass().remove("error");
    }

    @FXML
    public void hibaVege(Event event) {
        Control control = (Control) event.getSource();
        control.getStyleClass().remove("error");
    }

    public ProgramHourDay getModositando() {
        return modositando;
    }

    public void setModositando(ProgramHourDay modositando) {
        this.modositando = modositando;
        ertekekBeallitasa();
    }
    private void ertekekBeallitasa() {
        idoInput.setText(modositando.getIdo());
        LocalDate datum=LocalDate.parse(modositando.getValasztottDatum());
        datumInput.setValue(datum);

    }
}
