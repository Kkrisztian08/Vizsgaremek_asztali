package com.example.vizsgaremek_asztali.programApplication;

import com.example.vizsgaremek_asztali.Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

public class ProgramApplicationController extends Controller {
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableColumn<ProgramApplication, Integer> idCol;
    @FXML
    private TableColumn<ProgramApplication, Integer> userIdCol;
    @FXML
    private TableColumn<ProgramApplication, Integer> pInfoIdCol;
    @FXML
    private TableView<ProgramApplication> pATable;
    @FXML
    private Button pATorol;
    @FXML
    private Button pAModosit;
    private ObservableList<ProgramApplication> pALista = FXCollections.observableArrayList();


    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        pInfoIdCol.setCellValueFactory(new PropertyValueFactory<>("programInfo"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userid"));
        pAListaFeltolt();
        kereses();
    }

    public void pAListaFeltolt() {
        pATorol.setDisable(true);
        pAModosit.setDisable(true);
        pALista.clear();
        Platform.runLater(() -> {
            try {
                pALista.addAll(ProgramApplicationApi.get());
            } catch (IOException e) {
                hibaKiir(e);
                e.printStackTrace();
            }
        });
    }

    private void kereses() {
        FilteredList<ProgramApplication> filteredList = new FilteredList<>(pALista, b -> true);
        /*keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(events -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (events.getElnevezes().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }
                else if(events.getLeiras().toLowerCase().indexOf(kereses) > -1){
                    return true;
                }
                else if(events.getDatum().toLowerCase().indexOf(kereses) > -1){
                    return true;
                }
                else {
                    return false;
                }
            });
        });*/
        SortedList<ProgramApplication> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(pATable.comparatorProperty());
        pATable.setItems(sortedList);
    }

    @FXML
    public void onHozzaadPA(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/programApplications/hozzaad-view.fxml", "Program Jelentkezés hozzáadása",
                    500, 350);
            hozzadas.getStage().setOnCloseRequest(event -> pAListaFeltolt());
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onModositPA(ActionEvent actionEvent) {
        int selectedIndex = pATable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert("A módosításhoz előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        ProgramApplication modositando = pATable.getSelectionModel().getSelectedItem();
        try {
            ProgramApplicationModositController modositas = (ProgramApplicationModositController) ujAblak("FXML/programApplications/modosit-view.fxml", "Adatok Módosítása",
                    500, 350);
            modositas.setModositando(modositando);
            modositas.getStage().setOnHiding(event -> pATable.refresh());
            modositas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onPATorol(ActionEvent actionEvent) {
        int selectedIndex = pATable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        ProgramApplication torlendoEvent = pATable.getSelectionModel().getSelectedItem();
        if (!confirm("Valóban törölni szeretné a(z) " + torlendoEvent.getId() + " ID-val rendelkező program jelentkezést?")) {
            return;
        }
        try {
            boolean sikeres = ProgramApplicationApi.delete(torlendoEvent.getId());
            alert(sikeres ? "Sikeres törlés": "Sikertelen törlés");
            pALista.clear();
            pAListaFeltolt();
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onExportTabla(ActionEvent actionEvent) {
        FileChooser choose = new FileChooser();
        choose.setTitle("Exportálás");
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("MS Excel", "*.xlsx"));
        File file = choose.showSaveDialog(stage);
        if (!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        if (file.exists() == false) {
            Workbook workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("kutyák tábla");

            Row row = spreadsheet.createRow(0);

            for (int j = 0; j < pATable.getColumns().size(); j++) {
                row.createCell(j).setCellValue(pATable.getColumns().get(j).getText());
            }

            for (int i = 0; i < pATable.getItems().size(); i++) {
                row = spreadsheet.createRow(i + 1);
                for (int j = 0; j < pATable.getColumns().size(); j++) {
                    if (pATable.getColumns().get(j).getCellData(i) != null) {
                        row.createCell(j).setCellValue(pATable.getColumns().get(j).getCellData(i).toString());
                    } else {
                        row.createCell(j).setCellValue("");
                    }
                }
            }
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
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
    public void onSelectPHD(Event event) {
        int selectedIndex = pATable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            pAModosit.setDisable(false);
            pATorol.setDisable(false);
        }
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
