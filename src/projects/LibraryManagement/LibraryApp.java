package projects.LibraryManagement;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple Library CLI app.
 * Features:
 * - Add books (ISBN unique) + total copies
 * - Register members (auto ID)
 * - Borrow book (creates a Loan, checks availability)
 * - Return book (by loan id)
 * - Search books by title prefix
 * - List active loans + member loan history
 * - Update a member's email
 * - View members details +  number of total loans + number of active loans
 * - List of all members with each ones number of total loans + number of active loans
 * @author Giannis
 * @version 1.0.0
 */

public class LibraryApp {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        LibraryService service = new LibraryService();
        while(true){
            printMenu();
            int choice = readInt(reader, "Please choose an option: ");
            switch (choice){
                case 1 -> addBook(reader, service);
                case 2 -> registerMember(reader, service);
                case 3 -> listBooks(service);
                case 4 -> searchBooksByTitle(reader, service);
                case 5 -> borrowBook(reader, service);
                case 6 -> returnBook(reader, service);
                case 7 -> listActiveLoans(service);
                case 8 -> listLoansByMember(reader, service);
                case 9 -> updateMemberEmail(reader, service);
                case 10 -> viewMemberDetails(reader, service);
                case 11 -> listMembers(service);
                case 0 -> {
                    System.out.println("Goodbye!");
                    reader.close();
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }
    //-------------------------------- UI FLOWS -------------------------------------------

    private static void addBook(Scanner sc, LibraryService service) {
        System.out.println("*** Adding new book ***");
        String isbn = readIsbn(sc, "ISBN (unique): ");
        String title = readNonBlank(sc, "Title: ");
        String author = readNonBlank(sc, "Author: ");
        int copies = readNonNegativeInt(sc, "Total Copies: ");

        Book b = service.addBook(isbn, title, author, copies);
        if(b == null) System.out.println("A book with that ISBN already exists!");
        else System.out.println("Book added: " + b.pretty(service.availableCopies(isbn)));
    }

    private static void registerMember( Scanner sc, LibraryService service) {
        System.out.println("*** Registering member ***");
        String name = readNonBlank(sc, "Name: ");
        String email = readOptionalEmail(sc, "Email (optional): "); // optional
        Member m = service.registerMember(name, email);
        System.out.println("Member registered. ID: " + m.getId());
    }

    private static void listBooks(LibraryService service) {
        System.out.println("*** Listing books ***");
        List<Book> books = service.listBooksSortedByTitle();
        if(books.isEmpty()){
            System.out.println("No books found!");
            return;
        }
        for(Book b : books){
            int avail = service.availableCopies(b.getIsbn());
            System.out.println(b.pretty(avail));
        }
    }

    private static void searchBooksByTitle(Scanner sc, LibraryService service) {
        System.out.println("*** Searching books by title prefix ***");
        String prefix = readNonBlank(sc, "Title starts with: ");
        List<Book> res = service.searchBooksByTitlePrefix(prefix);
        if(res.isEmpty()){
            System.out.println("No matches found!");
            return;
        }
        System.out.println("Found " + res.size() + ":");
        for(Book b : res){
            int avail = service.availableCopies(b.getIsbn());
            System.out.println(b.pretty(avail));
        }
    }

    private static void borrowBook(Scanner sc , LibraryService service) {
        System.out.println("*** Borrowing book ***");
        String isbn = readIsbn(sc, "ISBN: ");
        long memberId = readLong(sc, "Member ID: ");
        Loan loan = service.borrow(isbn, memberId);
        if(loan == null){
            System.out.println("Loan failed! Check: ISBN exists, member exists " +
                    "and copies available");
            return;
        }
        System.out.println("Borrowed Successfully!");
        System.out.println(loan.pretty());
    }

    private static void returnBook(Scanner sc , LibraryService service) {
        System.out.println("*** Returning book ***");
        long loanId = readLong(sc, "Loan ID: ");
        boolean ok = service.returnLoan(loanId);
        System.out.println((ok) ? "Returned successfully!" : "Return failed! ( loan not found or already returned)");
    }

    private static void listActiveLoans(LibraryService service) {
        System.out.println("*** Listing active loans ***");
        List<Loan> active = service.listActiveLoansSortedByDueDate();
        if(active.isEmpty()){
            System.out.println("No active loans found!");
            return;
        }
        for(Loan l : active) System.out.println(l.pretty());
    }

    private static void listLoansByMember(Scanner sc, LibraryService service) {
        System.out.println("*** Loan history by Member ***");
        long memberId = readLong(sc, "Member ID: ");
        List<Loan> loans = service.listLoansByMember(memberId);
        if(loans.isEmpty()){
            System.out.println("No loans found for that member ( or member not found).");
            return;
        }
        System.out.println("Found " + loans.size() + ":");
        for(Loan l : loans) System.out.println(l.pretty());
    }

    private static void updateMemberEmail(Scanner sc, LibraryService service) {
        System.out.println("*** Updating member email ***");
        long memberId = readLong(sc, "Member ID: ");
        // optional email: blank -> null (means "remove email")
        String newEmail = readOptionalEmail(sc, "New Email (leave blank to remove): ");
        boolean ok = service.updateMemberEmail(memberId, newEmail);
        System.out.println((ok) ? "Email updated successfully!" : "Update failed ( member not found)");
    }

    private static void viewMemberDetails(Scanner sc, LibraryService service) {
        System.out.println("*** Viewing member details ***");
        long memberId = readLong(sc, "Member ID: ");
        Member m = service.findMemberById(memberId);
        if(m == null) {
            System.out.println("Member not found!");
            return;
        }
        System.out.println("ID: " + m.getId());
        System.out.println("Name: " + m.getName());
        System.out.println("Email: " + (m.getEmail().isBlank() ? "(none)" : m.getEmail()));
        List<Loan> loans = service.listLoansByMember(memberId);
        int totalLoans = loans.size();
        long activeLoans = loans.stream().filter(Loan::isActive).count();
        System.out.println("Loans: " + totalLoans + " active loans: " + activeLoans);
    }

    private static void listMembers(LibraryService service) {
        System.out.println("*** Listing members ***");
        List<Member> members = service.listMembersSortedByName();
        if(members.isEmpty()){
            System.out.println("No members found!");
            return;
        }
        Map<Long, LibraryService.LoanStats> stats = service.computeLoanStatsByMember();
        for(Member m : members){
            LibraryService.LoanStats s = stats.get(m.getId());
            int totalLoans = (s == null) ? 0 : s.total;
            int activeLoans = (s == null) ? 0 : s.active;
            String email = m.getEmail().isBlank() ? "(none)" : m.getEmail();
            System.out.println("ID: " + m.getId() + " | Name: " + m.getName() + " | Email: " + email
            + " | Total loans: " + totalLoans + " | Active loans: " + activeLoans);
        }
    }

    private static void printMenu() {
        System.out.println("****** Menu ******");
        System.out.println("1. Add new book");
        System.out.println("2. Register member");
        System.out.println("3. List books");
        System.out.println("4. Search books by title prefix");
        System.out.println("5. Borrow book");
        System.out.println("6. Return book (by loan ID)");
        System.out.println("7. List active loans");
        System.out.println("8. List loans by member");
        System.out.println("9. Update member email");
        System.out.println("10. View member details");
        System.out.println("11. List members");
        System.out.println("0. Exit");
    }

    // --------------------- INPUT HELPERS ------------------------------------------------
    private static String readLineOrExit(Scanner sc) {
        if (!sc.hasNextLine()) {
            System.out.println("\nInput ended. Goodbye!");
            System.exit(0);
        }
        return sc.nextLine().trim();
    }

    private static int readInt(Scanner sc, String prompt){
        while(true){
            System.out.print(prompt);
            String s = readLineOrExit(sc);
            try{
                return Integer.parseInt(s);
            } catch(NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private static long readLong(Scanner sc, String prompt){
        while(true){
            System.out.print(prompt);
            String s = readLineOrExit(sc);
            try {
                return Long.parseLong(s);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid integer.");}
        }

    }

    private static int readNonNegativeInt(Scanner sc, String prompt){
        while(true){
            int n = readInt(sc, prompt);
            if(n >= 0) return n;
            System.out.println("Must be a non-negative integer (0 allowed).");
        }
    }

    private static String readNonBlank(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = readLineOrExit(sc);
            if (!s.isEmpty()) return s;
            System.out.println("Please enter a non-blank string.");
        }
    }

    private static String readOptionalEmail(Scanner sc, String prompt){
        while(true){
            System.out.print(prompt);
            String e = readLineOrExit(sc);
            if (e.isEmpty()) return null; // optional
            if(isEmailLike(e)) return e;
            System.out.println("Email looks invalid. Try again or leave it blank.");
        }
    }

    // Email checker regex not RFC-perfect
    private static boolean isEmailLike(String email) {
        return (email != null) && (email.matches("(?i)^[A-Z0-9._%+-]+@(?:[A-Z0-9-]+\\.)+[A-Z]{2,}$"));
    }

    // Not "perfect ISBN", just decent: 10-17 chars digits/X/hyphen
    private static String readIsbn(Scanner sc, String prompt){
        while(true) {
            System.out.print(prompt);
            String isbn = readLineOrExit(sc);
            if (isbn.isEmpty()) {
                System.out.println("Please enter a valid ISBN.");
                continue;
            }
            if (!isbn.matches("(?i)^[0-9X-]{10,17}$")) {
                System.out.println("Please enter a valid ISBN. Example: 978-0134685991");
                continue;
            }
            return isbn.toUpperCase();
        }
    }

    //---------------------------------- DOMAIN -------------------------------------------
    static class Book {
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

    static class Member {
        private final long id; // PK immutable
        private final String name;
        private String email;
        public Member(long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public long getId() {return id;}
        public String getName() {return name;}
        public String getEmail() {return (email == null) ? "" : email;}

        public void setEmail(String email) {this.email = email;}
    }

    static class Loan {
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

    //----------------------- SERVICE (in-memory "DAO") -----------------------------------
    static class LibraryService {
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
            // email is either null (remove) or already validated by readOptionalEmail()
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

        static class LoanStats {
            int total;
            int active;
        }

        public Map<Long, LoanStats> computeLoanStatsByMember() {
            Map<Long, LoanStats> stats = new HashMap<>();
            for(Loan loan : loansById.values()) {
                long memberId = loan.getMemberId();
                LoanStats s = stats.get(memberId);
                if(s == null) {
                    s = new LoanStats();
                    stats.put(memberId, s);
                }
                s.total++;
                if(loan.isActive()) s.active++;
            }
            return stats;
        }
    }
}
