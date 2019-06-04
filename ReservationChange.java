/*
* Allow user to make changes to an existing reservation
* Accepts a reservation code and new values for
* (first name, last name, begin date, end date, num children, num adults)
* user can provide new value or indicate no change for a given field
* must check if begin and end dates conflict with another in the system
* */

public class ReservationChange {


    string SUCCESS = "Successfully updated Reservation %d.";
    string ERROR = "Failed to update Reservation %d.";
    string DATECONFLICT = "This date is unavailable - try a different date.";
    string NOTFOUND = "Could not find Reservation %d.";

    public string changeReservation(int code)
        throws SQLException {

        PreparedStatement findReservation = null;
        PreparedStatement checkDates = null;
        PreparedStatement updateReservation = null;

        /*
        * 1. Find Reservation
        *       if not found - print error
        * 2. If found - give prompts
        * 3. Check for date conflict
        *       if conflict - print error and reprompt?
        * 4. Commit Change
        * */

    }


}