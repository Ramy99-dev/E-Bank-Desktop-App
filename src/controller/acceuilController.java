package controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class acceuilController {
	@FXML
    void changeScreenComptes(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/choixClient.fxml"));
		Parent root = loader.load();
		
		choixClientController ctrl =loader.getController();
		
		ctrl.majList();
		Main.window.setScene(new Scene(root));
    }
	@FXML
    void changeScreenClents(ActionEvent event) throws IOException  {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/screen2.fxml"));
		Parent root = loader.load();
		
		ClientController ctrl =loader.getController();
		
		ctrl.setClientTable();;
		Main.window.setScene(new Scene(root));

    }
	 @FXML
	    void deconnect(ActionEvent event) throws IOException {
		 File F = new File("D:\\bank_files\\etat.txt");
	     FileWriter myWriter = new FileWriter(F);
	     myWriter.write("D");
	     myWriter.close();
	     FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/login.fxml"));
		 Parent root = loader.load();
		
		 loginController ctrl =loader.getController();
		 Main.window.setScene(new Scene(root));
		 root.getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());


	    }

}
