module com.example.santorini_engine_gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.santorini_engine_gui to javafx.fxml;
    exports com.example.santorini_engine_gui;
}