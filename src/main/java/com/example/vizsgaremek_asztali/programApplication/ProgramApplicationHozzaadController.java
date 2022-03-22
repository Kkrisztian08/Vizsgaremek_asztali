package com.example.vizsgaremek_asztali.programApplication;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.programHourDay.ProgramHourDay;
import com.example.vizsgaremek_asztali.programHourDay.ProgramHourDayApi;
import com.example.vizsgaremek_asztali.programType.ProgramType;
import com.example.vizsgaremek_asztali.programType.ProgramTypeApi;
import com.example.vizsgaremek_asztali.user.User;
import com.example.vizsgaremek_asztali.user.UserApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramApplicationHozzaadController extends Controller {
    @FXML
    private ComboBox<ProgramType> pTypeIdInput;
    @FXML
    private ComboBox<ProgramHourDay> pHDIdInput;
    @FXML
    private ComboBox<User> userIdInput;

    private List<User> userList;
    private List<ProgramType> typeList;
    private List<ProgramHourDay> phdList;


    public void initialize(){
        userList = new ArrayList<>();
        try {
            userList= UserApi.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (User user : userList){
            userIdInput.getItems().add(user);
        }
        userIdInput.getSelectionModel().selectFirst();

        typeList = new ArrayList<>();
        try {
            typeList= ProgramTypeApi.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ProgramType type : typeList){
            pTypeIdInput.getItems().add(type);
        }
        pTypeIdInput.getSelectionModel().selectFirst();

        phdList = new ArrayList<>();
        try {
            phdList= ProgramHourDayApi.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ProgramHourDay phd : phdList){
            pHDIdInput.getItems().add(phd);
        }
        pHDIdInput.getSelectionModel().selectFirst();
    }

    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
        int programTypeIndex = pTypeIdInput.getSelectionModel().getSelectedItem().getId();
        int userIndex = userIdInput.getSelectionModel().getSelectedItem().getId();
        int programHDIndex = pHDIdInput.getSelectionModel().getSelectedItem().getId();

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
