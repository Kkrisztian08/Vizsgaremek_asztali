module com.example.vizsgaremek_asztali {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.vizsgaremek_asztali to javafx.fxml;
    exports com.example.vizsgaremek_asztali;
    exports com.example.vizsgaremek_asztali.Controllers;
    opens com.example.vizsgaremek_asztali.Controllers to javafx.fxml;
}