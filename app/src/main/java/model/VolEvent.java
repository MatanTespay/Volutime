package model;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.utilityClass;

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

    public static JSONObject toJson(VolEvent obj){
        JSONObject iObj = new JSONObject();

        try {

            iObj.put("eventID", obj.getVolEventID());
            iObj.put("volunteerID", obj.getVolID());
            iObj.put("organizationID", obj.getOrgID());
            iObj.put("date", (obj.getDate() != null ) ? utilityClass.getInstance().getStringFromDateTime(obj.getDate()) : "");
            iObj.put("startTime", (obj.getStartTime() != null ) ? utilityClass.getInstance().getStringFromDateTime(obj.getStartTime()) : "");
            iObj.put("endTime",  (obj.getEndTime() != null ) ? utilityClass.getInstance().getStringFromDateTime(obj.getEndTime()) : "");
            iObj.put("details", obj.getDetails());
            iObj.put("title", obj.getTitle());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return iObj;
    }


    public static List<VolEvent> parseJson(String content) {

        List<VolEvent> list = null;
        try {

            JSONTokener jsonTokener = new JSONTokener(content);

            JSONObject json = (JSONObject) jsonTokener.nextValue();

            list = new ArrayList<>();

            JSONArray VolEventsJsonArr = json.getJSONArray("VolEvents");

            for (int i = 0; i < VolEventsJsonArr.length(); i++) {
                try {
                    JSONObject fObj = VolEventsJsonArr.getJSONObject(i);
                    VolEvent o = new VolEvent();
                    if(o.fromJson(fObj)){
                        list.add(o);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return list;
    }

    private boolean fromJson(JSONObject fObj) {
        boolean res = false;
        try {
            setVolEventID(fObj.getInt("eventID"));
            setVolID(fObj.getInt("volunteerID"));
            setOrgID(fObj.getInt("organizationID"));
            setDate(utilityClass.getInstance().getDateFromString(fObj.getString("date")));
            setStartTime(utilityClass.getInstance().getDateTimeFromString(fObj.getString("startTime")));
            setEndTime(utilityClass.getInstance().getDateTimeFromString(fObj.getString("endTime")));
            setDetails(fObj.getString("details"));
            setTitle(fObj.getString("title"));

           res = true;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return res;
    }

}
