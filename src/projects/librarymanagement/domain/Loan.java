package projects.librarymanagement.domain;

import java.time.LocalDate;
public class Loan {
    private final long id;
    private final String isbn;
    private final long memberId;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(long id, String isbn, long memberId, LocalDate loanDate, LocalDate dueDate) {
        this.id = id;
        this.isbn = isbn;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    public long getId() {return id;}
    public String getIsbn() {return isbn;}
    public long getMemberId() {return memberId;}
    public LocalDate getDueDate() {return dueDate;}
    public boolean isActive() {return returnDate == null;}

    public void markReturned(LocalDate date) {this.returnDate = date;}

    public String pretty() {
        String status = isActive() ? "ACTIVE" : "RETURNED ON : " + returnDate;
        return String.format("Loan#%d | ISBN = %s | member = %d | loan = %s | due = %s | %s",
                id, isbn, memberId, loanDate, dueDate, status);
    }
}
