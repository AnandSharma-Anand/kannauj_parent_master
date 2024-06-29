package app.added.kannauj.Models;

import java.util.Date;

public class DateModel {

    Date date;
    int day;
    String status = "";

    public DateModel(Date date, int day) {
        this.date = date;
        this.day = day;
    }

    public DateModel(Date date, int day, String status) {
        this.date = date;
        this.day = day;
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
