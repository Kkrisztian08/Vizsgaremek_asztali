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
    @FXML
    private BarChart felhasznalokChart;
    @FXML
    private BarChart orokbefogadBarChart;
    private int kutya;
    private int macska;
    private int user;
    private int admin;
    private int superAdmin;
    private int adoptedDog;
    private int notAdoptedDog;
    private int adoptedCat;
    private int notAdoptedCat;

    public void initialize() {
        try {
            kutya= DogApi.getDogCount();
            macska= CatApi.getCatCount();
            user= UserApi.getUsersCount();
            admin= UserApi.getAdminCount();
            superAdmin= UserApi.getSuperAdminCount();
            adoptedDog=DogApi.getAdoptedDogCount();
            notAdoptedDog=DogApi.getNotAdoptedDogCount();
            adoptedCat=CatApi.getAdoptedCatCount();
            notAdoptedCat=CatApi.getNotAdoptedCatCount();
        } catch (IOException e) {
           hibaKiir(e);
        }
        ObservableList<PieChart.Data> kordiagramData= FXCollections.observableArrayList(
                new PieChart.Data("Kutya",kutya),
                new PieChart.Data("Macska",macska)
        );
        kordiagram.setData(kordiagramData);
        kordiagram.setLegendVisible(false);
        kordiagram.setStartAngle(90);


        XYChart.Series<String,Number> series=new XYChart.Series<>();
        series.setName("Adminok");
        series.getData().add(new XYChart.Data<>("", admin));
        felhasznalokChart.getData().add(series);


        XYChart.Series<String,Number> series2=new XYChart.Series<>();
        series2.setName("Felhasználók");
        series2.getData().add(new XYChart.Data<>("", user));
        felhasznalokChart.getData().add(series2);


        XYChart.Series<String,Number> series3=new XYChart.Series<>();
        series3.setName("Super Adminok");
        series3.getData().add(new XYChart.Data<>("", superAdmin));
        felhasznalokChart.getData().add(series3);


        XYChart.Series<String,Number> series4=new XYChart.Series<>();
        series4.setName("Örökbefogadottak");
        series4.getData().add(new XYChart.Data<>("Kutya", adoptedDog));
        series4.getData().add(new XYChart.Data<>("Macska", adoptedCat));
        orokbefogadBarChart.getData().add(series4);


        XYChart.Series<String,Number> series5=new XYChart.Series<>();
        series5.setName("Menhelyi gondozás alattiak");
        series5.getData().add(new XYChart.Data<>("Kutya", notAdoptedDog));
        series5.getData().add(new XYChart.Data<>("Macska", notAdoptedCat));
        orokbefogadBarChart.getData().add(series5);
    }

    @FXML
    public void onMacskakClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/cats/cats-view.fxml", "Élethang alapitvány",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onKutyakClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/dogs/dogs-view.fxml", "Élethang alapitvány",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }

    }

    @FXML
    public void onProgramApplicationClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/programApplications/programApplications-view.fxml", "Élethang alapitvány",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onEventClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/events/events-view.fxml", "Élethang alapitvány",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onAdoptionTypeClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/adoptionTypes/adoptionTypes-view.fxml", "Élethang alapitvány",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onProgramInfoClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/programInfo/programInfo-view.fxml", "Élethang alapitvány",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onAdoptionClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/adoptions/adoptions-view.fxml", "Élethang alapitvány",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onUsersClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/users/users-view.fxml", "Élethang alapitvány",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onExit(ActionEvent actionEvent) {

        if (!confirm("Biztos szeretne kijelentkezni?")){
            return;
        }
        try {
            Controller oldalvaltas = ujAblak("FXML/login-view.fxml", "Élethang alapitvány",
                    400, 400);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onUserDataClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/userdata/userdata-view.fxml", "Élethang alapitvány",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }
    @FXML
    public void onStatisticClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/statistic-view.fxml", "Élethang alapitvány",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }


}
