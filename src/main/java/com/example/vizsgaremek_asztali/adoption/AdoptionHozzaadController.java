package com.example.vizsgaremek_asztali.adoption;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.adoptionType.AdoptionType;
import com.example.vizsgaremek_asztali.adoptionType.AdoptionTypeApi;
import com.example.vizsgaremek_asztali.user.User;
import com.example.vizsgaremek_asztali.user.UserApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdoptionHozzaadController extends Controller {
    @FXML
    private DatePicker szulidoInput;
    @FXML
    private ComboBox<AdoptionType> adoptionTypeIdInput;
    @FXML
    private ComboBox<User> userIdInput;

    private List<User> userList;
    private List<AdoptionType> typeList;


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
            typeList= AdoptionTypeApi.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (AdoptionType type : typeList){
            adoptionTypeIdInput.getItems().add(type);
        }
        adoptionTypeIdInput.getSelectionModel().selectFirst();
    }

    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
        LocalDate szuldatum=szulidoInput.getValue();
        String formazottSzuldatum;
        int adoptionTypeIndex = adoptionTypeIdInput.getSelectionModel().getSelectedItem().getId();
        int userIndex = userIdInput.getSelectionModel().getSelectedItem().getId();

        boolean hiba =false;
        StringBuilder alertBuilder=new StringBuilder();

        if (szuldatum==null){
            //alert("A dátum megadása kötelező");
            szulidoInput.getStyleClass().add("error");
            alertBuilder.append("A dátum megadása kötelező").append(System.lineSeparator());
            hiba=true;
        }

        /*if (adoptionTypeIndex == -1){
            //alert("A nem kiválasztása köztelező");
            adoptionTypeIdInput.getStyleClass().add("error");
            alertBuilder.append("A tipus kiválasztása köztelező").append(System.lineSeparator());
            hiba=true;
        }*/

        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        formazottSzuldatum=szuldatum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try {
            Adoption ujAdoption = new Adoption(0,adoptionTypeIndex , userIndex ,formazottSzuldatum);
            Adoption letrehozott = AdoptionApi.post(ujAdoption);
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
