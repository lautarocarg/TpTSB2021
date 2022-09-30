module com.tp_etapa2.tp_etapa2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens interfaz to javafx.fxml;
    exports interfaz;
}