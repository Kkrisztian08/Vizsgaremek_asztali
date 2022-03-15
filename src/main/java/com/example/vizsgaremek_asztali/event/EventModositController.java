package com.example.vizsgaremek_asztali.event;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EventModositController extends Controller {
    @FXML
    private TextField elnevezesInput;
    @FXML
    private TextArea leirasInput;
    private Event modositando;
    @FXML
    private DatePicker datumInput;

    @FXML
    public void onModositas(ActionEvent actionEvent) {
        String elnevezes = elnevezesInput.getText().trim();
        String leiras = leirasInput.getText().trim();
        LocalDate datum=datumInput.getValue();
        String formazottSzuldatum;

        boolean hiba = false;
        StringBuilder alertBuilder = new StringBuilder();

        if (elnevezes.isEmpty()) {
            //alert("A név megadása kötelező");
            elnevezesInput.getStyleClass().add("error");
            alertBuilder.append("Az elnevezés megadása kötelező").append(System.lineSeparator());
            hiba = true;
        }

        if (leiras.isEmpty()) {
            //alert("A leírás megadása kötelező");
            leirasInput.getStyleClass().add("error");
            alertBuilder.append("A leírás megadása kötelező").append(System.lineSeparator());
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

        modositando.setElnevezes(elnevezes);
        modositando.setLeiras(leiras);
        modositando.setDatum(formazottSzuldatum);

        try {
            Event modositott = EventApi.put(modositando);
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
    public void hibaVege(javafx.event.Event event) {
        Control control = (Control) event.getSource();
        control.getStyleClass().remove("error");
    }

    public Event getModositando() {
        return modositando;
    }

    public void setModositando(Event modositando) {
        this.modositando = modositando;
        ertekekBeallitasa();
    }

    private void ertekekBeallitasa() {
        elnevezesInput.setText(modositando.getElnevezes());
        leirasInput.setText(modositando.getLeiras());
        LocalDate datum=LocalDate.parse(modositando.getDatum());
        datumInput.setValue(datum);

    }

    @FXML
    public void hibakMegszuntet(ActionEvent actionEvent) {
        Control control = (Control) actionEvent.getSource();
        control.getStyleClass().remove("error");
        control.getStyleClass().remove("error");
    }
}
