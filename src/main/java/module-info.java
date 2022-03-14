module com.example.vizsgaremek_asztali {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.poi.ooxml;
    requires java.desktop;

    opens com.example.vizsgaremek_asztali to javafx.fxml, com.google.gson;
    exports com.example.vizsgaremek_asztali;
    exports com.example.vizsgaremek_asztali.dogs;
    opens com.example.vizsgaremek_asztali.dogs to javafx.fxml, com.google.gson;
    exports com.example.vizsgaremek_asztali.api;
    opens com.example.vizsgaremek_asztali.api to com.google.gson, javafx.fxml;
    exports com.example.vizsgaremek_asztali.cats;
    opens com.example.vizsgaremek_asztali.cats to com.google.gson, javafx.fxml;
    exports com.example.vizsgaremek_asztali.adoption;
    opens com.example.vizsgaremek_asztali.adoption to com.google.gson, javafx.fxml;
    exports com.example.vizsgaremek_asztali.adoptionType;
    opens com.example.vizsgaremek_asztali.adoptionType to com.google.gson, javafx.fxml;
}