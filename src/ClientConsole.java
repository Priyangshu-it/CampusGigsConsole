import java.sql.*;
import java.util.Scanner;

public class ClientConsole {

    // ---------- REGISTER ----------

    public static void register(Connection con, Scanner sc) throws SQLException {
        Statement stat = con.createStatement();

        System.out.println("Enter your full name: ");
        String name = sc.nextLine();

        System.out.println("Enter your organization full name: ");
        String org;
        int org_id;
        while (true) {
            org = sc.nextLine();
            String q1 = "select organization_id from organization where organization_title='" + org + "'";
            ResultSet rs = stat.executeQuery(q1);
            if (rs.next()) {
                org_id = rs.getInt("organization_id");
                break;
            } else {
                System.out.println("Organization not found or not verified.");
                System.out.println("Enter a verified organization if you work for any: ");
            }
        }

        System.out.println("Enter a password: ");
        String pass = sc.nextLine();
        System.out.println("Confirm password: ");
        while (!(sc.nextLine().equals(pass))) {
            System.out.println("Password didn't match.");
            System.out.println("Enter a password: ");
            pass = sc.nextLine();
            System.out.println("Confirm password: ");
        }

        String q = "insert into client (client_name,organization_id,client_password) values (?,?,?)";
        PreparedStatement pst = con.prepareStatement(q);
        pst.setString(1, name);
        pst.setInt(2, org_id);
        pst.setString(3, pass);
        pst.executeUpdate();
        System.out.println("Client registered successfully.");
        System.out.println("Welcome to CampusGigs Mr. " + name + ".");
    }

    // ---------- LOGIN + MENU ----------

