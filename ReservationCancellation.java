/*
* Allow user to cancel an existing reservation
* Accept the reservcation code, confirm the cancellation
* remove the reservatoin from the db
* */


public class ReservationCancellation {

    // result statements
    string SUCCESS = "Reservation %d has been cancelled.";
    string NOTFOUND = "Cannot find Reservation %d - please try again.";
    string ERROR = "There was an error when cancelling Reservation %d.";


    public string cancelReservation(int code)
        throw SQLException{

        //prepared statements
        PreparedStatement findReservation = null;
        PreparedStatement cancelReservation = null;

        /*
        * 1. Find reservation by code
        * 2. Delete Reservation
        * */

    }

}