import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Connection con = DBConnection.getConnection();
            System.out.println("Connection established successfully!!!");

            Scanner sc = new Scanner(System.in);

            System.out.println("Are you a student or a client?");
            System.out.println("Enter 1 for student or 2 for client: ");
            int user = sc.nextInt();
            sc.nextLine();
            while (user != 1 && user != 2) {
                System.out.println("Please enter 1 for student or 2 for client.");
                user = sc.nextInt();
                sc.nextLine();
            }

            System.out.println("Are you already registered?");
            System.out.println("If yes enter 1 else enter 2: ");
            int reg = sc.nextInt();
            sc.nextLine();
            while (reg != 1 && reg != 2) {
                System.out.println("Please enter 1 if already registered else enter 2.");
                reg = sc.nextInt();
                sc.nextLine();
            }

            if (reg == 2) {
                if (user == 1) {
                    StudentConsole.register(con, sc);
                } else {
                    ClientConsole.register(con, sc);
                }
            }

            if (user == 1) {
                StudentConsole.login(con, sc);
            } else {
                ClientConsole.login(con, sc);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
