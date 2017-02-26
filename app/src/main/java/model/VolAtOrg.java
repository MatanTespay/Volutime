package model;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Faina0502 on 17/02/2017.
 */

public class VolAtOrg {

    private int volID;
    private int orgID;
    private Date startDate;
    private Date endDate;

    public VolAtOrg(Date endDate, Date startDate, int volID, int orgID) {
        this.endDate = endDate;
        this.startDate = startDate;
        this.volID = volID;
        this.orgID = orgID;
    }

    public VolAtOrg() {

    }
// setters and getters
    public int getOrgID() {
        return orgID;
    }

    public void setOrgID(int orgID) {
        this.orgID = orgID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getVolID() {
        return volID;
    }

    public void setVolID(int volID) {
        this.volID = volID;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}