package jobportal;

import java.util.Scanner;

/**
 * Main class: handles high-level menus and uses PortalCore.JobService.
 */
public class JobPortalApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PortalCore.JobService service = new PortalCore.JobService();

        System.out.println("=== Job Portal System ===");

        boolean running = true;
        while (running) {
            System.out.println("\nSelect Role:");
            System.out.println("1. Admin");
            System.out.println("2. Company");
            System.out.println("3. Candidate");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int roleChoice = sc.nextInt();

            switch (roleChoice) {
                case 1:
                    handleAdmin(sc, service);
                    break;
                case 2:
                    handleCompany(sc, service);
                    break;
                case 3:
                    handleCandidate(sc, service);
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting Job Portal. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }

        sc.close();
    }

    private static void handleAdmin(Scanner sc, PortalCore.JobService service) {
        PortalCore.Admin admin = new PortalCore.Admin(1, "AdminUser");

        boolean adminSession = true;
        while (adminSession) {
            admin.showMenu();
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    service.viewAllJobs();
                    break;
                case 2:
                    service.viewApplicantsForJob(sc);
                    break;
                case 0:
                    adminSession = false;
                    System.out.println("Admin logged out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void handleCompany(Scanner sc, PortalCore.JobService service) {
        System.out.print("Enter Company ID (int): ");
        int id = sc.nextInt();
        System.out.print("Enter Company Name: ");
        sc.nextLine(); // consume newline
        String name = sc.nextLine();

        PortalCore.Company company = new PortalCore.Company(id, name);

        boolean companySession = true;
        while (companySession) {
            company.showMenu();
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    service.addJob(company, sc);
                    break;
                case 2:
                    service.closeJob(company, sc);
                    break;
                case 3:
                    service.viewCompanyJobs(company);
                    break;
                case 0:
                    companySession = false;
                    System.out.println("Company logged out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void handleCandidate(Scanner sc, PortalCore.JobService service) {
        System.out.print("Enter Candidate ID (int): ");
        int id = sc.nextInt();
        System.out.print("Enter Candidate Name: ");
        sc.nextLine(); // consume newline
        String name = sc.nextLine();

        PortalCore.Candidate candidate = new PortalCore.Candidate(id, name);

        boolean candidateSession = true;
        while (candidateSession) {
            candidate.showMenu();
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    service.viewOpenJobs();
                    break;
                case 2:
                    service.applyForJob(candidate, sc);
                    break;
                case 3:
                    service.viewMyApplications(candidate);
                    break;
                case 0:
                    candidateSession = false;
                    System.out.println("Candidate logged out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
