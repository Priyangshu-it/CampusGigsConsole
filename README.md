# CampusGigs

A console-based micro-internship and freelance portal for university students, built with **Java, JDBC and MySQL**.

Students find short-term gigs (a website for a professor, a video edit for a club, a bug fix for a senior's project). Clients, such as faculty, departments or local startups, post those gigs, review applications and assign the work. After completion, the client rates the student, so students build a campus profile.

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Database access | JDBC (MySQL Connector/J) |
| Database | MySQL |
| Interface | Console (Scanner / System.out) |
| IDE | IntelliJ IDEA |

## Features

### Students
- Register and log in
- View and update profile (name, department, year of study) and change password
- Add skills and view them
- Browse open gigs
- Search gigs by skill
- Filter gigs by budget range
- Apply to an open gig with a short proposal (applications are blocked once the deadline has passed)
- View application status (Waiting / Approved / Rejected)
- Withdraw an application that isn't approved
- View assigned and completed gigs
- View reviews received

### Clients
- Register and log in (each client belongs to an organization)
- View and update profile and change password
- Post a gig with title, description, budget, deadline and required skills
- View, update and delete their own open gigs
- View applications with each applicant's department, year and proposal
- Approve an application, which assigns the gig (status: In Progress) and rejects the other waiting applicants
- Handle expired, unassigned gigs by extending the deadline or deleting the gig
- Mark an assigned gig as Completed
- Give a 1-5 star rating and a written review for a completed gig (one review per gig)
- View reviews they have given

### Gig lifecycle

```
Open  ->  In Progress  ->  Completed  ->  Review
```

## Database design

The database is normalized and uses foreign keys with cascading deletes.

| Table | Purpose |
|---|---|
| `department` | List of departments |
| `students` | Student accounts (department, year of study) |
| `organization` | Organizations with a verification status |
| `client` | Client accounts, linked to an organization |
| `skills` | Master list of skills |
| `studentskills` | Student to skill (many-to-many) |
| `gigs` | Posted gigs, with client, assigned student, status and deadline |
| `gigskills` | Gig to skill (many-to-many) |
| `application` | Student proposals for gigs (unique per student and gig) |
| `reviews` | Client rating and remarks for a completed gig (unique per gig) |

## Project structure

```
CampusGigsConsole/
├── src/
│   ├── Main.java            # Entry point: role selection, register / login
│   ├── DBConnection.java    # Reads config.properties, opens the MySQL connection
│   ├── StudentConsole.java  # Student registration, login and menu
│   └── ClientConsole.java   # Client registration, login and menu
├── Database/
│   └── campusgigsdatabase.sql           # Tables and constraints 
├── config.properties.example
└── README.md
```

## Setup

### Prerequisites
- JDK 17 or later
- MySQL Server 8 or later
- MySQL Connector/J (the JDBC driver jar)
- IntelliJ IDEA (or any Java IDE)

### Steps

1. **Clone the repository**
   ```
   git clone https://github.com/Priyangshu-it/CampusGigsConsole.git
   ```

2. **Create the database.** Run `Database/campusgigsdatabase.sql` in MySQL Workbench or the MySQL command line.

3. **Add your database credentials.** Copy `config.properties.example` to `config.properties` in the project root (next to `src`) and fill in your MySQL details:
   ```
   db.url=jdbc:mysql://localhost:3306/campusgigs
   db.username=root
   db.password=your_password_here
   ```
   `config.properties` is listed in `.gitignore`, so your password is never committed.

4. **Add the JDBC driver.** In IntelliJ: *File -> Project Structure -> Libraries -> + -> Java*, then select the `mysql-connector-j` jar.

5. **Run** `Main.java`.

## Sample flow

1. Register a client and log in. Post a gig and tag it with required skills.
2. Register a student, add skills, find the gig and apply.
3. Log in as the client, view applications and approve one.
4. Mark the gig as completed and leave a rating.
5. Log in as the student and view the review.

## Possible improvements

- Hash passwords (for example with BCrypt) instead of storing plain text
- Use `PreparedStatement` for every query to prevent SQL injection
- Split SQL into DAO classes separate from the menu code
- Add a GUI or a REST API on top of the same database
