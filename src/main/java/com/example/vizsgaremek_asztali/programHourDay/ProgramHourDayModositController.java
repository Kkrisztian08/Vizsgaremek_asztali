package com.example.vizsgaremek_asztali.programHourDay;

import com.example.vizsgaremek_asztali.Controller;
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
    private ProgramHourDay modositando;
    @FXML
    private Spinner<Integer> orainput;
    @FXML
    private Spinner<Integer> percinput;

    @FXML
    public void onModositas(ActionEvent actionEvent) {
        String idopont="";
        int  ora=0;
        int  perc=0;
        LocalDate datum=datumInput.getValue();
        String formazottSzuldatum;

        boolean hiba = false;
        StringBuilder alertBuilder = new StringBuilder();

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
        if (ora<10 && perc == 0) {
            String formazottora="0"+Integer.toString(ora);
            String formazottperc=Integer.toString(perc)+"0";
            idopont = formazottora + ":" +formazottperc;
        } else if (perc == 0) {
            String formazottperc=Integer.toString(perc)+"0";
            idopont = Integer.toString(ora) + ":" + formazottperc;
        } else if (ora<10) {
            String formazottora="0"+Integer.toString(ora);
            idopont = formazottora + ":" +Integer.toString(perc);
        } else {
            idopont = Integer.toString(ora) + ":" + Integer.toString(perc);
        }

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

    @Deprecated
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
        String[] ido=modositando.getIdo().split(":");
        String ora=ido[0];
        String perc=ido[1];
        orainput.getValueFactory().setValue(Integer.parseInt(ora));
        percinput.getValueFactory().setValue(Integer.parseInt(perc));
        LocalDate datum=LocalDate.parse(modositando.getValasztottDatum());
        datumInput.setValue(datum);

    }
}
