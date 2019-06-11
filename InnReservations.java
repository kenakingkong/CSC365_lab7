import java.io.*;
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
import java.text.SimpleDateFormat;
import java.text.ParseException;
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

            ir.changeReservation();
            //ir.cancelReservation();

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
      PreparedStatement checkDate = null;
      PreparedStatement checkOccupancy = null;
      PreparedStatement updateReservation = null;

      String findString = "SELECT * FROM " + DB_NAME + ".lab7_reservations WHERE CODE = ?";
      String checkDateString = "SELECT ? FROM "+ DB_NAME + ".lab7_reservations " +
                                "WHERE (Room=?) AND (? BETWEEN CheckIn AND Checkout);";
      String checkOccupancyString = "SELECT maxOcc FROM " + DB_NAME + ".lab7_rooms " +
                                    "WHERE RoomCode = ?";
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
        findReservation = conn.prepareStatement(findString,
                                                ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                ResultSet.CONCUR_UPDATABLE);
        findReservation.setString(1, code);
        ResultSet foundReservation = findReservation.executeQuery();

        // check if Reservation exists
        if (foundReservation.next() == false){
          System.out.println("Reservation " + code + " not found.");
          return;
        }

        // print reservation
        //print_reservation(foundReservation);

        // get input
        System.out.println("Enter changes or press ENTER to skip.");
        System.out.println("Enter FirstName: ");
        String firstName = sc.nextLine().toUpperCase();
        System.out.println("Enter LastName: ");
        String lastName = sc.nextLine().toUpperCase();
        java.sql.Date beginDate = null;
        System.out.println("Enter Begin Date yyyy-MM-dd: ");
        String beginDateInput = sc.nextLine();
        if (beginDateInput.equals("") == false){
          try{
            Date tempDate = new SimpleDateFormat("yyyy-MM-dd").parse(beginDateInput);
            beginDate = new java.sql.Date(tempDate.getTime());
          } catch (ParseException e) {
            System.out.println("Invalid Date. Try again with format  yyyy-MM-dd.");
            return;
          }
        }
        java.sql.Date endDate = null;
        System.out.println("Enter EndDate yyyy-MM-dd: ");
        String endDateInput = sc.nextLine();
        if (endDateInput.equals("") == false){
          try {
            Date tempDate =  new SimpleDateFormat("yyyy-MM-dd").parse(endDateInput);
            endDate = new java.sql.Date(tempDate.getTime());
          } catch (ParseException e) {
            System.out.println("Invalid Date. Try again with format  yyyy-MM-dd.");
            return;
          }
        }
        System.out.println("Enter Number of Children: ");
        String numChildren = sc.nextLine();
        System.out.println("Enter Number of Adults: ");
        String numAdults = sc.nextLine();

        //room name first
        String roomCode = foundReservation.getString("Room");

        // check for date conflicts
        if ((beginDate != null) && (endDate != null) && (beginDate.equals(endDate) == true)){
          System.out.println("You cannot cannot CheckIn and Checkout on the same day.");
          return;
        }

        // check checkin date
        if (beginDate != null){
          //java.sql.Date beginDateSQL = new java.sql.Date(beginDate.getTime());
          checkDate = conn.prepareStatement(checkDateString);
          checkDate.setObject(1,"CheckIn");
          checkDate.setString(2,roomCode);
          checkDate.setObject(3,beginDate);
          ResultSet beginResults = checkDate.executeQuery();
          if (beginResults.next()){
            System.out.println("Check In Date is unavailable.");
            return;
          }
          if (beginDate.equals(foundReservation.getDate("Checkout"))){
            System.out.println("You cannot checkin and checkout on the same day.");
            return;
          }
        }

        //check checkout date
        if (endDate != null){
          //java.sql.Date endDateSQL = new java.sql.Date(endDate.getTime());
          checkDate = null;
          checkDate = conn.prepareStatement(checkDateString);
          checkDate.setObject(1,"Checkout");
          checkDate.setString(2,roomCode);
          checkDate.setObject(3,endDate);
          ResultSet endResults = checkDate.executeQuery();
          if (endResults.next()){
            System.out.println("Check out Date is unavailable.");
            return;
          }
          if (endDate.equals(foundReservation.getDate("CheckIn"))){
            System.out.println("You cannot checkin and checkout on the same day.");
            return;
          }
        }

        // check for max occupancy
        if ((numChildren.equals("") == false) || (numAdults.equals("") == false)) {
          int occInput = 0;
          if (numChildren.equals("") == false)
            occInput += Integer.parseInt(numChildren);
          if (numAdults.equals("") == false)
            occInput += Integer.parseInt(numAdults);
          checkOccupancy = conn.prepareStatement(checkOccupancyString);
          checkOccupancy.setString(1,roomCode);
          ResultSet occ = checkOccupancy.executeQuery();
          int occLimit = 1;
          if (occ.next()){
            occLimit = occ.getInt("maxOcc");
          }
          if (occInput > occLimit) {
            System.out.printf("Cannot exceed room occupany limit of %d.\n",occLimit);
          }
        }

        // confirm they want to cancel it
        Scanner scan = new Scanner(System.in);
        System.out.println("Are you sure you want to update this reservation? Y/N");
        char confirmation = scan.nextLine().charAt(0);

        if (confirmation == 'Y' || confirmation == 'y'){
          if (firstName.equals("") == false)
            foundReservation.updateString("FirstName", firstName);
          if (lastName.equals("") == false)
            foundReservation.updateString("LastName", lastName);
          if (beginDate != null)
            foundReservation.updateDate("CheckIn", beginDate);
          if (endDate != null)
            foundReservation.updateDate("Checkout", endDate);
          if (numChildren.equals("") == false)
            foundReservation.updateInt("Kids", Integer.parseInt(numChildren));
          if (numAdults.equals("") == false)
            foundReservation.updateInt("Adults", Integer.parseInt(numAdults));
          foundReservation.updateRow();
          System.out.println("Successfully updated Reservation " + code);
        } else {
          System.out.println("ok");
        }

        conn.commit();
        conn.close();
      } catch (SQLException ex) {
        System.out.println("Failed to update Reservation.");
      };

    }

    // R4
    public static void cancelReservation()
      throws SQLException{

      PreparedStatement findReservation = null;
      String findString = "SELECT * FROM " + DB_NAME + ".lab7_reservations WHERE CODE = ?";

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
        findReservation = conn.prepareStatement(findString,
                                                ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                ResultSet.CONCUR_UPDATABLE);
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
          //delete entry
          foundReservation.deleteRow();
          System.out.println("Successfully cancelled Reservation " + code + ".");
        } else {
          System.out.println("ok");
        }

        conn.commit();
        conn.close();
      } catch (SQLException ex) {
        System.out.println("Failed to cancel Reservation.");
      };

    }

}
