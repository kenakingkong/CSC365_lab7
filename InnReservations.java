import java.io.*;
import java.sql.*;
import java.lang.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

// RUN THIS ON YOUR TERMINAL!!!!!!!!!!
//export CLASSPATH=$CLASSPATH:mysql-connector-java-8.0.16.jar:.

// RUN THIS ON YOUR TERMINAL!!!!!!!!!!
//export CLASSPATH=$CLASSPATH:mysql-connector-java-8.0.16.jar:.

public class InnReservations {

  // JDBC Driver name and DB url
  public final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  public final static String DB_URL = "jdbc:mysql://db.labthreesixfive.com/mkong02?autoReconnect=true&useSSL=false";
  public final static String USER = "mkong02";
  public final static String PASS = "S19_CSC-365-012538483";
  public final static String DB_NAME = "mkong02";

  /**
   * Default maximum number of rows to query and print.
   */
  private static final int DEFAULT_MAX_ROWS = 10;

  /**
   * Default maximum width for text columns
   * (like a <code>VARCHAR</code>) column.
   */
  private static final int DEFAULT_MAX_TEXT_COL_WIDTH = 150;

  /**
   * Column type category for <code>CHAR</code>, <code>VARCHAR</code>
   * and similar text columns.
   */
  public static final int CATEGORY_STRING = 1;

  /**
   * Column type category for <code>TINYINT</code>, <code>SMALLINT</code>,
   * <code>INT</code> and <code>BIGINT</code> columns.
   */
  public static final int CATEGORY_INTEGER = 2;

  /**
   * Column type category for <code>REAL</code>, <code>DOUBLE</code>,
   * and <code>DECIMAL</code> columns.
   */
  public static final int CATEGORY_DOUBLE = 3;

  /**
   * Column type category for date and time related columns like
   * <code>DATE</code>, <code>TIME</code>, <code>TIMESTAMP</code> etc.
   */
  public static final int CATEGORY_DATETIME = 4;

  /**
   * Column type category for <code>BOOLEAN</code> columns.
   */
  public static final int CATEGORY_BOOLEAN = 5;

  /**
   * Column type category for types for which the type name
   * will be printed instead of the content, like <code>BLOB</code>,
   * <code>BINARY</code>, <code>ARRAY</code> etc.
   */
  public static final int CATEGORY_OTHER = 0;

  /**
   * Represents a database table column.
   */
  private static class Column {

      /**
       * Column label.
       */
      private String label;

      /**
       * Generic SQL type of the column as defined in
       * <a target="_blank"
       * href="http://docs.oracle.com/javase/8/docs/api/java/sql/Types.html">
       * java.sql.Types
       * </a>.
       */
      private int type;

      /**
       * Generic SQL type name of the column as defined in
       * <a target="_blank"
       * href="http://docs.oracle.com/javase/8/docs/api/java/sql/Types.html">
       * java.sql.Types
       * </a>.
       */
      private String typeName;

      /**
       * Width of the column that will be adjusted according to column label
       * and values to be printed.
       */
      private int width = 0;

      /**
       * Column values from each row of a <code>ResultSet</code>.
       */
      private List<String> values = new ArrayList<>();

      /**
       * Flag for text justification using <code>String.format</code>.
       * Empty string (<code>""</code>) to justify right,
       * dash (<code>-</code>) to justify left.
       *
       * @see #justifyLeft()
       */
      private String justifyFlag = "";

      /**
       * Column type category. The columns will be categorised according
       * to their column types and specific needs to print them correctly.
       */
      private int typeCategory = 0;

      /**
       * Constructs a new <code>Column</code> with a column label,
       * generic SQL type and type name (as defined in
       * <a target="_blank"
       * href="http://docs.oracle.com/javase/8/docs/api/java/sql/Types.html">
       * java.sql.Types
       * </a>)
       *
       * @param label Column label or name
       * @param type Generic SQL type
       * @param typeName Generic SQL type name
       */
      public Column (String label, int type, String typeName) {
          this.label = label;
          this.type = type;
          this.typeName = typeName;
      }

      /**
       * Returns the column label
       *
       * @return Column label
       */
      public String getLabel() {
          return label;
      }

      /**
       * Returns the generic SQL type of the column
       *
       * @return Generic SQL type
       */
      public int getType() {
          return type;
      }

