package esi.emaktaba.controller;

import esi.emaktaba.Application;
import esi.emaktaba.model.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class MainController {
  private Stage stage;
  private Connection connection;
  @FXML
  private BorderPane borderPane;
  @FXML
  private Label adminId;
  @FXML
  private Label adminUsername;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public void setAdmin(Admin admin) {
    adminId.setText(Integer.toString(admin.getId()));
    adminUsername.setText(admin.getUsername());
  }

  public void logout() throws IOException {
    adminId.setText("");
    adminUsername.setText("");
    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Login.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("Login");
    LoginController controller = fxmlLoader.getController();
    controller.setStage(stage);
    controller.setConnection(connection);
  }

  public void memberTab() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Member.fxml"));
    Node node = fxmlLoader.load();
    MemberController memberController = fxmlLoader.getController();
    memberController.setDao(connection);
    memberController.populateTable();
    borderPane.setCenter(node);
  }

  public void genreTab() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Genre.fxml"));
    Node node = fxmlLoader.load();
    GenreController genreController = fxmlLoader.getController();
    genreController.setDao(connection);
    genreController.populateTable();
    borderPane.setCenter(node);
  }

  public void bookTab() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Book.fxml"));
    Node node = fxmlLoader.load();
    BookController bookController = fxmlLoader.getController();
    bookController.setDao(connection);
    bookController.populateTable();
    bookController.populateGenre();
    borderPane.setCenter(node);
  }

  public void loanTab() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Loan.fxml"));
    Node node = fxmlLoader.load();
    LoanController loanController = fxmlLoader.getController();
    loanController.setDao(connection);
    loanController.populateTable();
    loanController.populateBooks();
    loanController.populateMembers();
    loanController.populateStatus();
    borderPane.setCenter(node);
  }
}
