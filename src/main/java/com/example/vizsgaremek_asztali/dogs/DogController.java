package com.example.vizsgaremek_asztali.dogs;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.stage.FileChooser;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

public class DogController extends Controller {
    @FXML
    private TableColumn<Dogs, String> szulIdoCol;
    @FXML
    private TableColumn<Dogs, Integer> adoptionIdCol;
    @FXML
    private TableColumn<Dogs,String> fajCol;
    @FXML
    private TableColumn<Dogs,String> kulsoCol;
    @FXML
    private TableView<Dogs> kutyakTable;
    @FXML
    private TableColumn<Dogs,String> nevCol;
    @FXML
    private TableColumn<Dogs,String> nemCol;
    @FXML
    private TableColumn<Dogs, Integer> erdeklodesCol;
    @FXML
    private TableColumn<Dogs, Integer> idCol;
    @FXML
    private TableColumn<Dogs, String> leirasCol;
    @FXML
    private  Button dogModosit;
    @FXML
    private  Button dogTorol;
    @FXML
    private TextArea leirasKulTulTextArea;
    @FXML
    private TextField keresesTextField;
    private  ObservableList<Dogs> kutyakLista = FXCollections.observableArrayList();

    public void initialize(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nevCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nemCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        szulIdoCol.setCellValueFactory(new PropertyValueFactory<>("likely_bday"));
        fajCol.setCellValueFactory(new PropertyValueFactory<>("species"));
        kulsoCol.setCellValueFactory(new PropertyValueFactory<>("external_property"));
        leirasCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        erdeklodesCol.setCellValueFactory(new PropertyValueFactory<>("interest"));
        adoptionIdCol.setCellValueFactory(new PropertyValueFactory<>("adoption_id"));
        kutyakListaFeltolt();
        kereses();
    }

    public  void kutyakListaFeltolt() {
        dogTorol.setDisable(true);
        dogModosit.setDisable(true);
        try {
            kutyakLista.clear();
            kutyakLista.addAll(DogApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    private void kereses(){
        FilteredList<Dogs> filteredList = new FilteredList<>(kutyakLista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(dog -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (dog.getName().toLowerCase().indexOf(kereses) > -1) {
                    return true;
                }
                else if(dog.getGender().toLowerCase().indexOf(kereses) > -1){
                    return true;
                }
                else if(dog.getSpecies().toLowerCase().indexOf(kereses) > -1){
                    return true;
                }
                else if(dog.getLikely_bday().toLowerCase().indexOf(kereses) > -1){
                    return true;
                }
                else if(dog.getExternal_property().toLowerCase().indexOf(kereses) > -1){
                    return true;
                }
                else {
                    return false;
                }

            });
        });
        SortedList<Dogs> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(kutyakTable.comparatorProperty());
        kutyakTable.setItems(sortedList);
    }
    @FXML
    public void onHozzaadKutya(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/dogs/hozzaad-view.fxml", "Kutya hozzáadása",
                    600, 400);
            hozzadas.getStage().setOnCloseRequest(event -> kutyakListaFeltolt());
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }
    @FXML
    public void onModositKutya(ActionEvent actionEvent) {
        int selectedIndex = kutyakTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A módosításhoz előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Dogs modositando = kutyakTable.getSelectionModel().getSelectedItem();
        try {
            DogModositController modositas = (DogModositController) ujAblak("FXML/dogs/modosit-view.fxml", "Adatok Módosítása",
                    700, 450);
            modositas.setModositando(modositando);
            modositas.getStage().setOnHiding(event -> kutyakTable.refresh());
            modositas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }
    @FXML
    public void onKutyaTorol(ActionEvent actionEvent) {
        int selectedIndex = kutyakTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Dogs torlendoKutya = kutyakTable.getSelectionModel().getSelectedItem();
        if (!confirm("Valóban törölni szeretné "  +torlendoKutya.getName() + " kutya adatait")){
            return;
        }
        try {
            boolean sikeres= DogApi.delete(torlendoKutya.getId());
            alert(sikeres? "Sikertelen törlés": "Sikeres törlés");
            kutyakLista.clear();
            kutyakListaFeltolt();
        } catch (IOException e) {
            hibaKiir(e);
        }
    }
    @FXML
    public void onExportKutyakTabla(ActionEvent actionEvent) throws IOException {
        //String filenev = "Kutyák tábla";
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

            for (int j = 0; j < kutyakTable.getColumns().size(); j++) {
                row.createCell(j).setCellValue(kutyakTable.getColumns().get(j).getText());
            }

            for (int i = 0; i < kutyakTable.getItems().size(); i++) {
                row = spreadsheet.createRow(i + 1);
                for (int j = 0; j < kutyakTable.getColumns().size(); j++) {
                    if(kutyakTable.getColumns().get(j).getCellData(i) != null) {
                        row.createCell(j).setCellValue(kutyakTable.getColumns().get(j).getCellData(i).toString());
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
    public void onSelectDog(Event event) {
        int selectedIndex = kutyakTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            dogModosit.setDisable(false);
            dogTorol.setDisable(false);
        }
        Dogs leiraskiir= kutyakTable.getSelectionModel().getSelectedItem();
        leirasKulTulTextArea.setText("Leírás:\n"+leiraskiir.getDescription()+"\n\nKül.tul.:\n"+leiraskiir.getExternal_property());

    }

    @FXML
    public void onMacskakClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/cats/cats-view.fxml", "Macskák Tábla",
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
            Controller oldalvaltas = ujAblak("FXML/dogs/dogs-view.fxml", "Kutyák Tábla",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }

    }

    @FXML
    public void onProgramApplicationClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onEventClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/events/events-view.fxml", "Események Tábla",
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
            Controller oldalvaltas = ujAblak("FXML/programTypes/programTypes-view.fxml", "Program Típus Tábla",
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
            Controller oldalvaltas = ujAblak("FXML/adoptionTypes/adoptionTypes-view.fxml", "Örökbefogadási Típus Tábla",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onProgramHourAndDayClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onAdoptionClick(ActionEvent actionEvent) {
        try {
            Controller oldalvaltas = ujAblak("FXML/adoptions/adoptions-view.fxml", "Örökbefogadás Tábla",
                    1100, 600);
            oldalvaltas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }
}