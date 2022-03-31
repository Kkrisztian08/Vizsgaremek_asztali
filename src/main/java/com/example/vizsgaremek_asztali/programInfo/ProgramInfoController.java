package com.example.vizsgaremek_asztali.programInfo;

import com.example.vizsgaremek_asztali.Controller;
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
    private TableColumn<ProgramInfo,Integer> idCol;
    @FXML
    private TableView<ProgramInfo> pHDTable;
    @FXML
    private TableColumn<ProgramInfo,String> vDatumCol;
    @FXML
    private TableColumn<ProgramInfo,String> idoCol;
    @FXML
    private TableColumn<ProgramInfo,String> typeCol;
    @FXML
    private Button pHDTorol;
    @FXML
    private Button pHDModosit;
    private ObservableList<ProgramInfo> pHDLista = FXCollections.observableArrayList();


    public void initialize(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        vDatumCol.setCellValueFactory(new PropertyValueFactory<>("valasztottDatum"));
        idoCol.setCellValueFactory(new PropertyValueFactory<>("ido"));
        pHDListaFeltolt();
        kereses();
    }

    public  void pHDListaFeltolt() {
        pHDTorol.setDisable(true);
        pHDModosit.setDisable(true);
        try {
            pHDLista.clear();
            pHDLista.addAll(ProgramInfoApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    private void kereses(){
        FilteredList<ProgramInfo> filteredList = new FilteredList<>(pHDLista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(programInfo -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (programInfo.getIdo().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }
                else if (programInfo.getValasztottDatum().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }else if (programInfo.getType().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }
                else {
                    return false;
                }
            });
        });
        SortedList<ProgramInfo> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(pHDTable.comparatorProperty());
        pHDTable.setItems(sortedList);
    }

    @FXML
    public void onHozzaadPHD(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/programInfo/hozzaad-view.fxml", "Program Infó hozzáadása",
                    500, 350);
            hozzadas.getStage().setOnCloseRequest(event -> pHDListaFeltolt());
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onModositPHD(ActionEvent actionEvent) {
        int selectedIndex = pHDTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A módosításhoz előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        ProgramInfo modositando = pHDTable.getSelectionModel().getSelectedItem();
        try {
            ProgramInfoModositController modositas = (ProgramInfoModositController) ujAblak("FXML/programInfo/modosit-view.fxml", "Adatok Módosítása",
                    500, 350);
            modositas.setModositando(modositando);
            modositas.getStage().setOnHiding(event -> pHDTable.refresh());
            modositas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onPHDTorol(ActionEvent actionEvent) {
        int selectedIndex = pHDTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        ProgramInfo torlendoInfo = pHDTable.getSelectionModel().getSelectedItem();
        if (!confirm("Valóban törölni szeretné a következő programot: "+torlendoInfo.getType()+", "+torlendoInfo.getValasztottDatum()+", "+torlendoInfo.getIdo() + " ?")){
            return;
        }
        try {
            boolean sikeres= ProgramInfoApi.delete(torlendoInfo.getId());
            alert(sikeres? "Sikeres törlés": "Sikertelen törlés");
            pHDLista.clear();
            pHDListaFeltolt();
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

            for (int j = 0; j < pHDTable.getColumns().size(); j++) {
                row.createCell(j).setCellValue(pHDTable.getColumns().get(j).getText());
            }

            for (int i = 0; i < pHDTable.getItems().size(); i++) {
                row = spreadsheet.createRow(i + 1);
                for (int j = 0; j < pHDTable.getColumns().size(); j++) {
                    if(pHDTable.getColumns().get(j).getCellData(i) != null) {
                        row.createCell(j).setCellValue(pHDTable.getColumns().get(j).getCellData(i).toString());
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
    public void onSelectPHD(Event event) {
        int selectedIndex = pHDTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            pHDModosit.setDisable(false);
            pHDTorol.setDisable(false);
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

    @FXML
    public void onAdoptionTypeClick(ActionEvent actionEvent) {
    }
}