      /**
       * Returns the generic SQL type name of the column
       *
       * @return Generic SQL type name
       */
      public String getTypeName() {
          return typeName;
      }

      /**
       * Returns the width of the column
       *
       * @return Column width
       */
      public int getWidth() {
          return width;
      }

      /**
       * Sets the width of the column to <code>width</code>
       *
       * @param width Width of the column
       */
      public void setWidth(int width) {
          this.width = width;
      }

      /**
       * Adds a <code>String</code> representation (<code>value</code>)
       * of a value to this column object's {@link #values} list.
       * These values will come from each row of a
       * <a target="_blank"
       * href="http://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html">
       * ResultSet
       * </a> of a database query.
       *
       * @param value The column value to add to {@link #values}
       */
      public void addValue(String value) {
          values.add(value);
      }

      /**
       * Returns the column value at row index <code>i</code>.
       * Note that the index starts at 0 so that <code>getValue(0)</code>
       * will get the value for this column from the first row
       * of a <a target="_blank"
       * href="http://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html">
       * ResultSet</a>.
       *
       * @param i The index of the column value to get
       * @return The String representation of the value
       */
      public String getValue(int i) {
          return values.get(i);
      }

      /**
       * Returns the value of the {@link #justifyFlag}. The column
       * values will be printed using <code>String.format</code> and
       * this flag will be used to right or left justify the text.
       *
       * @return The {@link #justifyFlag} of this column
       * @see #justifyLeft()
       */
      public String getJustifyFlag() {
          return justifyFlag;
      }

      /**
       * Sets {@link #justifyFlag} to <code>"-"</code> so that
       * the column value will be left justified when printed with
       * <code>String.format</code>. Typically numbers will be right
       * justified and text will be left justified.
       */
      public void justifyLeft() {
          this.justifyFlag = "-";
      }

      /**
       * Returns the generic SQL type category of the column
       *
       * @return The {@link #typeCategory} of the column
       */
      public int getTypeCategory() {
          return typeCategory;
      }

      /**
       * Sets the {@link #typeCategory} of the column
       *
       * @param typeCategory The type category
       */
      public void setTypeCategory(int typeCategory) {
          this.typeCategory = typeCategory;
      }
  }

  /**
    * Database Table Printer
    * Copyright (C) 2014  Hami Galip Torun
    * Email: hamitorun@e-fabrika.net
    * Project Home: https://github.com/htorun/dbtableprinter
  **/

  /**
   * Overloaded method to print rows of a <a target="_blank"
   * href="http://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html">
   * ResultSet</a> to standard out using {@link #DEFAULT_MAX_TEXT_COL_WIDTH}
   * to limit the width of text columns.
   *
   * @param rs The <code>ResultSet</code> to print
   */
  public static void printResultSet(ResultSet rs) {
      printResultSet(rs, DEFAULT_MAX_TEXT_COL_WIDTH);
  }

