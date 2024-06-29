package app.added.kannauj.Models;

import java.util.Date;

public class EDiaryModel {
    /**
     * id : 6
     * ClassName : 5
     * SectionName : B
     * Heading : Test Heading
     * Details : Details Testing
     * IsActive : 1
     * CreateUserID : 1003494
     * CreateUserName : riteshsharma2009@added
     * CreateDate : 2020-04-23 00:18:57
     */

    private String id;
    private String Heading;
    private String Details;
    private Date CreateDate;

    public EDiaryModel(String id, String heading, String details, Date createDate) {
        this.id = id;
        Heading = heading;
        Details = details;
        CreateDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getHeading() {
        return Heading;
    }

    public void setHeading(String Heading) {
        this.Heading = Heading;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String Details) {
        this.Details = Details;
    }


    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date CreateDate) {
        this.CreateDate = CreateDate;
    }

}
