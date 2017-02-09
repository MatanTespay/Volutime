package model;

import java.util.Date;

/**
 * Created by Faina0502 on 28/01/2017.
 */

public class VolEvent {

    private int volEventID;
    private int volID;
    private int orgID;
    private String details;
    private String title;
    private Date date;
    private Date startTime;
    private Date endTime;

    public VolEvent( int orgID,int volID, String details, Date date, Date startTime, Date endTime, String title ) {
        this.details = details;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.volID = volID;
        this.title = title;
        this.orgID=orgID;
    }

    public VolEvent() {

    }

    public int getVolEventID() {
        return volEventID;
    }

    public void setVolEventID(int volEventID) {
        this.volEventID = volEventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getVolID() {
        return volID;
    }


    public void setVolID(int volID){
        this.volID=volID;
    }

    public int getOrgID() {
        return orgID;
    }

    public void setOrgID(int orgID) {
        this.orgID = orgID;
    }
}
