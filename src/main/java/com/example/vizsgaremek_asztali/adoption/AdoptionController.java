package com.example.vizsgaremek_asztali.adoption;

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

public class AdoptionController extends Controller {
    @FXML
    private TableView<Adoption> adoptionTable;
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableColumn<Adoption,Integer> userIdCol;
    @FXML
    private TableColumn<Adoption,Integer> adoptionIdCol;
    @FXML
    private TableColumn<Adoption,String> beginCol;
    @FXML
    private Button adoptionModosit;
    @FXML
    private Button adoptionTorol;
    @FXML
    private TableColumn<Adoption,Integer> adoptionTypeIdCol;
    private  ObservableList<Adoption> adoptionsLista = FXCollections.observableArrayList();



    public void initialize(){
        adoptionIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        adoptionTypeIdCol.setCellValueFactory(new PropertyValueFactory<>("FormazottType"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        beginCol.setCellValueFactory(new PropertyValueFactory<>("begin"));
        adoptionsListaFeltolt();
        kereses();
    }
    public void adoptionsListaFeltolt() {
        adoptionTorol.setDisable(true);
        adoptionModosit.setDisable(true);
        try {
            adoptionsLista.clear();
            adoptionsLista.addAll(AdoptionApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }
    private void kereses() {
        FilteredList<Adoption> filteredList = new FilteredList<>(adoptionsLista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(adoptions -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (adoptions.getBegin().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }else if (adoptions.getFormazottType().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }
                else {
                    return false;
                }
            });
        });
        SortedList<Adoption> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(adoptionTable.comparatorProperty());
        adoptionTable.setItems(sortedList);
    }

    @FXML
    public void onHozzaadAdoption(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/adoptions/hozzaad-view.fxml", "Örökbefogadás hozzáadása",
                    600, 400);
            hozzadas.getStage().setOnCloseRequest(event -> adoptionsListaFeltolt());
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onModositAdoption(ActionEvent actionEvent) {
        int selectedIndex = adoptionTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A módosításhoz előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Adoption modositandoAdoption = adoptionTable.getSelectionModel().getSelectedItem();
        try {
            AdoptionModositController modosita = (AdoptionModositController) ujAblak("FXML/adoptions/modosit-view.fxml", "Adatok Módosítása",
                    700, 450);
            modosita.setModositando(modositandoAdoption);
            modosita.getStage().setOnHiding(event -> adoptionTable.refresh());
            modosita.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onAdoptionTorol(ActionEvent actionEvent) {
        int selectedIndex = adoptionTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Adoption torlendoAdoption = adoptionTable.getSelectionModel().getSelectedItem();
        if (!confirm("Valóban törölni szeretné "  +torlendoAdoption.getId() + "-s ID-val rendelkező örökbefogadást?")){
            return;
        }
        try {
            boolean sikeres= AdoptionApi.delete(torlendoAdoption.getId());
            alert(sikeres? "Sikertelen törlés": "Sikeres törlés");
            adoptionsLista.clear();
            adoptionsListaFeltolt();
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onExportAdoptionTabla(ActionEvent actionEvent) {
        //String filenev = "Örököbefogadás tábla";
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

            for (int j = 0; j < adoptionTable.getColumns().size(); j++) {
                row.createCell(j).setCellValue(adoptionTable.getColumns().get(j).getText());
            }

            for (int i = 0; i < adoptionTable.getItems().size(); i++) {
                row = spreadsheet.createRow(i + 1);
                for (int j = 0; j < adoptionTable.getColumns().size(); j++) {
                    if(adoptionTable.getColumns().get(j).getCellData(i) != null) {
                        row.createCell(j).setCellValue(adoptionTable.getColumns().get(j).getCellData(i).toString());
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
    public void onSelectAdoption(Event event) {
        int selectedIndex = adoptionTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            adoptionModosit.setDisable(false);
            adoptionTorol.setDisable(false);
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
}
