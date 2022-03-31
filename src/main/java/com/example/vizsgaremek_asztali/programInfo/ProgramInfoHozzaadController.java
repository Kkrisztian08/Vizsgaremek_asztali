package com.example.vizsgaremek_asztali.programInfo;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProgramInfoHozzaadController extends Controller {
    @FXML
    private DatePicker datumInput;
    @FXML
    private Spinner<Integer> percInput;
    @FXML
    private Spinner<Integer> oraInput;
    @FXML
    private TextField tipusInput;


    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
        String tipus=tipusInput.getText().trim();
        LocalDate datum=datumInput.getValue();
        String formazottdatum;
        int ora=0;
        int perc=0;
        String idopont="";

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();

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

        try {
            ora =  oraInput.getValue();
        } catch (NullPointerException ex){
            alert("Az óra megadása kötelező");
            return;
        }

        try {
            perc =  percInput.getValue();
        } catch (NullPointerException ex){
            alert("A perc megadása kötelező");
            return;
        }

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }

        formazottdatum=datum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

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

        try {
            ProgramInfo ujPInfo = new ProgramInfo(0,tipus,formazottdatum,idopont);
            ProgramInfo letrehozott = ProgramInfoApi.post(ujPInfo);
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