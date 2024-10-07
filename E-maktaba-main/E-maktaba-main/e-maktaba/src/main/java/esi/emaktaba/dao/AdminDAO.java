package esi.emaktaba.dao;

import esi.emaktaba.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {
  private final Connection connection;

  public AdminDAO(Connection connection) {
    this.connection = connection;
  }

  public Boolean login(Admin admin) throws SQLException {
    String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, admin.getUsername());
      statement.setString(2, admin.getPassword());
      ResultSet resultSet = statement.executeQuery();
      return resultSet.next();
    }
  }

  public void edit(Admin admin) throws SQLException {

  }
}
