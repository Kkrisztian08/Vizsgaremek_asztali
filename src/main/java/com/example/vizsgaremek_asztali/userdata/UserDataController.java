package com.example.vizsgaremek_asztali.userdata;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.ElethangApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserDataController extends Controller {
    @FXML
    private TextField keresesTextField;
    @FXML
    private TextArea leirasTextArea;
    @FXML
    private Label adminLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label birthdayLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label nameLabel;

    public void initialize(){
        nameLabel.setText(ElethangApp.BEJELENTKEZETT.getName());
        emailLabel.setText(ElethangApp.BEJELENTKEZETT.getEmail());
        birthdayLabel.setText(ElethangApp.BEJELENTKEZETT.getBirthday());
        phoneNumberLabel.setText(ElethangApp.BEJELENTKEZETT.getPhone_number());
        addressLabel.setText(ElethangApp.BEJELENTKEZETT.getAddress());
        usernameLabel.setText(ElethangApp.BEJELENTKEZETT.getUsername());
        adminLabel.setText(ElethangApp.BEJELENTKEZETT.getFormazottAdmin());
    }


    @FXML
    public void onModosit(ActionEvent actionEvent) {

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
    public void onProgramtypeClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/programTypes/programTypes-view.fxml", "Élethang alapitvány",
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
    public void onProgramHourAndDayClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/programHourDays/programHourDays-view.fxml", "Élethang alapitvány",
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


}
