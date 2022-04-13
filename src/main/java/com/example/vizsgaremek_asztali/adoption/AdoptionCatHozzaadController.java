package com.example.vizsgaremek_asztali.adoption;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.adoptionType.AdoptionType;
import com.example.vizsgaremek_asztali.adoptionType.AdoptionTypeApi;
import com.example.vizsgaremek_asztali.cats.Cat;
import com.example.vizsgaremek_asztali.cats.CatApi;
import com.example.vizsgaremek_asztali.user.User;
import com.example.vizsgaremek_asztali.user.UserApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdoptionCatHozzaadController extends Controller {
    @FXML
    private ComboBox<AdoptionType> adoptionTypeIdInput;
    @FXML
    private ComboBox<User> userIdInput;
    @FXML
    private ComboBox<Cat> catIdInput;
    private List<User> userList;
    private List<AdoptionType> typeList;
    private List<Cat> catList;
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

        catList = new ArrayList<>();
        try {
            catList= CatApi.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Cat cat : catList){
            catIdInput.getItems().add(cat);
        }
        catIdInput.getSelectionModel().selectFirst();
    }

    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
        LocalDate datum=LocalDate.now();
        int adoptionTypeIndex = adoptionTypeIdInput.getSelectionModel().getSelectedItem().getId();
        int userIndex = userIdInput.getSelectionModel().getSelectedItem().getId();
        int catIndex = catIdInput.getSelectionModel().getSelectedItem().getId();

        String formazottdatum=String.valueOf(datum);
        try {
            Adoption ujAdoption = new Adoption(0,adoptionTypeIndex , userIndex , formazottdatum);
            Adoption letrehozott = AdoptionApi.storeCatAdoption(ujAdoption,catIndex);
            if (letrehozott != null){
                if (runnableAfterHozzaadas!=null){
                    runnableAfterHozzaadas.run();
                }
                alert("Sikeres hozzáadás");
            } else {
                alert("Sikertelen hozzáadás");
            }
        } catch (Exception e) {
            //hibaKiir(e);
            alert("A kiválasztott állat már örökbe van fogadva.");
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
