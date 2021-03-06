package com.example.vizsgaremek_asztali.programInfo;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.ElethangApp;
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

public class ProgramInfoController extends Controller {
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableColumn<ProgramInfo, Integer> idCol;
    @FXML
    private TableView<ProgramInfo> pITable;
    @FXML
    private TableColumn<ProgramInfo, String> vDatumCol;
    @FXML
    private TableColumn<ProgramInfo, String> idoCol;
    @FXML
    private TableColumn<ProgramInfo, String> typeCol;
    @FXML
    private Button pITorol;
    @FXML
    private Button pIModosit;
    private ObservableList<ProgramInfo> pILista = FXCollections.observableArrayList();


    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        vDatumCol.setCellValueFactory(new PropertyValueFactory<>("valasztottDatum"));
        idoCol.setCellValueFactory(new PropertyValueFactory<>("ido"));
        pIListaFeltolt();
        kereses();
    }

    public void pIListaFeltolt() {
        pITorol.setDisable(true);
        pIModosit.setDisable(true);
        try {
            pILista.clear();
            pILista.addAll(ProgramInfoApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    private void kereses() {
        FilteredList<ProgramInfo> filteredList = new FilteredList<>(pILista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(programInfo -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (programInfo.getIdo().toLowerCase().contains(kereses)) {
                    return true;
                } else if (programInfo.getValasztottDatum().toLowerCase().contains(kereses)) {
                    return true;
                } else if (programInfo.getType().toLowerCase().contains(kereses)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<ProgramInfo> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(pITable.comparatorProperty());
        pITable.setItems(sortedList);
    }

    @FXML
    public void onHozzaadPI(ActionEvent actionEvent) {
        try {
            ProgramInfoHozzaadController hozzadas = (ProgramInfoHozzaadController) ujAblak("FXML/programInfo/hozzaad-view.fxml", "Program Inf?? hozz??ad??sa",
                    500, 450);
            hozzadas.setRunnableAfterHozzaadas(this::pIListaFeltolt);
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onModositPI(ActionEvent actionEvent) {
        int selectedIndex = pITable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert("A m??dos??t??shoz el??bb v??lasszon ki egy elemet a t??bl??zatb??l");
            return;
        }
        ProgramInfo modositando = pITable.getSelectionModel().getSelectedItem();
        try {
            ProgramInfoModositController modositas = (ProgramInfoModositController) ujAblak("FXML/programInfo/modosit-view.fxml", "Adatok M??dos??t??sa",
                    500, 450);
            modositas.setModositando(modositando);
            modositas.getStage().setOnHiding(event -> pITable.refresh());
            modositas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onPHDTorol(ActionEvent actionEvent) {
        int selectedIndex = pITable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert("A t??rl??shez el??bb v??lasszon ki egy elemet a t??bl??zatb??l");
            return;
        }
        ProgramInfo torlendoInfo = pITable.getSelectionModel().getSelectedItem();
        if (!confirm("Val??ban t??r??lni szeretn?? a k??vetkez?? programot: " + torlendoInfo.getType() + ", " + torlendoInfo.getValasztottDatum() + ", " + torlendoInfo.getIdo() + " ?")) {
            return;
        }
        try {
            boolean sikeres = ProgramInfoApi.delete(torlendoInfo.getId());
            alert(sikeres ? "Sikeres t??rl??s" : "Sikertelen t??rl??s");
            pILista.clear();
            pIListaFeltolt();
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onExportTabla(ActionEvent actionEvent) {
        FileChooser choose = new FileChooser();
        choose.setTitle("Export??l??s");
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("MS Excel", "*.xlsx"));
        File file = choose.showSaveDialog(stage);
        if (!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        if (file.exists() == false) {
            Workbook workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("program inf?? t??bla");

            Row row = spreadsheet.createRow(0);

            for (int j = 0; j < pITable.getColumns().size(); j++) {
                row.createCell(j).setCellValue(pITable.getColumns().get(j).getText());
            }

            for (int i = 0; i < pITable.getItems().size(); i++) {
                row = spreadsheet.createRow(i + 1);
                for (int j = 0; j < pITable.getColumns().size(); j++) {
                    if (pITable.getColumns().get(j).getCellData(i) != null) {
                        row.createCell(j).setCellValue(pITable.getColumns().get(j).getCellData(i).toString());
                    } else {
                        row.createCell(j).setCellValue("");
                    }
                }
            }
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
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
    public void onSelectPHD(Event event) {
        int selectedIndex = pITable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            pIModosit.setDisable(false);
            pITorol.setDisable(false);
        }
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
