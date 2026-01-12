package projects.librarymanagement.domain;

public class Book {
    private final String isbn; //PK unique
    private final String title;
    private final String author;
    private final int totalCopies;

    public Book(String isbn, String title, String author, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
    }

    public String getIsbn() {return isbn;}
    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public int getTotalCopies() {return totalCopies;}

    public String pretty(int available) {
        return String.format("%s | %s | %s | available %d/%d", isbn, title, author, available, totalCopies);
    }
}
