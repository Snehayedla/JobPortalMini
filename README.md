Mini Job Portal System (Java Console)
A menu-driven console-based Job Portal application built using Core Java, demonstrating OOP concepts and Java Collections without any frameworks or databases.​

Objective
Develop a small Job Portal where:

Admin can view all jobs and see applicants per job.​

Company can post new jobs and close existing jobs.​

Candidate can view open jobs and apply to them.​

Implement clean OOP design and use Collections for all in-memory data handling.​

Tech Stack & Concepts
Language: Java (Core, console-based)

Paradigm: OOP

Inheritance, Polymorphism, Abstraction, Encapsulation​

Collections: List, Set, Map (e.g., HashMap, HashSet)​

equals() and hashCode() for entity identity (e.g., Job)​

Exception handling and input validation

No frameworks, no database, no streams/lambdas (per assignment rules)​

Project Structure
text
src/
└── com/
    └── jobportal/
        ├── model/
        │   ├── User.java          // abstract base user
        │   ├── Admin.java         // admin role
        │   ├── Company.java       // company role
        │   ├── Candidate.java     // candidate role
        │   └── Job.java           // job entity
        ├── service/
        │   └── JobService.java    // business logic and collections
        └── main/
            └── JobPortalApp.java  // entry point, menus
This matches the mandatory package structure com.jobportal.model, com.jobportal.service, and com.jobportal.main.​

Core Features
User Roles
Admin

View all jobs in the system.

View list of applicants per job ID.​

Company

Post a new job with title, company name, required experience.

Close an existing job.​

Candidate

View all OPEN jobs.

Apply for a job by job ID.​

Business Rules
Each job has a unique job ID.​

A candidate cannot apply for the same job more than once.​

Candidates cannot apply to CLOSED jobs.​

All in-memory data is stored using Java Collections only (no arrays).​

How to Run
Prerequisites
Java JDK 8+ installed (java -version, javac -version).​

Compile
From the project root:

bash
cd src
javac com/jobportal/model/*.java com/jobportal/service/*.java com/jobportal/main/JobPortalApp.java
Run
bash
java com.jobportal.main.JobPortalApp
You will see:

text
=== Job Portal System ===
1. Admin
2. Company
3. Candidate
Enter choice:
