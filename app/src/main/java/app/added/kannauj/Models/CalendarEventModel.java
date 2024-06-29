package app.added.kannauj.Models;

import java.util.Date;

public class CalendarEventModel {

    String id,name,detail;
    Date date;

    public CalendarEventModel(String id, String name, String detail, Date date) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
