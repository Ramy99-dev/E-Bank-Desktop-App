package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import application.Main;
import application.database.Database;
import application.entities.Compte;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class compteController implements Initializable {

	private Long id;
	@FXML
	private TextField search;

	@FXML
	private TableColumn<Compte, Long> idCol;

	@FXML
	private TableColumn<Compte, Long> propCol;

	@FXML
	private TableColumn<Compte, Long> testCol;

	@FXML
	private TableColumn<Compte, Double> soldeCol;
	@FXML
	private TableColumn<Compte, Double> decouvertMax;
	@FXML
	private TableColumn<Compte, Double> decouvert;

	@FXML
	private TableColumn<Compte, Double> soldeMax;

	@FXML
	private TableView<Compte> compteTable;

	@FXML
	private HBox searchContainer;

	@FXML
	private Button addBtn;

	@FXML
	private HBox hbtn;

	private boolean all;

	@FXML
	void changeScreenAccueil(ActionEvent event) {
		Main.changeScreen("accueil");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		testCol.setCellValueFactory(new PropertyValueFactory<>("propiertaire"));
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		soldeCol.setCellValueFactory(new PropertyValueFactory<>("solde"));
		decouvertMax.setCellValueFactory(new PropertyValueFactory<>("minS"));
		soldeMax.setCellValueFactory(new PropertyValueFactory<>("maxS"));
		decouvert.setCellValueFactory(new PropertyValueFactory<>("decouvert"));

	}

	public void searchWord(boolean all) {
		if (all == true) {
			search.setOnKeyPressed(e -> {
				String searchWord = search.getText();
				ArrayList<Compte> comptes = new ArrayList<>();
				Database db = new Database();
				Connection conn = null;
				try {
					conn = db.connect();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				compteTable.getItems().clear();
				try {
					System.out.println(search.getText());
					String query = "SELECT C.id , nom , solde,minS , maxS FROM compte C , client Cl WHERE Cl.id = C.proprietarie  AND Cl.nom LIKE ?";
					PreparedStatement preparedStmt = conn.prepareStatement(query);
					preparedStmt.setString(1, "%" + searchWord + "%");
					ResultSet rs = preparedStmt.executeQuery();

					Compte c = null;
					while (rs.next()) {
						if (rs.getLong("solde") < 0) {
							c = new Compte(rs.getLong("id"), rs.getString("nom"), rs.getDouble("solde"),
									rs.getDouble("minS"), rs.getDouble("maxS"), -(rs.getDouble("solde")));
						} else {
							c = new Compte(rs.getLong("id"), rs.getString("nom"), rs.getDouble("solde"),
									rs.getDouble("minS"), rs.getDouble("maxS"), null);
						}
						System.out.println(c);
						comptes.add(c);
					}
				} catch (Exception E) {
					E.getMessage();
				}
				this.compteTable.getItems().addAll(comptes);

			});
		} else {
           searchContainer.getChildren().clear();
		}
	}

	@FXML
	void delete(ActionEvent event) throws SQLException, ClassNotFoundException {
		try {
			Long id = compteTable.getSelectionModel().getSelectedItems().get(0).getId();
			compteTable.getItems().removeAll(compteTable.getSelectionModel().getSelectedItems());

			String query = " DELETE FROM compte WHERE id = ?";
			Database db = new Database();
			Connection conn = db.connect();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setLong(1, id);

			preparedStmt.execute();
			conn.close();
			showCompteInformation(this.id, this.all);
		} catch (NullPointerException e) {
			Alert alert = new Alert(AlertType.ERROR);

			alert.setTitle("info alert");

			alert.setContentText("Vous devez choisir un compte");

			alert.showAndWait();
		}

	}

	@FXML
	void exit(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void add(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
		Compte c = null;
		Database db = new Database();
		System.out.println("test 00 : " + id);
		Connection conn = db.connect();
		String query = "INSERT INTO compte(proprietarie,solde,maxS,minS) VALUES(?,?,?,?)";
		System.out.println(id);
		PreparedStatement preparedStmt = conn.prepareStatement(query);
		preparedStmt.setLong(1, id);
		preparedStmt.setDouble(2, 0.00);
		preparedStmt.setDouble(3, 1000);
		preparedStmt.setDouble(4, 800);
		System.out.println("test");
		preparedStmt.execute();

		// compteController ctrl =loader.getController();
		// Main.window.setScene(new Scene(root));
		String query1 = "SELECT C.id , nom , solde FROM compte C , client Cl WHERE Cl.id = C.proprietarie AND C.proprietarie="
				+ id + " AND C.id = (SELECT MAX(id) FROM compte WHERE proprietarie = " + id + ")";
		PreparedStatement preparedStmt1 = conn.prepareStatement(query1);
		ResultSet rs = preparedStmt1.executeQuery();
		while (rs.next()) {
			c = new Compte(rs.getLong("id"), rs.getString("nom"), rs.getDouble("solde"), 800.00, 1000.00, null);
			System.out.println(c);

		}
		this.compteTable.getItems().add(c);

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
		System.out.println(id);
	}

	public void showCompteInformation(Long id, boolean all) throws ClassNotFoundException, SQLException {
		setId(id);
		setAll(all);
		ArrayList<Compte> comptes = new ArrayList<>();
		Database db = new Database();
		Connection conn = db.connect();
		compteTable.getItems().clear();
		if (all == false) {
			try {
				System.out.println(id);
				String query = "SELECT C.id , nom , solde ,minS , maxS FROM compte C , client Cl WHERE Cl.id = C.proprietarie AND C.proprietarie="
						+ id;
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				ResultSet rs = preparedStmt.executeQuery();
				Compte c = null;
				while (rs.next()) {
					if (rs.getLong("solde") < 0) {
						c = new Compte(rs.getLong("id"), rs.getString("nom"), rs.getDouble("solde"),
								rs.getDouble("minS"), rs.getDouble("maxS"), -(rs.getDouble("solde")));
					} else {
						c = new Compte(rs.getLong("id"), rs.getString("nom"), rs.getDouble("solde"),
								rs.getDouble("minS"), rs.getDouble("maxS"), null);
					}

					comptes.add(c);
					searchContainer.getChildren().clear();
				}
			} catch (Exception e) {
				e.getMessage();
			}
			this.compteTable.getItems().addAll(comptes);
		} else {
			hbtn.getChildren().remove(addBtn);
			try {

				String query = "SELECT C.id , nom , solde,minS , maxS FROM compte C , client Cl WHERE Cl.id = C.proprietarie ";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				ResultSet rs = preparedStmt.executeQuery();

				Compte c = null;
				while (rs.next()) {
					if (rs.getLong("solde") < 0) {
						c = new Compte(rs.getLong("id"), rs.getString("nom"), rs.getDouble("solde"),
								rs.getDouble("minS"), rs.getDouble("maxS"), -(rs.getDouble("solde")));
					} else {
						c = new Compte(rs.getLong("id"), rs.getString("nom"), rs.getDouble("solde"),
								rs.getDouble("minS"), rs.getDouble("maxS"), null);
					}

					comptes.add(c);
				}
			} catch (Exception e) {
				e.getMessage();
			}
			this.compteTable.getItems().addAll(comptes);
		}

	}

	@FXML
	void retrait(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

		try {
			Double solde = null;
			Database db = new Database();
			Connection conn = db.connect();
			Long id = compteTable.getSelectionModel().getSelectedItems().get(0).getId();

			String query = "SELECT solde FROM compte WHERE id =" + id;
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				solde = rs.getDouble("solde");
			}

			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/retrait.fxml"));
			Parent root = loader.load();
			retraitController ctrl = loader.getController();
			ctrl.setSolde(solde);
			ctrl.setIdClient(this.id);
			ctrl.setId(id);
			ctrl.setAll(this.all);
			Stage stage = new Stage();
			ctrl.setStage(stage);

			stage.setScene(new Scene(root));
			stage.show();
		} catch (NullPointerException e) {

			Alert alert = new Alert(AlertType.ERROR);

			alert.setTitle("Error alert");

			alert.setContentText("vous devez sélectionner un compte");

			alert.showAndWait();

		}

	}

	@FXML
	void ajouter(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		try {
			Double solde = null;
			Database db = new Database();
			Connection conn = db.connect();
			Long id = compteTable.getSelectionModel().getSelectedItems().get(0).getId();

			String query = "SELECT solde FROM compte WHERE id =" + id;
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				solde = rs.getDouble("solde");
			}

			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/ajouter.fxml"));
			Parent root = loader.load();
			crediterController ctrl = loader.getController();
			ctrl.setSolde(solde);
			ctrl.setId(id);
			ctrl.setIdClient(this.id);
			ctrl.setAll(this.all);
			Stage stage = new Stage();
			ctrl.setStage(stage);
			stage.setScene(new Scene(root));
			stage.show();
		} catch (NullPointerException e) {

			Alert alert = new Alert(AlertType.ERROR);

			alert.setTitle("Error alert");

			alert.setContentText("vous devez sélectionner un compte");

			alert.showAndWait();

		}

	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	@FXML
	void virement(ActionEvent event) throws IOException {
		if (this.all == true) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/virement.fxml"));
			Parent root = loader.load();
			virementController ctrl = loader.getController();
			ctrl.showInformation(this.all);

			Stage stage = new Stage();
			ctrl.s = stage;
			stage.setScene(new Scene(root));
			stage.show();
		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/virementClient.fxml"));
			Parent root = loader.load();
			virementClientController ctrl = loader.getController();
			ctrl.showCompte(this.id, this.id);
            ctrl.setIdClient(this.id);
			Stage stage = new Stage();
			ctrl.stage = stage;
			stage.setScene(new Scene(root));
			stage.show();

		}

	}

	@FXML
	void edit(ActionEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/editCompte.fxml"));
			Parent root = loader.load();
			editCompteController ctrl = loader.getController();
			Compte c = compteTable.getSelectionModel().getSelectedItems().get(0);
			ctrl.showInformation(c);
			ctrl.all = this.all;
			ctrl.idClient = this.id;
			Stage s = new Stage();
			ctrl.s=s;
			s.setScene(new Scene(root));
			s.show();

		} catch (NullPointerException e) {
			Alert alert = new Alert(AlertType.ERROR);

			alert.setTitle("Error alert");

			alert.setContentText("Tu dois choisir un client");

			alert.showAndWait();
		}

	}

	@FXML
	void best(ActionEvent event) throws ClassNotFoundException, SQLException {
		Database db = new Database();
		Connection conn = db.connect();

		if (this.all == true) {
			String query = "SELECT C.id , nom , solde FROM compte C , client Cl WHERE Cl.id = C.proprietarie  order by solde desc ";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			ResultSet rs = preparedStmt.executeQuery();
			rs.next();
			
			String nom = rs.getString("nom");
			Double solde = rs.getDouble("solde");
			Long id = rs.getLong("id");
			final Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(Main.window);
			BorderPane dialogContent = new BorderPane();
			VBox vb = new VBox();
			Text t = new Text("Meilleure Compte :" + id);
			Text t1 = new Text("Du client :" + nom);
			Text t2 = new Text("Solde :" + solde);
			vb.getChildren().add(t);
			vb.getChildren().add(t1);
			vb.getChildren().add(t2);
			dialogContent.setCenter(vb);
			Scene dialogScene = new Scene(dialogContent, 300, 200);
			dialog.setScene(dialogScene);
			dialog.show();
		} else {
			String query = "SELECT id , solde FROM compte C  WHERE proprietarie = ? order by solde desc ";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setLong(1, this.id);
			ResultSet rs = preparedStmt.executeQuery();
			rs.next();

			Double solde = rs.getDouble("solde");
			Long id = rs.getLong("id");
			final Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(Main.window);
			BorderPane dialogContent = new BorderPane();
			VBox vb = new VBox();
			Text t = new Text("Meilleure Compte :" + id);

			Text t2 = new Text("Solde :" + solde);
			vb.getChildren().add(t);

			vb.getChildren().add(t2);
			dialogContent.setCenter(vb);
			Scene dialogScene = new Scene(dialogContent, 300, 200);
			dialog.setScene(dialogScene);
			dialog.show();
		}

	}

	@FXML
	void decouvert(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/decouvert.fxml"));
		Parent root = loader.load();
		decouvertController ctrl = loader.getController();
		ctrl.all = this.all;
		ctrl.idClient = id;
		ctrl.showData();
		Stage stage = new Stage();
        ctrl.stage=stage;
		stage.setScene(new Scene(root));
		stage.show();

	}

}
