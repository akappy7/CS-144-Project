package edu.ucla.cs.cs144;

/**
 * Created by Nero on 3/2/17.
 */
public class Bid {
    private String userID;
    private String rating;
    private String location;
    private String country;
    private String time;
    private String amount;

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAmount() {
        return amount;
    }

    public String getCountry() {
        return country;
    }

    public String getLocation() {
        return location;
    }

    public String getRating() {
        return rating;
    }

    public String getTime() {
        return time;
    }

    public String getUserID() {
        return userID;
    }
}
