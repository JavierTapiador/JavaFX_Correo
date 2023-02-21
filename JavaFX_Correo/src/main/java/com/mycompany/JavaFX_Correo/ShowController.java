package com.mycompany.JavaFX_Correo;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class ShowController implements Initializable {

	private static String mail;
	private static String pass;
	private POP3Client client;
	private static int contMails = 1;
	
	@FXML Label lblWelcome, lblCount;
	@FXML TextField txtFrom, txtSubject, txtDate;
	@FXML TextArea txtMes;
	@FXML Button btnShow, btnPrev, btnNext, btnWrite, btnExit;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// NO MARCAR NINGÚN ELEMENTO POR DEFECTO
		txtFrom.setFocusTraversable(false);
		txtSubject.setFocusTraversable(false);
		txtDate.setFocusTraversable(false);
		txtMes.setFocusTraversable(false);
		btnNext.setFocusTraversable(false);
		btnPrev.setFocusTraversable(false);
		
		// ESTABLECER IMAGENES PARA BTNNEXT & BTNPREV
		btnNext.setGraphic(new ImageView(getClass().getResource("img/next.png").toExternalForm()));
		btnPrev.setGraphic(new ImageView(getClass().getResource("img/previous.png").toExternalForm()));
		btnWrite.setGraphic(new ImageView(getClass().getResource("img/write.png").toExternalForm()));
		btnExit.setGraphic(new ImageView(getClass().getResource("img/x.png").toExternalForm()));

		// CONEXION CON DOMMINIO CORREO
		client = new POP3Client();
		try {
			client.connect("mail.damiansu.com");
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// INSERTAR DATOS DEL CORREO
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				
				lblWelcome.setText("Bienvenido "+ mail);
				txtMes.clear();
				txtSubject.clear();
				txtFrom.clear();
				txtDate.clear();
				contMails = 1;
				btnNext.setVisible(true);
				
				// CREAR ARRAY CON TODOS LOS MENSAJES
				try {
					POP3MessageInfo[] mensajes = client.listMessages();
					
					String linea;
					BufferedReader buffer = (BufferedReader) client.retrieveMessage(contMails);
					lblCount.setText("Mensajes: "+ contMails +"/"+ mensajes.length);
					
					boolean escribir = false;
					while((linea = buffer.readLine()) != null) {
						
						if (linea.startsWith("From:")) {
                            txtFrom.setText(linea.substring(5).trim());
                            
						} if (linea.startsWith("Return-Path:")) {
                            txtFrom.setText(linea.substring(12).trim());
						
						} if(linea.startsWith("Date:")) {
							txtDate.setText(linea.substring(5).trim());
							
						} if (linea.startsWith("Subject:")) {
                            txtSubject.setText(linea.substring(8).trim());
						}if (linea.isBlank()) {
                            escribir = true;
						}
						
						if(escribir == true) {
								txtMes.appendText(linea +"\n");
							
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
		// BTNNEXT MOUSECLICKED
		btnNext.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent arg0) {

				// CREAR ARRAY CON TODOS LOS MENSAJES
				try {
					POP3MessageInfo[] mensajes = client.listMessages();
					btnPrev.setVisible(true);
					
					if (contMails < mensajes.length) {
						
						txtMes.clear();
						txtSubject.clear();
						txtFrom.clear();
						contMails++;
						
						lblCount.setText("Mensajes: "+ contMails +"/"+ mensajes.length);
						
						String linea;
						BufferedReader buffer = (BufferedReader) client.retrieveMessage(contMails);
						
						boolean escribir = false;
						while((linea = buffer.readLine()) != null) {
							
							
							if (linea.startsWith("From:")) {
	                            txtFrom.setText(linea.substring(5).trim());
	                            
							} if (linea.startsWith("Return-Path:")) {
	                            txtFrom.setText(linea.substring(12).trim());
							
							} if(linea.startsWith("Date:")) {
								txtDate.setText(linea.substring(5).trim());
								
							} if (linea.startsWith("Subject:")) {
	                            txtSubject.setText(linea.substring(8).trim());
							}if (linea.isBlank()) {
	                            escribir = true;
							}
							
							if(escribir == true) {
									txtMes.appendText(linea +"\n");
								
							}
						}
						
					} else {
						btnNext.setVisible(false);
					}

					

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		// BTPREV MOUSECLICKED
		btnPrev.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				
				// CREAR ARRAY CON TODOS LOS MENSAJES
				try {
					POP3MessageInfo[] mensajes = client.listMessages();
					btnNext.setVisible(true);

					if (contMails > 1) {

						txtMes.clear();
						txtSubject.clear();
						txtFrom.clear();
						contMails--;
						
						lblCount.setText("Mensajes: "+ contMails +"/"+ mensajes.length);
						
						String linea;
						BufferedReader buffer = (BufferedReader) client.retrieveMessage(contMails);
						boolean escribir = false;
						while((linea = buffer.readLine()) != null) {
							
							if (linea.startsWith("From:")) {
	                            txtFrom.setText(linea.substring(5).trim());
	                            
							} if (linea.startsWith("Return-Path:")) {
	                            txtFrom.setText(linea.substring(12).trim());
							
							} if(linea.startsWith("Date:")) {
								txtDate.setText(linea.substring(5).trim());
								
							} if (linea.startsWith("Subject:")) {
	                            txtSubject.setText(linea.substring(8).trim());
							}if (linea.isBlank()) {
	                            escribir = true;
							}
							
							if(escribir == true) {
									txtMes.appendText(linea +"\n");
								
							}
						}
						
					} else {
						btnPrev.setVisible(false);
					}

					

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
		// BTNWRITE MOUSEONCLICK
		btnWrite.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				// CARGAR VENTANA WRITE
				FXMLLoader loader = new FXMLLoader(App.class.getResource("write.fxml"));
				Parent root;
				try {
					root = loader.load();
					WriteController controller = loader.getController();
					controller.recibirCorreo(mail, pass);
					App.scene.setRoot(root);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
		// BTEXIT MOUSEONCLICK
		btnExit.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				try {
					App.setRoot("InicioSesion");
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		
				
		// NAV MOUSEENTERED & MOUSEEXITED
		Button[] btnNav = new Button[] { btnNext, btnPrev };

		for (Button b : btnNav) {
			b.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					b.setStyle("-fx-background-color : #b0b0b0cd; " + "-fx-background-radius: 10");
				}

			});

			b.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					b.setStyle("-fx-background-color : TRANSPARENT");
				}

			});
		}
			
		// BTNWRITE MOUSEENTERED & MOUSEEXITED
		btnWrite.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				btnWrite.setFont(new Font("System BOLD", 18));
				btnWrite.setStyle("-fx-background-color : #6fb238");
			}

		});

		btnWrite.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				btnWrite.setFont(new Font("System BOLD", 16));
				btnWrite.setStyle("-fx-background-color : #9cda6a");
			}

		});
		
		
		// BTNEXIT MOUSEENTERED & MOUSEEXITED
		btnExit.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				btnExit.setStyle("-fx-background-color : #cd2b2b");
			}

		});

		btnExit.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				btnExit.setStyle("-fx-background-color : TRANSPARENT");
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


















