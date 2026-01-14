# CURRENT STATUS
- Architecture is currently: Domain -> Service -> UI.
- Code is functional but monolithic.

# THE PLAN (PAUSED)
- GOAL: Refactor to: Controller -> Service -> DAO -> Database.
- 1. Create `BookDAO` (Connects to Postgres).
- 2. Create `BookDTO` (Data Transfer Object).
- 3. Refactor `LibraryService` to use DAO, not memory lists.
- 4. Create `LibraryController` to handle user input and call Service.

# REMINDER
- Do NOT put SQL in the Controller.
- Do NOT pass Entities to the UI.