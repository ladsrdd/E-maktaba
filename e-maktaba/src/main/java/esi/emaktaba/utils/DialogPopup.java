package esi.emaktaba.utils;

import javafx.scene.control.Alert;

public class DialogPopup {
  public static void showDialogPopup(javafx.scene.control.Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(message);
    alert.showAndWait();

  }
}
