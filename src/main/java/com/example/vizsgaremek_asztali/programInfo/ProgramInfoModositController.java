package com.example.vizsgaremek_asztali.programInfo;

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

public class ProgramInfoModositController extends Controller {
    @FXML
    private DatePicker datumInput;
    private ProgramInfo modositando;
    @FXML
    private Spinner<Integer> orainput;
    @FXML
    private Spinner<Integer> percinput;
    @FXML
    private TextField tipusInput;

    @FXML
    public void onModositas(ActionEvent actionEvent) {
        String tipus=tipusInput.getText().trim();
        String idopont="";
        int  ora=0;
        int  perc=0;
        LocalDate datum=datumInput.getValue();
        String formazottSzuldatum;

        boolean hiba = false;
        StringBuilder alertBuilder = new StringBuilder();

        if (tipus.isEmpty()){
            //alert("A név megadása kötelező");
            tipusInput.getStyleClass().add("error");
            alertBuilder.append("A típus megadása kötelező").append(System.lineSeparator());
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

        modositando.setType(tipus);
        modositando.setIdo(idopont);
        modositando.setValasztottDatum(formazottSzuldatum);

        try {
            ProgramInfo modositott = ProgramInfoApi.put(modositando);
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

    public ProgramInfo getModositando() {
        return modositando;
    }

    public void setModositando(ProgramInfo modositando) {
        this.modositando = modositando;
        ertekekBeallitasa();
    }
    private void ertekekBeallitasa() {
        tipusInput.setText(modositando.getType());
        String[] ido=modositando.getIdo().split(":");
        String ora=ido[0];
        String perc=ido[1];
        orainput.getValueFactory().setValue(Integer.parseInt(ora));
        percinput.getValueFactory().setValue(Integer.parseInt(perc));
        LocalDate datum=LocalDate.parse(modositando.getValasztottDatum());
        datumInput.setValue(datum);

    }


}
