package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.Main;
import application.database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class virementClientController {

	private Long idRec ; 
	private Long idSend ;
	private boolean all;
	private Long idClient;
	public Stage stage ;
	@FXML
	private TextField amount;
    @FXML
    private ChoiceBox<String> senderAcc;

    @FXML
    private ChoiceBox<String> receiverAcc;

    @FXML
    void cancel(ActionEvent event) {
      stage.close();
    }

    @FXML
    void send(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
    	
    		Long idAccSend = null ;
    		Long idAccRec = null ;
    		Double solde = null;
    		try
    		{
    			solde = Double.parseDouble(amount.getText());
    		}
    		catch(NumberFormatException  | NullPointerException e)
    		{
    			Alert alert = new Alert(AlertType.ERROR);

    			alert.setTitle("Error alert");
    	
    			alert.setContentText("Vous devez preciser le montant");
    			
    			alert.showAndWait();
    		}
	    	try
	    	{
	    		 idAccSend = Long.parseLong(senderAcc.getSelectionModel().getSelectedItem());
		    	 idAccRec = Long.parseLong(receiverAcc.getSelectionModel().getSelectedItem());
	    	}
	    	catch(NumberFormatException | NullPointerException e)
    		{
    			Alert alert = new Alert(AlertType.ERROR);

    			alert.setTitle("Error alert");
    	
    			alert.setContentText("Vous choisir preciser les Compte");
    			
    			alert.showAndWait();
    		}
	    	
	    	
	    	if(idAccRec.equals(idAccSend)  && idAccRec!=null && idAccSend !=null)
	    	{
	    		System.out.println("test");
	    		Alert alert = new Alert(AlertType.INFORMATION);

				alert.setTitle("info alert");
		
				alert.setContentText("Vous devez choisir deux comptes different ");
				
				alert.showAndWait();
	    	}
	    	else if(idAccRec!=null && idAccSend !=null && solde!=null)
	    	{
	    		Database db = new Database();
				Connection conn = db.connect();
				Double soldeBefore =null;
				Double minS = null;
	    		String query ="SELECT solde,minS FROM compte WHERE id = ?";
	    		PreparedStatement preparedStmt = conn.prepareStatement(query);
	    		preparedStmt.setDouble(1,idAccSend );
	    		ResultSet rs = preparedStmt.executeQuery();
	    		while(rs.next())
	    		{
	    			soldeBefore = rs.getDouble("solde");
	    			minS = rs.getDouble("minS");
	    		}
	    		Double soldeAfter =  soldeBefore - solde ; 
	    		System.out.println(soldeAfter);
	    		System.out.println(-minS);
	    		if(soldeAfter<-(minS))
	    		{
	    			Alert alert = new Alert(AlertType.INFORMATION);

					alert.setTitle("info alert");
			
					alert.setContentText("Le solde du compte "+idAccSend +" est inferieure au découvert");
					
					alert.showAndWait();
	    		}
	    		else
	    		{
	    			
					
					Double maxS = null;
					query ="SELECT solde,maxS FROM compte WHERE id = ?";
		    		preparedStmt = conn.prepareStatement(query);
		    		preparedStmt.setDouble(1,idAccRec );
		    		rs = preparedStmt.executeQuery();
		    		while(rs.next())
		    		{
		    			soldeBefore = rs.getDouble("solde");
		    			maxS = rs.getDouble("maxS");
		    		}
		    		 soldeAfter =  soldeBefore + solde ; 
		    		
		    		if(soldeAfter>(maxS))
		    		{
		    			Alert alert = new Alert(AlertType.INFORMATION);

						alert.setTitle("info alert");
				
						alert.setContentText("Le solde du compte "+idAccRec +" a dépasser le solde maximale");
						
						alert.showAndWait();
		    		}
		    		else
		    		{
		    			query = "UPDATE compte SET solde = solde-? WHERE id = ? " ;
						
						preparedStmt = conn.prepareStatement(query);
						preparedStmt.setDouble(1,solde );
						preparedStmt.setDouble(2,idAccSend );
						
						preparedStmt.executeUpdate();
						
		    			query  = "UPDATE compte SET solde = solde+? WHERE id = ?";
						preparedStmt = conn.prepareStatement(query);
						preparedStmt.setDouble(1,solde );
						preparedStmt.setDouble(2,idAccRec);
						
						preparedStmt.executeUpdate();
						
						FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/screen1.fxml"));
						Parent root = loader.load();
						compteController ctrl =loader.getController();
						ctrl.showCompteInformation(this.idClient, this.all);
						Main.window.setScene(new Scene(root));
					    stage.close();

		    		}
					
	    		}
	    		
	    		
	    	}
	    	
    	
		
    
        
    }
    public void showCompte(Long idSend , Long idRec)
    {
    	
    	ArrayList<String> receivers = new ArrayList<>();
		try {
			String query = " SELECT id , solde  FROM compte where proprietarie = "+idRec ;
			Database db = new Database();
			Connection conn = db.connect();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			ResultSet rs = preparedStmt.executeQuery();
			
			while (rs.next()) {
				
				String c = rs.getLong("id")+"";
				receivers.add(c);
				
			}
		} catch (Exception e) {
			e.getMessage();
		}
		
		this.receiverAcc.getItems().addAll(receivers);
		
		
		ArrayList<String> senders = new ArrayList<>();
		try {
			String query = " SELECT id , solde  FROM compte where proprietarie = "+idSend ;
			Database db = new Database();
			Connection conn = db.connect();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			ResultSet rs = preparedStmt.executeQuery();
			
			while (rs.next()) {
				
				String c = rs.getLong("id")+"";
				senders.add(c);
				
			}
		} catch (Exception e) {
			e.getMessage();
		}
		
		this.senderAcc.getItems().addAll(senders);
    }
	public Long getIdRec() {
		return idRec;
	}

	public void setIdRec(Long idRec) {
		this.idRec = idRec;
	}

	public Long getIdSend() {
		return idSend;
	}

	public void setIdSend(Long idSend) {
		this.idSend = idSend;
	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public Stage getStage() {
		return stage;
	}

	public void setS(Stage s) {
		this.stage = s;
	}

	public Long getIdClient() {
		return idClient;
	}

	public void setIdClient(Long idClient) {
		this.idClient = idClient;
	}

	
    

}