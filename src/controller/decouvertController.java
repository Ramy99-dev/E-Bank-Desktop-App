package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import application.database.Database;
import application.entities.Compte;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class decouvertController   {

	@FXML
    private ListView<Compte> decouvertList;
	
	public boolean all;
	public Stage stage ;
	public Long idClient ;

    @FXML
    void exit(ActionEvent event) {
       stage.close();
    }

	
	public void showData() {
		
		if(this.all == true)
		{
			try
			{
				Database db = new Database();
				Connection conn = db.connect();
		    	String query =" SELECT C.id , nom , solde FROM compte C , client Cl WHERE Cl.id = C.proprietarie AND solde < 0";
		    	
		    	PreparedStatement preparedStmt = conn.prepareStatement(query);
		   
		    	ResultSet rs = preparedStmt.executeQuery();
		        while(rs.next())
		        {
		        	Compte c = new Compte(rs.getLong("id"), rs.getDouble("solde") , rs.getString("nom"));
		        	decouvertList.getItems().add(c);
		        }
			}
			catch(Exception e)
			{
				e.getMessage();
			}
		}
		else
		{
			try
			{
				Database db = new Database();
				Connection conn = db.connect();
		    	String query =" SELECT C.id , nom , solde FROM compte C , client Cl WHERE Cl.id = C.proprietarie AND solde < 0 AND Cl.id = ?";
		    	
		    	PreparedStatement preparedStmt = conn.prepareStatement(query);
		    	preparedStmt.setLong(1,idClient);
		   
		    	ResultSet rs = preparedStmt.executeQuery();
		        while(rs.next())
		        {
		        	Compte c = new Compte(rs.getLong("id"), rs.getDouble("solde") , rs.getString("nom"));
		        	decouvertList.getItems().add(c);
		        }
			}
			catch(Exception e)
			{
				e.getMessage();
			}
		}
		
		
		
	}

}
