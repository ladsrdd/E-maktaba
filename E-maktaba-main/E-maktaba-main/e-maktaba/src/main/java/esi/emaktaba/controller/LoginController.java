package esi.emaktaba.controller;

import esi.emaktaba.Application;
import esi.emaktaba.dao.AdminDAO;
import esi.emaktaba.model.Admin;
import esi.emaktaba.utils.DialogPopup;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginController {
  @FXML
  private TextField username;
  @FXML
  private PasswordField password;
  private Connection connection;
  private Stage stage;
  private AdminDAO adminDAO;

  public void setConnection(Connection connection) {
    this.connection = connection;
    this.adminDAO = new AdminDAO(connection);
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void login() throws SQLException, IOException {
    Admin admin = new Admin(username.getText(), password.getText());
    if (adminDAO.login(admin)) {//if credentials are correct
      FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Main.fxml"));
      Parent root = fxmlLoader.load();

      MainController controller = fxmlLoader.getController();
      controller.setStage(stage);
      controller.setConnection(connection);
      controller.setAdmin(admin);
      controller.memberTab();

      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.setTitle("E-Maktaba");
      return;
    }
    DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Username and/or password incorrect.");
  }


}
