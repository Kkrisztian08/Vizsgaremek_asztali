package com.example.vizsgaremek_asztali.superAdmin;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.ElethangApp;
import com.example.vizsgaremek_asztali.user.User;
import com.example.vizsgaremek_asztali.user.UserApi;
import com.example.vizsgaremek_asztali.user.UserHozzaadController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SuperAdminUserController extends Controller {
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableColumn szulIdoCol;
    @FXML
    private TextArea leirasTextArea;
    @FXML
    private TableColumn<User,Integer> adminCol;
    @FXML
    private TableColumn<User,String> falhasznalonevCol;
    @FXML
    private TableColumn<User,String> cimCol;
    @FXML
    private TableColumn<User,Integer> idCol;
    @FXML
    private Button userTorol;
    @FXML
    private TableColumn<User,String> emailCol;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User,String> nevCol;
    @FXML
    private TableColumn<User,String> jelszoCol;
    @FXML
    private TableColumn<User,String> telefonCol;
    @FXML
    private Button adminJogButton;
    @FXML
    private Button lefokozButton;
    private ObservableList<User> userLista = FXCollections.observableArrayList();






    public void initialize(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        adminCol.setCellValueFactory(new PropertyValueFactory<>("FormazottAdmin"));
        nevCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        falhasznalonevCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        szulIdoCol.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        cimCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        telefonCol.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        jelszoCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        Platform.runLater(()->{
            userListaFeltolt();
            kereses();
        });

    }

    public void userListaFeltolt() {
        userTorol.setDisable(true);
        adminJogButton.setDisable(true);
        lefokozButton.setDisable(true);
        try {
            userLista.clear();
            userLista.addAll(UserApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    private void kereses(){
        FilteredList<User> filteredList = new FilteredList<>(userLista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(users -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (users.getName().toLowerCase().contains(kereses)) {
                    return true;
                }
                else if (users.getAddress().toLowerCase().contains(kereses)) {
                    return true;
                }
                else if (users.getPhone_number().toLowerCase().contains(kereses)) {
                    return true;
                }
                else if (users.getBirthday().toLowerCase().contains(kereses)) {
                    return true;
                }
                else if (users.getEmail().toLowerCase().contains(kereses)) {
                    return true;
                }
                else if (users.getFormazottAdmin().toLowerCase().contains(kereses)) {
                    return true;
                }
                else {
                    return false;
                }
            });
        });
        SortedList<User> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(userTable.comparatorProperty());
        userTable.setItems(sortedList);
    }

    @FXML
    public void onHozzaadAdmin(ActionEvent actionEvent) {
        try {
            UserHozzaadController hozzadas = (UserHozzaadController) ujAblak("FXML/users/hozzaad-view.fxml", "Admin hozz??ad??sa",
                    700, 400);
            hozzadas.setRunnableAfterHozzaadas(this::userListaFeltolt);
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onUserTorol(ActionEvent actionEvent) {
        int selectedIndex = userTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A t??rl??shez el??bb v??lasszon ki egy elemet a t??bl??zatb??l");
            return;
        }
        User torlendoUser = userTable.getSelectionModel().getSelectedItem();
        if (torlendoUser.getId() == ElethangApp.BEJELENTKEZETT.getId()) {
            alert("A saj??t fi??kj??t nem t??r??lheti!");
        }else {
            if (!confirm("Val??ban t??r??lni szeretn?? " + torlendoUser.getName() + " adatait")) {
                return;
            }
            if (torlendoUser.getAdmin() == 2) {
                alert("A Super Admint biztons??gi okok miatt nem lehet kit??r??lni!");
            } else {
                try {
                    boolean sikeres = UserApi.delete(torlendoUser.getId());
                    alert(sikeres ? "Sikeres t??rl??s" : "Sikertelen t??rl??s");
                    userLista.clear();
                    userListaFeltolt();
                } catch (IOException e) {
                    hibaKiir(e);
                }
            }
        }
    }

    @FXML
    public void onAdminJog(ActionEvent actionEvent) {
        User felhasznalo = userTable.getSelectionModel().getSelectedItem();
        if (felhasznalo.getId() == ElethangApp.BEJELENTKEZETT.getId()) {
            alert("A saj??t jogosults??g??t biztons??gi okok miatt nem m??dos??thatja!");
        }else {
            if (felhasznalo.getAdmin() == 0) {
                if (!confirm("Val??ban szeretn?? " + felhasznalo.getName() + "-t admin jogosults??ggal felruh??zni?")) {
                    return;
                }
                try {
                    User letrehozott = UserApi.adminJog(felhasznalo);
                    if (letrehozott != null) {
                        alert("Sikeres jogosults??g m??dos??t??s");
                        userLista.clear();
                        userListaFeltolt();
                    } else {
                        alert("Sikertelen jogosults??g m??dos??t??s");
                    }
                } catch (Exception e) {
                    hibaKiir(e);
                }

            }else if (felhasznalo.getAdmin() == 1) {
                if (!confirm("Val??ban szeretn?? " + felhasznalo.getName() + "-t super admin jogosults??ggal felruh??zni?")) {
                    return;
                }
                try {
                    User letrehozott = UserApi.adminJog(felhasznalo);
                    if (letrehozott != null) {
                        alert("Sikeres jogosults??g m??dos??t??s");
                        userLista.clear();
                        userListaFeltolt();
                    } else {
                        alert("Sikertelen jogosults??g m??dos??t??s");
                    }
                } catch (Exception e) {
                    hibaKiir(e);
                }

            } else {
                alert("A kiv??lasztott felhaszn??l?? m??r nem lehet magasabb jogosults??ggal felruh??zni!");
            }
        }
    }

    @FXML
    public void onLefokoz(ActionEvent actionEvent) {
        User felhasznalo = userTable.getSelectionModel().getSelectedItem();
        if (felhasznalo.getId() == ElethangApp.BEJELENTKEZETT.getId()) {
            alert("A saj??t jogosults??g??t biztons??gi okok miatt nem m??dos??thatja!");
        }else {
            if (felhasznalo.getAdmin() == 2) {
                if (!confirm("Val??ban szeretn?? " + felhasznalo.getName() + "-t lefokozni adminn???")) {
                    return;
                }
                try {
                    User letrehozott = UserApi.adminJogElvesz(felhasznalo);
                    if (letrehozott != null) {
                        alert("Sikeres m??velet");
                        userLista.clear();
                        userListaFeltolt();
                    } else {
                        alert("Sikertelen m??velet");
                    }
                } catch (Exception e) {
                    hibaKiir(e);
                }

            } else if (felhasznalo.getAdmin() == 1) {
                if (!confirm("Val??ban szeretn?? " + felhasznalo.getName() + "-t lefokozni ??ltal??nos felhaszn??l??v???")) {
                    return;
                }
                try {
                    User letrehozott = UserApi.adminJogElvesz(felhasznalo);
                    if (letrehozott != null) {
                        alert("Sikeres m??velet");
                        userLista.clear();
                        userListaFeltolt();
                    } else {
                        alert("Sikertelen m??velet");
                    }
                } catch (Exception e) {
                    hibaKiir(e);
                }

            } else {
                alert("A kiv??lasztott felhaszn??l?? m??r nem lehet lejebb fokozni!");
            }
        }
    }

    @FXML
    public void onExportTabla(ActionEvent actionEvent) {
        FileChooser choose = new FileChooser();
        choose.setTitle("Export??l??s");
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("MS Excel", "*.xlsx"));
        File file = choose.showSaveDialog(stage);
        if(!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        if (file.exists() == false) {
            Workbook workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("felhaszn??l??k t??bla");

            Row row = spreadsheet.createRow(0);

            for (int j = 0; j < userTable.getColumns().size(); j++) {
                row.createCell(j).setCellValue(userTable.getColumns().get(j).getText());
            }

            for (int i = 0; i < userTable.getItems().size(); i++) {
                row = spreadsheet.createRow(i + 1);
                for (int j = 0; j < userTable.getColumns().size(); j++) {
                    if( userTable.getColumns().get(j).getCellData(i) != null) {
                        row.createCell(j).setCellValue(userTable.getColumns().get(j).getCellData(i).toString());
                    }
                    else {
                        row.createCell(j).setCellValue("");
                    }
                }
            }
            try (FileOutputStream fileOut = new FileOutputStream(file)){
                workbook.write(fileOut);
                alert("Sikeres export??l??s");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            alerthiba("Sikertelen export??l??s! A file m??r l??tezik!");
        }
    }

    @FXML
    public void onSelectUser(Event event) {
        int selectedIndex = userTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            userTorol.setDisable(false);
            adminJogButton.setDisable(false);
            lefokozButton.setDisable(false);
        }
        User leiraskiir= (User) userTable.getSelectionModel().getSelectedItem();
        leirasTextArea.setText("C??m:\n"+leiraskiir.getAddress()+"\n\nEmail c??m:\n"+leiraskiir.getEmail()+"\n\nTitikositott jelsz??:\n"+leiraskiir.getPassword());

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

        if (!confirm("Biztos szeretne kijelentkezni?")){
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
