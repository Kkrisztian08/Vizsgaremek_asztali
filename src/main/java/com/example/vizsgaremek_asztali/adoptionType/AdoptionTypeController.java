package com.example.vizsgaremek_asztali.adoptionType;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.ElethangApp;
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

public class AdoptionTypeController extends Controller {
    @FXML
    private TextField keresesTextField;
    @FXML
    private Button typeModosit;
    @FXML
    private Button typeTorol;
    @FXML
    private TableView<AdoptionType> tipusTable;
    @FXML
    private TableColumn<AdoptionType,Integer> typeidCol;
    @FXML
    private TableColumn<AdoptionType,String> typeCol;
    private ObservableList<AdoptionType> typeLista = FXCollections.observableArrayList();

    public void initialize(){
        typeidCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeListaFeltolt();
        kereses();
    }

    public  void typeListaFeltolt() {
        typeModosit.setDisable(true);
        typeTorol.setDisable(true);
        try {
            typeLista.clear();
            typeLista.addAll(AdoptionTypeApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    private void kereses(){
        FilteredList<AdoptionType> filteredList = new FilteredList<>(typeLista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(type -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (type.getType().toLowerCase().contains(kereses)) {
                    return true;
                }
                else {
                    return false;
                }

            });
        });
        SortedList<AdoptionType> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tipusTable.comparatorProperty());
        tipusTable.setItems(sortedList);
    }

    @FXML
    public void onHozzaadType(ActionEvent actionEvent) {
        try {
            AdoptionTypeHozzaadController hozzadas = (AdoptionTypeHozzaadController) ujAblak("FXML/adoptionTypes/hozzaad-view.fxml", "Típus hozzáadása",
                    500, 230);
            hozzadas.setRunnableAfterHozzaadas(this::typeListaFeltolt);
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onModositType(ActionEvent actionEvent) {
        int selectedIndex = tipusTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A módosításhoz előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        AdoptionType modositandomacska = tipusTable.getSelectionModel().getSelectedItem();
        try {
            AdoptionTypeModositController modosita = (AdoptionTypeModositController) ujAblak("FXML/adoptionTypes/modosit-view.fxml", "Adatok Módosítása",
                    500, 230);
            modosita.setModositando(modositandomacska);
            modosita.getStage().setOnHiding(event -> tipusTable.refresh());
            modosita.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onTypeTorol(ActionEvent actionEvent) {
        int selectedIndex = tipusTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        AdoptionType torlendoAdoptionType = tipusTable.getSelectionModel().getSelectedItem();
        if (!confirm("Valóban törölni szeretné a következő örökbefogadási típust: "  +torlendoAdoptionType.getType() + "?")){
            return;
        }
        try {
            boolean sikeres= AdoptionTypeApi.delete(torlendoAdoptionType.getId());
            alert(sikeres? "Sikeres törlés": "Sikertelen törlés");
            typeLista.clear();
            typeListaFeltolt();
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onExportTypeTabla(ActionEvent actionEvent) {
        //String filenev = "Örököbefogadási Tipus tábla";
        FileChooser choose = new FileChooser();
        choose.setTitle("Exportálás");
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("MS Excel", "*.xlsx"));
        File file = choose.showSaveDialog(stage);
        if(!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        if (file.exists() == false) {
            Workbook workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("örökbefogadási típus tábla");

            Row row = spreadsheet.createRow(0);

            for (int j = 0; j < tipusTable.getColumns().size(); j++) {
                row.createCell(j).setCellValue(tipusTable.getColumns().get(j).getText());
            }

            for (int i = 0; i < tipusTable.getItems().size(); i++) {
                row = spreadsheet.createRow(i + 1);
                for (int j = 0; j < tipusTable.getColumns().size(); j++) {
                    if(tipusTable.getColumns().get(j).getCellData(i) != null) {
                        row.createCell(j).setCellValue(tipusTable.getColumns().get(j).getCellData(i).toString());
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
            alerthiba("Sikertelen exportálás! A file már létezik ezen a helyen!");
        }
    }

    @FXML
    public void onSelectType(Event event) {
        int selectedIndex = tipusTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            typeTorol.setDisable(false);
            typeModosit.setDisable(false);
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
            if ( ElethangApp.BEJELENTKEZETT.getAdmin() == 1) {
                Controller oldalvaltas = ujAblak("FXML/users/users-view.fxml", "Élethang alapitvány",
                        1100, 600);
                oldalvaltas.getStage().show();
                this.stage.close();
            }else if ( ElethangApp.BEJELENTKEZETT.getAdmin() == 2) {
                Controller oldalvaltas = ujAblak("FXML/superAdmin/superAdminUsers-view.fxml", "Élethang alapitvány",
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
