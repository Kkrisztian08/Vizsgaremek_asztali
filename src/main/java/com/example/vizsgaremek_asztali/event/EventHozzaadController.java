package com.example.vizsgaremek_asztali.event;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EventHozzaadController extends Controller {
    @FXML
    private TextField elnevezesInput;
    @FXML
    private TextArea leirasInput;
    @FXML
    private DatePicker datumInput;
    private Runnable runnableAfterHozzaadas;


    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
        String elnevezes=elnevezesInput.getText().trim();
        String leiras=leirasInput.getText().trim();
        LocalDate datum=datumInput.getValue();
        String formazottSzuldatum;

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();


        if (elnevezes.isEmpty()){
            //alert("A név megadása kötelező");
            elnevezesInput.getStyleClass().add("error");
            alertBuilder.append("Az elnevezés megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }


        if (leiras.isEmpty()){
            //alert("A leírás megadása kötelező");
            leirasInput.getStyleClass().add("error");
            alertBuilder.append("A leírás megadása kötelező").append(System.lineSeparator());
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
        try {
            Event ujEvent = new Event(0,elnevezes,leiras,formazottSzuldatum);
            Event letrehozott = EventApi.post(ujEvent);
            if (letrehozott != null){
                if (runnableAfterHozzaadas!=null){
                    runnableAfterHozzaadas.run();
                }
                alert("Sikeres hozzáadás");
            } else {
                alert("Sikeretelen hozzáadás");
            }
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    public void setRunnableAfterHozzaadas(Runnable runnableAfterHozzaadas) {
        this.runnableAfterHozzaadas = runnableAfterHozzaadas;
    }

    @FXML
    public void hibaVege(javafx.event.Event event) {
        Control control = (Control) event.getSource();
        control.getStyleClass().remove("error");
    }

    @FXML
    public void hibakMegszuntet(ActionEvent actionEvent) {
        Control control = (Control) actionEvent.getSource();
        control.getStyleClass().remove("error");
    }
}
