package com.example.vizsgaremek_asztali.dogs;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.stage.FileChooser;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

public class DogController extends Controller {
    @FXML
    private TableColumn<Dog, String> szulIdoCol;
    @FXML
    private TableColumn<Dog, Integer> adoptionIdCol;
    @FXML
    private TableColumn<Dog,String> fajCol;
    @FXML
    private TableColumn<Dog,String> kulsoCol;
    @FXML
    private TableView<Dog> kutyakTable;
    @FXML
    private TableColumn<Dog,String> nevCol;
    @FXML
    private TableColumn<Dog,String> nemCol;
    @FXML
    private TableColumn<Dog, Integer> erdeklodesCol;
    @FXML
    private TableColumn<Dog, Integer> idCol;
    @FXML
    private TableColumn<Dog, String> leirasCol;
    @FXML
    private  Button dogModosit;
    @FXML
    private  Button dogTorol;
    @FXML
    private TextArea leirasKulTulTextArea;
    @FXML
    private TextField keresesTextField;
    private  ObservableList<Dog> kutyakLista = FXCollections.observableArrayList();

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

    public void kutyakListaFeltolt() {
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
        FilteredList<Dog> filteredList = new FilteredList<>(kutyakLista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(dog -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (dog.getName().toLowerCase().contains(kereses)) {
                    return true;
                }
                else if(dog.getGender().toLowerCase().contains(kereses)){
                    return true;
                }
                else if(dog.getSpecies().toLowerCase().contains(kereses)){
                    return true;
                }
                else if(dog.getLikely_bday().toLowerCase().contains(kereses)){
                    return true;
                }
                else if(dog.getExternal_property().toLowerCase().contains(kereses)){
                    return true;
                }
                else {
                    return false;
                }

            });
        });
        SortedList<Dog> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(kutyakTable.comparatorProperty());
        kutyakTable.setItems(sortedList);
    }
    @FXML
    public void onHozzaadKutya(ActionEvent actionEvent) {
        try {
            DogHozzaadController hozzadas = (DogHozzaadController) ujAblak("FXML/dogs/hozzaad-view.fxml", "Kutya hozz??ad??sa",
                    600, 400);
            hozzadas.setRunnableAfterHozzaadas(this::kutyakListaFeltolt);
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }
    @FXML
    public void onModositKutya(ActionEvent actionEvent) {
        int selectedIndex = kutyakTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A m??dos??t??shoz el??bb v??lasszon ki egy elemet a t??bl??zatb??l");
            return;
        }
        Dog modositando = kutyakTable.getSelectionModel().getSelectedItem();
        try {
            DogModositController modositas = (DogModositController) ujAblak("FXML/dogs/modosit-view.fxml", "Adatok M??dos??t??sa",
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
            alert("A t??rl??shez el??bb v??lasszon ki egy elemet a t??bl??zatb??l");
            return;
        }
        Dog torlendoKutya = kutyakTable.getSelectionModel().getSelectedItem();
        if (!confirm("Val??ban t??r??lni szeretn?? "  +torlendoKutya.getName() + " kutya adatait")){
            return;
        }
        try {
            boolean sikeres= DogApi.delete(torlendoKutya.getId());
            alert(sikeres? "Sikeres t??rl??s": "Sikertelen t??rl??s");
            kutyakLista.clear();
            kutyakListaFeltolt();
        } catch (IOException e) {
            hibaKiir(e);
        }
    }
    @FXML
    public void onExportKutyakTabla(ActionEvent actionEvent) throws IOException {
        //String filenev = "Kuty??k t??bla";
        FileChooser choose = new FileChooser();
        choose.setTitle("Export??l??s");
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("MS Excel", "*.xlsx"));
        File file = choose.showSaveDialog(stage);
        if(!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        if (file.exists() == false) {
            Workbook workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("kuty??k t??bla");

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
    public void onSelectDog(Event event) {
        int selectedIndex = kutyakTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            dogModosit.setDisable(false);
            dogTorol.setDisable(false);
        }
        Dog leiraskiir= kutyakTable.getSelectionModel().getSelectedItem();
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
}