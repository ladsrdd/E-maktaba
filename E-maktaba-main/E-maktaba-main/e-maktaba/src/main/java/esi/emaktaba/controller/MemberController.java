package esi.emaktaba.controller;

import esi.emaktaba.dao.MemberDAO;
import esi.emaktaba.model.Member;
import esi.emaktaba.utils.DialogPopup;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

public class MemberController {
  @FXML
  private TextField searchField;
  @FXML
  private TextField cinField;
  @FXML
  private TextField fullNameField;
  @FXML
  private TextField emailField;
  @FXML
  private DatePicker subStartDateField;
  @FXML
  private DatePicker subEndDateField;
  @FXML
  private TableView<Member> memberTable;
  @FXML
  private TableColumn<Member, String> cinColumn;
  @FXML
  private TableColumn<Member, String> fullNameColumn;
  @FXML
  private TableColumn<Member, String> emailColumn;
  @FXML
  private TableColumn<Member, String> subStartDateColumn;
  @FXML
  private TableColumn<Member, String> subEndDateColumn;
  private MemberDAO memberDAO;
  private Member selectedMember = null;
  private List<Member> memberList;

  public void setDao(Connection connection) {
    this.memberDAO = new MemberDAO(connection);
  }

  public void populateTable() {
    try {
      this.memberList = memberDAO.getAllMembers();
      cinColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getCin()));
      fullNameColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getFullName()));
      emailColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getEmail()));
      subStartDateColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getSubStartDate().toString()));
      subEndDateColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getSubEndDate().toString()));
      memberTable.getItems().setAll(memberList);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void add() throws SQLException {
    Boolean flag = checkInput();
    if (!flag) return;

    Member member = new Member();
    member.setCin(cinField.getText());
    member.setFullName(fullNameField.getText());
    member.setEmail(emailField.getText());
    member.setSubStartDate(Date.valueOf(subStartDateField.getValue().format(DateTimeFormatter.ISO_DATE)));
    member.setSubEndDate(Date.valueOf(subEndDateField.getValue().format(DateTimeFormatter.ISO_DATE)));

    memberDAO.addMember(member);

    populateTable();
    selectedMember = null;
    DialogPopup.showDialogPopup(Alert.AlertType.INFORMATION, "Member added", "Member added successfully");
    clearInput();
  }

  public void edit() throws SQLException {
    if (selectedMember == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a member to edit");
      return;
    }

    Boolean flag = checkInput();
    if (!flag) return;

    selectedMember.setCin(cinField.getText());
    selectedMember.setFullName(fullNameField.getText());
    selectedMember.setEmail(emailField.getText());
    selectedMember.setSubStartDate(Date.valueOf(subStartDateField.getValue().toString()));
    selectedMember.setSubEndDate(Date.valueOf(subEndDateField.getValue().toString()));
    memberDAO.updateMember(selectedMember);

    populateTable();
    selectedMember = null;
    DialogPopup.showDialogPopup(Alert.AlertType.INFORMATION, "Member updated", "Member updated successfully");
    clearInput();
  }

  public void delete() throws SQLException {
    if (selectedMember == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a member to delete");
      return;
    }
    memberDAO.deleteMember(selectedMember);
    populateTable();
    selectedMember = null;
    DialogPopup.showDialogPopup(Alert.AlertType.INFORMATION, "Member deleted", "Member deleted successfully");
    clearInput();
  }

  public void selectRow() {
    Member newSelectedMember = memberTable.getSelectionModel().getSelectedItem();
    if (newSelectedMember == this.selectedMember) return;
    this.selectedMember = newSelectedMember;
    cinField.setText(newSelectedMember.getCin());
    fullNameField.setText(newSelectedMember.getFullName());
    emailField.setText(newSelectedMember.getEmail());
    subStartDateField.setValue(newSelectedMember.getSubStartDate().toLocalDate());
    subEndDateField.setValue(newSelectedMember.getSubEndDate().toLocalDate());
    return;
  }

  public Boolean checkInput() {
    if (cinField.getText().isEmpty()) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please enter a cin code");
      return false;
    }
    if (fullNameField.getText().isEmpty()) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please enter a full name");
      return false;
    }
    if (emailField.getText().isEmpty()) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please enter an email address");
      return false;
    }
    if (subStartDateField.getValue() == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a start date for the subscription");
      return false;
    }
    if (subEndDateField.getValue() == null) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Please select a end date for the subscription");
      return false;
    }
    if (subStartDateField.getValue().isAfter(subEndDateField.getValue())) {
      DialogPopup.showDialogPopup(Alert.AlertType.ERROR, "Error", "Start date cannot be after end date");
      return false;
    }
    return true;
  }

  public void clearInput() {
    cinField.clear();
    fullNameField.clear();
    emailField.clear();
    subStartDateField.setValue(null);
    subEndDateField.setValue(null);
  }

  public void searchTable() {
    String searchValue = searchField.getText();
    Predicate<Member> predicate = member -> member.getCin().toLowerCase().contains(searchValue.toLowerCase()) || member.getFullName().toLowerCase().contains(searchValue.toLowerCase()) || member.getEmail().toLowerCase().contains(searchValue.toLowerCase());

    List<Member> filteredList = memberList.stream().filter(predicate).toList();
    memberTable.setItems(FXCollections.observableList(filteredList));
  }
}
