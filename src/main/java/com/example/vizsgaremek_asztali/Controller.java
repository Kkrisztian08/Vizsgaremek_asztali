package com.example.vizsgaremek_asztali;

import com.example.vizsgaremek_asztali.dogs.HozzaadController;
import com.example.vizsgaremek_asztali.dogs.ModositController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Controller {
    protected Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //hozzaad controllerből áthelyezve
    protected void alert(String uzenet) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        setAlertIcon(alert);
        alert.setContentText(uzenet);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.show();
    }

    protected void alerthiba(String uzenet) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        setAlertIcon(alert);
        alert.setContentText(uzenet);
        alert.show();
    }

    protected boolean confirm(String uzenet){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Törlés megerősitése!");
        alert.setHeaderText(uzenet);
        setAlertIcon(alert);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    private void setAlertIcon(Alert alert){
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(ElethangApp.class.getResourceAsStream("icons/logo_szerkesztve.png")));
    }

    protected void hibaKiir(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        setAlertIcon(alert);
        alert.setTitle("Hiba!");
        alert.setHeaderText(e.getClass().toString());
        alert.setContentText(e.getMessage());
        Platform.runLater(alert::show);
    }

    public static Controller ujAblak(String fxml, String title, int width, int height) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ElethangApp.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.getIcons().add(new Image(ElethangApp.class.getResourceAsStream("icons/logo_szerkesztve.png")));
        Controller controller = fxmlLoader.getController();
        controller.stage = stage;
        return controller;
    }
}
