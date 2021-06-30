package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.pdfjet.A4;
import com.pdfjet.Cell;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.Table;

import application.Main;
import application.database.Database;
import application.entities.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ClientController implements Initializable {

	
   
	
	@FXML
    void initialize(){
		
		ArrayList<Client> clients = new ArrayList<>();
		try {
			String query = " SELECT * FROM client";
			Database db = new Database();
			Connection conn = db.connect();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				System.out.println();
				Client c = new Client(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom"),
						rs.getString("adresse"));
				clients.add(c);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		this.clientTable.getItems().addAll(clients);
    }
	@FXML
	private TableColumn<Client, Long> idCol;

	@FXML
	private TableView<Client> clientTable;

	@FXML
	private TableColumn<Client, String> nomCol;

	@FXML
	private TableColumn<Client, String> prenomCol;

	@FXML
	private TableColumn<Client, String> adresseCol;

	@FXML
	void changeScreenAccueil(ActionEvent event) {
		Main.changeScreen("accueil");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
		prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
		
		setClientTable();

	}

	@FXML
	void exit(ActionEvent event) {
		System.exit(0);
	}
	

	@FXML
	void add(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/clientForm.fxml"));
		Parent root = loader.load();
		Main.window.setScene(new Scene(root));
	}
	@FXML
	 void modifClient(ActionEvent event) throws IOException 
	 {
		
	    try
	    {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/modifClient.fxml"));
		Parent root = loader.load();
		modifClientController ctrl =loader.getController();
		Client c = clientTable.getSelectionModel().getSelectedItems().get(0);
		ctrl.showInformation(c);
		
		Stage s = new Stage();
		ctrl.s=s;
		s.setScene(new Scene(root));
		s.show();
		
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
    void deleteClient(ActionEvent event) throws SQLException, ClassNotFoundException {
		try
		{
			Long id = clientTable.getSelectionModel().getSelectedItems().get(0).getId();
			 clientTable.getItems().removeAll(
		                clientTable.getSelectionModel().getSelectedItems()
		        );
			
			String query = " DELETE FROM client WHERE id = ?";
			Database db = new Database();
			Connection conn = db.connect();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setLong(1, id);
			
			preparedStmt.execute();
			conn.close();
			setClientTable();
		}
		catch(NullPointerException e)
		{
			Alert alert = new Alert(AlertType.ERROR);

			alert.setTitle("Error alert");
	
			alert.setContentText("Vous devez choisir un client");
			
			alert.showAndWait();
		}
	        
    }
	public TableView<Client> getClientTable() {
		return clientTable;
	}

	public void setClientTable() {
		this.clientTable.getItems().clear();
		ArrayList<Client> clients = new ArrayList<>();
		try {
			String query = " SELECT * FROM client";
			Database db = new Database();
			Connection conn = db.connect();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				System.out.println();
				Client c = new Client(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom"),
						rs.getString("adresse"));
				clients.add(c);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		this.clientTable.getItems().addAll(clients);
	}

	  @FXML
	    void print(ActionEvent event) throws Exception {
          File compte = new File("D:\\bank_files\\compte.pdf");
          FileOutputStream fos = new FileOutputStream(compte);
          PDF pdf = new PDF(fos);
          Page page = new Page(pdf,A4.PORTRAIT);
          
          Font f1 = new Font(pdf , CoreFont.HELVETICA_BOLD);
          Font f2 = new Font(pdf , CoreFont.TIMES_ROMAN);
         
          Table table = new Table();
          List<List<Cell>> tableData  = new ArrayList<>();
          
          List<Cell> tableRow = new ArrayList<>();
        
          Cell cell = new Cell(f1 , idCol.getText());
          tableRow.add(cell);
          
          cell = new Cell(f1 , nomCol.getText());
          tableRow.add(cell);
          
          cell = new Cell(f1 , prenomCol.getText());
          tableRow.add(cell);
          
          cell = new Cell(f1 , adresseCol.getText());
          tableRow.add(cell);
          
          tableData.add(tableRow);
          
          List<Client> items = clientTable.getItems();
          for (Client client : items) {
			Cell id = new Cell(f2,client.getId()+"");
			Cell nom = new Cell(f2,client.getNom());
			Cell prenom = new Cell(f2,client.getPrenom());
			Cell adresse = new Cell(f2,client.getAdresse());
			
			tableRow = new ArrayList<Cell>();
			tableRow.add(id);
			tableRow.add(nom);
			tableRow.add(prenom);
			tableRow.add(adresse);
			
			tableData.add(tableRow);
		  }
          table.setData(tableData);
          table.setPosition(70f, 60f);
          table.setColumnWidth(0, 100f);
          table.setColumnWidth(1, 100f);
          table.setColumnWidth(2, 100f);
          table.setColumnWidth(3, 100f);
          
          while(true)
          {
        	  table.drawOn(page);
        	  if(!table.hasMoreData())
        	  {
        		  table.resetRenderedPagesCount();
        		  break;
        	  }
        	  page = new Page(pdf,A4.PORTRAIT);
          }
          pdf.flush();
          fos.close();
          Desktop desktop = Desktop.getDesktop();
          desktop.open(compte);
          System.out.println(compte.getAbsolutePath());
	    }
	
	

}