    public static void login(Connection con, Scanner sc) throws SQLException {
        Statement stat = con.createStatement();

        System.out.println("Enter your full name: ");
        String name = sc.nextLine();
        System.out.println("Enter your password: ");
        String pass = sc.nextLine();

        String q = "select * from client where client_name='" + name + "' and client_password='" + pass + "'";
        ResultSet rs = stat.executeQuery(q);

        if (!(rs.next())) {
            System.out.println("Incorrect username or password.");
            return;
        }

        int id = rs.getInt("client_id");
        int org_id = rs.getInt("organization_id");
        q = "select organization_title from organization where organization_id=" + org_id;
        ResultSet rs2 = stat.executeQuery(q);
        rs2.next();
        String org = rs2.getString("organization_title");

        while (true) {
            System.out.println("Client Menu: ");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Change Password");
            System.out.println("4. Post a Gig");
            System.out.println("5. View My Gigs");
            System.out.println("6. Update a Gig");
            System.out.println("7. Delete a Gig");
            System.out.println("8. View Applications");
            System.out.println("9. Approve Application");
            System.out.println("10. Action Required for Expired Gigs");
            System.out.println("11. Mark Gig as Completed");
            System.out.println("12. Give Review");
            System.out.println("13. View Given Reviews");
            System.out.println("14. Logout");
            System.out.println("Enter your choice: ");
            int c = sc.nextInt();
            sc.nextLine();

            if (c == 1) {
                System.out.println("Client Profile:");
                System.out.println("Client Name: " + name);
                System.out.println("Organization: " + org);
            }

            if (c == 2) {
                System.out.println("Do you want to change your name? Enter Yes or No: ");
                String c2 = sc.nextLine();

                if (c2.equalsIgnoreCase("yes")) {
                    System.out.println("Enter your new name: ");
                    String name2 = sc.nextLine();
                    String q2 = "update client set client_name='" + name2 + "' where client_id=" + id;
                    stat.executeUpdate(q2);
                    name = name2;
                    System.out.println("Name changed successfully!!");
                    System.out.println("Welcome to CampusGigs " + name + ".");
                }

                System.out.println("Do you want to change your organization? Enter Yes or No: ");
                c2 = sc.nextLine();

                if (c2.equalsIgnoreCase("yes")) {
                    System.out.println("Enter your new organization: ");
                    String org2;
                    int org2_id;
                    while (true) {
                        org2 = sc.nextLine();
                        String q2 = "select organization_id from organization where organization_title='" + org2 + "' and verification_status='Verified'";
                        ResultSet rs1 = stat.executeQuery(q2);
                        if (rs1.next()) {
                            org2_id = rs1.getInt("organization_id");
                            break;
                        } else {
                            System.out.println("Organization not found or not verified.");
                            System.out.println("Enter a valid verified organization: ");
                        }
                    }
                    String q2 = "update client set organization_id=" + org2_id + " where client_id=" + id;
                    stat.executeUpdate(q2);
                    org_id = org2_id;
                    org = org2;
                    System.out.println("Organization changed successfully!!");
                }
            }

            if (c == 3) {
                System.out.println("Enter your new password: ");
                String pass2 = sc.nextLine();
                System.out.println("Confirm password: ");
                while (!(sc.nextLine().equals(pass2))) {
                    System.out.println("Password didn't match.");
                    System.out.println("Enter a password: ");
                    pass2 = sc.nextLine();
                    System.out.println("Confirm password: ");
                }
                String q2 = "update client set client_password=? where client_id=?";
                PreparedStatement pst = con.prepareStatement(q2);
                pst.setString(1, pass2);
                pst.setInt(2, id);
                pst.executeUpdate();
                System.out.println("Password changed successfully!!");
                pass = pass2;
            }

            if (c == 4) {
                System.out.println("Enter gig title: ");
                String title = sc.nextLine();
                System.out.println("Enter gig description: ");
                String description = sc.nextLine();
                System.out.println("Enter your budget (in ₹): ");
                double budget = sc.nextDouble();
                sc.nextLine();
                System.out.println("Enter the deadline (YYYY-MM-DD): ");
                String deadline = sc.nextLine();

                String q2 = "insert into gigs (client_id, gig_title, description, budget, deadline, status) values (?, ?, ?, ?, ?, 'Open')";
                PreparedStatement pst = con.prepareStatement(q2, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, id);
                pst.setString(2, title);
                pst.setString(3, description);
                pst.setDouble(4, budget);
                pst.setDate(5, Date.valueOf(deadline));
                pst.executeUpdate();

                System.out.println("Gig posted successfully!!");
                System.out.println("Your gig \"" + title + "\" is now open for applications.");

                int gig_id = -1;
                ResultSet keys = pst.getGeneratedKeys();
                if (keys.next()) {
                    gig_id = keys.getInt(1);
                }

                System.out.println("Available Skills: ");
                String q4 = "select * from skills order by skill_id";
                ResultSet r = stat.executeQuery(q4);
                while (r.next()) {
                    System.out.println(r.getInt("skill_id") + ". " + r.getString("skill_name"));
                }
                System.out.println("Enter the skill Ids required for this gig.");
                System.out.println("Enter 0 when you have finished adding skills.");
                while (true) {
                    System.out.println("Enter skill no.: ");
                    int skill = sc.nextInt();
                    sc.nextLine();
                    if (skill == 0) break;
                    q4 = "insert into gigskills (gig_id,skill_id) values (" + gig_id + "," + skill + ")";
                    stat.executeUpdate(q4);
                    System.out.println("Skill added to gig successfully.");
                }
            }

            if (c == 5) {
                System.out.println("My Gigs: ");
                String q2 = "select gig_id, gig_title, description, budget, deadline, status from gigs where client_id=?";
                PreparedStatement pst = con.prepareStatement(q2);
                pst.setInt(1, id);
                ResultSet rs1 = pst.executeQuery();
                if (!rs1.next()) {
                    System.out.println("You have not posted any gigs yet.");
                } else {
                    int i = 1;
                    do {
                        System.out.println(i + ". Gig ID: " + rs1.getInt("gig_id"));
                        System.out.println("Title: " + rs1.getString("gig_title"));
                        System.out.println("Description: " + rs1.getString("description"));
                        System.out.println("Budget: ₹" + rs1.getDouble("budget"));
                        System.out.println("Deadline: " + rs1.getDate("deadline"));
                        System.out.println("Status: " + rs1.getString("status"));
                        System.out.println("--------------------------------");
                        i++;
                    } while (rs1.next());
                }
            }

            if (c == 6) {
                System.out.println("Enter the Gig ID you want to update: ");
                int gig_id = sc.nextInt();
                sc.nextLine();

                String q2 = "select * from gigs where gig_id=? and client_id=?";
                PreparedStatement pst = con.prepareStatement(q2);
                pst.setInt(1, gig_id);
                pst.setInt(2, id);
                ResultSet rs1 = pst.executeQuery();

                if (!rs1.next()) {
                    System.out.println("Gig not found or this gig does not belong to you.");
                } else {
                    String currentStatus = rs1.getString("status");

                    if (!currentStatus.equalsIgnoreCase("Open")) {
                        System.out.println("This gig cannot be updated because its status is " + currentStatus + ".");
                    } else {
                        System.out.println("Current Gig Details:");
                        System.out.println("Title: " + rs1.getString("gig_title"));
                        System.out.println("Description: " + rs1.getString("description"));
                        System.out.println("Budget: ₹" + rs1.getDouble("budget"));
                        System.out.println("Deadline: " + rs1.getDate("deadline"));

                        System.out.println("\nDo you want to change the title? Enter Yes or No: ");
                        String choice = sc.nextLine();
                        if (choice.equalsIgnoreCase("yes")) {
                            System.out.println("Enter new title: ");
                            String newTitle = sc.nextLine();
                            String q3 = "update gigs set gig_title=? where gig_id=? and client_id=?";
                            PreparedStatement pst2 = con.prepareStatement(q3);
                            pst2.setString(1, newTitle);
                            pst2.setInt(2, gig_id);
                            pst2.setInt(3, id);
                            pst2.executeUpdate();
                            System.out.println("Gig title updated successfully!!");
                        }

                        System.out.println("Do you want to change the description? Enter Yes or No: ");
                        choice = sc.nextLine();
                        if (choice.equalsIgnoreCase("yes")) {
                            System.out.println("Enter new description: ");
                            String newDescription = sc.nextLine();
                            String q3 = "update gigs set description=? where gig_id=? and client_id=?";
                            PreparedStatement pst2 = con.prepareStatement(q3);
                            pst2.setString(1, newDescription);
                            pst2.setInt(2, gig_id);
                            pst2.setInt(3, id);
                            pst2.executeUpdate();
                            System.out.println("Gig description updated successfully!!");
                        }

                        System.out.println("Do you want to change the budget? Enter Yes or No: ");
                        choice = sc.nextLine();
                        if (choice.equalsIgnoreCase("yes")) {
                            System.out.println("Enter new budget: ");
                            double newBudget = sc.nextDouble();
                            sc.nextLine();
                            String q3 = "update gigs set budget=? where gig_id=? and client_id=?";
                            PreparedStatement pst2 = con.prepareStatement(q3);
                            pst2.setDouble(1, newBudget);
                            pst2.setInt(2, gig_id);
                            pst2.setInt(3, id);
                            pst2.executeUpdate();
                            System.out.println("Gig budget updated successfully!!");
                        }

                        System.out.println("Do you want to change the deadline? Enter Yes or No: ");
                        choice = sc.nextLine();
                        if (choice.equalsIgnoreCase("yes")) {
                            System.out.println("Enter new deadline (YYYY-MM-DD): ");
                            String newDeadline = sc.nextLine();
                            String q3 = "update gigs set deadline=? where gig_id=? and client_id=?";
                            PreparedStatement pst2 = con.prepareStatement(q3);
                            pst2.setDate(1, Date.valueOf(newDeadline));
                            pst2.setInt(2, gig_id);
                            pst2.setInt(3, id);
                            pst2.executeUpdate();
                            System.out.println("Gig deadline updated successfully!!");
                        }

                        System.out.println("Gig update process completed!!");
                    }
                }
            }

            if (c == 7) {
                System.out.println("Enter the Gig ID you want to delete: ");
                int gig_id = sc.nextInt();
                sc.nextLine();

                String q2 = "select status from gigs where gig_id=? and client_id=?";
                PreparedStatement pst = con.prepareStatement(q2);
                pst.setInt(1, gig_id);
                pst.setInt(2, id);
                ResultSet rs1 = pst.executeQuery();

                if (!rs1.next()) {
                    System.out.println("Gig not found or this gig does not belong to you.");
                } else {
                    String status = rs1.getString("status");
                    if (!status.equalsIgnoreCase("Open")) {
                        System.out.println("This gig cannot be deleted because its status is " + status + ".");
                    } else {
                        System.out.println("Are you sure you want to delete this gig? Enter Yes or No: ");
                        String choice = sc.nextLine();
                        if (choice.equalsIgnoreCase("yes")) {
                            String q3 = "delete from gigs where gig_id=? and client_id=?";
                            PreparedStatement pst2 = con.prepareStatement(q3);
                            pst2.setInt(1, gig_id);
                            pst2.setInt(2, id);
                            pst2.executeUpdate();
                            System.out.println("Gig deleted successfully!!");
                        } else {
                            System.out.println("Gig deletion cancelled.");
                        }
                    }
                }
            }

            if (c == 8) {
                System.out.println("Applications for My Gigs:");
                String q2 = "select application.application_id, gigs.gig_title, students.student_name, department.department_name, students.year_of_study, application.proposal, application.approval " +
                        "from application " +
                        "join gigs on application.gig_id = gigs.gig_id " +
                        "join students on application.student_id = students.student_id " +
                        "join department on students.department_id = department.department_id " +
                        "where gigs.client_id = " + id;
                ResultSet rs1 = stat.executeQuery(q2);

                if (!rs1.next()) {
                    System.out.println("No applications have been received for your gigs.");
                } else {
                    int i = 1;
                    do {
                        System.out.println("\nApplication " + i + ":");
                        System.out.println("Application ID: " + rs1.getInt("application_id"));
                        System.out.println("Gig Title: " + rs1.getString("gig_title"));
                        System.out.println("Student Name: " + rs1.getString("student_name"));
                        System.out.println("Department: " + rs1.getString("department_name"));
                        System.out.println("Year of Study: " + rs1.getInt("year_of_study"));
                        System.out.println("Proposal: " + rs1.getString("proposal"));
                        System.out.println("Application Status: " + rs1.getString("approval"));
                        i++;
                    } while (rs1.next());
                }
            }

            if (c == 9) {
                System.out.println("Enter the Application ID you want to approve: ");
                int app_id = sc.nextInt();
                sc.nextLine();

                String q2 = "select application.gig_id, application.student_id " +
                        "from application join gigs on application.gig_id = gigs.gig_id " +
                        "where application.application_id=" + app_id +
                        " and gigs.client_id=" + id +
                        " and application.approval='Waiting' and gigs.status='Open'";
                ResultSet rs1 = stat.executeQuery(q2);

                if (!rs1.next()) {
                    System.out.println("Application not found, already processed, or it does not belong to your gig.");
                } else {
                    int gig_id = rs1.getInt("gig_id");
                    int student_id = rs1.getInt("student_id");

                    String q3 = "update application set approval='Approved' where application_id=" + app_id;
                    stat.executeUpdate(q3);

                    String q4 = "update gigs set student_id=" + student_id + ", status='In Progress' where gig_id=" + gig_id;
                    stat.executeUpdate(q4);

                    System.out.println("Application approved successfully!!");
                    System.out.println("The student has been assigned to the gig.");

                    String q5 = "update application set approval='Rejected' where gig_id=" + gig_id +
                            " and application_id<>" + app_id + " and approval='Waiting'";
                    stat.executeUpdate(q5);
                }
            }

            if (c == 10) {
                System.out.println("Checking your expired gigs...");
                String q2 = "select gig_id, gig_title, deadline from gigs " +
                        "where client_id=" + id + " and deadline < curdate() and student_id is null";
                ResultSet rs1 = stat.executeQuery(q2);

                if (!rs1.next()) {
                    System.out.println("You have no expired gigs requiring action.");
                } else {
                    do {
                        int gig_id = rs1.getInt("gig_id");
                        String gig_title = rs1.getString("gig_title");
                        Date deadline = rs1.getDate("deadline");

                        System.out.println("\nGig Title: " + gig_title);
                        System.out.println("Deadline: " + deadline);
                        System.out.println("No student has been assigned to this gig.");

                        System.out.println("What would you like to do?");
                        System.out.println("1. Extend Deadline");
                        System.out.println("2. Delete Gig");
                        System.out.println("Enter your choice: ");
                        int choice = sc.nextInt();
                        sc.nextLine();

                        while (choice != 1 && choice != 2) {
                            System.out.println("Invalid choice.");
                            System.out.println("Enter 1 to extend deadline or 2 to delete gig: ");
                            choice = sc.nextInt();
                            sc.nextLine();
                        }

                        if (choice == 1) {
                            System.out.println("Enter the new deadline (YYYY-MM-DD): ");
                            String new_deadline = sc.nextLine();
                            String q3 = "update gigs set deadline='" + new_deadline + "' where gig_id=" + gig_id + " and client_id=" + id;
                            stat.executeUpdate(q3);
                            System.out.println("Deadline extended successfully!!");
                        }

                        if (choice == 2) {
                            System.out.println("Are you sure you want to delete this gig?");
                            System.out.println("Enter Yes or No: ");
                            String confirm = sc.nextLine();
                            if (confirm.equalsIgnoreCase("yes")) {
                                String q3 = "delete from gigs where gig_id=" + gig_id + " and client_id=" + id;
                                stat.executeUpdate(q3);
                                System.out.println("Gig deleted successfully!!");
                                System.out.println("All related applications and gig skills have also been deleted automatically.");
                            } else {
                                System.out.println("Gig was not deleted.");
                            }
                        }
                    } while (rs1.next());
                }
            }

            if (c == 11) {
                System.out.println("Enter the gig title you want to mark as completed: ");
                String gig = sc.nextLine();

                String q2 = "select gig_id, student_id, status from gigs where gig_title=? and client_id=?";
                PreparedStatement pst = con.prepareStatement(q2);
                pst.setString(1, gig);
                pst.setInt(2, id);
                ResultSet rs1 = pst.executeQuery();

                if (!rs1.next()) {
                    System.out.println("Gig not found.");
                } else {
                    int gig_id = rs1.getInt("gig_id");
                    boolean isUnassigned = rs1.getObject("student_id") == null;
                    String status = rs1.getString("status");

                    if (isUnassigned) {
                        System.out.println("This gig has not been assigned to any student yet.");
                    } else if (status.equals("Completed")) {
                        System.out.println("This gig is already marked as completed.");
                    } else {
                        String q3 = "update gigs set status='Completed' where gig_id=? and client_id=?";
                        PreparedStatement pst2 = con.prepareStatement(q3);
                        pst2.setInt(1, gig_id);
                        pst2.setInt(2, id);
                        int rows = pst2.executeUpdate();
                        if (rows > 0) {
                            System.out.println("Gig marked as completed successfully!!");
                        }
                    }
                }
            }

            if (c == 12) {
                System.out.println("Give Review");
                String q2 = "select gig_id, gig_title, student_id from gigs " +
                        "where client_id=" + id + " and status='Completed' and student_id is not null " +
                        "and gig_id not in (select gig_id from reviews)";
                ResultSet rs1 = stat.executeQuery(q2);

                if (!rs1.next()) {
                    System.out.println("There are no completed gigs available for review.");
                } else {
                    int i = 1;
                    do {
                        System.out.println(i + ". Gig ID: " + rs1.getInt("gig_id"));
                        System.out.println("   Gig Title: " + rs1.getString("gig_title"));
                        System.out.println("   Student ID: " + rs1.getInt("student_id"));
                        i++;
                    } while (rs1.next());

                    System.out.println("Enter the Gig ID for which you want to give a review: ");
                    int gig_id = sc.nextInt();
                    sc.nextLine();

                    String q3 = "select student_id from gigs where gig_id=" + gig_id +
                            " and client_id=" + id + " and status='Completed' and student_id is not null " +
                            " and gig_id not in (select gig_id from reviews)";
                    rs2 = stat.executeQuery(q3);

                    if (!rs2.next()) {
                        System.out.println("Invalid gig ID or review has already been given.");
                    } else {
                        int student_id = rs2.getInt("student_id");

                        System.out.println("Enter your rating (1-5): ");
                        String rating = sc.nextLine();
                        while (!(rating.equals("1") || rating.equals("2") || rating.equals("3") || rating.equals("4") || rating.equals("5"))) {
                            System.out.println("Invalid rating.");
                            System.out.println("Enter a rating between 1 and 5: ");
                            rating = sc.nextLine();
                        }

                        System.out.println("Enter your remarks: ");
                        String remarks = sc.nextLine();

                        String q4 = "insert into reviews (student_id, client_id, gig_id, rating, remarks) values (?, ?, ?, ?, ?)";
                        PreparedStatement pst = con.prepareStatement(q4);
                        pst.setInt(1, student_id);
                        pst.setInt(2, id);
                        pst.setInt(3, gig_id);
                        pst.setString(4, rating);
                        pst.setString(5, remarks);
                        pst.executeUpdate();

                        System.out.println("Review submitted successfully!!");
                    }
                }
            }

            if (c == 13) {
                System.out.println("Reviews Given by You: ");
                String q2 = "select reviews.review_id, gigs.gig_title, students.student_name, reviews.rating, reviews.remarks " +
                        "from reviews " +
                        "join gigs on reviews.gig_id = gigs.gig_id " +
                        "join students on reviews.student_id = students.student_id " +
                        "where reviews.client_id = " + id;
                ResultSet rs1 = stat.executeQuery(q2);

                if (!rs1.next()) {
                    System.out.println("You have not given any reviews yet.");
                } else {
                    int i = 1;
                    do {
                        System.out.println(i + ". Gig Title: " + rs1.getString("gig_title"));
                        i++;
                        System.out.println("Student Name: " + rs1.getString("student_name"));
                        System.out.println("Rating: " + rs1.getString("rating") + "/5");
                        System.out.println("Remarks: " + rs1.getString("remarks"));
                        System.out.println("----------------------------------");
                    } while (rs1.next());
                }
            }

            if (c == 14) {
                break;
            }
        }

        System.out.println("Logged out successfully.");
    }
}
