package com.example.vizsgaremek_asztali.adoption;

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
                if (adoptions.getBegin().toLowerCase().contains(kereses)) {
                    return true;
                }else if (adoptions.getFormazottType().toLowerCase().contains(kereses)) {
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
    public void onHozzaadDogAdoption(ActionEvent actionEvent) {
        try {
            AdoptionDogHozzaadController hozzadas = (AdoptionDogHozzaadController) ujAblak("FXML/adoptions/hozzaadDog-view.fxml", "??r??kbefogad??s hozz??ad??sa",
                    600, 400);
            hozzadas.setRunnableAfterHozzaadas(this::adoptionsListaFeltolt);
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onHozzaadCatAdoption(ActionEvent actionEvent) {
        try {
            AdoptionCatHozzaadController hozzadas = (AdoptionCatHozzaadController) ujAblak("FXML/adoptions/hozzaadCat-view.fxml", "??r??kbefogad??s hozz??ad??sa",
                    600, 400);
            hozzadas.setRunnableAfterHozzaadas(this::adoptionsListaFeltolt);
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onModositAdoption(ActionEvent actionEvent) {
        int selectedIndex = adoptionTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A m??dos??t??shoz el??bb v??lasszon ki egy elemet a t??bl??zatb??l");
            return;
        }
        Adoption modositandoAdoption = adoptionTable.getSelectionModel().getSelectedItem();
        try {
            AdoptionModositController modosita = (AdoptionModositController) ujAblak("FXML/adoptions/modosit-view.fxml", "Adatok M??dos??t??sa",
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
            alert("A t??rl??shez el??bb v??lasszon ki egy elemet a t??bl??zatb??l");
            return;
        }
        Adoption torlendoAdoption = adoptionTable.getSelectionModel().getSelectedItem();
        if (!confirm("Val??ban t??r??lni szeretn?? "  +torlendoAdoption.getId() + "-s ID-val rendelkez?? ??r??kbefogad??st?")){
            return;
        }
        try {
            boolean sikeres= AdoptionApi.delete(torlendoAdoption.getId());
            alert(sikeres? "Sikeres t??rl??s": "Sikertelen t??rl??s");
            adoptionsLista.clear();
            adoptionsListaFeltolt();
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onExportAdoptionTabla(ActionEvent actionEvent) {
        //String filenev = "??r??k??befogad??s t??bla";
        FileChooser choose = new FileChooser();
        choose.setTitle("Export??l??s");
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("MS Excel", "*.xlsx"));
        File file = choose.showSaveDialog(stage);
        if(!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        if (file.exists() == false) {
            Workbook workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("??r??kbefogad??s t??bla");

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
