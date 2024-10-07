package esi.emaktaba;

import esi.emaktaba.controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class Application extends javafx.application.Application {
  private Connection connection;

  @Override
  public void init() throws SQLException {
    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/emaktaba", "root", "");
  }

  @Override
  public void stop() throws SQLException {
    connection.close();
  }

  @Override
  public void start(Stage stage) throws IOException {
    stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png"))));
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
    Parent root = fxmlLoader.load();

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("Login");
    stage.show();

    LoginController controller = fxmlLoader.getController();
    controller.setStage(stage);
    controller.setConnection(connection);

  }

  public static void main(String[] args) throws SQLException {
    launch();
  }
}
