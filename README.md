# Java Projects ☕
![Java](https://img.shields.io/badge/Java-JDK_21-orange)
![IDE](https://img.shields.io/badge/IDE-IntelliJ_IDEA-blue)
![Status](https://img.shields.io/badge/Status-Active-brightgreen)

A collection of Java console applications developed during my training at **Coding Factory (AUEB)**. These projects demonstrate my progression from basic syntax to more complex Object-Oriented Design patterns and Data Structures.

## 🚀 Featured Projects (in `src/projects`)

### 1. [Library Management System (New!)](src/projects/librarymanagement)
**Focus:** *Separation of Concerns, Java Streams, Service Layer Pattern*

A robust library simulation that manages books, members, and loans completely in-memory. This project represents a step up in architecture, separating the User Interface (Console) from the Business Logic.

* **Architecture:** Separation of `UI` (Main), `Service` (Logic), and `Domain` (Entities).
* **Key Features:**
    * **Smart Loan System:** Prevents borrowing if copies are unavailable or user validation fails.
    * **Java Streams:** Used extensively for filtering (searching books) and sorting (listing active loans by due date).
    * **Robust Validation:** Inputs are protected against crashes; ISBNs and Dates are handled logically.
    * **Logic:** Calculates available copies dynamically based on active loans (Single Source of Truth).

### 2. [Contact Book](src/projects/contactbook)
**Focus:** *CRUD Operations, Data Structures (Maps), Input Validation*

A functional address book application that allows users to manage their contacts efficiently.

* **Algorithms:** Uses **HashMap Indexing** to search for phone numbers in O(1) time (instant lookup) rather than looping through lists.
* **Validation:** Strict Regex validation for E.164 phone formats and email addresses.
* **Features:** Add, Search (by name prefix or phone), Update, and Delete contacts.

---

## 🕹️ Mini Apps (in `src/mini_apps`)
Smaller exercises focused on specific logic challenges:

* **[Guess Game](src/mini_apps/GuessGameApp.java):** A number guessing game implementing "lives" and retry logic.
* **[Entry System](src/mini_apps/EntrySystemApp.java):** Logic gates for age checks and VIP access control.
* **[FizzBuzz](src/mini_apps/FizzBuzzApp.java):** The classic coding interview question implementation.

## 🛠️ Tech Stack
* **Language:** Java 21 (Amazon Corretto)
* **Concepts:** OOP (Encapsulation, Polymorphism), Java Streams API, Collections Framework (List, Map, Set).
* **Tools:** IntelliJ IDEA, Git & GitHub.

## 📂 Project Structure
The repository is organized into two main categories:

```text
Java-Projects/
└── src/
    ├── mini_apps/             # Logic puzzles & basic exercises
    │   ├── EntrySystem
    │   ├── FizzBuzz
    │   └── GuessGame
    │
    └── projects/              # Full-featured applications
        ├── contactbook/       # HashMap & CRUD demo
        └── librarymanagement/ # Advanced architecture demo
            ├── domain/        # Entities (Book, Loan, Member)
            ├── service/       # Business Logic (LibraryService)
            └── ui/            # Entry point (LibraryApp)
```
## 🏃 How to Run
1. Clone the repository.
2. Open the folder in IntelliJ IDEA.
3. Navigate to the entry point: `src/projects/librarymanagement/ui/LibraryApp.java`
4. Right-click the class file and select Run.
##
Created by Giannis as part of the Coding Factory curriculum.
