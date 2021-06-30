package application;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;


public class Main extends Application {
	
	public static Stage window ;
	public static HashMap<String,Scene> scenes = new HashMap<>();
	@Override
	public void start(Stage primaryStage) throws IOException  {
		window =primaryStage;
		AnchorPane login = null ;
		GridPane accueil = null  ;
		
		GridPane client = null ;
		GridPane clientForm = null ;
	    GridPane modifForm = null ;
		try {
			accueil = FXMLLoader.load(getClass().getResource("../scenes/acceuil.fxml"));
			login = FXMLLoader.load(getClass().getResource("../scenes/login.fxml"));
			client =  FXMLLoader.load(getClass().getResource("../scenes/screen2.fxml"));
			clientForm =  FXMLLoader.load(getClass().getResource("../scenes/clientForm.fxml"));
			modifForm =  FXMLLoader.load(getClass().getResource("../scenes/modifClient.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scenes.put("accueil",new Scene(accueil , 641 , 739));
		scenes.put("login",new Scene(login , 600 , 400));
		scenes.put("client",new Scene(client , 641 , 739));
		scenes.put("clientForm", new Scene(clientForm , 641 , 739));
		scenes.put("modifForm", new Scene(modifForm , 400 , 400));
		scenes.get("accueil").getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		scenes.get("login").getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		File F = new File("D:\\bank_files\\etat.txt");
		FileReader myReader = new FileReader(F);
	    BufferedReader br = new BufferedReader(myReader);
	    String etat = br.readLine();
	   
	    System.out.println(etat.length());
	    System.out.println(etat);
        System.out.println(etat=="D");
	    br.close();
	    if(etat.equals("D"))
	    {
	    	System.out.println(etat);
	    	window.setScene(scenes.get("login"));
	    }
	    else if (etat.equals("C")) {
	    	window.setScene(scenes.get("accueil"));
	    	
	    }
		
		window.show();
	}
	public static void changeScreen(String screen)
	{
		window.setScene(scenes.get(screen));
	}
	public static void main(String[] args) {
		launch(args);
	}
}
