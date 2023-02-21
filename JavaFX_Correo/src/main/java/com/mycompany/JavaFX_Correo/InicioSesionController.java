package com.mycompany.JavaFX_Correo;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.commons.net.pop3.POP3Client;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class InicioSesionController implements Initializable {

	@FXML private TextField txtMail, txtPass;
	@FXML private Button btnConnect;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// CONEXION A EL DOMINIO
		POP3Client client = new POP3Client();
		try {
			client.connect("mail.damiansu.com");
			
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		btnConnect.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {

				String mail = txtMail.getText().toString();
				String pass = txtPass.getText().toString();
				
				
				try {
					if(!client.login(mail, pass)) {

						txtMail.setStyle("-fx-border-color: RED; -fx-border-radius: 5");
						txtPass.setStyle("-fx-border-color: RED; -fx-border-radius: 5");
						
						// dialogo de error
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("ERROR AL INICIAR SESIÓN");
						alert.setHeaderText("Ha ocurrido un error al intentar iniciar sesión");
						alert.setContentText("Por favor compruebe su usuario o su contraseña");

						alert.showAndWait();
						
					} else {
						
						FXMLLoader loader = new FXMLLoader(App.class.getResource("show.fxml"));
						Parent root = loader.load();
						ShowController controller = loader.getController();
						controller.recibirCorreo(mail, pass);
						App.scene.setRoot(root);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	// BTNCONNECT ONMOUSEENTERED & ONMOUSEEXITED
	@FXML private void btnConnectEntered() {
		
		btnConnect.setFont(new Font("System BOLD", 18));
		btnConnect.setStyle("-fx-background-color: #6fb238");
	}
	
	@FXML private void btnConnectExited() {
		btnConnect.setFont(new Font("System BOLD", 16));
		btnConnect.setStyle("-fx-background-color: #9fdd6f");
	}
	
}
