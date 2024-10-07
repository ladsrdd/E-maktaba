package esi.emaktaba.dao;

import esi.emaktaba.model.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO {
  private final Connection connection;

  public GenreDAO(Connection connection) {
    this.connection = connection;
  }

  public void addGenre(Genre genre) throws SQLException {
    String query = "INSERT INTO genre (name, description) VALUES (?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, genre.getName());
      preparedStatement.setString(2, genre.getDescription());
      preparedStatement.executeUpdate();
    }
  }

  public void updateGenre(Genre genre) throws SQLException {
    String query = "UPDATE genre SET name = ?, description = ? WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, genre.getName());
      preparedStatement.setString(2, genre.getDescription());
      preparedStatement.setInt(3, genre.getId());
      preparedStatement.executeUpdate();
    }
  }

  public void deleteGenre(Genre genre) throws SQLException {
    String query = "DELETE FROM genre WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, genre.getId());
      preparedStatement.executeUpdate();
    }
  }

  public Genre getGenreById(int id) throws SQLException {
    String query = "SELECT * FROM genre WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("id"));
        genre.setName(resultSet.getString("name"));
        genre.setDescription(resultSet.getString("description"));
        return genre;
      }
    }
    return null;
  }

  public List<Genre> getAllGenres() throws SQLException {
    List<Genre> genres = new ArrayList<>();
    String query = "SELECT * FROM genre";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("id"));
        genre.setName(resultSet.getString("name"));
        genre.setDescription(resultSet.getString("description"));
        genres.add(genre);
      }
    }
    return genres;
  }
}
