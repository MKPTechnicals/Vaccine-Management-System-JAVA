import java.sql.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Connection con;
        Statement stmt;
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Provide your actual database credentials and database name here
            con = DriverManager.getConnection(
                    "jdbc:mysql://db4free.net/YOUR_DATABASE_NAME",
                    "YOUR_USERNAME",
                    "YOUR_PASSWORD");
            stmt = con.createStatement();

            while (true) {
                System.out.println("_________________________________");
                System.out.println("-------Vaccination Centre--------");
                System.out.println("_________________________________");
                System.out.println("1.Registration for vaccination");
                System.out.println("2.Entry of vaccinated person");
                System.out.println("3.Search");
                System.out.println("0.Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                if (choice == 0) {
                    break;
                } else if (choice == 1) {
                    registration(scanner, stmt);
                } else if (choice == 2) {
                    System.out.print("Enter dose number:");
                    int choice1 = scanner.nextInt();
                    vaccinated(scanner, stmt, choice1);
                } else if (choice == 3) {
                    System.out.print("Enter Aadhaar Number:");
                    int aadhar = scanner.nextInt();
                    search(stmt, aadhar);
                } else {
                    System.out.println("Invalid Choice");
                }
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void registration(Scanner scanner, Statement stmt) throws SQLException {
        try {
            System.out.print("Enter Name:");
            String name = scanner.next();
            System.out.print("Enter Aadhaar No.:");
            long aadhar = scanner.nextLong();

            // Check if the user is already vaccinated for the 1st dose
            String alreadyVaccinatedQuery = "SELECT * FROM DOSE_1_COVAXIN WHERE AADHAAR=" + aadhar +
                    " UNION SELECT * FROM DOSE_1_COVISHIELD WHERE AADHAAR=" + aadhar;

            ResultSet vaccinatedResultSet = stmt.executeQuery(alreadyVaccinatedQuery);

            if (vaccinatedResultSet.next()) {
                System.out.println(
                        "You are not eligible for 1st dose registration. You are already vaccinated for the 1st dose.");
                vaccinatedResultSet.close();
                return;
            }

            System.out.print("Enter your city:");
            String city = scanner.next();
            System.out.print("Enter Contact No.:");
            long contactno = scanner.nextLong();
            System.out.print("Enter dose of vaccination(1 or 2):");
            int dose = scanner.nextInt();
            System.out.print("Enter name of vaccine(Covaxin or Covishield):");
            String vaccine = scanner.next();

            String sql = "";
            if (dose == 1) {
                if (vaccine.equalsIgnoreCase("Covaxin")) {
                    sql = "INSERT INTO REGISTRATION_COVAXIN (NAME,AADHAAR,CITY,CONTACT_NUMBER,DATE,DOSE) " +
                            "VALUES ('" + name + "'," + aadhar + ",'" + city + "'," + contactno + ",now(),1)";
                } else if (vaccine.equalsIgnoreCase("Covishield")) {
                    sql = "INSERT INTO REGISTRATION_COVISHIELD (NAME,AADHAAR,CITY,CONTACT_NUMBER,DATE,DOSE) " +
                            "VALUES ('" + name + "'," + aadhar + ",'" + city + "'," + contactno + ",now(),1)";
                } else {
                    System.out.println("Invalid vaccine name");
                    return;
                }
            } else if (dose == 2) {
                // Check if the user is eligible for 2nd dose
                String eligibilityQuery = "SELECT DOSE FROM DOSE_1_COVAXIN WHERE AADHAAR=" + aadhar +
                        " UNION SELECT DOSE FROM DOSE_1_COVISHIELD WHERE AADHAAR=" + aadhar;

                ResultSet eligibilityResultSet = stmt.executeQuery(eligibilityQuery);

                boolean eligibleFor2ndDose = false;

                while (eligibilityResultSet.next()) {
                    int userDose = eligibilityResultSet.getInt("DOSE");
                    if (userDose == 1) {
                        eligibleFor2ndDose = true;
                        break;
                    }
                }

                eligibilityResultSet.close();

                if (!eligibleFor2ndDose) {
                    System.out
                            .println("You are not eligible for 2nd dose registration. Please get your 1st dose first.");
                    return;
                }

                // Check if the user is already vaccinated for the 2nd dose
                String alreadyVaccinatedQuery2 = "SELECT * FROM DOSE_2_COVAXIN WHERE AADHAAR=" + aadhar +
                        " UNION SELECT * FROM DOSE_2_COVISHIELD WHERE AADHAAR=" + aadhar;

                ResultSet vaccinatedResultSet2 = stmt.executeQuery(alreadyVaccinatedQuery2);

                if (vaccinatedResultSet2.next()) {
                    System.out.println(
                            "You are not eligible for 2nd dose registration. You are already vaccinated for the 2nd dose.");
                    vaccinatedResultSet2.close();
                    return;
                }

                if (vaccine.equalsIgnoreCase("Covaxin")) {
                    sql = "INSERT INTO REGISTRATION_COVAXIN (NAME,AADHAAR,CITY,CONTACT_NUMBER,DATE,DOSE) " +
                            "VALUES ('" + name + "'," + aadhar + ",'" + city + "'," + contactno + ",now(),2)";
                } else if (vaccine.equalsIgnoreCase("Covishield")) {
                    sql = "INSERT INTO REGISTRATION_COVISHIELD (NAME,AADHAAR,CITY,CONTACT_NUMBER,DATE,DOSE) " +
                            "VALUES ('" + name + "'," + aadhar + ",'" + city + "'," + contactno + ",now(),2)";
                } else {
                    System.out.println("Invalid vaccine name");
                    return;
                }
            } else {
                System.out.println("Enter valid dose for vaccination");
                return;
            }

            stmt.executeUpdate(sql);
            System.out.println("Registered for " + (dose == 1 ? "1st" : "2nd") + " dose vaccination");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void vaccinated(Scanner scanner, Statement stmt, int n) throws SQLException {
        try {
            System.out.print("Enter PIN:");
            int pin = scanner.nextInt();
            if (pin != 1265) {
                System.out.println("Wrong PIN");
                return;
            }
            System.out.print("Enter Name:");
            String name = scanner.next();
            System.out.print("Enter Aadhaar No.:");
            long aadhar = scanner.nextLong();
            System.out.print("Enter your city:");
            String city = scanner.next();
            System.out.print("Enter Contact No.:");
            long contactno = scanner.nextLong();
            System.out.print("Enter name of vaccine(Covaxin or Covishield):");
            String vaccine = scanner.next();

            String tableName = "";
            if (n == 1 && vaccine.equals("Covaxin")) {
                tableName = "DOSE_1_COVAXIN";
            } else if (n == 1 && vaccine.equals("Covishield")) {
                tableName = "DOSE_1_COVISHIELD";
            } else if (n == 2 && vaccine.equals("Covaxin")) {
                tableName = "DOSE_2_COVAXIN";
            } else if (n == 2 && vaccine.equals("Covishield")) {
                tableName = "DOSE_2_COVISHIELD";
            } else {
                System.out.println("Invalid dose or vaccine");
                return;
            }

            String sql = "INSERT INTO " + tableName + " (NAME,AADHAAR,CITY,CONTACT_NUMBER,DATE) " +
                    "VALUES ('" + name + "'," + aadhar + ",'" + city + "'," + contactno + ",now())";

            stmt.executeUpdate(sql);
            System.out.println(name + " is vaccinated for " + (n == 1 ? "1st" : "2nd") + " dose");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void search(Statement stmt, int aadhar) throws SQLException {
        try {
            String sql1 = "SELECT *, 'Covaxin' AS VACCINE FROM DOSE_1_COVAXIN WHERE AADHAAR=" + aadhar +
                    " UNION SELECT *, 'Covishield' AS VACCINE FROM DOSE_1_COVISHIELD WHERE AADHAAR=" + aadhar;

            ResultSet resultSet1 = stmt.executeQuery(sql1);

            boolean found = false;

            while (resultSet1.next()) {
                found = true;
                System.out.println("--------------------------------------------------");
                System.out.println("Name: " + resultSet1.getString("NAME"));
                System.out.println("Aadhar No.: " + resultSet1.getLong("AADHAAR"));
                System.out.println("City: " + resultSet1.getString("CITY"));
                System.out.println("Contact No.: " + resultSet1.getLong("CONTACT_NUMBER"));
                System.out.println("Dose: 1");
                System.out.println("Vaccine: " + resultSet1.getString("VACCINE"));
                System.out.println("--------------------------------------------------");
            }
            resultSet1.close();

            String sql2 = "SELECT *, 'Covaxin' AS VACCINE FROM DOSE_2_COVAXIN WHERE AADHAAR=" + aadhar +
                    " UNION SELECT *, 'Covishield' AS VACCINE FROM DOSE_2_COVISHIELD WHERE AADHAAR=" + aadhar;
            ResultSet resultSet2 = stmt.executeQuery(sql2);

            while (resultSet2.next()) {
                found = true;
                System.out.println("--------------------------------------------------");
                System.out.println("Name: " + resultSet2.getString("NAME"));
                System.out.println("Aadhar No.: " + resultSet2.getLong("AADHAAR"));
                System.out.println("City: " + resultSet2.getString("CITY"));
                System.out.println("Contact No.: " + resultSet2.getLong("CONTACT_NUMBER"));
                System.out.println("Dose: 2");
                System.out.println("Vaccine: " + resultSet2.getString("VACCINE"));
                System.out.println("--------------------------------------------------");
            }

            resultSet2.close();

            if (!found) {
                System.out.println("--------------------------------------------------");
                System.out.println("No data found for Aadhaar: " + aadhar);
                System.out.println("--------------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("--------------------------------------------------");
            System.out.println("Error: " + e.getMessage());
            System.out.println("--------------------------------------------------");
        }
    }
}
