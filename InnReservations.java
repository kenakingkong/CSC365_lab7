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
  public final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  public final static String DB_URL = "jdbc:mysql://db.labthreesixfive.com/spring2019";
  public final static String USER = "mkong02";
  public final static String PASS = "S19_CSC-365-012538483";

  public static void main(String[] args) {
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

        // load driver
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Succesfuully loaded Driver");
        } catch (SQLException ex) {
            System.out.println("Unable to load Driver")
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        // est connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
                // CODE?



        finally (){
            conn.close();
        }
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


    /*
    private void resInfo(){
      Scanner in = new Scanner();
    }*/

}
