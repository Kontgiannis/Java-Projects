package projects;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple Contact Book CLI app.
 * Features: add contacts, list sorted by name, search (name prefix / phone), update, delete, exit.
 *
 * Architecture notes:
 * - Model: Contact
 * - Service: ContactBookService
 * - UI: ContactBookApp (menu + input helpers)
 * - Persistence: in-memory maps (acts as DAO/repository)
 * - DTOs: not used here because this is a simple CLI app (no API/UI boundary).
 *   DTOs become useful when exposing data to a GUI/web/API layer.
 *
 * @author Giannis
 * @version 1.0.0
 */

public class ContactBookApp {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        ContactBookService service = new ContactBookService();

        while (true) {
            printMenu();
            int choice = readInt(reader, "Choose: ");
            switch (choice) {
                case 1 -> addContact(reader, service);
                case 2 -> listContact(service);
                case 3 -> searchContact(reader, service);
                case 4 -> searchContactByPhone(reader, service);
                case 5 -> updateContact(reader, service);
                case 6 -> deleteContact(reader, service);
                case 0 -> {
                    System.out.println("Goodbye!");
                    reader.close();
                    return;
                }
                default -> {
                    System.out.println("Invalid option");
                }
            }
        }
    }
    // ------------------------------------------- UI FLOWS --------------------------------------------

    private static void addContact(Scanner sc, ContactBookService service) {
        System.out.println("*** Adding contact ***");
        String name = readNonBlank(sc, "Name: ");
        String phone = readPhone(sc, "Phone (unique): ");
        String email = readOptionalEmail(sc, "Email (optional): ");
        Contact c = service.add(name, phone, email);
        if(c != null) System.out.println("Contact added ID: " + c.getId());
        else System.out.println("A contact with that phone already exists!");
    }

    private static void listContact(ContactBookService service) {
        System.out.println("*** Listing contacts ***");
        List<Contact> all = service.listAllSortedByName();
        // checking if Contact list is empty
        if (all.isEmpty()) {
            System.out.println("No contacts found!");
            return;
        }
        for(Contact c : all) {
            System.out.println(c.pretty());
        }
    }
    private static void searchContact(Scanner sc, ContactBookService service) {
        System.out.println("*** Searching for contact ***");
        String prefix = readNonBlank(sc, "Name starts with: ");
        List<Contact> res = service.searchByNamePrefix(prefix);
        if (res.isEmpty()) {
            System.out.println("No matches found!");
            return;
        }
        System.out.println("Found " + res.size() + ":");
        for(Contact c : res) {
            System.out.println(c.pretty());
        }
    }

    private static void searchContactByPhone(Scanner sc, ContactBookService service) {
        System.out.println("*** Searching for contact by Phone ***");
        String phone = readPhone(sc, "Phone: ");
        Contact c = service.findByPhone(phone);
        if (c == null) {
            System.out.println("No contact found with that phone!");
            return;
        }
        System.out.println(c.pretty());
    }

    private static void updateContact(Scanner sc, ContactBookService service){
        System.out.println("*** Updating contact ***");
        // Checking if the contact exists so it can be updated
        long id = readLong(sc, "ID of contact to update: ");
        Contact existing = service.findById(id);
        if (existing == null) {
            System.out.println("No contact with that ID found!");
            return;
        }
        System.out.println("Found: " + existing.pretty());
        System.out.println("Leave field blank to keep current value");
        // update  name phone email
        String newName = readMaybeBlank(sc, "New name: ");
        String newPhone = readOptionalPhone(sc, "New phone: ");
        String newEmail = readMaybeBlank(sc, "New email: ");
        boolean ok = service.update(id, newName, newPhone, newEmail);
        System.out.println((ok) ? "Updated successfully!" : "Failed to update! Phone already exists!");
    }

    private static void deleteContact(Scanner sc, ContactBookService service) {
        System.out.println("*** Deleting contact ***");
        // Checking if the contact exists so it can be updated
        long id = readLong(sc, "ID of contact to delete: ");
        Contact existing = service.findById(id);
        if (existing == null) {
            System.out.println("No contact with that ID found!");
            return;
        }
        boolean ok = service.deleteById(id);
        System.out.println((ok) ? "Deleted successfully!" : "No contact with that ID found!");
    }

    private static void printMenu() {
        System.out.println("*** ContactBook App ***");
        System.out.println("1. Add contact");
        System.out.println("2. List contacts");
        System.out.println("3. Search by name prefix");
        System.out.println("4. Search by phone");
        System.out.println("5. Update contact (by id)");
        System.out.println("6. Delete contact (by id)");
        System.out.println("0. Exit");
    }
    // ----------------------------------- INPUT HELPERS -----------------------------------------------
    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number");
            }
        }
    }

    private static String readNonBlank(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Cannot be empty");
        }
    }

    private static String readMaybeBlank(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim(); // can be empty
    }

    private static String readPhone(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String p = sc.nextLine().trim();
            if (p.isEmpty()) {
                System.out.println("Phone cannot be empty");
                continue;
            }
            // Phone simple validation
            if(!p.matches("^\\+[1-9]\\d{1,14}$")) {
                System.out.println("Phone number is invalid. Try again Use E.164 like +306912345678.");
                continue;
            }
            return p;
        }
    }

    private static String readOptionalEmail(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String e = sc.nextLine().trim();
            if (e.isEmpty()) return ""; // optional
            if(isEmailLike(e)) return e;
            System.out.println("Email looks invalid try again or leave blank");
        }
    }

    private static boolean isEmailLike(String email) {
        return (email != null) && (email.matches("(?i)^[A-Z0-9._%+-]+@(?:[A-Z0-9-]+\\.)+[A-Z]{2,}$"));
    }

    private static long readLong(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private static String readOptionalPhone(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String phone = sc.nextLine().trim();
            if (phone.isEmpty()) return ""; // optional
            if (isPhoneLike(phone)) return phone;
            System.out.println("Invalid phone. Use E.164 like +306912345678 or leave blank");
        }
    }

    private static boolean isPhoneLike(String phone) {
        return phone.matches("^\\+[1-9]\\d{1,14}$");
    }

    // --------------------------------------- DOMAIN --------------------------------------------------
    static class Contact {
        private final long id; // PK immutable
        private String name;
        private String phone; // UNIQUE ( mutable)
        private String email;

        public Contact( long id, String name, String phone, String email) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.email = email == null ? "" : email;
        }

        public long getId() {return id;}
        public String getName() {return name;}
        public String getPhone() {return phone;}
        public String getEmail() {return email;}

        public void setName(String name) {this.name = name;}
        public void setPhone(String phone) {this.phone = phone;}
        public void setEmail(String email) {this.email = email;}

        public String pretty() {
            String emailPart = (email == null || email.isBlank()) ? "(no email)" : email;
            return String.format("%d | %s | %s | %s", id, name, phone, emailPart);
        }
    }

    static class ContactBookService {
        private final Map<Long, Contact> byID = new HashMap<>();
        private final Map<String, Long> phoneIndex = new HashMap<>();
        private long nextID = 1L;

       public Contact add(String name, String phone, String email) {
           if(phoneIndex.containsKey(phone)) return null;
           long id = nextID++;
           Contact c = new Contact(id, name, phone, email);
           byID.put(id, c);
           phoneIndex.put(phone, id);
           return c;
       }

       public Contact findById(long id) {
           return byID.get(id);
       }

       public Contact  findByPhone(String phone) {
           Long id = phoneIndex.get(phone);
           return (id == null) ? null : byID.get(id);
       }

        public boolean deleteById(long id) {
           Contact c = byID.remove(id);
            if (c == null) return false;
            phoneIndex.remove(c.getPhone());
            return true;
        }

        public boolean update( long id, String newName, String newPhone, String newEmail) {
            Contact c = byID.get(id);
            if (c == null) return false;
            if(newName != null && !newName.isBlank()) c.setName(newName);
            if(newEmail != null && !newEmail.isBlank()) {
                // validate if provided
                if(!isEmailLike(newEmail)) {
                    System.out.println("Invalid email format. Keeping previous email.");
                } else {
                    c.setEmail(newEmail);
                }
            }
            if(newPhone != null && !newPhone.isBlank()) {
                // UNIQUE check
                Long existingID = phoneIndex.get(newPhone);
                if (existingID != null && existingID != id) return false;

                // re-index
                phoneIndex.remove(c.getPhone());
                c.setPhone(newPhone);
                phoneIndex.put(newPhone, id);
            }
            return true;
        }

        public List<Contact> listAllSortedByName() {
            return byID.values().stream()
                    .sorted(Comparator.comparing(Contact::getName, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
        }

        public List<Contact> searchByNamePrefix(String prefix) {
            String p = prefix.toLowerCase();
            return byID.values().stream()
                    .filter(c -> c.getName().toLowerCase().startsWith(p))
                    .sorted(Comparator.comparing(Contact::getName, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
        }
    }
}
