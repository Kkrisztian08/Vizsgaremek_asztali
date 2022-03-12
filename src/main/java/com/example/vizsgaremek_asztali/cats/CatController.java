package com.example.vizsgaremek_asztali.cats;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.dogs.DogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CatController extends Controller {
    @FXML
    private TextArea leirasKulTulTextArea;
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableColumn<Cats,String> szulIdoCol;
    @FXML
    private TableColumn<Cats,Integer> adoptionIdCol;
    @FXML
    private Button catModosit;
    @FXML
    private TableColumn<Cats,String> kulsoCol;
    @FXML
    private Button catTorol;
    @FXML
    private TableView<Cats> macskakTable;
    @FXML
    private TableColumn<Cats,String>  leirasCol;
    @FXML
    private TableColumn<Cats,String>  nevCol;
    @FXML
    private TableColumn<Cats,String>  nemCol;
    @FXML
    private TableColumn<Cats,Integer> erdeklodesCol;
    private DogController dogsController;
    private final ObservableList<Cats> macskakLista = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Cats,Integer> idColmacska;


    public void initialize(){
        this.idColmacska.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.nevCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.nemCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        this.szulIdoCol.setCellValueFactory(new PropertyValueFactory<>("likely_bday"));
        this.kulsoCol.setCellValueFactory(new PropertyValueFactory<>("external_property"));
        this.leirasCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.erdeklodesCol.setCellValueFactory(new PropertyValueFactory<>("interest"));
        this.adoptionIdCol.setCellValueFactory(new PropertyValueFactory<>("adoption_id"));
        macskakListaFeltolt();
        kereses();
    }
    public void macskakListaFeltolt() {
        catTorol.setDisable(true);
        catModosit.setDisable(true);
        try {
            macskakLista.clear();
            macskakLista.addAll(CatApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }
    private void kereses() {
        FilteredList<Cats> filteredList = new FilteredList<>(macskakLista, b -> true);
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
        SortedList<Cats> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(macskakTable.comparatorProperty());
        macskakTable.setItems(sortedList);
    }

    @FXML
    public void onHozzaadMacska(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/cats/hozzaad-view.fxml", "Macska hozzáadása",
                    600, 400);
            hozzadas.getStage().setOnCloseRequest(event -> macskakListaFeltolt());
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onModositMacska(ActionEvent actionEvent) {
        int selectedIndex = macskakTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A módosításhoz előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Cats modositandomacska = macskakTable.getSelectionModel().getSelectedItem();
        try {
            CatModositController modosita = (CatModositController) ujAblak("FXML/cats/modosit-view.fxml", "Adatok Módosítása",
                    700, 450);
            modosita.setModositando(modositandomacska);
            modosita.getStage().setOnHiding(event -> macskakTable.refresh());
            modosita.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onMacskaTorol(ActionEvent actionEvent) {
        int selectedIndex = macskakTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Cats torlendoMacska = macskakTable.getSelectionModel().getSelectedItem();
        if (!confirm("Valóban törölni szeretné "  +torlendoMacska.getName() + " kutya adatait")){
            return;
        }
        try {
            boolean sikeres= CatApi.delete(torlendoMacska.getId());
            alert(sikeres? "Sikertelen törlés": "Sikeres törlés");
            macskakLista.clear();
            macskakListaFeltolt();
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onExportMacskaTabla(ActionEvent actionEvent) {
        String filenev = "Macskak tábla";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportálás");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("MS Excel", "xlsx"));
        fileChooser.setSelectedFile(new File(filenev));
        fileChooser.setVisible(true);
        int result = fileChooser.showSaveDialog(fileChooser);
        if (result == JFileChooser.APPROVE_OPTION) {
            filenev = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filenev.endsWith(".xlsx")) {
                filenev+=".xlsx";
            }
        } else {
            return;
        }
        File file = new File(filenev);
        if (file.exists() == false) {
            Workbook workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("kutyák tábla");

            Row row = spreadsheet.createRow(0);

            for (int j = 0; j < macskakTable.getColumns().size(); j++) {
                row.createCell(j).setCellValue(macskakTable.getColumns().get(j).getText());
            }

            for (int i = 0; i < macskakTable.getItems().size(); i++) {
                row = spreadsheet.createRow(i + 1);
                for (int j = 0; j < macskakTable.getColumns().size(); j++) {
                    if(macskakTable.getColumns().get(j).getCellData(i) != null) {
                        row.createCell(j).setCellValue(macskakTable.getColumns().get(j).getCellData(i).toString());
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
    public void onSelectCat(Event event) {
        int selectedIndex = macskakTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            catModosit.setDisable(false);
            catTorol.setDisable(false);
        }
        Cats leiraskiir= macskakTable.getSelectionModel().getSelectedItem();
        leirasKulTulTextArea.setText("Leírás:\n"+leiraskiir.getDescription()+"\n\nKül.tul.:\n"+leiraskiir.getExternal_property());

    }

    @FXML
    public void onKutyakClick(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("FXML/dogs/dogs-view.fxml", "Kutyák tábla",
                    1100, 600);
            hozzadas.getStage().setOnCloseRequest(event -> dogsController.kutyakListaFeltolt());
            hozzadas.getStage().show();
            this.stage.close();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onMacskakClick(ActionEvent actionEvent) {

    }
}