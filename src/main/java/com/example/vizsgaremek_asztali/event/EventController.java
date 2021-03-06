package com.example.vizsgaremek_asztali.event;

import com.example.vizsgaremek_asztali.Controller;
import com.example.vizsgaremek_asztali.ElethangApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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

public class EventController extends Controller {
    @FXML
    private TextField keresesTextField;
    @FXML
    private TableColumn<Event,Integer> idCol;
    @FXML
    private TableColumn<Event,String> leirasCol;
    @FXML
    private TextArea leirasTextArea;
    @FXML
    private TableColumn<Event,String> elnevezesCol;
    @FXML
    private TableColumn<Event,String> datumCol;
    @FXML
    private TableView<Event> eventTable;
    @FXML
    private Button eventTorol;
    @FXML
    private Button eventModosit;
    private ObservableList<Event> eventsLista = FXCollections.observableArrayList();

    public void initialize(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        leirasCol.setCellValueFactory(new PropertyValueFactory<>("leiras"));
        elnevezesCol.setCellValueFactory(new PropertyValueFactory<>("elnevezes"));
        datumCol.setCellValueFactory(new PropertyValueFactory<>("datum"));
        eventsListaFeltolt();
        kereses();
    }

    public  void eventsListaFeltolt() {
        eventModosit.setDisable(true);
        eventTorol.setDisable(true);
        try {
            eventsLista.clear();
            eventsLista.addAll(EventApi.get());
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    private void kereses(){
        FilteredList<Event> filteredList = new FilteredList<>(eventsLista, b -> true);
        keresesTextField.textProperty().addListener((observable, oldValue, newValue ) -> {
            filteredList.setPredicate(events -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String kereses = newValue.toLowerCase();
                if (events.getElnevezes().toLowerCase().contains(kereses)) {
                    return true;
                }
                else if(events.getLeiras().toLowerCase().contains(kereses)){
                    return true;
                }
                else if(events.getDatum().toLowerCase().contains(kereses)){
                    return true;
                }
                else {
                    return false;
                }
            });
        });
        SortedList<Event> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(eventTable.comparatorProperty());
        eventTable.setItems(sortedList);
    }

    @FXML
    public void onHozzaadEvent(ActionEvent actionEvent) {
        try {
            EventHozzaadController hozzadas = (EventHozzaadController) ujAblak("FXML/events/hozzaad-view.fxml", "Esem??ny hozz??ad??sa",
                    500, 474);
            hozzadas.setRunnableAfterHozzaadas(this::eventsListaFeltolt);
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onModositEvent(ActionEvent actionEvent) {
        int selectedIndex = eventTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A m??dos??t??shoz el??bb v??lasszon ki egy elemet a t??bl??zatb??l");
            return;
        }
        Event modositando = eventTable.getSelectionModel().getSelectedItem();
        try {
            EventModositController modositas = (EventModositController) ujAblak("FXML/events/modosit-view.fxml", "Adatok M??dos??t??sa",
                    500, 474);
            modositas.setModositando(modositando);
            modositas.getStage().setOnHiding(event -> eventTable.refresh());
            modositas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onEventTorol(ActionEvent actionEvent) {
        int selectedIndex = eventTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            alert("A t??rl??shez el??bb v??lasszon ki egy elemet a t??bl??zatb??l");
            return;
        }
        Event torlendoEvent = eventTable.getSelectionModel().getSelectedItem();
        if (!confirm("Val??ban t??r??lni szeretn?? a(z)"  +torlendoEvent.getElnevezes() + " nev?? esem??nyt?")){
            return;
        }
        try {
            boolean sikeres= EventApi.delete(torlendoEvent.getId());
            alert(sikeres? "Sikeres t??rl??s": "Sikertelen t??rl??s");
            eventsLista.clear();
            eventsListaFeltolt();
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
        if(!file.getName().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        if (file.exists() == false) {
            Workbook workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("rendezv??ny t??bla");

            Row row = spreadsheet.createRow(0);

            for (int j = 0; j < eventTable.getColumns().size(); j++) {
                row.createCell(j).setCellValue(eventTable.getColumns().get(j).getText());
            }

            for (int i = 0; i < eventTable.getItems().size(); i++) {
                row = spreadsheet.createRow(i + 1);
                for (int j = 0; j < eventTable.getColumns().size(); j++) {
                    if(eventTable.getColumns().get(j).getCellData(i) != null) {
                        row.createCell(j).setCellValue(eventTable.getColumns().get(j).getCellData(i).toString());
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
    public void onSelectEvent(javafx.event.Event event) {
        int selectedIndex = eventTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            eventTorol.setDisable(false);
            eventModosit.setDisable(false);
        }
        Event leiraskiir= eventTable.getSelectionModel().getSelectedItem();
        leirasTextArea.setText("Le??r??s:\n"+leiraskiir.getLeiras());

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