  /**
   * Overloaded method to print rows of a <a target="_blank"
   * href="http://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html">
   * ResultSet</a> to standard out using <code>maxStringColWidth</code>
   * to limit the width of text columns.
   *
   * @param rs The <code>ResultSet</code> to print
   * @param maxStringColWidth Max. width of text columns
   */
  public static void printResultSet(ResultSet rs, int maxStringColWidth) {
      try {
          if (rs == null) {
              System.err.println("DBTablePrinter Error: Result set is null!");
              return;
          }
          if (rs.isClosed()) {
              System.err.println("DBTablePrinter Error: Result Set is closed!");
              return;
          }
          if (maxStringColWidth < 1) {
              System.err.println("DBTablePrinter Info: Invalid max. varchar column width. Using default!");
              maxStringColWidth = DEFAULT_MAX_TEXT_COL_WIDTH;
          }

          // Get the meta data object of this ResultSet.
          ResultSetMetaData rsmd;
          rsmd = rs.getMetaData();

          // Total number of columns in this ResultSet
          int columnCount = rsmd.getColumnCount();

          // List of Column objects to store each columns of the ResultSet
          // and the String representation of their values.
          List<Column> columns = new ArrayList<>(columnCount);

          // List of table names. Can be more than one if it is a joined
          // table query
          List<String> tableNames = new ArrayList<>(columnCount);

          // Get the columns and their meta data.
          // NOTE: columnIndex for rsmd.getXXX methods STARTS AT 1 NOT 0
          for (int i = 1; i <= columnCount; i++) {
              Column c = new Column(rsmd.getColumnLabel(i),
                      rsmd.getColumnType(i), rsmd.getColumnTypeName(i));
              c.setWidth(c.getLabel().length());
              c.setTypeCategory(whichCategory(c.getType()));
              columns.add(c);

              if (!tableNames.contains(rsmd.getTableName(i))) {
                  tableNames.add(rsmd.getTableName(i));
              }
          }

          // Go through each row, get values of each column and adjust
          // column widths.
          int rowCount = 0;
          while (rs.next()) {

              // NOTE: columnIndex for rs.getXXX methods STARTS AT 1 NOT 0
              for (int i = 0; i < columnCount; i++) {
                  Column c = columns.get(i);
                  String value;
                  int category = c.getTypeCategory();

                  if (category == CATEGORY_OTHER) {

                      // Use generic SQL type name instead of the actual value
                      // for column types BLOB, BINARY etc.
                      value = "(" + c.getTypeName() + ")";

                  } else {
                      value = rs.getString(i+1) == null ? "NULL" : rs.getString(i+1);
                  }
                  switch (category) {
                      case CATEGORY_DOUBLE:

                          // For real numbers, format the string value to have 3 digits
                          // after the point. THIS IS TOTALLY ARBITRARY and can be
                          // improved to be CONFIGURABLE.
                          if (!value.equals("NULL")) {
                              Double dValue = rs.getDouble(i+1);
                              value = String.format("%.3f", dValue);
                          }
                          break;

                      case CATEGORY_STRING:

                          // Left justify the text columns
                          c.justifyLeft();

                          // and apply the width limit
                          if (value.length() > maxStringColWidth) {
                              value = value.substring(0, maxStringColWidth - 3) + "...";
                          }
                          break;
                  }

                  // Adjust the column width
                  c.setWidth(value.length() > c.getWidth() ? value.length() : c.getWidth());
                  c.addValue(value);
              } // END of for loop columnCount
              rowCount++;

          } // END of while (rs.next)

          /*
          At this point we have gone through meta data, get the
          columns and created all Column objects, iterated over the
          ResultSet rows, populated the column values and adjusted
          the column widths.
          We cannot start printing just yet because we have to prepare
          a row separator String.
           */

          // For the fun of it, I will use StringBuilder
          StringBuilder strToPrint = new StringBuilder();
          StringBuilder rowSeparator = new StringBuilder();

          /*
          Prepare column labels to print as well as the row separator.
          It should look something like this:
          +--------+------------+------------+-----------+  (row separator)
          | EMP_NO | BIRTH_DATE | FIRST_NAME | LAST_NAME |  (labels row)
          +--------+------------+------------+-----------+  (row separator)
           */

          // Iterate over columns
          for (Column c : columns) {
              int width = c.getWidth();

            // Center the column label
              String toPrint;
              String name = c.getLabel();
              int diff = width - name.length();

              if ((diff%2) == 1) {
                  // diff is not divisible by 2, add 1 to width (and diff)
                  // so that we can have equal padding to the left and right
                  // of the column label.
                  width++;
                  diff++;
                  c.setWidth(width);
              }

              int paddingSize = diff/2; // InteliJ says casting to int is redundant.

              // Cool String repeater code thanks to user102008 at stackoverflow.com
              // (http://tinyurl.com/7x9qtyg) "Simple way to repeat a string in java"
              String padding = new String(new char[paddingSize]).replace("\0", " ");

              toPrint = "| " + padding + name + padding + " ";
            // END centering the column label

              strToPrint.append(toPrint);

              rowSeparator.append("+");
              rowSeparator.append(new String(new char[width + 2]).replace("\0", "-"));
          }

          String lineSeparator = System.getProperty("line.separator");

          // Is this really necessary ??
          lineSeparator = lineSeparator == null ? "\n" : lineSeparator;

          rowSeparator.append("+").append(lineSeparator);

          strToPrint.append("|").append(lineSeparator);
          strToPrint.insert(0, rowSeparator);
          strToPrint.append(rowSeparator);

          StringJoiner sj = new StringJoiner(", ");
          for (String name : tableNames) {
              sj.add(name);
          }

          String info = "Printing " + rowCount;
          info += rowCount > 1 ? " rows from " : " row from ";
          info += tableNames.size() > 1 ? "tables " : "table ";
          info += sj.toString();

          //System.out.println(info);

          // Print out the formatted column labels
          System.out.print(strToPrint.toString());

          String format;

          // Print out the rows
          for (int i = 0; i < rowCount; i++) {
              for (Column c : columns) {

                  // This should form a format string like: "%-60s"
                  format = String.format("| %%%s%ds ", c.getJustifyFlag(), c.getWidth());
                  System.out.print(
                          String.format(format, c.getValue(i))
                  );
              }

              System.out.println("|");
              System.out.print(rowSeparator);
          }

          System.out.println();

          /*
              Hopefully this should have printed something like this:
              +--------+------------+------------+-----------+--------+-------------+
              | EMP_NO | BIRTH_DATE | FIRST_NAME | LAST_NAME | GENDER |  HIRE_DATE  |
              +--------+------------+------------+-----------+--------+-------------+
              |  10001 | 1953-09-02 | Georgi     | Facello   | M      |  1986-06-26 |
              +--------+------------+------------+-----------+--------+-------------+
              |  10002 | 1964-06-02 | Bezalel    | Simmel    | F      |  1985-11-21 |
              +--------+------------+------------+-----------+--------+-------------+
           */

      } catch (SQLException e) {
          System.err.println("SQL exception in DBTablePrinter. Message:");
          System.err.println(e.getMessage());
      }
  }

