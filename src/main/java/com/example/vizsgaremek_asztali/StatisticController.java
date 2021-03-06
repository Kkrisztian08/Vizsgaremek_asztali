package com.example.vizsgaremek_asztali;

import com.example.vizsgaremek_asztali.cats.CatApi;
import com.example.vizsgaremek_asztali.dogs.DogApi;
import com.example.vizsgaremek_asztali.user.UserApi;
import javafx.application.Platform;
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
        statisztikaMegjelenit();
    }

    private void statisztikaMegjelenit() {
        try {
            kutya = DogApi.getDogCount();
            macska = CatApi.getCatCount();
            user = UserApi.getUsersCount();
            admin = UserApi.getAdminCount();
            superAdmin = UserApi.getSuperAdminCount();
            adoptedDog = DogApi.getAdoptedDogCount();
            notAdoptedDog = DogApi.getNotAdoptedDogCount();
            adoptedCat = CatApi.getAdoptedCatCount();
            notAdoptedCat = CatApi.getNotAdoptedCatCount();
        } catch (IOException e) {
            hibaKiir(e);
        }
        ObservableList<PieChart.Data> kordiagramData = FXCollections.observableArrayList(
                new PieChart.Data("Kutya", kutya),
                new PieChart.Data("Macska", macska)
        );
        kordiagram.setData(kordiagramData);
        kordiagram.setLegendVisible(false);
        kordiagram.setStartAngle(90);


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Adminok");
        series.getData().add(new XYChart.Data<>("", admin));
        felhasznalokChart.getData().add(series);


        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Felhaszn??l??k");
        series2.getData().add(new XYChart.Data<>("", user));
        felhasznalokChart.getData().add(series2);


        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        series3.setName("Super Adminok");
        series3.getData().add(new XYChart.Data<>("", superAdmin));
        felhasznalokChart.getData().add(series3);


        XYChart.Series<String, Number> series4 = new XYChart.Series<>();
        series4.setName("??r??kbefogadottak");
        series4.getData().add(new XYChart.Data<>("Kutya", adoptedDog));
        series4.getData().add(new XYChart.Data<>("Macska", adoptedCat));
        orokbefogadBarChart.getData().add(series4);


        XYChart.Series<String, Number> series5 = new XYChart.Series<>();
        series5.setName("Menhelyi gondoz??s alattiak");
        series5.getData().add(new XYChart.Data<>("Kutya", notAdoptedDog));
        series5.getData().add(new XYChart.Data<>("Macska", notAdoptedCat));
        orokbefogadBarChart.getData().add(series5);
    }

    @FXML
    public void onMacskakClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/cats/cats-view.fxml", "??lethang alapitv??ny",
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
            Controller oldalvaltas = ujAblak("FXML/dogs/dogs-view.fxml", "??lethang alapitv??ny",
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
            Controller oldalvaltas = ujAblak("FXML/programApplications/programApplications-view.fxml", "??lethang alapitv??ny",
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
            Controller oldalvaltas = ujAblak("FXML/events/events-view.fxml", "??lethang alapitv??ny",
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
            Controller oldalvaltas = ujAblak("FXML/adoptionTypes/adoptionTypes-view.fxml", "??lethang alapitv??ny",
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
            Controller oldalvaltas = ujAblak("FXML/programInfo/programInfo-view.fxml", "??lethang alapitv??ny",
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
            Controller oldalvaltas = ujAblak("FXML/adoptions/adoptions-view.fxml", "??lethang alapitv??ny",
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
            if ( ElethangApp.BEJELENTKEZETT.getAdmin() == 1) {
                Controller oldalvaltas = ujAblak("FXML/users/users-view.fxml", "??lethang alapitv??ny",
                        1100, 600);
                oldalvaltas.getStage().show();
                this.stage.close();
            }else if ( ElethangApp.BEJELENTKEZETT.getAdmin() == 2) {
                Controller oldalvaltas = ujAblak("FXML/superAdmin/superAdminUsers-view.fxml", "??lethang alapitv??ny",
                        1100, 600);
                oldalvaltas.getStage().show();
                this.stage.close();
            }

        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onExit(ActionEvent actionEvent) {

        if (!confirm("Biztos szeretne kijelentkezni?")) {
            return;
        }
        try {
            Controller oldalvaltas = ujAblak("FXML/login-view.fxml", "??lethang alapitv??ny",
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
            Controller oldalvaltas = ujAblak("FXML/userdata/userdata-view.fxml", "??lethang alapitv??ny",
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
            Controller oldalvaltas = ujAblak("FXML/statistic-view.fxml", "??lethang alapitv??ny",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }


}
