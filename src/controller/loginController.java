package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class loginController implements Initializable {

	@FXML
    private PasswordField pwd;

    @FXML
    private TextField user;
    @FXML
    private Button connectBtn;
    
    
    @FXML
    private Text alert;

	private int tentative ;

    @FXML
    void connect(ActionEvent event) throws IOException {
    	
    	File F = new File("D:\\bank_files\\tentative.txt");
		FileWriter myWriter = new FileWriter(F);
    	if (user.getText().equals("admin") &&  pwd.getText().equals("admin"))
    	{
    		File F1 = new File("D:\\bank_files\\etat.txt");
    	    FileWriter myWriter1 = new FileWriter(F1);
    	    myWriter1.write("C");
    	    myWriter1.close();
    	   
    		
    		System.out.println("connect");
    		tentative = 0 ;
    	    myWriter.write(tentative+"");
    	    myWriter.close();
    	    
    		Main.changeScreen("accueil");
    		
    		
    	}
    	else {
    		tentative ++;
    		alert.setText("Il vous reste " +(4-tentative) + " tentative !" );
    		 if(tentative > 3)
		      {
    			 alert.setText("Systéme bloquer" );
		    	  connectBtn.setDisable(true);

		      }
    		
			myWriter.write(tentative+"");
    	    myWriter.close();	
    	}
		

    }
    public void initVal()
    {
    	
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		user.setText("");
		pwd.setText("");
		alert.setText("");
		
		try {
			  File F = new File("D:\\bank_files\\tentative.txt");
			  FileReader myReader = new FileReader(F);
		      BufferedReader br = new BufferedReader(myReader);
		      tentative =Integer.parseInt(br.readLine()) ;
		      myReader.close();
		      if(tentative > 3)
		      {
		    	  alert.setText("Systéme bloquer" );
		    	  connectBtn.setDisable(true);

		      }
		}
		catch(Exception e)
		{
			e.getStackTrace();
		}
		
	}
	
	

}
