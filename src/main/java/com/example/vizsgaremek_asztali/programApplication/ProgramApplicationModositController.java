package com.example.vizsgaremek_asztali.programApplication;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.adoption.Adoption;
import com.example.vizsgaremek_asztali.adoption.AdoptionApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import java.io.IOException;


public class ProgramApplicationModositController extends Controller {
    @FXML
    private ComboBox<Integer> pTypeIdInput;
    @FXML
    private ComboBox<Integer> pHDIdInput;
    @FXML
    private ComboBox<Integer> userIdInput;
    private ProgramApplication modositando;

    @FXML
    public void onModositas(ActionEvent actionEvent) {
        int programTypeIndex = pTypeIdInput.getSelectionModel().getSelectedIndex();
        int userIndex = userIdInput.getSelectionModel().getSelectedIndex();
        int programHDIndex = pHDIdInput.getSelectionModel().getSelectedIndex();

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }

        modositando.setProgramTypeid(programTypeIndex);
        modositando.setUserid(userIndex);
        modositando.setProgramHDid(programHDIndex);

        try {
            ProgramApplication modositott= ProgramApplicationApi.put(modositando);
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
        control.getStyleClass().remove("error");
    }

    public ProgramApplication getModositando() {
        return modositando;
    }

    public void setModositando(ProgramApplication modositando) {
        this.modositando = modositando;
        ertekekBeallitasa();
    }
    private void ertekekBeallitasa() {
        pHDIdInput.setValue(modositando.getProgramHDid());
        userIdInput.setValue(modositando.getUserid());
        pTypeIdInput.setValue(modositando.getProgramTypeid());

    }
}
