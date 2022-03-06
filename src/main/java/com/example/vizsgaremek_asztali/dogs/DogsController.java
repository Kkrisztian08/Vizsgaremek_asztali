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
import javax.swing.filechooser.FileFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.spi.FileTypeDetector;

import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

public class DogsController extends Controller {

    //táblázat
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
    private Button dogModosit;
    @FXML
    private Button dogTorol;
    @FXML
    private TextArea leirasKulTulTextArea;
    @FXML
    private TextField keresesTextField;

    private ObservableList<Dogs> dogLista = FXCollections.observableArrayList();



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

    private void kutyakListaFeltolt() {
        dogTorol.setDisable(true);
        dogModosit.setDisable(true);
        /*try {
            List<Dogs> dogsList = DogApi.get();
            kutyakTable.getItems().clear();
            for(Dogs dogs: dogsList){
                kutyakTable.getItems().add(dogs);
            }
        } catch (IOException e) {
            hibaKiir(e);
        }*/
        try {
            dogLista.clear();
            dogLista.addAll(DogApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }
    private void kereses(){
        FilteredList<Dogs> filteredList = new FilteredList<>(dogLista, b -> true);
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
        try {
            Controller modositas = ujAblak("FXML/dogs/modosit-view.fxml", "Adatok Módosítása",
                    600, 400);
            modositas.getStage().setOnCloseRequest(event -> kutyakListaFeltolt());
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
            dogLista.clear();
            kutyakListaFeltolt();
        } catch (IOException e) {
            hibaKiir(e);
        }
    }


    @FXML
    public void onExportKutyakTabla(ActionEvent actionEvent) throws FileNotFoundException, IOException {
        String fileDictName = "Kutyák tábla.xlsx";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportálás"); //name for chooser
        fileChooser.setAcceptAllFileFilterUsed(false); //to show or not all other files
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("MS Excel", "xlsx"));
        fileChooser.setSelectedFile(new File(fileDictName)); //when you want to show the name of file into the chooser
        fileChooser.setVisible(true);
        int result = fileChooser.showOpenDialog(fileChooser);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileDictName = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return;
        }
        File file = new File(fileDictName);
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
            try (
                    //Write the workbook in file system
                    FileOutputStream fileOut = new FileOutputStream(file)){
                    workbook.write(fileOut);
                    alert("Sikeres exportálás");
            }
        } else {
            alert("Sikertelen exportálás! A file már létezik!");
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

}