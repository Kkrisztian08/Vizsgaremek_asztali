package com.example.vizsgaremek_asztali.programApplication;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.programInfo.ProgramInfo;
import com.example.vizsgaremek_asztali.programInfo.ProgramInfoApi;
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
    private ComboBox<User> userIdInput;
    @FXML
    private ComboBox<ProgramInfo> pInfoIdInput;
    private List<User> userList;
    private List<ProgramInfo> programInfoList;
    private Runnable runnableAfterHozzaadas;



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

        programInfoList = new ArrayList<>();
        try {
            programInfoList = ProgramInfoApi.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ProgramInfo pinfo : programInfoList){
            pInfoIdInput.getItems().add(pinfo);
        }
        pInfoIdInput.getSelectionModel().selectFirst();
    }

    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
        int userIndex = userIdInput.getSelectionModel().getSelectedItem().getId();
        int programInfoIndex = pInfoIdInput.getSelectionModel().getSelectedItem().getId();

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        try {
            ProgramApplication ujApplication = new ProgramApplication(0 ,programInfoIndex, userIndex);
            ProgramApplication letrehozott = ProgramApplicationApi.post(ujApplication);
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
    public void hibakMegszuntet(ActionEvent actionEvent) {
        Control control = (Control) actionEvent.getSource();
        control.getStyleClass().remove("error");
    }
}
