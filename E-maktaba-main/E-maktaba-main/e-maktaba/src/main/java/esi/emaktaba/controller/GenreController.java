package esi.emaktaba.controller;

import esi.emaktaba.dao.GenreDAO;
import esi.emaktaba.model.Genre;
import esi.emaktaba.utils.DialogPopup;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public class GenreController {
  @FXML
  private TextField nameField;
  @FXML
  private TextArea descriptionField;
  @FXML
  private TextField searchField;
  @FXML
  private TableView<Genre> genreTable;
  @FXML
  private TableColumn<Genre, String> nameColumn;
  @FXML
  private TableColumn<Genre, String> descriptionColumn;
  private GenreDAO genreDAO;
  private Genre selectedGenre = null;
  private List<Genre> genreList;

  public void setDao(Connection connection) {
    this.genreDAO = new GenreDAO(connection);
  }

  public void populateTable() {
    try {
      this.genreList = genreDAO.getAllGenres();
      nameColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getName()));
      descriptionColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getDescription()));
      genreTable.getItems().setAll(genreList);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void add() throws SQLException {
    Boolean flag = checkInput();
    if (!flag) return;

    Genre newGenre = new Genre();
    newGenre.setName(nameField.getText());
    newGenre.setDescription(descriptionField.getText());
    genreDAO.addGenre(newGenre);

    populateTable();
    selectedGenre = null;
    DialogPopup.showDialogPopup(Alert.AlertType.INFORMATION, "Genre added", "Genre added successfully");
    clearInput();
  }

  public void edit() throws SQLException {
    if (selectedGenre == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a genre to edit");
      return;
    }

    Boolean flag = checkInput();
    if (!flag) return;
    selectedGenre.setName(nameField.getText());
    selectedGenre.setDescription(descriptionField.getText());

    genreDAO.updateGenre(selectedGenre);

    populateTable();
    selectedGenre = null;
    DialogPopup.showDialogPopup(Alert.AlertType.INFORMATION, "Genre updated", "Genre updated successfully");
    clearInput();
  }

  public void delete() throws SQLException {
    if (selectedGenre == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a genre to delete");
      return;
    }
    genreDAO.deleteGenre(selectedGenre);
    populateTable();
    selectedGenre = null;
    DialogPopup.showDialogPopup(Alert.AlertType.INFORMATION, "Genre deleted", "Genre deleted successfully");
    clearInput();
  }

  public void selectRow() {
    Genre newSelectedGenre = genreTable.getSelectionModel().getSelectedItem();
    if (newSelectedGenre == this.selectedGenre) return;
    this.selectedGenre = newSelectedGenre;
    nameField.setText(newSelectedGenre.getName());
    descriptionField.setText(newSelectedGenre.getDescription());
    return;
  }

  public Boolean checkInput() {
    if (nameField.getText().isEmpty()) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please enter a name");
      return false;
    }
    if (descriptionField.getText().isEmpty()) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please enter a description");
      return false;
    }
    return true;
  }

  public void clearInput() {
    nameField.clear();
    descriptionField.clear();
  }

  public void searchTable() {
    String searchValue = searchField.getText();
    Predicate<Genre> predicate = genre -> genre.getName().toLowerCase().contains(searchValue.toLowerCase()) || genre.getDescription().toLowerCase().contains(searchValue.toLowerCase());

    List<Genre> filteredList = genreList.stream().filter(predicate).toList();
    genreTable.setItems(FXCollections.observableList(filteredList));
  }
}
