package app.added.kannauj.Models;

/**
 * Created by Sudipta Samanta on 19-07-2019.
 */

public class ChatListModel {
    String id;
    String FirstName;
    String LastName;
    String IsActive;
    String LastMessage;
    String BranchStaffID;
    String Image;
    String Subject;

    public ChatListModel(String id, String firstName, String lastName, String isActive, String lastMessage, String BranchStaffId, String image, String subject) {
        this.id = id;
        FirstName = firstName;
        LastName = lastName;
        IsActive = isActive;
        LastMessage = lastMessage;
        BranchStaffID = BranchStaffId;
        Image = image;
        Subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    public String getBranchStaffID() {
        return BranchStaffID;
    }

    public void setBranchStaffID(String branchStaffID) {
        BranchStaffID = branchStaffID;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }


}
