ipmort java.sql.*;

public class InnReservations {

    // JDBC Driver name and DB url
    static final string JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql:db.labthreesixfive.com/mkong02";

    // DB credentials
    static final String USER = "mkong02";
    static final String PASS = "S19_CSC-365-012538483";


    public static void main(String[] args) {

        try {
            InnReservations ir = new InnReservations();

            // testing connection
            ir.sample();

            //switch statement??????????
            // to go betweeen r1, r2. ......

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }

    }

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

        } catch (){

        } finally (){
            conn.close();
        }
    }


    /*
    * PROBLEMS:
    * R1: Rooms and rates
    * R2: Reservations
    * R3: Reservation Change
    * R4: Reservation Cancellation
    * R5: Detailed Reservation Information
    * R6: Revenue
    * */

}