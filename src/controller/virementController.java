package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class virementController implements Initializable{

	private boolean all ;
	
	public Stage s ;
    @FXML
    private ChoiceBox<Object> receiver;

    @FXML
    private ChoiceBox<Object> sender;

    @FXML
    void cancel(ActionEvent event) 
    {
       s.close();
    }

    @FXML
    void send(ActionEvent event) throws IOException 
    {
    	try
    	{
    	String c = (String) sender.getSelectionModel().getSelectedItem();
        String [] cl = c.split("-");
        Long idSend = Long.parseLong(cl[0]);
        
        c = (String) receiver.getSelectionModel().getSelectedItem();
        cl = c.split("-");
        Long idRec = Long.parseLong(cl[0]);
      
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/virementClient.fxml"));
		Parent root = loader.load();
		virementClientController ctrl =loader.getController();
		
		ctrl.showCompte(idSend,idRec);
		ctrl.setAll(this.all);
		ctrl.setS(s);
		s.setScene(new Scene(root));
    	}
		catch(NullPointerException e)
    	{

			Alert alert = new Alert(AlertType.ERROR);

			alert.setTitle("Error alert");
	
			alert.setContentText("vous devez choisir les clients");
			
			alert.showAndWait();
		
    	}
      
      
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		
		
	}
	
	public void showInformation(boolean all)
	{
		this.all = all ;
		if(this.all == true)
		{
			
			ArrayList<String> senders = new ArrayList<>();
			try {
				String query = " SELECT id , nom , prenom FROM client";
				Database db = new Database();
				Connection conn = db.connect();
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				ResultSet rs = preparedStmt.executeQuery();
				while (rs.next()) {
					
					String c = rs.getLong("id") +"- "+rs.getString("nom") +" " + rs.getString("prenom");
					senders.add(c);
					
				}
			} catch (Exception e) {
				e.getMessage();
			}
			
			this.sender.getItems().addAll(senders);
			
			
			ArrayList<String> receivers = new ArrayList<>();
			try {
				String query = " SELECT id , nom , prenom FROM client";
				Database db = new Database();
				Connection conn = db.connect();
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				ResultSet rs = preparedStmt.executeQuery();
				while (rs.next()) {
					
					String c = rs.getLong("id") +"- "+rs.getString("nom") +" " + rs.getString("prenom");
					receivers.add(c);
					
				}
			} catch (Exception e) {
				e.getMessage();
			}
			this.receiver.getItems().addAll(receivers);
			
			
			 
			
		}
		
	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public ChoiceBox<Object> getReceiver() {
		return receiver;
	}

	public void setReceiver(ChoiceBox<Object> receiver) {
		this.receiver = receiver;
	}

	public ChoiceBox<Object> getSender() {
		return sender;
	}

	
	

}