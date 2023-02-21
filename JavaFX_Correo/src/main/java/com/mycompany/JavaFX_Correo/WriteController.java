package com.mycompany.JavaFX_Correo;

import java.io.IOException;
import java.io.Writer;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SimpleSMTPHeader;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class WriteController implements Initializable {
	
	@FXML Button btnReturn, btnSend;
	@FXML TextField txtTo, txtSubject;
	@FXML TextArea txtMes;
	
	POP3Client client;
	String mail, pass;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// ESTABLECER IMAGENES PARA BTNNEXT & BTNPREV
		btnReturn.setGraphic(new ImageView(getClass().getResource("img/return.png").toExternalForm()));
		btnSend.setGraphic(new ImageView(getClass().getResource("img/send.png").toExternalForm()));
		
		// CONEXION CON DOMMINIO CORREO
		client = new POP3Client();
		try {
			client.connect("mail.damiansu.com");

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		// BTNRETURN MOUSEONCLICK
		btnReturn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				FXMLLoader loader = new FXMLLoader(App.class.getResource("show.fxml"));
				Parent root;
				try {
					root = loader.load();
					ShowController controller = loader.getController();
					controller.recibirCorreo(mail, pass);
					App.scene.setRoot(root);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
		// BTNSEND MOUSEONCLICK
		btnSend.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				if(!txtTo.getText().isEmpty() && !txtMes.getText().isEmpty() && !txtSubject.getText().isEmpty()) {
					
					// conexion cliente smtp
					SMTPClient cliente = new SMTPClient();
			        try {
						cliente.connect("mail.damiansu.com");

						
						// cargamos los valores en las variables
				        String asunto = txtSubject.getText().toString();
				        String msg = txtMes.getText().toString();
				        String remit = mail;
						String dest = txtTo.getText().toString();
						String[] dests = dest.split(" ");
						
						// establecemos la cabecera, receptor y emisor
						SimpleSMTPHeader cabecera = new SimpleSMTPHeader(remit, dests[0], asunto);
				        cliente.setSender(remit);
						for(String val: dests) {

							cliente.addRecipient(val);
						}
				        
				        // enviamos el mensaje
				        Writer escribo = cliente.sendMessageData();
				        escribo.write(cabecera.toString());
				        escribo.write(msg);
				        escribo.close();
						
			        } catch (SocketException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
			        txtMes.clear();
			        txtSubject.clear();
			        txtTo.clear();
			        
			        
				} else {
					
					// dialogo de error
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR AL ENVIAR MENSAJE");
					alert.setHeaderText("Ha ocurrido un error al intentar enviar el mnsaje");
					alert.setContentText("Por favor compruebe que todos los campos han sido completados");

					alert.showAndWait();
				}
				
		        
			}
		});
		
		
		// BTNRETURN MOUSEENTERED & MOUSEEXITED
		btnReturn.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				btnReturn.setStyle("-fx-background-color: #aeaeae");
			}
		});
		
		btnReturn.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				btnReturn.setStyle("-fx-background-color: TRANSPARENT");
			}
		});
		
		
		// BTNSEND MOUSEENTERED & MOUSEEXITED
		btnSend.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				btnSend.setFont(new Font("System BOLD", 18));
				btnSend.setStyle("-fx-background-color: #79c933");
			}
		});

		btnSend.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				btnSend.setFont(new Font("System BOLD", 16));
				btnSend.setStyle("-fx-background-color: #89cd51");
			}
		});

	}
	
	
	
	// METODO RECIBIR VARIABLES DE VENTANA INICIOSESION
		public void recibirCorreo(String correo, String contra) {
			pass = contra;
			mail = correo;
			
			try {
				client.login(correo, contra);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

}
