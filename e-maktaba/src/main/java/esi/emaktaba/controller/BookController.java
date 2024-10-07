package esi.emaktaba.controller;

import esi.emaktaba.dao.BookDAO;
import esi.emaktaba.dao.GenreDAO;
import esi.emaktaba.model.Book;
import esi.emaktaba.model.Genre;
import esi.emaktaba.utils.DialogPopup;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.controlsfx.control.SearchableComboBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public class BookController {
  @FXML
  private TextField isbnField;
  @FXML
  private TextField titleField;
  @FXML
  private SearchableComboBox<Genre> genreField;
  @FXML
  private TextField quantityField;
  @FXML
  private TextField searchField;
  @FXML
  private TableView<Book> bookTable;
  @FXML
  private TableColumn<Book, String> isbnColumn;
  @FXML
  private TableColumn<Book, String> titleColumn;
  @FXML
  private TableColumn<Book, String> genreColumn;
  @FXML
  private TableColumn<Book, String> quantityColumn;
  private BookDAO bookDAO;
  private GenreDAO genreDAO;
  private Book selectedBook = null;
  private List<Book> bookList;

  public void setDao(Connection connection) {
    this.bookDAO = new BookDAO(connection);
    this.genreDAO = new GenreDAO(connection);
  }

  public void populateTable() {
    try {
      this.bookList = bookDAO.getAllBooks();
      isbnColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getIsbn()));
      titleColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getTitle()));
      genreColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> (cellData.getValue().getGenre() != null) ? cellData.getValue().getGenre().getName() : "(Deleted Genre)"));
      quantityColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> String.valueOf(cellData.getValue().getQuantity())));
      bookTable.getItems().setAll(bookList);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void populateGenre() {
    try {
      List<Genre> genreList = genreDAO.getAllGenres();
      genreField.getItems().addAll(genreList);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void add() throws SQLException {
    Boolean flag = checkInput();
    if (!flag) return;

    Book newBook = new Book();
    newBook.setIsbn(isbnField.getText());
    newBook.setTitle(titleField.getText());
    newBook.setGenre(genreField.getValue());
    newBook.setQuantity(Integer.parseInt(quantityField.getText()));

    bookDAO.addBook(newBook);
    populateTable();
    selectedBook = null;
    DialogPopup.showDialogPopup(Alert.AlertType.INFORMATION, "Book added", "Book added successfully");
    clearInput();

  }

  public void edit() throws SQLException {
    if (selectedBook == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a book to edit");
      return;
    }

    Boolean flag = checkInput();
    if (!flag) return;

    selectedBook.setIsbn(isbnField.getText());
    selectedBook.setTitle(titleField.getText());
    selectedBook.setGenre(genreField.getValue());
    selectedBook.setQuantity(Integer.parseInt(quantityField.getText()));
    bookDAO.editBook(selectedBook);
    populateTable();
    selectedBook = null;
    DialogPopup.showDialogPopup(Alert.AlertType.INFORMATION, "Book updated", "Book updated successfully");
    clearInput();
  }

  public void delete() throws SQLException {
    if (selectedBook == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a book to delete");
      return;
    }
    bookDAO.deleteBook(selectedBook);
    populateTable();
    selectedBook = null;
    DialogPopup.showDialogPopup(Alert.AlertType.INFORMATION, "Book deleted", "Book deleted successfully");
    clearInput();
  }

  public void selectRow() {
    Book newSelectedBook = bookTable.getSelectionModel().getSelectedItem();
    if (newSelectedBook == this.selectedBook) return;
    this.selectedBook = newSelectedBook;
    isbnField.setText(newSelectedBook.getIsbn());
    titleField.setText(newSelectedBook.getTitle());
    genreField.setValue(newSelectedBook.getGenre());
    quantityField.setText(String.valueOf(newSelectedBook.getQuantity()));
    return;
  }

  public Boolean checkInput() {
    if (isbnField.getText().isEmpty()) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please enter a ISBN number");
      return false;
    }
    if (titleField.getText().isEmpty()) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please enter a title");
      return false;
    }
    if (genreField.getSelectionModel().getSelectedItem() == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a genre");
      return false;
    }
    if (quantityField.getText().isEmpty()) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please enter a quantity");
      return false;
    }
    try {
      Integer.parseInt(quantityField.getText());
    } catch (NumberFormatException e) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please enter a valid quantity");
      return false;
    }
    return true;
  }

  public void clearInput() {
    isbnField.clear();
    titleField.clear();
    genreField.setValue(null);
    quantityField.clear();
  }

  public void searchTable() {
    String searchValue = searchField.getText();
    Predicate<Book> predicate = book -> book.getIsbn().toLowerCase().contains(searchValue.toLowerCase()) || book.getTitle().toLowerCase().contains(searchValue.toLowerCase()) || book.getGenre().getName().toLowerCase().contains(searchValue.toLowerCase());

    List<Book> filteredList = bookList.stream().filter(predicate).toList();
    bookTable.setItems(FXCollections.observableList(filteredList));
  }
}
