package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.Main;
import application.database.Database;
import application.entities.Compte;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editCompteController {

	private Long id;
	public Stage s;
	public Long idClient;
	public boolean all;
	@FXML
	private TextField minS;

	@FXML
	private TextField maxS;

	@FXML
	void cancel(ActionEvent event) {
     s.close();
	}

	@FXML
	void edit(ActionEvent event) throws NumberFormatException, SQLException, ClassNotFoundException, IOException {
		
		String query = "update compte SET minS=? , maxS = ?  where id = ?";
		Database db = new Database();
		Connection conn = db.connect();
		PreparedStatement preparedStmt = conn.prepareStatement(query);
		preparedStmt.setDouble(1, Double.parseDouble(minS.getText()));
		preparedStmt.setDouble(2, Double.parseDouble(maxS.getText()));
		preparedStmt.setLong(3,id);
		preparedStmt.execute();
		conn.close();
		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/screen1.fxml"));
		Parent root = loader.load();
	
		compteController ctrl =loader.getController();
		
		ctrl.showCompteInformation(this.idClient, this.all);
		Main.window.setScene(new Scene(root));
		s.close();

	}

	public void showInformation(Compte c) 
	{
		this.id = c.getId();
		System.out.println(this.id);
		this.minS.setText(c.getMinS()+"");
		this.maxS.setText(c.getMaxS()+"");
		
	}

}
