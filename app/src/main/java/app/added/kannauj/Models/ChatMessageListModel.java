package app.added.kannauj.Models;

/**
 * Created by Sudipta Samanta on 19-07-2019.
 */

public class ChatMessageListModel {
    String CreateDate;
    String id;
    String IsDelivered;
    String IsSeen;
    String isSelfMessage;
    String Message;
    String SeenDateTime;

    public ChatMessageListModel(String createDate, String id, String isDelivered, String isSeen, String isSelfMessage, String message, String seenDateTime) {
        CreateDate = createDate;
        this.id = id;
        IsDelivered = isDelivered;
        IsSeen = isSeen;
        this.isSelfMessage = isSelfMessage;
        Message = message;
        SeenDateTime = seenDateTime;
    }


    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsDelivered() {
        return IsDelivered;
    }

    public void setIsDelivered(String isDelivered) {
        IsDelivered = isDelivered;
    }

    public String getIsSeen() {
        return IsSeen;
    }

    public void setIsSeen(String isSeen) {
        IsSeen = isSeen;
    }

    public String getIsSelfMessage() {
        return isSelfMessage;
    }

    public void setIsSelfMessage(String isSelfMessage) {
        this.isSelfMessage = isSelfMessage;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSeenDateTime() {
        return SeenDateTime;
    }

    public void setSeenDateTime(String seenDateTime) {
        SeenDateTime = seenDateTime;
    }

}
