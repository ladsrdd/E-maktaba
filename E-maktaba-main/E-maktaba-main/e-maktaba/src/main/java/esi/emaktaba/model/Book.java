package esi.emaktaba.model;

public class Book {
  private int id;
  private String isbn;
  private String title;
  private Genre genre;
  private int quantity;

  @Override
  public String toString() {
    return title;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Genre getGenre() {
    return genre;
  }

  public void setGenre(Genre genre) {
    this.genre = genre;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public Book(int id, String isbn, String title, Genre genre, int quantity) {
    this.id = id;
    this.isbn = isbn;
    this.title = title;
    this.genre = genre;
    this.quantity = quantity;
  }

  public Book(String isbn, String title, Genre genre, int quantity) {
    this.isbn = isbn;
    this.title = title;
    this.genre = genre;
    this.quantity = quantity;
  }

  public Book() {
  }
}
