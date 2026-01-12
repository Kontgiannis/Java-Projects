package projects.librarymanagement.ui;

import projects.librarymanagement.domain.Book;
import projects.librarymanagement.domain.Loan;
import projects.librarymanagement.domain.Member;
import projects.librarymanagement.service.LibraryService;

import java.util.*;

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
        String email = readEmail(sc, "Email (optional): "); // optional
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
        // email is required ( validated by readEmail)
        String newEmail = readEmail(sc, "New Email: ");
        boolean ok = service.updateMemberEmail(memberId, newEmail);
        System.out.println((ok) ? "Email updated successfully!" : "Update failed ( member not found / invalid email)");
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
        Map<Long, LibraryService.LoanStats> stats = service.computeLoanStatsByMember();
        LibraryService.LoanStats stat = stats.get(memberId);
        int totalLoans = (stat == null) ? 0 : stat.getTotal();
        long activeLoans = (stat == null) ? 0 : stat.getTotal();
        System.out.println("Loans: " + totalLoans + " | active loans: " + activeLoans);
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
            int totalLoans = (s == null) ? 0 : s.getTotal();
            int activeLoans = (s == null) ? 0 : s.getActive();
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

    private static String readEmail(Scanner sc, String prompt){
        while(true){
            System.out.print(prompt);
            String e = readLineOrExit(sc);
            if (e.isBlank()) {
                System.out.println("Email is required.");
                continue;
            }
            if(isEmailLike(e)) return e.trim();
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
}
