package com.example.vizsgaremek_asztali.programApplication;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.adoption.Adoption;
import com.example.vizsgaremek_asztali.adoption.AdoptionApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;

public class ProgramApplicationHozzaadController extends Controller {
    @FXML
    private ComboBox pTypeIdInput;
    @FXML
    private ComboBox pHDIdInput;
    @FXML
    private ComboBox userIdInput;

    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
        int programTypeIndex = pTypeIdInput.getSelectionModel().getSelectedIndex();
        int userIndex = userIdInput.getSelectionModel().getSelectedIndex();
        int programHDIndex = pHDIdInput.getSelectionModel().getSelectedIndex();

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();





        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        try {
            ProgramApplication ujAdoption = new ProgramApplication(0,programTypeIndex, userIndex ,programHDIndex);
            ProgramApplication letrehozott = ProgramApplicationApi.post(ujAdoption);
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
