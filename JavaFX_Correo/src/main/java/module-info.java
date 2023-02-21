module com.mycompany.JavaFX_Correo {
    requires javafx.controls;
    requires javafx.fxml;
	requires commons.net;
	requires javafx.graphics;

    opens com.mycompany.JavaFX_Correo to javafx.fxml;
    exports com.mycompany.JavaFX_Correo;
}
