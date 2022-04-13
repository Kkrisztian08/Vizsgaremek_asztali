package com.example.vizsgaremek_asztali.programApplication;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.adoption.Adoption;
import com.example.vizsgaremek_asztali.adoption.AdoptionApi;
import com.example.vizsgaremek_asztali.adoptionType.AdoptionType;
import com.example.vizsgaremek_asztali.adoptionType.AdoptionTypeApi;
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


public class ProgramApplicationModositController extends Controller {
    @FXML
    private ComboBox<User> userIdInput;
    @FXML
    private ComboBox<ProgramInfo> pInfoInput;
    private ProgramApplication modositando;

    private List<User> userList;
    private List<ProgramInfo> pInfoList;

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

        pInfoList = new ArrayList<>();
        try {
            pInfoList= ProgramInfoApi.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ProgramInfo info : pInfoList){
            pInfoInput.getItems().add(info);
        }
        pInfoInput.getSelectionModel().selectFirst();

    }


    @FXML
    public void onModositas(ActionEvent actionEvent) {
        int pInfoIndex = pInfoInput.getSelectionModel().getSelectedItem().getId();
        int userIndex = userIdInput.getSelectionModel().getSelectedItem().getId();


        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }

        modositando.setUserid(userIndex);
        modositando.setProgramInfo(pInfoIndex);

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
        User user=userList.stream().filter(user1 -> user1.getId()==modositando.getUserid()).findFirst().get();
        userIdInput.setValue(user);
        ProgramInfo info=pInfoList.stream().filter(info1 -> info1.getId()==modositando.getProgramInfo()).findFirst().get();
        pInfoInput.setValue(info);


    }
}
