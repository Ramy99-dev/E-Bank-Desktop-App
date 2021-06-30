package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.Main;
import application.database.Database;
import application.entities.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class modifClientController 
{
	    private Long id ;
	    public Stage s ;
	
	    @FXML
	    private TextField nom;

	    @FXML
	    private TextField prenom;

	    @FXML
	    private TextField email;
	    @FXML
	    private Text adresseAlert;

	    @FXML
	    private Text alertNom;
	    
	    @FXML
	    private Text prenomAlert;

	    @FXML
	    void cancel(ActionEvent event) {
           s.close();
	    }

	    @FXML
	    void edit(ActionEvent event) throws SQLException, ClassNotFoundException, IOException 
	    {
	    	if ( prenom.getText().matches("^[a-zA-Z\\s]+")==false ||  (nom.getText().matches("^[a-zA-Z\\s]+"))==false)  
	    	{
	    		
					String expression = "^[a-zA-Z\\s]+";
					if (!(nom.getText().matches(expression))) {
						alertNom.setText("Le nom ne doit pas contenir des caractére spéciaux");
					}
					else
					{
						alertNom.setText("");
					}
				
				
					if (!(prenom.getText().matches(expression))) {
						prenomAlert.setText("Le prenom ne doit pas contenir des caractére spéciaux");
					}
					else
					{
						prenomAlert.setText("");
					}
				
	    	
	    	}
	    	else {

		    	String query = "update client SET nom=? , prenom = ? , adresse = ? where id = ?";
				Database db = new Database();
				Connection conn = db.connect();
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, nom.getText());
				preparedStmt.setString(2, prenom.getText());
				preparedStmt.setString(3, email.getText());
				System.out.println(id);
				preparedStmt.setLong(4,id);
				preparedStmt.execute();
				conn.close();
				
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/screen2.fxml"));
				Parent root = loader.load();
				
				ClientController ctrl =loader.getController();
				
				ctrl.setClientTable();
				Main.window.setScene(new Scene(root));
				s.close();
	    	}

	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public void showInformation(Client c) 
		{
			this.id = c.getId();
			this.nom.setText(c.getNom());
			this.prenom.setText(c.getPrenom());
			this.email.setText(c.getAdresse());
		}
		
	    

}
