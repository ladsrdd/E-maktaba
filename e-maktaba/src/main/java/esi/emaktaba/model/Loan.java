package esi.emaktaba.model;

import java.sql.Date;

public class Loan {
  private int id;
  private Member member;
  private Book book;
  private Date loanDate;
  private Date returnDate;
  private Status status;

  @Override
  public String toString() {
    return "loan{" + "id=" + id + ", member=" + member + ", book=" + book + ", loanDate=" + loanDate + ", returnDate=" + returnDate + ", status=" + status + '}';
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Member getMember() {
    return member;
  }

  public void setMember(Member member) {
    this.member = member;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public Date getLoanDate() {
    return loanDate;
  }

  public void setLoanDate(Date loanDate) {
    this.loanDate = loanDate;
  }

  public Date getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(Date returnDate) {
    this.returnDate = returnDate;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Loan(int id, Member member, Book book, Date loanDate, Date returnDate, Status status) {
    this.id = id;
    this.member = member;
    this.book = book;
    this.loanDate = loanDate;
    this.returnDate = returnDate;
    this.status = status;
  }

  public Loan(Member member, Book book, Date loanDate, Date returnDate, Status status) {
    this.member = member;
    this.book = book;
    this.loanDate = loanDate;
    this.returnDate = returnDate;
    this.status = status;
  }

  public Loan() {
  }
}
