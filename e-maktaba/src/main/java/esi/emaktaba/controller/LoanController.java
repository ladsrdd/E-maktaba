package esi.emaktaba.controller;

import esi.emaktaba.dao.BookDAO;
import esi.emaktaba.dao.LoanDAO;
import esi.emaktaba.dao.MemberDAO;
import esi.emaktaba.model.Book;
import esi.emaktaba.model.Loan;
import esi.emaktaba.model.Member;
import esi.emaktaba.model.Status;
import esi.emaktaba.utils.DialogPopup;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.SearchableComboBox;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

public class LoanController {
  @FXML
  private SearchableComboBox<Member> memberField;
  @FXML
  private SearchableComboBox<Book> bookField;
  @FXML
  private DatePicker loanDateField;
  @FXML
  private DatePicker returnDateField;
  @FXML
  private SearchableComboBox<Status> statusField;
  @FXML
  private TextField searchField;
  @FXML
  private TableView<Loan> loanTable;
  @FXML
  private TableColumn<Loan, String> memberColumn;
  @FXML
  private TableColumn<Loan, String> bookColumn;
  @FXML
  private TableColumn<Loan, String> loanDateColumn;
  @FXML
  private TableColumn<Loan, String> returnDateColumn;
  @FXML
  private TableColumn<Loan, String> statusColumn;
  private LoanDAO loanDAO;
  private MemberDAO memberDAO;
  private BookDAO bookDAO;
  private Loan selectedLoan = null;
  private List<Loan> loanList;

  public void setDao(Connection connection) {
    this.loanDAO = new LoanDAO(connection);
    this.memberDAO = new MemberDAO(connection);
    this.bookDAO = new BookDAO(connection);
  }

  public void populateTable() {
    try {
      this.loanList = loanDAO.getAllLoans();
      memberColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> (cellData.getValue().getMember() != null) ? cellData.getValue().getMember().getFullName() : "(Deleted member)"));
      bookColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> (cellData.getValue().getBook() != null) ? cellData.getValue().getBook().getTitle() : "(Deleted book)"));
      loanDateColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getLoanDate().toString()));
      returnDateColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getReturnDate().toString()));
      statusColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getStatus().toString()));
      loanTable.getItems().setAll(loanList);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void populateMembers() {
    try {
      List<Member> memberList = memberDAO.getAllMembersWithActiveSubscription();
      memberField.getItems().setAll(memberList);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void populateBooks() {
    try {
      List<Book> bookList = bookDAO.getAllAvailableBooks();
      bookField.getItems().setAll(bookList);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void populateStatus() {
    statusField.getItems().setAll(Status.values());
  }

  public void add() throws SQLException {
    Boolean flag = checkInput();
    if (!flag) return;

    Loan newLoan = new Loan();
    newLoan.setMember(memberField.getValue());
    newLoan.setBook(bookField.getValue());
    newLoan.setLoanDate(Date.valueOf(loanDateField.getValue().format(DateTimeFormatter.ISO_DATE)));
    newLoan.setReturnDate(Date.valueOf(returnDateField.getValue().format(DateTimeFormatter.ISO_DATE)));
    newLoan.setStatus(statusField.getValue());

    loanDAO.addLoan(newLoan);
    populateTable();
    selectedLoan = null;
    DialogPopup.showDialogPopup(Alert.AlertType.INFORMATION, "Loan added", "Loan added successfully");
    clearInput();
    populateBooks();
  }

  public void edit() throws SQLException {
    if (selectedLoan == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a loan to edit");
      return;
    }

    Boolean flag = checkInput();
    if (!flag) return;

    selectedLoan.setMember(memberField.getValue());
    selectedLoan.setBook(bookField.getValue());
    selectedLoan.setLoanDate(Date.valueOf(loanDateField.getValue().toString()));
    selectedLoan.setReturnDate(Date.valueOf(returnDateField.getValue().toString()));
    selectedLoan.setStatus(statusField.getValue());

    loanDAO.updateLoan(selectedLoan);
    populateTable();
    selectedLoan = null;
    DialogPopup.showDialogPopup(Alert.AlertType.INFORMATION, "Loan updated", "Loan updated successfully");
    clearInput();
    populateBooks();
  }

  public void delete() throws SQLException {
    if (selectedLoan == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a loan to delete");
      return;
    }
    loanDAO.deleteLoan(selectedLoan);
    populateTable();
    selectedLoan = null;
    DialogPopup.showDialogPopup(Alert.AlertType.INFORMATION, "Loan deleted", "Loan deleted successfully");
    clearInput();
    populateBooks();
  }

  public void selectRow() {
    Loan newSelectedLoan = loanTable.getSelectionModel().getSelectedItem();
    if (newSelectedLoan == this.selectedLoan) return;
    this.selectedLoan = newSelectedLoan;
    memberField.setValue(newSelectedLoan.getMember());
    bookField.setValue(newSelectedLoan.getBook());
    loanDateField.setValue(newSelectedLoan.getLoanDate().toLocalDate());
    returnDateField.setValue(newSelectedLoan.getReturnDate().toLocalDate());
    statusField.setValue(newSelectedLoan.getStatus());
    return;
  }

  public Boolean checkInput() {
    if (memberField.getSelectionModel().getSelectedItem() == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a member");
      return false;
    }
    if (bookField.getSelectionModel().getSelectedItem() == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a book");
      return false;
    }
    if (loanDateField.getValue() == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a loan date");
      return false;
    }
    if (returnDateField.getValue() == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a return date");
      return false;
    }
    if (statusField.getSelectionModel().getSelectedItem() == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a status");
      return false;
    }
    if (memberField.getValue().getSubEndDate().before(Date.valueOf(returnDateField.getValue().format(DateTimeFormatter.ISO_DATE)))) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "The return date exceed the member's subscription expiration date");
      return false;
    }
if (returnDateField.getValue().isBefore(loanDateField.getValue())) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "The return date can't be before the loan date");
      return false;
    }
    return true;
  }

  public void clearInput() {
    memberField.setValue(null);
    bookField.setValue(null);
    loanDateField.setValue(null);
    returnDateField.setValue(null);
    statusField.setValue(null);
  }

  public void searchTable() {
    String searchValue = searchField.getText();
    Predicate<Loan> predicate = loan -> loan.getMember().getFullName().toLowerCase().contains(searchValue.toLowerCase()) || loan.getBook().getTitle().toLowerCase().contains(searchValue.toLowerCase()) || loan.getStatus().toString().toLowerCase().contains(searchValue.toLowerCase());
    List<Loan> filteredList = loanList.stream().filter(predicate).toList();
    loanTable.setItems(FXCollections.observableList(filteredList));
  }
}
