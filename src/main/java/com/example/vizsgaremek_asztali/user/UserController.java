package com.example.vizsgaremek_asztali.user;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.dogs.DogApi;
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

public class UserController extends Controller {
    @FXML
    private TextArea leirasTextArea;
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User,String> szulIdoCol;
    @FXML
    private TableColumn<User,Integer> adminCol;
    @FXML
    private TableColumn<User,String> cimCol;
    @FXML
    private TableColumn<User,Integer> idCol;
    @FXML
    private Button userTorol;
    @FXML
    private TableColumn<User,String> emailCol;
    @FXML
    private TableColumn<User,String> nevCol;
    @FXML
    private TableColumn<User,String> jelszoCol;
    @FXML
    private TableColumn<User,String> falhasznalonevCol;
    @FXML
    private TableColumn<User,String> telefonCol;
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
        userListaFeltolt();
        kereses();
    }

    public void userListaFeltolt() {
        userTorol.setDisable(true);
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
    public void onHozzaadUser(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/users/hozzaad-view.fxml", "Admin hozzáadása",
                    700, 400);
            hozzadas.getStage().setOnCloseRequest(event -> userListaFeltolt());
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }


    @FXML
    public void onUserTorol(ActionEvent actionEvent) {
        int selectedIndex = userTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        User torlendoUser = userTable.getSelectionModel().getSelectedItem();
        if (!confirm("Valóban törölni szeretné "  +torlendoUser.getName() + " adatait")){
            return;
        }
        if (torlendoUser.getAdmin()==2) {
            alert("A Super Admint biztonsági okok miatt nem lehet kitörölni!");
        }else {
            try {
                boolean sikeres= UserApi.delete(torlendoUser.getId());
                alert(sikeres? "Sikeres törlés": "Sikertelen törlés");
                userLista.clear();
                userListaFeltolt();
            } catch (IOException e) {
                hibaKiir(e);
            }
        }
    }

    @FXML
    public void onExportTabla(ActionEvent actionEvent) {
        FileChooser choose = new FileChooser();
        choose.setTitle("Exportálás");
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("MS Excel", "*.xlsx"));
        File file = choose.showSaveDialog(stage);
        if(!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        if (file.exists() == false) {
            Workbook workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("felhasználók tábla");

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
                alert("Sikeres exportálás");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            alerthiba("Sikertelen exportálás! A file már létezik!");
        }
    }

    @FXML
    public void onSelectUser(Event event) {
        int selectedIndex = userTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            userTorol.setDisable(false);
        }
        User leiraskiir= (User) userTable.getSelectionModel().getSelectedItem();
        leirasTextArea.setText("Cím:\n"+leiraskiir.getAddress()+"\n\nEmail cím:\n"+leiraskiir.getEmail()+"\n\nTitikositott jelszó:\n"+leiraskiir.getPassword());

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
