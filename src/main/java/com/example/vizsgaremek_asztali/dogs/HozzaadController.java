package com.example.vizsgaremek_asztali.dogs;

import com.example.vizsgaremek_asztali.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HozzaadController extends Controller {

    @FXML
    private TextField nevInput;
    @FXML
    private DatePicker szulidoInput;
    @FXML
    private Spinner KedvelésInput;
    @FXML
    private TextField fajInput;
    @FXML
    private TextArea leirasInput;
    @FXML
    private TextArea kultulInput;
    @FXML
    private ComboBox nemInput;
    @FXML
    private ComboBox orokbefogadasInput;

    @FXML
    public void onHozzaadas(ActionEvent actionEvent) {
    }
}
