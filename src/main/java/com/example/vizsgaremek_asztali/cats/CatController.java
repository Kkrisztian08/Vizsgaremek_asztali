package com.example.vizsgaremek_asztali.cats;

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


public class CatController extends Controller {
    @FXML
    private TextArea leirasKulTulTextArea;
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableColumn<Cat,String> szulIdoCol;
    @FXML
    private TableColumn<Cat,Integer> adoptionIdCol;
    @FXML
    private Button catModosit;
    @FXML
    private TableColumn<Cat,String> kulsoCol;
    @FXML
    private Button catTorol;
    @FXML
    private TableView<Cat> macskakTable;
    @FXML
    private TableColumn<Cat,String>  leirasCol;
    @FXML
    private TableColumn<Cat,String>  nevCol;
    @FXML
    private TableColumn<Cat,String>  nemCol;
    @FXML
    private TableColumn<Cat,Integer> erdeklodesCol;
    private final ObservableList<Cat> macskakLista = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Cat,Integer> idColmacska;


    public void initialize(){
        idColmacska.setCellValueFactory(new PropertyValueFactory<>("id"));
        nevCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nemCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        szulIdoCol.setCellValueFactory(new PropertyValueFactory<>("likely_bday"));
        kulsoCol.setCellValueFactory(new PropertyValueFactory<>("external_property"));
        leirasCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        erdeklodesCol.setCellValueFactory(new PropertyValueFactory<>("interest"));
        adoptionIdCol.setCellValueFactory(new PropertyValueFactory<>("adoption_id"));
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
        FilteredList<Cat> filteredList = new FilteredList<>(macskakLista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(cat -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (cat.getName().toLowerCase().contains(kereses)) {
                    return true;
                }
                else if(cat.getGender().toLowerCase().contains(kereses)){
                    return true;
                }
                else if(cat.getLikely_bday().toLowerCase().contains(kereses)){
                    return true;
                }
                else if(cat.getExternal_property().toLowerCase().contains(kereses)){
                    return true;
                }
                else {
                    return false;
                }

            });
        });
        SortedList<Cat> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(macskakTable.comparatorProperty());
        macskakTable.setItems(sortedList);
    }

    @FXML
    public void onHozzaadMacska(ActionEvent actionEvent) {
        try {
            CatHozzaadController hozzadas = (CatHozzaadController) ujAblak("FXML/cats/hozzaad-view.fxml", "Macska hozz??ad??sa",
                    600, 400);
            hozzadas.setRunnableAfterHozzaadas(this::macskakListaFeltolt);
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onModositMacska(ActionEvent actionEvent) {
        int selectedIndex = macskakTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A m??dos??t??shoz el??bb v??lasszon ki egy elemet a t??bl??zatb??l");
            return;
        }
        Cat modositandomacska = macskakTable.getSelectionModel().getSelectedItem();
        try {
            CatModositController modosita = (CatModositController) ujAblak("FXML/cats/modosit-view.fxml", "Adatok M??dos??t??sa",
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
            alert("A t??rl??shez el??bb v??lasszon ki egy elemet a t??bl??zatb??l");
            return;
        }
        Cat torlendoMacska = macskakTable.getSelectionModel().getSelectedItem();
        if (!confirm("Val??ban t??r??lni szeretn?? "  +torlendoMacska.getName() + " macska adatait")){
            return;
        }
        try {
            boolean sikeres= CatApi.delete(torlendoMacska.getId());
            alert(sikeres? "Sikeres t??rl??s": "Sikertelen t??rl??s");
            macskakLista.clear();
            macskakListaFeltolt();
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onExportMacskaTabla(ActionEvent actionEvent) {
        //String filenev = "Macskak t??bla";
        FileChooser choose = new FileChooser();
        choose.setTitle("Export??l??s");
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("MS Excel", "*.xlsx"));
        File file = choose.showSaveDialog(stage);
        if(!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        if (file.exists() == false) {
            Workbook workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("macsk??k t??bla");

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
                alert("Sikeres export??l??s");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            alerthiba("Sikertelen export??l??s! A file m??r l??tezik ezen a helyen!");
        }
    }

    @FXML
    public void onSelectCat(Event event) {
        int selectedIndex = macskakTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            catModosit.setDisable(false);
            catTorol.setDisable(false);
        }
        Cat leiraskiir= macskakTable.getSelectionModel().getSelectedItem();
        leirasKulTulTextArea.setText("Le??r??s:\n"+leiraskiir.getDescription()+"\n\nK??l.tul.:\n"+leiraskiir.getExternal_property());

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
    public void onExit(ActionEvent actionEvent) {

        if (!confirm("Biztos szeretne kijelentkezni?")){
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
