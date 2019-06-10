import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import java.sql.SQLException;
import java.lang.*;
import java.util.Scanner;
import java.util.Map;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.util.Date;
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

            //ir.changeReservation();
            ir.cancelReservation();

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
  }

    public static void print_reservation(ResultSet r) throws SQLException{
      int code = r.getInt(1);
      String room = r.getString(2);
      Date begin = r.getDate(3);
      Date end = r.getDate(4);
      int rate = r.getInt(5);
      String last = r.getString(6);
      String first = r.getString(7);
      int adults = r.getInt(8);
      int children = r.getInt(9);

      String half_result = "Reservation %d\nRoom: %s,  Dates: %s to %s, Rate: %3.2f, Name: %s %s, Adults: %d, Children: %d";
      String result = String.format(half_result, code, room, begin, end, rate, first, last, adults, children);
      System.out.println(result);
    }

    // R3
    public static void changeReservation()
      throws SQLException {

      PreparedStatement findReservation = null;
      PreparedStatement checkBeginDate = null;
      PreparedStatement checkEndDate = null;
      PreparedStatement checkOccupancy = null;
      PreparedStatement updateReservation = null;

      String findString = "SELECT * FROM " + DB_NAME + ".lab7_reservations WHERE CODE = ?";
      String checkBeginString = "SELECT CheckIn FROM " + DB_NAME + ".lab7_reservations" +
                                "WHERE Room = ? AND NOT CODE = ?";
      String checkEndString = "SELECT Checkout FROM " + DB_NAME + ".lab7_reservations" +
                              "WHERE Room = ? AND NOT CODE = ?";
      String checkOccupancyString = "SELECT maxOcc FROM " + DB_NAME + "./lab7_rooms" +
                                    "WHERE Room = ? AND NOT CODE = ?";
      String updateString = "UPDATE " + DB_NAME + ".lab7_reservations" +
                            "SET CheckIn=?, Checkout=?, LastName=?, FirstName=?, Adults=?,Kids=?" +
                            "WHERE CODE = ?";

      try {
          Class.forName(JDBC_DRIVER);
          System.out.println("MySQL JDBC Driver loaded");
      } catch (ClassNotFoundException ex) {
          System.err.println("Unable to load JDBC Driver");
          System.exit(-1);
      };

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

        //System.out.println("connected!");
        System.out.println("Change a Reservation");
        conn.setAutoCommit(false);
        Scanner sc = new Scanner(System.in);

        // get reservation code
        System.out.println("Enter Reservation Code: ");
        String code = sc.nextLine();

        // get reservation
        findReservation = conn.prepareStatement(findString);
        findReservation.setString(1, code);
        ResultSet foundReservation = findReservation.executeQuery();

        // check if Reservation exists
        if (foundReservation.next() == false){
          System.out.println("Reservation " + code + " not found.");
          return;
        }

        /*
        // get changes
        System.out.println("Enter changes or press ENTER to skip.\n");
        System.out.println("Enter FirstName: ");
        String firstName = sc.nextLine();
        System.out.println("Enter LastName: ");
        String lastName = sc.nextLine();
        System.out.println("Enter Begin Date: ");
        Date beginDate = new Date(sc.nextLine());
        System.out.println("Enter EndDate: ");
        Date endDate = new Date(sc.nextLine());
        System.out.println("Enter Number of Children: ");
        int numChildren = sc.nextInt();
        System.out.println("Enter Number of Adults: ");
        int numAdults = sc.nextInt();

        // check for date conflicts
        //checkBeginDate = conn.prepareStatement(checkBeginString);
        //checkBeginDate.setString(0,);


        // check for max occupancy?


        // print the updates
        System.out.println("Are you sure you want to make these changes? Y/N");
        char confirmation = sc.nextChar();

        if (confirmation == 'Y' || confimation == 'y'){
          // put update query here
          //updateReservation = conn.prepareStatement(updateString);
          //updateReservation.setString();

          //ResultSet updatedReservation =
          //updated = updatedReservation.rowUpdated( )
          print("should update");

          if (updated){
            System.out.println("Successfully updated Reservation " + code);
          } else {
            System.out.println("Failed to update Reservation " + code);
          }
        } else {
          System.out.println("ok");
        }
        */

        conn.close();
      } catch (SQLException ex) {
        System.out.println("Unable to load Driver");
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
      };


    }

    // R4
    public static void cancelReservation()
      throws SQLException{

      PreparedStatement findReservation = null;
      PreparedStatement deleteReservation = null;

      String findString = "SELECT * FROM " + DB_NAME + ".lab7_reservations WHERE CODE = ?";
      String deleteString = "DELETE FROM " + DB_NAME + ".lab7_reservations WHERE CODE = ?";

      try {
          Class.forName(JDBC_DRIVER);
          System.out.println("MySQL JDBC Driver loaded");
      } catch (ClassNotFoundException ex) {
          System.err.println("Unable to load JDBC Driver");
          System.exit(-1);
      };

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

        //System.out.println("connected!");
        System.out.println("Cancel a Reservation");
        conn.setAutoCommit(false);
        Scanner sc = new Scanner(System.in);

        // get reservation code
        System.out.println("Enter Reservation Code: ");
        String code = sc.nextLine();

        // get reservation
        findReservation = conn.prepareStatement(findString);
        findReservation.setString(1, code);
        ResultSet foundReservation = findReservation.executeQuery();

        // check if Reservation exists
        if (foundReservation.next() == false){
          System.out.println("Reservation " + code + " not found.");
          return;
        }

        // print reservation
        //print_reservation(foundReservation);

        // confirm they want to cancel it
        System.out.println("Are you sure you want to cancel this reservation? Y/N");
        char confirmation = sc.nextLine().charAt(0);

        if (confirmation == 'Y' || confirmation == 'y'){
          deleteReservation = conn.prepareStatement(deleteString);
          deleteReservation.setString(1, code);
          deleteReservation.executeUpdate();
          System.out.println("Successfully cancelled Reservation " + code + ".");
        } else {
          System.out.println("ok");
        }

        conn.commit();
        conn.close();
      } catch (SQLException ex) {
        System.out.println("Failed to cancel Reservation.");
        /*
        System.out.println("Unable to load Driver");
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
        */
      };

    }

}
