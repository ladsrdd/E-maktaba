package esi.emaktaba.dao;

import esi.emaktaba.model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
  private final Connection connection;

  public MemberDAO(Connection connection) {
    this.connection = connection;
  }

  public void addMember(Member member) throws SQLException {
    String query = "INSERT INTO member (cin, fullName, email, subStartDate, subEndDate) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, member.getCin());
      preparedStatement.setString(2, member.getFullName());
      preparedStatement.setString(3, member.getEmail());
      preparedStatement.setDate(4, member.getSubStartDate());
      preparedStatement.setDate(5, member.getSubEndDate());
      preparedStatement.executeUpdate();
    }
  }

  public void updateMember(Member member) throws SQLException {
    String query = "UPDATE member set cin = ?, fullName = ?, email = ?, subStartDate = ?, subEndDate = ? WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, member.getCin());
      preparedStatement.setString(2, member.getFullName());
      preparedStatement.setString(3, member.getEmail());
      preparedStatement.setDate(4, member.getSubStartDate());
      preparedStatement.setDate(5, member.getSubEndDate());
      preparedStatement.setInt(6, member.getId());
      preparedStatement.executeUpdate();
    }
  }

  public void deleteMember(Member member) throws SQLException {
    String query = "DELETE FROM member WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, member.getId());
      preparedStatement.executeUpdate();
    }
  }

  public Member getMemberById(int id) throws SQLException {
    String query = "SELECT * FROM member WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return getBookFromRS(resultSet);
      }
    }
    return null;
  }


  public List<Member> getAllMembers() throws SQLException {
    List<Member> members = new ArrayList<>();
    String query = "SELECT * FROM member";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        members.add(getBookFromRS(resultSet));
      }
    }
    return members;
  }

  public List<Member> getAllMembersWithActiveSubscription() throws SQLException {
    List<Member> members = new ArrayList<>();
    String query = "SELECT * FROM member WHERE subEndDate > current_date";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        members.add(getBookFromRS(resultSet));
      }
    }
    return members;
  }

  private Member getBookFromRS(ResultSet resultSet) throws SQLException {
    Member member = new Member();
    member.setId(resultSet.getInt("id"));
    member.setCin(resultSet.getString("cin"));
    member.setFullName(resultSet.getString("fullName"));
    member.setEmail(resultSet.getString("email"));
    member.setSubStartDate(resultSet.getDate("subStartDate"));
    member.setSubEndDate(resultSet.getDate("subEndDate"));
    return member;
  }
}
