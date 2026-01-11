# Java Projects ☕

A collection of Java console applications developed during my training at **Coding Factory (AUEB)**. These projects demonstrate my progression from basic syntax to more complex Object-Oriented Design patterns and Data Structures.

## 🚀 Featured Projects (in `/projects`)

### 1. Library Management System (New!)
**Focus:** *Separation of Concerns, Java Streams, Service Layer Pattern*

A robust library simulation that manages books, members, and loans completely in-memory. This project represents a step up in architecture, separating the User Interface (Console) from the Business Logic.

* **Architecture:** Separation of `UI` (Main), `Service` (Logic), and `Domain` (Entities).
* **Key Features:**
    * **Smart Loan System:** Prevents borrowing if copies are unavailable or user validation fails.
    * **Java Streams:** Used extensively for filtering (searching books) and sorting (listing active loans by due date).
    * **Robust Validation:** Inputs are protected against crashes; ISBNs and Dates are handled logically.
    * **Logic:** Calculates available copies dynamically based on active loans (Single Source of Truth).

### 2. Contact Book
**Focus:** *CRUD Operations, Data Structures (Maps), Input Validation*

A functional address book application that allows users to manage their contacts efficiently.

* **Algorithms:** Uses **HashMap Indexing** to search for phone numbers in O(1) time (instant lookup) rather than looping through lists.
* **Validation:** Strict Regex validation for E.164 phone formats and email addresses.
* **Features:** Add, Search (by name prefix or phone), Update, and Delete contacts.

---

## 🕹️ Mini Apps (in `/mini_apps`)
Smaller exercises focused on specific logic challenges:

* **Guess Game:** A number guessing game implementing "lives" and retry logic.
* **Entry System:** Logic gates for age checks and VIP access control.
* **FizzBuzz:** The classic coding interview question implementation.

## 🛠️ Tech Stack
* **Language:** Java 21 (Amazon Corretto)
* **Concepts:** OOP (Encapsulation, Polymorphism), Java Streams API, Collections Framework (List, Map, Set).
* **Tools:** IntelliJ IDEA, Git & GitHub.

## 📂 Project Structure
The repository is organized into two main categories:

```text
Java-Projects/
├── mini_apps/             # Logic puzzles & basic exercises
│   ├── EntrySystem
│   ├── FizzBuzz
│   └── GuessGame
│
└── projects/              # Full-featured applications
    ├── ContactBook/       # HashMap & CRUD demo
    └── LibraryManagment/  # Advanced architecture demo
```
## 🏃 How to Run
1. Clone the repository.
2. Open the folder in IntelliJ IDEA.
3. Navigate to the specific file (e.g., projects/LibraryManagment/LibraryApp.java).
4. Right-click the class file and select Run.
##
Created by Giannis as part of the Coding Factory curriculum.
