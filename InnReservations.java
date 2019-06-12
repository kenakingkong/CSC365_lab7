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

// RUN THIS ON YOUR TERMINAL!!!!!!!!!!
//export CLASSPATH=$CLASSPATH:mysql-connector-java-8.0.16.jar:.

public class InnReservations {

  // JDBC Driver name and DB url
  public final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  public final static String DB_URL = "jdbc:mysql://db.labthreesixfive.com/mkong02?autoReconnect=true&useSSL=false";
  public final static String USER = "mkong02";
  public final static String PASS = "S19_CSC-365-012538483";

  public static void main(String[] args) {
      try {
            Class.forName(JDBC_DRIVER);
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



      /*
        try {
            InnReservations ir = new InnReservations();

            // testing connection
            ir.sample();

            //switch statement??????????

            // read the

            // to go betweeen r1, r2. ......


        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }*/

    }

    /*
    private void sample() throws SQLException {

        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Successfully loaded Driver");
        } catch (SQLException ex) {
            System.out.println("Unable to load Driver")
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        // est connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){

      try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

          System.out.println("connected!");
          conn.close();
        } catch (SQLException ex) {
          System.out.println("Unable to load Driver");
          System.out.println("SQLException: " + ex.getMessage());
          System.out.println("SQLState: " + ex.getSQLState());
          System.out.println("VendorError: " + ex.getErrorCode());
        };
    }
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


    private static void revenue(){
      Scanner in = new Scanner(System.in);

    }


    private static void resInfo(){
      String constraints = "";
      String begin = "SELECT res.CODE,\n\tres.Room,\n\trooms.RoomName,\n\tres.CheckIn,\n\tres.CheckOut,\n\tres.Rate,\n\tres.LastName,\n\tres.FirstName,\n\tres.Adults,\n\tres.Kids\nFROM INN.reservations as res\nJOIN INN.rooms AS rooms ON\n\trooms.RoomCode = res.Room\n";
      String end = "\nORDER BY res.CODE;";
      String input;
      Scanner in = new Scanner(System.in);
      System.out.println("Leave entry blank to indicate 'Any'");
      System.out.println("Enter first name:");
      input = in.nextLine();
      constraints = constraints + inputToSQL(input, "res.FirstName");
      System.out.println("Enter last name:");
      input = in.nextLine();
      constraints = constraints + inputToSQL(input, "res.LastName");
      System.out.println("Enter range of dates [format: yyyy-mm-dd to yyyy-mm-dd]:");
      input = in.nextLine();
      constraints = constraints + inputToSQL(input, "res.CheckIn");
      System.out.println("Enter room code:");
      input = in.nextLine();
      constraints = constraints + inputToSQL(input, "res.Room");
      System.out.println("Enter reservation code:");
      input = in.nextLine();
      constraints = constraints + inputToSQL(input, "res.CODE");
      if(constraints.length() > 0){
        constraints = "WHERE\n\t" + constraints.substring(0, constraints.length() - 6);
      }
      String query = begin + constraints + end;
      System.out.println(query);
    }

    private static String inputToSQL(String input, String column){
      input = input.trim();
      System.out.println(input);
      if(input.length() == 0) return "";
      String output;
      switch(column){
        case "res.FirstName":
          output = column + " LIKE '" + input + "'";
          break;
        case "res.LastName":
          output = column + " LIKE '" + input + "'";
          break;
        case "res.CheckIn":
          output = dateFormat(input);
          break;
        case "res.Room":
          output = column + "LIKE '" + input + "'";
          break;
        default:
          output = column + "LIKE " + input;
      }
      output = output + " AND\n\t";
      return output;
    }

    private static String dateFormat(String input){
      String[] arr = input.split(" to ");
      String checkDate = "BETWEEN DATE '%s' AND DATE '%s'";
      checkDate = String.format(checkDate, arr[0], arr[1]);
      return "res.CheckIn " + checkDate + " AND res.CheckOut " + checkDate;
    }

}
