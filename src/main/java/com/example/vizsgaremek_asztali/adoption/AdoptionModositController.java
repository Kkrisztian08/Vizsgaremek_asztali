package com.example.vizsgaremek_asztali.adoption;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdoptionModositController extends Controller {
    @FXML
    private DatePicker szulidoInput;
    @FXML
    private ComboBox<Integer> adoptionTypeIdInput;
    @FXML
    private ComboBox<Integer> userIdInput;
    private Adoptions modositando;

    @FXML
    public void onModositas(ActionEvent actionEvent) {
        LocalDate szuldatum=szulidoInput.getValue();
        String formazottSzuldatum;
        int adoptionTypeIndex = adoptionTypeIdInput.getSelectionModel().getSelectedIndex();
        int userIndex = userIdInput.getSelectionModel().getSelectedIndex();

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();

        if (szuldatum==null){
            //alert("A dátum megadása kötelező");
            szulidoInput.getStylesheets().addAll(new File("C:\\Users\\kkris\\IdeaProjects\\Vizsgaremek_asztali\\src\\main\\resources\\com\\example\\vizsgaremek_asztali\\css\\FelugroAblakHiba.css").toURI().toString());
            alertBuilder.append("A dátum megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }




        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        formazottSzuldatum=szuldatum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        modositando.setAdoptionTypeId(adoptionTypeIndex);
        modositando.setUserId(userIndex);
        modositando.setBegin(formazottSzuldatum);

        try {
            Adoptions modositott= AdoptionApi.put(modositando);
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
    public void hibakMegszuntet(ActionEvent actionEvent) {
        Control control = (Control) actionEvent.getSource();
        control.getStylesheets().removeAll(new File("C:\\Users\\kkris\\IdeaProjects\\Vizsgaremek_asztali\\src\\main\\resources\\com\\example\\vizsgaremek_asztali\\css\\FelugroAblakHiba.css").toURI().toString());
        control.getStyleClass().remove("error");
    }

    public Adoptions getModositando() {
        return modositando;
    }

    public void setModositando(Adoptions modositando) {
        this.modositando = modositando;
        ertekekBeallitasa();
    }

    private void ertekekBeallitasa() {
        adoptionTypeIdInput.setValue(modositando.getAdoptionTypeId());
        userIdInput.setValue(modositando.getUserId());
        LocalDate datum=LocalDate.parse(modositando.getBegin());
        szulidoInput.setValue(datum);
    }
}