  /**
   * Takes a generic SQL type and returns the category this type
   * belongs to. Types are categorized according to print formatting
   * needs:
   * <p>
   * Integers should not be truncated so column widths should
   * be adjusted without a column width limit. Text columns should be
   * left justified and can be truncated to a max. column width etc...</p>
   *
   * See also: <a target="_blank"
   * href="http://docs.oracle.com/javase/8/docs/api/java/sql/Types.html">
   * java.sql.Types</a>
   *
   * @param type Generic SQL type
   * @return The category this type belongs to
   */
  private static int whichCategory(int type) {
      switch (type) {
          case Types.BIGINT:
          case Types.TINYINT:
          case Types.SMALLINT:
          case Types.INTEGER:
              return CATEGORY_INTEGER;

          case Types.REAL:
          case Types.DOUBLE:
          case Types.DECIMAL:
              return CATEGORY_DOUBLE;

          case Types.DATE:
          case Types.TIME:
          case Types.TIME_WITH_TIMEZONE:
          case Types.TIMESTAMP:
          case Types.TIMESTAMP_WITH_TIMEZONE:
              return CATEGORY_DATETIME;

          case Types.BOOLEAN:
              return CATEGORY_BOOLEAN;

          case Types.VARCHAR:
          case Types.NVARCHAR:
          case Types.LONGVARCHAR:
          case Types.LONGNVARCHAR:
          case Types.CHAR:
          case Types.NCHAR:
              return CATEGORY_STRING;

          default:
              return CATEGORY_OTHER;
      }
  }

  /**
    * START OF LAB 7 CODE !!!!!!!!!!!!!!!!!!!!!!!
    * !!!!!!!!!!!!!!!!!!!!!!!!!!!!
  **/

  public class RoomReservation {

    String room;
    String enddate;
    String startdate;

    RoomReservation(String room, String startdate, String enddate) {
      this.room = room;
      this.startdate = startdate;
      this.enddate = enddate;
    }
  }

