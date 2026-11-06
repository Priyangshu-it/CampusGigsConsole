import java.sql.*;
import java.util.Scanner;

public class StudentConsole {

    // ---------- REGISTER ----------

    public static void register(Connection con, Scanner sc) throws SQLException {
        Statement stat = con.createStatement();

        System.out.println("Enter your full name: ");
        String name = sc.nextLine();

        System.out.println("Enter your department full name: ");
        String dep;
        int dep_id;
        while (true) {
            dep = sc.nextLine();
            String q1 = "select department_id from department where department_name='" + dep + "'";
            ResultSet rs = stat.executeQuery(q1);
            if (rs.next()) {
                dep_id = rs.getInt("department_id");
                break;
            } else {
                System.out.println("Department not found.");
                System.out.println("Enter a valid department: ");
            }
        }

        System.out.println("Which year of study are you currently in: ");
        int year;
        while (true) {
            year = sc.nextInt();
            sc.nextLine();
            if (year > 4 || year < 1) {
                System.out.println("Enter a valid year of study: ");
            } else break;
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

        String q = "insert into students (student_name,department_id,year_of_study,student_password) values ('"
                + name + "'," + dep_id + "," + year + ",'" + pass + "')";
        stat.executeUpdate(q);
        System.out.println("Student registered successfully!!");
        System.out.println("Welcome to CampusGigs " + name + "!!");
    }

    // ---------- LOGIN + MENU ----------

    public static void login(Connection con, Scanner sc) throws SQLException {
        Statement stat = con.createStatement();

        System.out.println("Enter your full name: ");
        String name = sc.nextLine();
        System.out.println("Enter your password: ");
        String pass = sc.nextLine();

        String q = "select * from students where student_name='" + name + "' and student_password='" + pass + "'";
        ResultSet rs = stat.executeQuery(q);

        if (!(rs.next())) {
            System.out.println("Incorrect username or password.");
            return;
        }

        int id = rs.getInt("student_id");
        int dep_id = rs.getInt("department_id");
        int year = rs.getInt("year_of_study");

        String q1 = "select department_name from department where department_id='" + dep_id + "'";
        ResultSet rs2 = stat.executeQuery(q1);
        rs2.next();
        String dep = rs2.getString("department_name");

        while (true) {
            System.out.println("Student Menu: ");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Change Password");
            System.out.println("4. Add Skills");
            System.out.println("5. View Skills");
            System.out.println("6. View Available Gigs");
            System.out.println("7. Search Gigs by Skills");
            System.out.println("8. Filter Gigs by Budget");
            System.out.println("9. Apply for a Gig");
            System.out.println("10. View My Applications");
            System.out.println("11. View My Assigned Gigs");
            System.out.println("12. Withdraw Application");
            System.out.println("13. View Completed Gigs");
            System.out.println("14. View My Reviews");
            System.out.println("15. Logout");
            System.out.println("Enter your choice: ");
            int c = sc.nextInt();
            sc.nextLine();

            if (c == 1) {
                System.out.println("My Profile: ");
                System.out.println("Student name : " + name);
                System.out.println("Department : " + dep);
                System.out.println("Year of study : " + year);
            }

            if (c == 2) {
                System.out.println("Do you want to change your name? Enter Yes or No: ");
                String c2 = sc.nextLine();
                if (c2.equalsIgnoreCase("yes")) {
                    System.out.println("Enter your new name: ");
                    String name2 = sc.nextLine();
                    String q2 = "update students set student_name='" + name2 + "' where student_id=" + id;
                    stat.executeUpdate(q2);
                    name = name2;
                    System.out.println("Name changed successfully!!");
                    System.out.println("Welcome to CampusGigs " + name);
                }
                System.out.println("Do you want to change your department? Enter Yes or No: ");
                c2 = sc.nextLine();
                if (c2.equalsIgnoreCase("yes")) {
                    System.out.println("Enter your new department: ");
                    String dep2;
                    String q2;
                    int dep2_id;
                    while (true) {
                        dep2 = sc.nextLine();
                        q2 = "select department_id from department where department_name='" + dep2 + "'";
                        ResultSet rs1 = stat.executeQuery(q2);
                        if (rs1.next()) {
                            dep2_id = rs1.getInt("department_id");
                            break;
                        } else {
                            System.out.println("Department not found.");
                            System.out.println("Enter a valid department: ");
                        }
                    }
                    q2 = "update students set department_id=" + dep2_id + " where student_id=" + id;
                    stat.executeUpdate(q2);
                    System.out.println("Department changed successfully!!");
                    dep = dep2;
                    dep_id = dep2_id;
                }
                System.out.println("Do you want to change your year of study? Enter Yes or No: ");
                c2 = sc.nextLine();
                if (c2.equalsIgnoreCase("yes")) {
                    System.out.println("Enter your new year of study: ");
                    int year2 = sc.nextInt();
                    sc.nextLine();
                    while (year2 < 1 || year2 > 4) {
                        System.out.println("Invalid input!!");
                        System.out.println("Enter a valid year of study: ");
                        year2 = sc.nextInt();
                        sc.nextLine();
                    }
                    String q2 = "update students set year_of_study=" + year2 + " where student_id=" + id;
                    stat.executeUpdate(q2);
                    System.out.println("Year of study changed successfully!!");
                    year = year2;
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
                String q2 = "update students set student_password='" + pass2 + "' where student_id=" + id;
                stat.executeUpdate(q2);
                System.out.println("Password changed successfully!!");
                pass = pass2;
            }

            if (c == 4) {
                System.out.println("Available Skills: ");
                String q2 = "select * from skills order by skill_id";
                ResultSet rs1 = stat.executeQuery(q2);
                while (rs1.next()) {
                    System.out.println(rs1.getInt("skill_id") + ". " + rs1.getString("skill_name"));
                }
                System.out.println("Enter your choice: ");
                int skill = sc.nextInt();
                sc.nextLine();
                q2 = "Insert into studentskills (student_id,skill_id) values (" + id + "," + skill + ")";
                stat.executeUpdate(q2);
                System.out.println("Skill added successfully!!");
            }

            if (c == 5) {
                System.out.println("Your skills: ");
                String q2 = "select skills.skill_id,skills.skill_name from studentskills join skills on studentskills.skill_id=skills.skill_id where studentskills.student_id=" + id;
                ResultSet rs1 = stat.executeQuery(q2);
                if (!rs1.next()) {
                    System.out.println("You have not added any skills yet.");
                } else {
                    int i = 1;
                    do {
                        System.out.println(i + ". " + rs1.getString("skill_name"));
                        i++;
                    } while (rs1.next());
                }
            }

            if (c == 6) {
                System.out.println("Available gigs");
                String q2 = "select gig_title, description, budget, deadline from gigs where status='Open'";
                ResultSet rs1 = stat.executeQuery(q2);
                if (!rs1.next()) {
                    System.out.println("There are no open gigs available at the moment.");
                } else {
                    int i = 1;
                    do {
                        System.out.println(i + ". Title: " + rs1.getString("gig_title"));
                        i++;
                        System.out.println("Description: " + rs1.getString("description"));
                        System.out.println("Budget: " + rs1.getDouble("budget"));
                        System.out.println("Deadline: " + rs1.getDate("deadline"));
                    } while (rs1.next());
                }
            }

            if (c == 7) {
                System.out.println("Enter the skill you want to search for: ");
                String skill = sc.nextLine();
                String q2 = "select gigs.gig_title, gigs.description, gigs.budget, gigs.deadline from gigs join gigskills on gigs.gig_id=gigskills.gig_id join skills on gigskills.skill_id=skills.skill_id where skills.skill_name='" + skill + "' and gigs.status='Open'";
                ResultSet rs1 = stat.executeQuery(q2);
                if (!rs1.next()) {
                    System.out.println("No open gigs found requiring this skill.");
                } else {
                    int i = 1;
                    do {
                        System.out.println(i + ". Title: " + rs1.getString("gig_title"));
                        i++;
                        System.out.println("Description: " + rs1.getString("description"));
                        System.out.println("Budget: " + rs1.getDouble("budget"));
                        System.out.println("Deadline: " + rs1.getDate("deadline"));
                    } while (rs1.next());
                }
            }

            if (c == 8) {
                System.out.println("Enter minimum budget: (in ₹) ");
                double min = sc.nextDouble();
                sc.nextLine();
                System.out.println("Enter maximum budget: (in ₹) ");
                double max = sc.nextDouble();
                sc.nextLine();
                String q2 = "select gig_title,description,budget,deadline from gigs where budget between " + min + " and " + max + " and status='Open' order by budget";
                ResultSet rs1 = stat.executeQuery(q2);
                if (!rs1.next()) {
                    System.out.println("No open gigs found within this budget range.");
                } else {
                    int i = 1;
                    do {
                        System.out.println(i + ". Title: " + rs1.getString("gig_title"));
                        i++;
                        System.out.println("Description: " + rs1.getString("description"));
                        System.out.println("Budget: " + rs1.getDouble("budget"));
                        System.out.println("Deadline: " + rs1.getDate("deadline"));
                    } while (rs1.next());
                }
            }

            if (c == 9) {
                System.out.println("Enter the gig title you want to apply for: ");
                String gig = sc.nextLine();
                String q2 = "select gig_id from gigs where gig_title='" + gig + "' and status='Open'";
                ResultSet rs1 = stat.executeQuery(q2);
                if (!rs1.next()) {
                    System.out.println("Gig not found or the gig is no longer open.");
                } else {
                    int gig_id = rs1.getInt("gig_id");
                    System.out.println("Enter your proposal: ");
                    String p = sc.nextLine();
                    q2 = "insert into application (student_id,gig_id,proposal) values (" + id + "," + gig_id + ",'" + p + "')";
                    stat.executeUpdate(q2);
                    System.out.println("Application submitted successfully!!");
                }
            }

            if (c == 10) {
                System.out.println("My Applications: ");
                String q2 = "select gigs.gig_title,gigs.budget,gigs.deadline,application.proposal,application.approval from application join gigs on application.gig_id=gigs.gig_id where application.student_id=" + id;
                ResultSet rs1 = stat.executeQuery(q2);
                if (!rs1.next()) {
                    System.out.println("You have not applied for any gigs yet.");
                } else {
                    int i = 1;
                    do {
                        System.out.println(i + ". Title: " + rs1.getString("gig_title"));
                        i++;
                        System.out.println("Budget: " + rs1.getDouble("budget"));
                        System.out.println("Deadline: " + rs1.getDate("deadline"));
                        System.out.println("Your proposal: " + rs1.getString("proposal"));
                        System.out.println("Application status: " + rs1.getString("approval"));
                    } while (rs1.next());
                }
            }

            if (c == 11) {
                System.out.println("My Assigned Gigs: ");
                String q2 = "select gig_id,gig_title,description,budget,status from gigs where student_id=" + id;
                ResultSet rs1 = stat.executeQuery(q2);
                if (!rs1.next()) {
                    System.out.println("You have no gigs assigned to you.");
                } else {
                    int i = 1;
                    do {
                        System.out.println(i + ". Title: " + rs1.getString("gig_title"));
                        i++;
                        System.out.println("Description: " + rs1.getString("description"));
                        System.out.println("Budget: " + rs1.getDouble("budget"));
                        System.out.println("Status: " + rs1.getString("status"));
                    } while (rs1.next());
                }
            }

            if (c == 12) {
                System.out.println("Enter the gig title whose application you want to withdraw: ");
                String gig = sc.nextLine();
                String q2 = "select application.application_id from application join gigs on application.gig_id=gigs.gig_id where application.student_id=" + id + " and gigs.gig_title='" + gig + "' and application.approval!='Approved'";
                ResultSet rs1 = stat.executeQuery(q2);
                if (!rs1.next()) {
                    System.out.println("No waiting or rejected application found for this gig.");
                } else {
                    int app_id = rs1.getInt("application_id");
                    q2 = "delete from application where application_id=" + app_id;
                    stat.executeUpdate(q2);
                    System.out.println("Application withdrawn successfully!!");
                }
            }

            if (c == 13) {
                System.out.println("My completed gigs: ");
                String q2 = "select gig_title,description,budget from gigs where student_id=" + id + " and status='Completed'";
                ResultSet rs1 = stat.executeQuery(q2);
                if (!rs1.next()) {
                    System.out.println("You have not completed any gigs yet.");
                } else {
                    int i = 1;
                    do {
                        System.out.println(i + ". Title: " + rs1.getString("gig_title"));
                        i++;
                        System.out.println("Description: " + rs1.getString("description"));
                        System.out.println("Budget: " + rs1.getDouble("budget"));
                    } while (rs1.next());
                }
            }

            if (c == 14) {
                System.out.println("My Reviews: ");
                String q2 = "select gigs.gig_title, client.client_name, reviews.rating, reviews.remarks from reviews join gigs on reviews.gig_id=gigs.gig_id join client on reviews.client_id=client.client_id where reviews.student_id=" + id;
                ResultSet rs1 = stat.executeQuery(q2);
                if (!rs1.next()) {
                    System.out.println("You have not received any reviews yet.");
                } else {
                    int i = 1;
                    do {
                        System.out.println(i + ". Gig Title: " + rs1.getString("gig_title"));
                        i++;
                        System.out.println("Client Name: " + rs1.getString("client_name"));
                        System.out.println("Rating: " + rs1.getString("rating") + "/5");
                        System.out.println("Remarks: " + rs1.getString("remarks"));
                    } while (rs1.next());
                }
            }

            if (c == 15) {
                break;
            }
        }

        System.out.println("Logged out successfully.");
    }
}
