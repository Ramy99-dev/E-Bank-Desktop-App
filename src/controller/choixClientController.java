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
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;

public class choixClientController implements Initializable {
	
	
	@FXML
    private ChoiceBox<String> clients;

    @FXML
    void cancel(ActionEvent event) {
      Main.changeScreen("accueil");
    }

    @FXML
    void choose(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
    	try
    	{
    		String c = clients.getSelectionModel().getSelectedItem();
            String [] cl = c.split("-");
            Long id = Long.parseLong(cl[0]);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/screen1.fxml"));
    		Parent root = loader.load();
    		compteController ctrl =loader.getController();
    		ctrl.setId(id);
    		ctrl.searchWord(false);
    		ctrl.showCompteInformation(id,false);
    		
    		Main.window.setScene(new Scene(root));
    	}
    	catch(NullPointerException e)
    	{

			Alert alert = new Alert(AlertType.ERROR);

			alert.setTitle("Error alert");
	
			alert.setContentText("Vous devez choisir un client");
			
			alert.showAndWait();
		
    	}
    	
    }
    @FXML
    void initialize(){
    	
    }
    @FXML
    void allClient(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/screen1.fxml"));
		Parent root = loader.load();
		compteController ctrl =loader.getController();
		
		ctrl.setAll(true);
		ctrl.searchWord(true);
		ctrl.showCompteInformation(null,true);
		
		Main.window.setScene(new Scene(root));
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		clients.getItems().clear();
    	ArrayList<String> clients = new ArrayList<>();
		try {
			String query = " SELECT id , nom , prenom FROM client";
			Database db = new Database();
			Connection conn = db.connect();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				
				String c = rs.getLong("id") +"- "+rs.getString("nom") +" " + rs.getString("prenom");
				clients.add(c);
				
			}
		} catch (Exception e) {
			e.getMessage();
		}
		this.clients.getItems().addAll(clients);
		
	}
    public void majList() {
    	System.out.println("test");
    	clients.getItems().clear();
    	ArrayList<String> clients = new ArrayList<>();
		try {
			String query = " SELECT id , nom , prenom FROM client";
			Database db = new Database();
			Connection conn = db.connect();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				
				String c = rs.getLong("id") +"- "+rs.getString("nom") +" " + rs.getString("prenom");
				
				clients.add(c);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		this.clients.getItems().addAll(clients);
    }

}
