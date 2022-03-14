package com.example.vizsgaremek_asztali.adoption;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdoptionHozzaadController extends Controller {
    @FXML
    private DatePicker szulidoInput;
    @FXML
    private ComboBox adoptionTypeIdInput;
    @FXML
    private ComboBox userIdInput;

    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
        LocalDate szuldatum=szulidoInput.getValue();
        String formazottSzuldatum;
        int adoptionTypeIndex = adoptionTypeIdInput.getSelectionModel().getSelectedIndex();
        int userIndex = userIdInput.getSelectionModel().getSelectedIndex();

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();

        if (szuldatum==null){
            //alert("A dátum megadása kötelező");
            szulidoInput.getStyleClass().add("error");
            alertBuilder.append("A dátum megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }



        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        formazottSzuldatum=szuldatum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try {
            Adoptions ujAdoption = new Adoptions(0,adoptionTypeIndex , userIndex ,formazottSzuldatum);
            Adoptions letrehozott = AdoptionApi.post(ujAdoption);
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
}
