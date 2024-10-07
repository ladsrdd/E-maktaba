package esi.emaktaba.dao;

import esi.emaktaba.model.Book;
import esi.emaktaba.model.Loan;
import esi.emaktaba.model.Member;
import esi.emaktaba.model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
  private final Connection connection;
  private final BookDAO bookDAO;
  private final MemberDAO memberDAO;

  public LoanDAO(Connection connection) {
    this.connection = connection;
    this.bookDAO = new BookDAO(connection);
    this.memberDAO = new MemberDAO(connection);
  }

  public void addLoan(Loan loan) throws SQLException {
    String query = "INSERT INTO loan (memberID, bookID, loanDate, returnDate, status) VALUES (?,?,?,?,?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, loan.getMember().getId());
      preparedStatement.setInt(2, loan.getBook().getId());
      preparedStatement.setDate(3, loan.getLoanDate());
      preparedStatement.setDate(4, loan.getReturnDate());
      preparedStatement.setString(5, loan.getStatus().toString());
      preparedStatement.executeUpdate();
    }
  }

  public void updateLoan(Loan loan) throws SQLException {
    String query = "UPDATE loan SET memberID = ?, bookID = ?, loanDate = ?, returnDate = ?, status = ? WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, loan.getMember().getId());
      preparedStatement.setInt(2, loan.getBook().getId());
      preparedStatement.setDate(3, loan.getLoanDate());
      preparedStatement.setDate(4, loan.getReturnDate());
      preparedStatement.setString(5, loan.getStatus().toString());
      preparedStatement.setInt(6, loan.getId());
      preparedStatement.executeUpdate();
    }
  }

  public void deleteLoan(Loan loan) throws SQLException {
    String query = "DELETE FROM loan WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, loan.getId());
      preparedStatement.executeUpdate();
    }
  }

  public List<Loan> getAllLoans() throws SQLException {
    List<Loan> loans = new ArrayList<>();
    String query = "SELECT * FROM loan";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Loan loan = new Loan();
        Book book = bookDAO.getBookById(resultSet.getInt("bookID"));
        Member member = memberDAO.getMemberById(resultSet.getInt("memberID"));
        loan.setId(resultSet.getInt("id"));
        loan.setMember(member);
        loan.setBook(book);
        loan.setLoanDate(resultSet.getDate("loanDate"));
        loan.setReturnDate(resultSet.getDate("returnDate"));
        loan.setStatus(Status.valueOf(resultSet.getString("status")));
        loans.add(loan);
      }
    }
    return loans;
  }
}
