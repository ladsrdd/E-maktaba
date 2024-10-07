package esi.emaktaba.dao;

import esi.emaktaba.model.Book;
import esi.emaktaba.model.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
  private final Connection connection;
  private final GenreDAO genreDAO;

  public BookDAO(Connection connection) {
    this.connection = connection;
    this.genreDAO = new GenreDAO(connection);
  }

  public void addBook(Book book) throws SQLException {
    String query = "INSERT INTO book (isbn, title, genreID, quantity) VALUES (?, ?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, book.getIsbn());
      preparedStatement.setString(2, book.getTitle());
      preparedStatement.setInt(3, book.getGenre().getId());
      preparedStatement.setInt(4, book.getQuantity());
      preparedStatement.executeUpdate();
    }
  }

  public void editBook(Book book) throws SQLException {
    String query = "UPDATE book set isbn = ?, title = ?, genreID = ?, quantity = ? WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, book.getIsbn());
      preparedStatement.setString(2, book.getTitle());
      preparedStatement.setInt(3, book.getGenre().getId());
      preparedStatement.setInt(4, book.getQuantity());
      preparedStatement.setInt(5, book.getId());
      preparedStatement.executeUpdate();
    }
  }

  public void deleteBook(Book book) throws SQLException {
    String query = "DELETE FROM book WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, book.getId());
      preparedStatement.executeUpdate();
    }
  }

  public Book getBookById(int id) throws SQLException {
    String query = "SELECT * FROM book WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getBookFromRS(resultSet);
      }
    }
    return null;
  }

  public List<Book> getAllBooks() throws SQLException {
    List<Book> books = new ArrayList<>();
    String query = "SELECT * FROM book";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        books.add(getBookFromRS(resultSet));
      }
    }
    return books;
  }

  public List<Book> getAllAvailableBooks() throws SQLException {
    List<Book> books = new ArrayList<>();
    String query = "SELECT * FROM book WHERE ((book.quantity - (SELECT COUNT(*) FROM loan WHERE (status = 'ACTIVE' OR status = 'OVERDUE') AND bookID = book.id)) > 0)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        books.add(getBookFromRS(resultSet));
      }
    }
    return books;
  }


  private Book getBookFromRS(ResultSet resultSet) throws SQLException {
    Book book = new Book();
    Genre genre = genreDAO.getGenreById(resultSet.getInt("genreID"));
    book.setId(resultSet.getInt("id"));
    book.setIsbn(resultSet.getString("isbn"));
    book.setTitle(resultSet.getString("title"));
    book.setGenre(genre);
    book.setQuantity(resultSet.getInt("quantity"));
    return book;
  }
}
