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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdoptionHozzaadController extends Controller {
    @FXML
    private ComboBox<AdoptionType> adoptionTypeIdInput;
    @FXML
    private ComboBox<User> userIdInput;
    @FXML
    private ComboBox<Dog> dogIdInput;
    private List<User> userList;
    private List<AdoptionType> typeList;
    private List<Dog> dogList;



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

        dogList = new ArrayList<>();
        try {
            dogList= DogApi.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Dog dog : dogList){
            dogIdInput.getItems().add(dog);
        }
        dogIdInput.getSelectionModel().selectFirst();
    }

    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {

        LocalDate datum=LocalDate.now();
        int adoptionTypeIndex = adoptionTypeIdInput.getSelectionModel().getSelectedItem().getId();
        int userIndex = userIdInput.getSelectionModel().getSelectedItem().getId();
        int dogIndex = dogIdInput.getSelectionModel().getSelectedItem().getId();

        String formazottdatum=String.valueOf(datum);
        try {
            Adoption ujAdoption = new Adoption(0,adoptionTypeIndex , userIndex , formazottdatum);
            Adoption letrehozott = AdoptionApi.storeDogAdoption(ujAdoption,dogIndex);
            //TODO:post-ba kell egy paraméter id és ide irom be az elmentett változót
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
