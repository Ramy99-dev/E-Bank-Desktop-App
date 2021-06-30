package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;
import application.database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class formClientController implements Initializable {

	@FXML
	private TextField adresse;

	@FXML
	private TextField nom;

	@FXML
	private TextField prenom;

	@FXML
	private ImageView alertPrenom;

	@FXML
	private ImageView alertAdresse;

	@FXML
	private ImageView alertNom;

	@FXML
	private Text adresseAlertText;

	@FXML
	private Text nomAlertText;
	
	@FXML
    private Text prenomAlertText;

	@FXML
	private Button addBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		alertAdresse.setVisible(false);
		alertNom.setVisible(false);
		alertPrenom.setVisible(false);

	}

	@FXML
	void add(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
		System.out.println();
		if (nom.getText().length() == 0 || prenom.getText().length() == 0 || adresse.getText().length() == 0 
				|| (prenom.getText().matches("^[a-zA-Z\\s]+"))==false ||  (nom.getText().matches("^[a-zA-Z\\s]+"))==false)  {

			if (nom.getText().length() == 0) {
				alertNom.setVisible(true);

			} else {
				alertNom.setVisible(false);
				String expression = "^[a-zA-Z\\s]+";
				if (!(nom.getText().matches(expression))) {
					nomAlertText.setText("Le nom ne doit pas contenir des caractére spéciaux");
				}
				else
				{
					nomAlertText.setText("");
				}
			}
			if (prenom.getText().length() == 0) {
				alertPrenom.setVisible(true);

			} else {
				alertPrenom.setVisible(false);
				String expression = "^[a-zA-Z\\s]+";
				if (!(prenom.getText().matches(expression))) {
					prenomAlertText.setText("Le prenom ne doit pas contenir des caractére spéciaux");
				}
				else
				{
					prenomAlertText.setText("");
				}
			}
			if (adresse.getText().length() == 0) {
				alertAdresse.setVisible(true);

			} else {
				alertAdresse.setVisible(false);
			}
		} else {
			String query = " insert into client (nom, prenom, adresse)" + " values (?, ?, ?)";
			Database db = new Database();
			Connection conn = db.connect();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, nom.getText());
			preparedStmt.setString(2, prenom.getText());
			preparedStmt.setString(3, adresse.getText());
			preparedStmt.execute();
			conn.close();

			FXMLLoader loader1 = new FXMLLoader(getClass().getResource("../scenes/choixClient.fxml"));
			Parent root1 = loader1.load();

			choixClientController ctrl1 = loader1.getController();
			ctrl1.majList();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/screen2.fxml"));
			Parent root = loader.load();

			ClientController ctrl = loader.getController();

			ctrl.setClientTable();
			Main.window.setScene(new Scene(root));
		}

	}

	@FXML
	void changeScreenClient(ActionEvent event) {
		Main.changeScreen("client");
	}

}