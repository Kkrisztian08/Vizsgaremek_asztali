module com.example.vizsgaremek_asztali {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.vizsgaremek_asztali to javafx.fxml, com.google.gson;
    exports com.example.vizsgaremek_asztali;
    exports com.example.vizsgaremek_asztali.dogs;
    opens com.example.vizsgaremek_asztali.dogs to javafx.fxml, com.google.gson;
}