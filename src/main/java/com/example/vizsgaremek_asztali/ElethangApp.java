package com.example.vizsgaremek_asztali;

import com.example.vizsgaremek_asztali.user.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ElethangApp extends Application {
    public static User BEJELENTKEZETT = null;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ElethangApp.class.getResource("FXML/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        //Scene scene = new Scene(fxmlLoader.load(), 1100, 600);
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