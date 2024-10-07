package esi.emaktaba.model;

import java.sql.Date;

public class Member {
  private int id;
  private String cin;
  private String fullName;
  private String email;
  private Date subStartDate;
  private Date subEndDate;

  @Override
  public String toString() {
    return fullName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCin() {
    return cin;
  }

  public void setCin(String cin) {
    this.cin = cin;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getSubStartDate() {
    return subStartDate;
  }

  public void setSubStartDate(Date subStartDate) {
    this.subStartDate = subStartDate;
  }

  public Date getSubEndDate() {
    return subEndDate;
  }

  public void setSubEndDate(Date subEndDate) {
    this.subEndDate = subEndDate;
  }

  public Member(int id, String cin, String fullName, String email, Date subStartDate, Date subEndDate) {
    this.id = id;
    this.cin = cin;
    this.fullName = fullName;
    this.email = email;
    this.subStartDate = subStartDate;
    this.subEndDate = subEndDate;
  }

  public Member(String cin, String fullName, String email, Date subStartDate, Date subEndDate) {
    this.cin = cin;
    this.fullName = fullName;
    this.email = email;
    this.subStartDate = subStartDate;
    this.subEndDate = subEndDate;
  }

  public Member() {
  }
}