  private void R1() throws SQLException {

    try {
          Class.forName(JDBC_DRIVER);
          //System.out.println("MySQL JDBC Driver loaded");
      } catch (ClassNotFoundException ex) {
          System.err.println("Unable to load JDBC Driver");
          System.exit(-1);
      };

      try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

        String sqlA = "WITH A AS (     SELECT RoomCode, RoomName, Beds, bedType, maxOcc, basePrice, decor, ROUND(SUM(OccupiedDays)/180,2) as PopScore          FROM (             SELECT *, DATEDIFF(LEAST(NOW(), CheckOut), GREATEST(DATEDIFF(NOW(), CheckIn) - 180, CheckIn)) as OccupiedDays             FROM lab7_reservations as Res                 INNER JOIN lab7_rooms as Rooms ON Res.Room = Rooms.RoomCode             WHERE CheckOut >= DATEDIFF(NOW(), CheckIn) - 180 AND CheckIn <= NOW()         ) as occupied     GROUP BY ROOM     ORDER BY PopScore DESC ), B AS (     SELECT distinct Room, CURRENT_DATE() as NextAvailableCheckIn         FROM lab7_reservations          WHERE Room NOT IN (SELECT Room             FROM lab7_reservations              WHERE NOW() BETWEEN CheckIn AND CheckOut)     UNION     SELECT Room, CheckOut     FROM (     SELECT *,         RANK() OVER (PARTITION BY ROOM ORDER BY NextDate ASC) as Ranking     FROM (         SELECT *, DATEDIFF(CheckOut, NOW()) as NextDate             FROM lab7_reservations as R         WHERE CODE NOT IN (SELECT R.CODE             FROM lab7_reservations as R             INNER JOIN lab7_reservations as R1 ON R.Room = R1.Room             AND R.CheckOut = R1.CheckIn)              AND Room IN (SELECT Room             FROM lab7_reservations              WHERE NOW() BETWEEN CheckIn AND CheckOut)     ) as CheckOuts     WHERE NextDate > 0     ) as Ranker     WHERE Ranking = 1 ), C AS (     SELECT ROOM, LatestLengthofStay, CheckOut AS LatestCheckOut     FROM (         SELECT *, RANK() OVER (PARTITION BY ROOM ORDER BY Prev DESC) as Ranker         FROM (             SELECT *, DATEDIFF(CheckOut, NOW()) as Prev, DATEDIFF(CheckOut, CheckIn) as LatestLengthofStay             FROM lab7_reservations as Res                 INNER JOIN lab7_rooms as Rooms ON Res.Room = Rooms.RoomCode             WHERE CheckOut <= NOW()         ) AS Stays     ) as Ranking     WHERE Ranker = 1 ) SELECT RoomCode, RoomName, Beds, bedType, maxOcc, basePrice, decor, PopScore, NextAvailableCheckIn, LatestLengthofStay, LatestCheckOut FROM A     INNER JOIN B ON A.RoomCode = B.ROOM     INNER JOIN C ON A.RoomCode = C.ROOM ORDER BY A.PopScore DESC";

        try(Statement stmt = conn.createStatement()) {
          ResultSet rs = stmt.executeQuery(sqlA);
          printResultSet(rs);

        }
        System.out.println();
        conn.close();
      } catch (SQLException ex) {
        System.out.println("Oops, something went wrong.");
      };
    }

  //r2
  public void R2() throws SQLException {

    try {
          Class.forName(JDBC_DRIVER);
          //System.out.println("MySQL JDBC Driver loaded");
      } catch (ClassNotFoundException ex) {
          System.err.println("Unable to load JDBC Driver");
          System.exit(-1);
      };

      try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

        Scanner scan = new Scanner(System.in);

        /* Get information */
        System.out.println("Hi! Welcome to the reservation system. Please enter the following information: ");
        System.out.println("Please enter your First Name.");
        String firstname = scan.nextLine().toUpperCase();
        System.out.println("Please enter your Last Name.");
        String lastname = scan.nextLine().toUpperCase();
        System.out.println("Please enter your desired Room Type.");
        String room = scan.nextLine().toUpperCase();
        System.out.println("Please enter your desired Bed Type.");
        String bed = scan.nextLine();
        System.out.println("Please enter the begin date of your stay.");
        String startdate = scan.nextLine();
        System.out.println("Please enter the end date of your stay.");
        String enddate = scan.nextLine();
        System.out.println("Please enter the number of children.");
        int children = scan.nextInt();
        System.out.println("Please enter the number of adults.");
        int adults = scan.nextInt();
        int total = adults + children;


        /* Check for MaxOccupancy */
        if(room.toUpperCase().equals("ANY") == false)
        {
          String checkOccupancy = String.format("SELECT * FROM lab7_rooms WHERE RoomCode = '%s'", room);
          try(Statement stmt = conn.createStatement()) {
              ResultSet rs = stmt.executeQuery(checkOccupancy);
              while (rs.next()) {
                if(rs.getInt("maxOcc") < total) {
                  System.out.print("Max occupancy exceeded for this room -- would you like to continue [1] or start over? [2]");
                  int ans = scan.nextInt();
                  if(ans != 1) R2();
                }
              }
          }
        } else {
          String checkOccupancy = "SELECT MAX(maxOcc) AS maxOCC FROM lab7_rooms";
          try(Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery(checkOccupancy);

              while (rs.next()) {
                if(rs.getInt("maxOcc") < total) {
                  System.out.print("Max occupancy exceeded for all rooms -- would you like to continue [1] or start over? [2]");
                  int ans = scan.nextInt();
                  if(ans != 1) R2();
                }
              }
          }
        }

        boolean match = false;

        List<RoomReservation> reservations  = new ArrayList<RoomReservation>();

        /* Look for match */
        if(room.toUpperCase().equals("ANY") == false) {
          String findmatch = String.format("SELECT * FROM lab7_reservations as Res WHERE Room = '%s' AND ('%s' < CheckOut) AND ('%s' > CheckIn)", room, startdate, enddate);

          try(Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(findmatch);
              if(rs.next() == false) {
                match = true;
                RoomReservation res = new RoomReservation(room, startdate, enddate);
                reservations.add(res);
              }
          }
        }

        /* if match not found */
        if(match == false){

          String similar = String.format("SELECT * FROM lab7_rooms WHERE RoomCode = '%s'", room);
          String decor = "";
          String beds = "";

          try(Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(similar);
                      while(rs.next()) {
              decor = rs.getString("decor");
              beds = rs.getString("bedType");
            }
          }

          System.out.println(decor + " " +  beds);

          similar = String.format("SELECT RoomCode FROM lab7_rooms WHERE decor = '%s' OR bedType = '%s'", decor, beds);
          int i = 0;

          try(Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(similar);
            while(rs.next()) {
            }
          }

          String find_partialmatch = String.format("SELECT RoomCode FROM lab7_rooms WHERE RoomCode NOT IN (SELECT Room FROM lab7_reservations WHERE ('%s' < CheckOut) AND ('%s' > CheckIn))", startdate, enddate);

          try(Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(find_partialmatch);
            while(rs.next() && reservations.size() < 5) {
              RoomReservation res = new RoomReservation(rs.getString("RoomCode"), startdate, enddate);
              reservations.add(res);
            }
          }
        }

        if(reservations.size() < 5) {
          System.out.println("I AM FINDING");
          String find_more = String.format("SELECT Room, CheckOut as StartDate, DATE_ADD(CheckOut, INTERVAL 1 DAY) as EndDate FROM ( SELECT      *, RANK() OVER (PARTITION BY ROOM ORDER BY NextDate ASC) as Ranking FROM( SELECT *, DATEDIFF(CheckOut, '%s') AS NextDate     FROM lab7_reservations as R WHERE CODE NOT IN (SELECT R.CODE FROM lab7_reservations as R INNER JOIN lab7_reservations as R1 ON R.Room = R1.Room AND R.CheckOut = R1.CheckIn) AND Room IN (SELECT Room     FROM lab7_reservations      WHERE '%s' BETWEEN CheckIn AND CheckOut) ) as ValidDates WHERE NextDate > 0 ) as Ranker WHERE Ranking = 1 ORDER BY StartDate ASC", startdate, startdate);

          try(Statement stmt = conn.createStatement()) {
                      ResultSet rs = stmt.executeQuery(find_more);
                      while(rs.next() && reservations.size() < 5) {
                          RoomReservation res = new RoomReservation(rs.getString("Room"), rs.getString("StartDate"), rs.getString("EndDate"));
                          reservations.add(res);
                      }
                  }
        }

        int counter = 1;
        for (RoomReservation r: reservations){
          System.out.println(String.format("[%d] Room: '%s'\nCheckIn: '%s'\nCheckOut:'%s'", counter, r.room, r.startdate, r.enddate));
          counter++;
        }

        /*Finalize booking*/
        int book = scan.nextInt() - 1;
        if((0 <= book) && (book  < reservations.size())) {
          System.out.println("RESERVATION CONFIRMATION");
          System.out.println(String.format("%s, %s", firstname, lastname));

          String room_info = String.format("SELECT * FROM lab7_rooms WHERE RoomCode = '%s'", reservations.get(book).room);
          double base_rate = 0;
          try(Statement stmt = conn.createStatement()) {
                      ResultSet rs = stmt.executeQuery(room_info);
                      while(rs.next()) {
                        System.out.println(String.format("%s, %s, %s", rs.getString("RoomCode"), rs.getString("RoomName"), rs.getString("bedType")));
              base_rate = rs.getFloat("basePrice");
            }
                  }
          System.out.println(String.format("%s to %s", reservations.get(book).startdate, reservations.get(book).enddate));
          System.out.println(String.format("Adults %d, Children %d", adults, children));

          /*rate calculations*/

          String find_week_days = String.format("SELECT 5 * (DATEDIFF('%s', '%s') DIV 7) + MID('0123444401233334012222340111123400012345001234550', 7 * WEEKDAY('%s') + WEEKDAY('%s') + 1, 1) AS WeekDays", reservations.get(book).enddate, reservations.get(book).startdate, reservations.get(book).startdate, reservations.get(book).enddate);
          int week_days = 0;
          try(Statement stmt = conn.createStatement()) {
                      ResultSet rs = stmt.executeQuery(find_week_days);
                      while(rs.next()) {
                        week_days = rs.getInt("WeekDays");
            }
                  }

          String find_days = String.format("SELECT DATEDIFF('%s', '%s') AS total", reservations.get(book).enddate, reservations.get(book).startdate);

          int total_days = 0;

          try(Statement stmt = conn.createStatement()) {
                      ResultSet rs = stmt.executeQuery(find_days);
                      while(rs.next()) {
                          total_days = rs.getInt("total");
                      }
                  }

          int weekends = total_days - week_days;

          double total_cost = 0;

          total_cost += week_days * base_rate;
          total_cost += weekends * base_rate * 1.1;
          total_cost = total_cost * 1.18;

          System.out.println(String.format("YOUR RESERVATION TOTAL IS: %f. Press [1] to confirm.", total_cost));

          int confirm = scan.nextInt();

          if(confirm == 1) {

            int randomNum = ThreadLocalRandom.current().nextInt(1,  1000 + 1);

            String insert = String.format("INSERT INTO lab7_reservations ( CODE, Room, CheckIn, CheckOut, Rate, LastName, FirstName, Adults, Kids) VALUES ( '%d', '%s', '%s', '%s', '%f', '%s', '%s', '%d', '%d' );", randomNum, reservations.get(book).room, reservations.get(book).startdate, reservations.get(book).enddate, total_cost,
          lastname, firstname, adults, children);

            try(Statement stmt = conn.createStatement()) {
                        stmt.executeUpdate(insert);
                    }
          }
        }
          System.out.println("Thank you!");
          conn.close(); //where does the close go?
      } catch (SQLException ex) {
        System.out.println("Couldn't make this reservation.");
      };
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
          //System.out.println("MySQL JDBC Driver loaded");
      } catch (ClassNotFoundException ex) {
          System.err.println("Unable to load JDBC Driver");
          System.exit(-1);
      };

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

        //System.out.println("connected!");
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
        } else {
          foundReservation.beforeFirst();
        }

        // print reservation
        printResultSet(foundReservation);
        foundReservation.beforeFirst();
        foundReservation.next();

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
            return;
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

          // apply changes
          foundReservation.updateRow();

          //show updates
          System.out.println("Successfully updated Reservation " + code);
          foundReservation.beforeFirst();
          printResultSet(foundReservation);
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
          //System.out.println("MySQL JDBC Driver loaded");
      } catch (ClassNotFoundException ex) {
          //System.err.println("Unable to load JDBC Driver");
          System.exit(-1);
      };

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

        //System.out.println("connected!");
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
        } else {
          foundReservation.beforeFirst();
        }

        // print reservation
        printResultSet(foundReservation);
        foundReservation.beforeFirst();

        // confirm they want to cancel it
        System.out.println("Are you sure you want to cancel this reservation? Y/N");
        char confirmation = sc.nextLine().charAt(0);
        if (confirmation == 'Y' || confirmation == 'y'){
          //delete entry
          foundReservation.next();
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

    //R5
    private static void resInfo()
      throws SQLException{

      PreparedStatement findReservation = null;

      try {
          Class.forName(JDBC_DRIVER);
          //System.out.println("MySQL JDBC Driver loaded");
      } catch (ClassNotFoundException ex) {
          //System.err.println("Unable to load JDBC Driver");
          System.exit(-1);
      };

      try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

          //System.out.println("connected!");
          conn.setAutoCommit(false);

          String constraints = "";
          String begin = "SELECT res.CODE,\n\tres.Room,\n\trooms.RoomName,\n\tres.CheckIn,\n\tres.CheckOut,\n\tres.Rate,\n\tres.LastName,\n\tres.FirstName,\n\tres.Adults,\n\tres.Kids" +
                        "\nFROM mkong02.lab7_reservations as res\nJOIN mkong02.lab7_rooms AS rooms ON\n\trooms.RoomCode = res.Room\n";
          String end = "\nORDER BY res.CODE;";
          String input;
          Scanner in = new Scanner(System.in);
          System.out.println("Leave entry blank to indicate 'Any'");
          System.out.println("Enter first name:");
          input = in.nextLine();
          constraints = constraints + inputToSQL(input.toUpperCase(), "res.FirstName");
          System.out.println("Enter last name:");
          input = in.nextLine();
          constraints = constraints + inputToSQL(input.toUpperCase(), "res.LastName");
          System.out.println("Enter range of dates [format: yyyy-mm-dd to yyyy-mm-dd]:");
          input = in.nextLine();
          constraints = constraints + inputToSQL(input, "res.CheckIn");
          System.out.println("Enter room code:");
          input = in.nextLine();
          constraints = constraints + inputToSQL(input.toUpperCase(), "res.Room");
          System.out.println("Enter reservation code:");
          input = in.nextLine();
          constraints = constraints + inputToSQL(input, "res.CODE");
          if(constraints.length() > 0){
            constraints = "WHERE\n\t" + constraints.substring(0, constraints.length() - 6);
          }
          String query = begin + constraints + end;
          //System.out.println(query);

          findReservation = conn.prepareStatement(query);
          ResultSet foundReservations = findReservation.executeQuery();

          if (foundReservations.next()==false){
            System.out.println("No results found.");
          } else {
            foundReservations.beforeFirst();
          }

          printResultSet(foundReservations);

          //conn.commit();
          conn.close();
        } catch (SQLException ex) {
          System.out.println("No results found.");
        };

    }

    private static String inputToSQL(String input, String column){
      input = input.trim();
      //System.out.println(input);
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
          output = column + " LIKE '" + input + "'";
          break;
        case "res.CODE":
          output = column + " LIKE " + input ;
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

    public static void main(String[] args) {

          try {
              InnReservations ir = new InnReservations();

              String menu = "\nChoose one of the following options: \n" +
                            "\ta - View rooms and rates.\n" +
                            "\tb - Make a reservation.\n" +
                            "\tc - Update a reservation.\n" +
                            "\td - Cancel a reservation.\n" +
                            "\te - Search for a reservation.\n" +
                            "\tf - Show monthly revenue.\n" +
                            "\tm - Show options.\n" +
                            "\tq - Quit.";

              // display main menu
              System.out.println("\nWELCOME TO THE JDBC HOTEL! :)\n");
              System.out.println(menu);

              // get user input
              while (true) {
                Scanner mainScanner = new Scanner(System.in);
                System.out.println("\nEnter option: ");
                char option = mainScanner.nextLine().charAt(0);
                switch(option) {
                  case 'a':
                    System.out.println("\nView rooms and rates.\n");
                    ir.R1();
                    break;
                  case 'b':
                    System.out.println("\nMake a reservation.\n");
                    ir.R2();
                    break;
                  case 'c':
                  System.out.println("\nUpdate a reservation.\n");
                    ir.changeReservation();
                    break;
                  case 'd':
                  System.out.println("\nCancel a eservation.\n");
                    ir.cancelReservation();
                    break;
                  case 'e':
                    System.out.println("\nSearch for a reservation.\n");
                    ir.resInfo();
                    break;
                  case 'f':
                    System.out.println("\nShow monthly revenue\n");
                    //call function
                    break;
                  case 'm':
                    System.out.println(menu);
                    break;
                  case 'q':
                    System.out.println("Bye!\n");
                    System.exit(0);
                  default:
                    System.out.println("Sorry that wasn't an option!\n" +
                                        "Try using lower case or enter 'm' to show options.\n");
                    break;
                }
              }

          } catch (SQLException e) {
              System.err.println("SQLException: " + e.getMessage());
          }
    }

}
