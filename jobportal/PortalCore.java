package jobportal;

import java.util.*;

/**
 * Single core file: contains model classes + service using Collections only.
 */
public class PortalCore {

    // ========= MODEL LAYER =========

    public static abstract class User {
        protected int id;
        protected String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public abstract void showMenu();
    }

    public static class Admin extends User {

        public Admin(int id, String name) {
            super(id, name);
        }

        @Override
        public void showMenu() {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. View all jobs");
            System.out.println("2. View applicants for a job");
            System.out.println("0. Logout");
        }
    }

    public static class Company extends User {

        public Company(int id, String name) {
            super(id, name);
        }

        @Override
        public void showMenu() {
            System.out.println("\n=== Company Menu ===");
            System.out.println("1. Post job");
            System.out.println("2. Close job");
            System.out.println("3. View own jobs");
            System.out.println("0. Logout");
        }
    }

    public static class Candidate extends User {

        public Candidate(int id, String name) {
            super(id, name);
        }

        @Override
        public void showMenu() {
            System.out.println("\n=== Candidate Menu ===");
            System.out.println("1. View all OPEN jobs");
            System.out.println("2. Apply for a job");
            System.out.println("3. View my applications");
            System.out.println("0. Logout");
        }
    }

    public static class Job {
        private int jobId;
        private String title;
        private String companyName;
        private int requiredExperience;
        private String status; // OPEN or CLOSED

        public Job(int jobId, String title, String companyName, int requiredExperience) {
            this.jobId = jobId;
            this.title = title;
            this.companyName = companyName;
            this.requiredExperience = requiredExperience;
            this.status = "OPEN";
        }

        public int getJobId() {
            return jobId;
        }

        public String getTitle() {
            return title;
        }

        public String getCompanyName() {
            return companyName;
        }

        public int getRequiredExperience() {
            return requiredExperience;
        }

        public String getStatus() {
            return status;
        }

        public void closeJob() {
            this.status = "CLOSED";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Job)) return false;
            Job job = (Job) o;
            return jobId == job.jobId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(jobId);
        }

        @Override
        public String toString() {
            return "JobId: " + jobId +
                    ", Title: " + title +
                    ", Company: " + companyName +
                    ", Exp: " + requiredExperience +
                    ", Status: " + status;
        }
    }

    // ========= SERVICE LAYER =========

    public static class JobService {

        // jobId -> Job
        private Map<Integer, Job> jobs = new HashMap<>();

        // jobId -> Set of candidateIds
        private Map<Integer, Set<Integer>> jobApplications = new HashMap<>();

        // candidateId -> Set of jobIds
        private Map<Integer, Set<Integer>> candidateApplications = new HashMap<>();

        private int jobIdCounter = 100;

        // ---- Company operations ----

        public void addJob(Company company, Scanner sc) {
            System.out.print("Enter Job Title: ");
            sc.nextLine(); // consume leftover newline
            String title = sc.nextLine();

            System.out.print("Enter Required Experience (years): ");
            int exp = sc.nextInt();

            int newJobId = ++jobIdCounter;
            Job job = new Job(newJobId, title, company.name, exp);

            // unique ID rule: map key ensures uniqueness
            jobs.put(job.getJobId(), job);

            System.out.println("Job posted with ID: " + job.getJobId());
        }

        public void closeJob(Company company, Scanner sc) {
            System.out.print("Enter Job ID to close: ");
            int jobId = sc.nextInt();

            Job job = jobs.get(jobId);
            if (job == null) {
                System.out.println("Job not found.");
                return;
            }

            if (!job.getCompanyName().equals(company.name)) {
                System.out.println("You can close only your own jobs.");
                return;
            }

            if ("CLOSED".equals(job.getStatus())) {
                System.out.println("Job is already CLOSED.");
                return;
            }

            job.closeJob();
            System.out.println("Job closed successfully.");
        }

        public void viewCompanyJobs(Company company) {
            boolean found = false;
            for (Job job : jobs.values()) {
                if (job.getCompanyName().equals(company.name)) {
                    System.out.println(job);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No jobs posted by you yet.");
            }
        }

        // ---- Candidate operations ----

        public void viewOpenJobs() {
            boolean found = false;
            for (Job job : jobs.values()) {
                if ("OPEN".equals(job.getStatus())) {
                    System.out.println(job);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No OPEN jobs available.");
            }
        }

        public void applyForJob(Candidate candidate, Scanner sc) {
            System.out.print("Enter Job ID to apply: ");
            int jobId = sc.nextInt();

            Job job = jobs.get(jobId);
            if (job == null) {
                System.out.println("Job not found.");
                return;
            }

            if (!"OPEN".equals(job.getStatus())) {
                System.out.println("Cannot apply. Job is CLOSED.");
                return;
            }

            // candidate must not apply for same job twice
            Set<Integer> alreadyAppliedJobs =
                    candidateApplications.getOrDefault(candidate.id, new HashSet<>());
            if (alreadyAppliedJobs.contains(jobId)) {
                System.out.println("You have already applied for this job.");
                return;
            }

            // add to jobApplications
            jobApplications
                    .computeIfAbsent(jobId, k -> new HashSet<>())
                    .add(candidate.id);

            // add to candidateApplications
            alreadyAppliedJobs.add(jobId);
            candidateApplications.put(candidate.id, alreadyAppliedJobs);

            System.out.println("Application submitted successfully.");
        }

        public void viewMyApplications(Candidate candidate) {
            Set<Integer> appliedJobs =
                    candidateApplications.getOrDefault(candidate.id, new HashSet<>());

            if (appliedJobs.isEmpty()) {
                System.out.println("You have not applied for any jobs yet.");
                return;
            }

            System.out.println("Your applied jobs:");
            for (Integer jobId : appliedJobs) {
                Job job = jobs.get(jobId);
                if (job != null) {
                    System.out.println(job);
                }
            }
        }

        // ---- Admin operations ----

        public void viewAllJobs() {
            if (jobs.isEmpty()) {
                System.out.println("No jobs in the portal.");
                return;
            }
            for (Job job : jobs.values()) {
                System.out.println(job);
            }
        }

        public void viewApplicantsForJob(Scanner sc) {
            System.out.print("Enter Job ID to view applicants: ");
            int jobId = sc.nextInt();

            Job job = jobs.get(jobId);
            if (job == null) {
                System.out.println("Job not found.");
                return;
            }

            Set<Integer> applicants = jobApplications.get(jobId);
            if (applicants == null || applicants.isEmpty()) {
                System.out.println("No applicants for this job yet.");
                return;
            }

            System.out.println("Applicants for Job ID " + jobId + ": " + applicants);
        }
    }
}
