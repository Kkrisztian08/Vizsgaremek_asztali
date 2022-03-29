package com.example.vizsgaremek_asztali;

import com.example.vizsgaremek_asztali.cats.CatApi;
import com.example.vizsgaremek_asztali.dogs.DogApi;
import com.example.vizsgaremek_asztali.user.UserApi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class StatisticController extends Controller {
    @FXML
    private TextArea leirasKulTulTextArea;
    @FXML
    private PieChart kordiagram;
    private int kutya;
    private int macska;
    private int user;
    private int admin;
    private int superAdmin;
    @FXML
    private BarChart felhasznalokChart;

    public void initialize() {
        try {
            kutya= DogApi.getDogCount();
            macska= CatApi.getCatCount();
            user= UserApi.getUsersCount();
            admin= UserApi.getAdminCount();
            superAdmin= UserApi.getSuperAdminCount();
        } catch (IOException e) {
           hibaKiir(e);
        }
        ObservableList<PieChart.Data> kordiagramData= FXCollections.observableArrayList(
                new PieChart.Data("Kutya",kutya),
                new PieChart.Data("Macska",macska)
        );
        kordiagram.setData(kordiagramData);
        kordiagram.setStartAngle(90);

        XYChart.Series<String,Number> series=new XYChart.Series<>();
        series.setName("Jogosultságok");
        series.getData().add(new XYChart.Data<>("Felhasználók", user));
        series.getData().add(new XYChart.Data<>("Adminok", admin));
        series.getData().add(new XYChart.Data<>("Super Adminok", superAdmin));

        felhasznalokChart.getData().add(series);
    }

    @FXML
    public void onMacskakClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onProgramtypeClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onAdoptionTypeClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onExit(ActionEvent actionEvent) {
    }

    @FXML
    public void onProgramHourAndDayClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onUsersClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onEventClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onProgramApplicationClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onKutyakClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onAdoptionClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onUserDataClick(ActionEvent actionEvent) {
    }
}
