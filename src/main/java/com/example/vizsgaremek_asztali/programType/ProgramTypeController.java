package com.example.vizsgaremek_asztali.programType;

import com.example.vizsgaremek_asztali.Controller;
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

public class ProgramTypeController extends Controller {
    @FXML
    private TableView<ProgramType> pTypeTable;
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableColumn<ProgramType,Integer> idCol;
    @FXML
    private TableColumn<ProgramType,String> megnevezesCol;
    @FXML
    private Button pTypeTorol;
    @FXML
    private Button pTypeModosit;
    private ObservableList<ProgramType> pTypeLista = FXCollections.observableArrayList();


    public void initialize(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        megnevezesCol.setCellValueFactory(new PropertyValueFactory<>("megnevezes"));
        pTypeListaFeltolt();
        kereses();
    }

    public  void pTypeListaFeltolt() {
        pTypeTorol.setDisable(true);
        pTypeModosit.setDisable(true);
        try {
            pTypeLista.clear();
            pTypeLista.addAll(ProgramTypeApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    private void kereses(){
        FilteredList<ProgramType> filteredList = new FilteredList<>(pTypeLista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(events -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (events.getMegnevezes().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }
                else {
                    return false;
                }
            });
        });
        SortedList<ProgramType> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(pTypeTable.comparatorProperty());
        pTypeTable.setItems(sortedList);
    }

    @FXML
    public void onHozzaadPType(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/programTypes/hozzaad-view.fxml", "Program Típus hozzáadása",
                    500, 230);
            hozzadas.getStage().setOnCloseRequest(event -> pTypeListaFeltolt());
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onModositPType(ActionEvent actionEvent) {
        int selectedIndex = pTypeTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A módosításhoz előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        ProgramType modositando = pTypeTable.getSelectionModel().getSelectedItem();
        try {
            ProgramTypeModositController modositas = (ProgramTypeModositController) ujAblak("FXML/programTypes/modosit-view.fxml", "Adatok Módosítása",
                    500, 230);
            modositas.setModositando(modositando);
            modositas.getStage().setOnHiding(event -> pTypeTable.refresh());
            modositas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onPTypeTorol(ActionEvent actionEvent) {
        int selectedIndex = pTypeTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        ProgramType torlendoEvent = pTypeTable.getSelectionModel().getSelectedItem();
        if (!confirm("Valóban törölni szeretné a(z) "  +torlendoEvent.getMegnevezes() + " nevű programot?")){
            return;
        }
        try {
            boolean sikeres= ProgramTypeApi.delete(torlendoEvent.getId());
            alert(sikeres? "Sikertelen törlés": "Sikeres törlés");
            pTypeLista.clear();
            pTypeListaFeltolt();
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
        if(!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        if (file.exists() == false) {
            Workbook workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("kutyák tábla");

            Row row = spreadsheet.createRow(0);

            for (int j = 0; j < pTypeTable.getColumns().size(); j++) {
                row.createCell(j).setCellValue(pTypeTable.getColumns().get(j).getText());
            }

            for (int i = 0; i < pTypeTable.getItems().size(); i++) {
                row = spreadsheet.createRow(i + 1);
                for (int j = 0; j < pTypeTable.getColumns().size(); j++) {
                    if(pTypeTable.getColumns().get(j).getCellData(i) != null) {
                        row.createCell(j).setCellValue(pTypeTable.getColumns().get(j).getCellData(i).toString());
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
    public void onSelectEvent(Event event) {
        int selectedIndex = pTypeTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            pTypeModosit.setDisable(false);
            pTypeTorol.setDisable(false);
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
