package projects.librarymanagement.service;

import projects.librarymanagement.domain.Book;
import projects.librarymanagement.domain.Loan;
import projects.librarymanagement.domain.Member;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class LibraryService {
    private final Map<String, Book> booksByIsbn = new HashMap<>();
    private final Map<Long, Member> membersById = new HashMap<>();
    private final Map<Long, Loan> loansById = new HashMap<>();

    // counts active loans per ISBN ( so we can support multiple copies)
    private final Map<String, Integer> activeLoansCountByIsbn = new HashMap<>();

    private long nextMemberId = 1L;
    private long nextLoanId = 1L;

    public Book addBook(String isbn, String title, String author, int totalCopies) {
        if(booksByIsbn.containsKey(isbn)) return null;
        Book b = new Book(isbn, title, author, totalCopies);
        booksByIsbn.put(isbn, b);
        activeLoansCountByIsbn.put(isbn, 0);
        return b;
    }

    public Member registerMember(String name, String email) {
        long id = nextMemberId++;
        Member m = new Member(id, name, email);
        membersById.put(id, m);
        return m;
    }

    public List<Book> listBooksSortedByTitle() {
        return  booksByIsbn.values().stream()
                .sorted(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByTitlePrefix(String prefix) {
        String p = prefix.toLowerCase();
        return booksByIsbn.values().stream()
                .filter(b -> b.getTitle().toLowerCase().startsWith(p))
                .sorted(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public int availableCopies(String isbn) {
        Book b = booksByIsbn.get(isbn);
        if(b == null) return 0;
        int active = activeLoansCountByIsbn.getOrDefault(isbn, 0);
        return b.getTotalCopies() - active;
    }

    public Loan borrow(String isbn, long memberId) {
        Book b = booksByIsbn.get(isbn);
        Member m = membersById.get(memberId);
        if(b == null || m == null) return null;
        if(availableCopies(isbn) <= 0) return null;
        long id = nextLoanId++;
        LocalDate now = LocalDate.now();
        LocalDate dueDate = now.plusDays(14); // simple rule 2 weeks

        Loan loan = new Loan(id, isbn, memberId, now, dueDate);
        loansById.put(id, loan);
        activeLoansCountByIsbn.put(isbn, activeLoansCountByIsbn.getOrDefault(isbn, 0) + 1);
        return loan;
    }

    public boolean returnLoan(long loanId) {
        Loan loan = loansById.get(loanId);
        if(loan == null || !loan.isActive()) return false;
        loan.markReturned(LocalDate.now());
        String isbn = loan.getIsbn();
        int active = activeLoansCountByIsbn.getOrDefault(isbn, 0);
        activeLoansCountByIsbn.put(isbn, Math.max(0, active - 1));
        return true;
    }

    public List<Loan> listActiveLoansSortedByDueDate() {
        return loansById.values().stream()
                .filter(Loan::isActive)
                .sorted(Comparator.comparing(Loan::getDueDate))
                .collect(Collectors.toList());
    }

    public List<Loan> listLoansByMember(long memberId) {
        if(!membersById.containsKey(memberId)) return Collections.emptyList();
        return loansById.values().stream()
                .filter( l -> l.getMemberId() == memberId)
                .sorted(Comparator.comparing(Loan::getId))
                .collect(Collectors.toList());
    }

    public boolean updateMemberEmail(long memberId, String email) {
        Member m = membersById.get(memberId);
        if(m == null) return false;
        if(email == null || email.isBlank()) return false;
        m.setEmail(email);
        return true;
    }

    public Member findMemberById(long memberId) {
        return membersById.get(memberId);
    }

    public List<Member> listMembersSortedByName() {
        return membersById.values().stream()
                .sorted(Comparator.comparing(Member::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public class LoanStats {
        public int total;
        public int active;

        public void incTotal() {total++;}
        public void incActive() {active++;}

        public int getTotal() {return total;}
        public int getActive() {return active;}
    }

    public Map<Long, LoanStats> computeLoanStatsByMember() {
        Map<Long, LoanStats> stats = new HashMap<>();
        for(Loan loan : loansById.values()) {
            long memberId = loan.getMemberId();
            LoanStats s = stats.computeIfAbsent(memberId, k -> new LoanStats());
            if(loan.isActive()) s.active++;
        }
        return stats;
    }
}
