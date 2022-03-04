module com.example.vizsgaremek_asztali {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;
    //requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.example.vizsgaremek_asztali to javafx.fxml, com.google.gson;
    exports com.example.vizsgaremek_asztali;
    exports com.example.vizsgaremek_asztali.dogs;
    opens com.example.vizsgaremek_asztali.dogs to javafx.fxml, com.google.gson;
    exports com.example.vizsgaremek_asztali.api;
    opens com.example.vizsgaremek_asztali.api to com.google.gson, javafx.fxml;
}