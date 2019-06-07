import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.lang.*;
import java.util.Scanner;
import java.util.Map;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class InnReservations {

  // JDBC Driver name and DB url
  public final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  public final static String DB_URL = "jdbc:mysql://db.labthreesixfive.com/mkong02?autoReconnect=true&useSSL=false";
  public final static String USER = "mkong02";
  public final static String PASS = "S19_CSC-365-012538483";
  public final static String DB_NAME = "mkong02";

  public static void main(String[] args) {

        try {
            InnReservations ir = new InnReservations();

            // PRINT MAIN MENU HERE
            // SCANNER INPUT
            // SWITCH OR IF STATEMENT
            // CALL ir.sample();

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());

    }

    /*
    private void sample() throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded");
        } catch (ClassNotFoundException ex) {
            System.err.println("Unable to load JDBC Driver");
            System.exit(-1);
        };

      try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

          System.out.println("connected!");
          conn.close();
        } catch (SQLException ex) {
          System.out.println("Unable to load Driver");
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
        };
*/

    /*
    * PROBLEMS:
    * R1: Rooms and rates
    * R2: Reservations
    * R3: Reservation Change
    * R4: Reservation Cancellation
    * R5: Detailed Reservation Information
    * R6: Revenue
    * */

    // R3
    private void changeReservation(int code) throws SQLException{

      PreparedStatement findReservation = null;
      PreparedStatement checkDates = null;
      PreparedStatement updateReservation = null;

      String findString = "SELECT * FROM " + DB_NAME +
                          ".lab7_reservations WHERE CODE = ?";
      String checkString = "";
      String updateString = "";

      try {
          Class.forName("com.mysql.jdbc.Driver");
          System.out.println("MySQL JDBC Driver loaded");
      } catch (ClassNotFoundException ex) {
          System.err.println("Unable to load JDBC Driver");
          System.exit(-1);
      };

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

        System.out.println("connected!");

        // ask for user input for
        // FirstName, LastName, BeginDate, EndDate, Numberof Children, Number of Adults

        // 

        conn.close();
      } catch (SQLException ex) {
        System.out.println("Unable to load Driver");
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
      };

    }

    // R4
    private void cancelReservation(int code) throws SQLException{

      Boolean confirmation = false;
      PreparedStatement findReservation = null;
      PreparedStatement deleteReservation = null;

      String findString = "SELECT * FROM " + DB_NAME +
                            ".lab7_reservations WHERE CODE = ?";
      String deleteString = "DELETE * FROM " + DB_NAME +
                            ".lab7_reservations WHERE CODE = ?";

      try {
          Class.forName("com.mysql.jdbc.Driver");
          System.out.println("MySQL JDBC Driver loaded");
      } catch (ClassNotFoundException ex) {
          System.err.println("Unable to load JDBC Driver");
          System.exit(-1);
      };

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

        System.out.println("connected!");
        conn.setAutoCommit(false)

        findReservation = conn.prepareStatement(findString);
        findReservation.setInt(1, code);
        ResultSet foundReservation = findReservation.executeQuery();

        // check if Reservation exists
        if (foundReservation.next() == false){
          System.out.println("Reservation not found.");
          return;
        }

        // print result set
        // confirm they want to cancel it

        if (confirmation){
          deleteReservation = conn.prepareStatement(deleteString);
          deleteReservation.setInt(code)
          ResultSet deleted = deleteReservation.executeUpdate()

          if (deleted){
            System.out.println("Successfully cancelled Reservation " + code);
          } else {
            System.out.println("Failed to cancel Reservation " + code);
          }
        } else {
          System.out.println("ok");
        }

        conn.commit();
        conn.close();
      } catch (SQLException ex) {
        System.out.println("Unable to load Driver");
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
      };

    }

}
