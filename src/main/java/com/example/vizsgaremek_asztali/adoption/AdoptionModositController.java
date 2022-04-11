package com.example.vizsgaremek_asztali.adoption;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.adoptionType.AdoptionType;
import com.example.vizsgaremek_asztali.adoptionType.AdoptionTypeApi;
import com.example.vizsgaremek_asztali.dogs.Dog;
import com.example.vizsgaremek_asztali.dogs.DogApi;
import com.example.vizsgaremek_asztali.user.User;
import com.example.vizsgaremek_asztali.user.UserApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdoptionModositController extends Controller {
    @FXML
    private DatePicker szulidoInput;
    @FXML
    private ComboBox<AdoptionType> adoptionTypeIdInput;
    @FXML
    private ComboBox<User> userIdInput;
    private List<User> userList;
    private List<AdoptionType> typeList;
    private Adoption modositando;

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
    public void onModositas(ActionEvent actionEvent) {
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




        if (hiba) {
            alert(alertBuilder.toString());
            return;
        }
        formazottSzuldatum=szuldatum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        modositando.setAdoptionTypeId(adoptionTypeIndex);
        modositando.setUserId(userIndex);
        modositando.setBegin(formazottSzuldatum);

        try {
            Adoption modositott= AdoptionApi.put(modositando);
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

    public Adoption getModositando() {
        return modositando;
    }

    public void setModositando(Adoption modositando) {
        this.modositando = modositando;
        ertekekBeallitasa();
    }

    private void ertekekBeallitasa() {
        User user=userList.stream().filter(user1 -> user1.getId()==modositando.getUserId()).findFirst().get();
        userIdInput.setValue(user);
        LocalDate datum=LocalDate.parse(modositando.getBegin());
        szulidoInput.setValue(datum);

    }
}
