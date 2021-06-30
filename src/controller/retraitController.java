package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;
import application.database.Database;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class retraitController 
{
	private Double solde ; 
	private Long id ; 
	private Long idClient ; 
	private Stage stage ; 
	private boolean all;
	@FXML
    private TextField montant;

    @FXML
    void cancel(ActionEvent event) {
    	stage.close();

    }

    @FXML
    void retrait(ActionEvent event) throws SQLException, ClassNotFoundException, IOException  {
    	Database db = new Database();
		Connection conn = db.connect();
    	String query1 =" SELECT minS from compte where id = ?";
    	Double minS = null ;
    	PreparedStatement preparedStmt1 = conn.prepareStatement(query1);
    	preparedStmt1.setLong(1,id);
    	ResultSet rs = preparedStmt1.executeQuery();
        while(rs.next())
        {
        	minS = rs.getDouble("minS");
        }
        System.out.println(minS);
    	Double newSolde =  solde - Double.parseDouble(montant.getText());
    	if(newSolde < -minS)
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);

			alert.setTitle("info alert");
	
			alert.setContentText("Le solde du compte "+id +" est inferieure au découvert");
			
			alert.showAndWait();
    	}
    	else
    	{
    		String query = " update compte SET solde=?  where id = ?";
    		
    		PreparedStatement preparedStmt = conn.prepareStatement(query);
    		preparedStmt.setDouble(1, newSolde);
    		preparedStmt.setLong(2, id);
    		preparedStmt.execute();
    		conn.close();
    		
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/screen1.fxml"));
    		Parent root = loader.load();
    		
    		compteController ctrl =loader.getController();
    		
    		ctrl.showCompteInformation(this.idClient,this.all);
    		
    		Main.window.setScene(new Scene(root));
    	}
    	
		
		stage.close();

    }

	public Double getSolde() {
		return solde;
	}

	public void setSolde(Double solde) {
		this.solde = solde;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdClient() {
		return idClient;
	}

	public void setIdClient(Long idClient) {
		this.idClient = idClient;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}
	
	
    
}
