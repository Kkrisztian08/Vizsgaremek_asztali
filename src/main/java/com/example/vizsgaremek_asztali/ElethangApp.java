package com.example.vizsgaremek_asztali;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ElethangApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ElethangApp.class.getResource("FXML/dogs/dogs-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 600);
        stage.setTitle("Élethang alapítvány");
        stage.getIcons().add(new Image(ElethangApp.class.getResourceAsStream("icons/logo_szerkesztve.png")));
        stage.setScene(scene);
        Controller c = fxmlLoader.getController();
        c.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}