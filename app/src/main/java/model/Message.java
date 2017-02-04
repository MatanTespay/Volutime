package model;

/**
 * Created by Faina0502 on 28/01/2017.
 */

public class Message {

    private int messageID;
    private String content;
    private String sendDate;
    private int sendTime;
    private int parentID;
    private int senderID;
    private int receiverID;

    public Message(int messageID, String content, String sendDate, int sendTime, int parentID, int senderID, int receiverID) {
        this.messageID = messageID;
        this.content = content;
        this.sendDate = sendDate;
        this.sendTime = sendTime;
        this.parentID = parentID;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }
    public Message() {

    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public int getSendTime() {
        return sendTime;
    }

    public void setSendTime(int sendTime) {
        this.sendTime = sendTime;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }


}
