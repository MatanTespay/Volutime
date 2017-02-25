package model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.utilityClass;

/**
 * Created by Faina0502 on 21/01/2017.
 */
public class Volunteer {

    private int id;
    private String fName;
    private String lName;
    private Date birthDate;
    private String address;
    private String email;
    private String password;
    private Bitmap profilePic;
    private ArrayList<VolEvent> volEvents;

    public Volunteer(String fName, String lName, Date birthDate, String address, String email , String password,Bitmap profilePic){
        this.fName = fName;
        this.lName =lName;
        this.birthDate =birthDate;
        this.address = address;
        this.email = email;
        this.password = password;
        this.profilePic =profilePic;
        this.volEvents=new ArrayList<>();
    }

    public Volunteer() {
    }

    public  Volunteer (int id , String fName, String lName, Date birthDate, String address, String email, String password, Bitmap profilePic ){
        this.id =id;
        this.fName = fName;
        this.lName =lName;
        this.birthDate =birthDate;
        this.address = address;

        this.email = email;
        this.profilePic = profilePic;
        this.password = password;
        this.volEvents=new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public ArrayList<VolEvent> getVolEvents() {
        return volEvents;
    }

    public void setVolEvents(ArrayList<VolEvent> volEvents) {
        this.volEvents = volEvents;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public static List<Volunteer> parseJson(String content) {

        List<Volunteer> list = null;
        try {

            JSONTokener jsonTokener = new JSONTokener(content);

            JSONObject json = (JSONObject) jsonTokener.nextValue();

            list = new ArrayList<>();

            JSONArray volunteersJsonArr = json.getJSONArray("volunteers");

            for (int i = 0; i < volunteersJsonArr.length(); i++) {
                try {
                    JSONObject fObj = volunteersJsonArr.getJSONObject(i);
                    Volunteer v = new Volunteer();
                    if(v.fromJson(fObj)){
                        list.add(v);
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

    public static JSONObject toJson(Volunteer obj){
        JSONObject json = null;

        try {
            json.put("volID",obj.getId() );
            json.put("fname",obj.getfName() );
            json.put("lname",obj.getlName() );
            Date d = obj.getBirthDate();
            json.put("birthDate",(d != null ) ? utilityClass.getInstance().getSortStringFromDateTime(obj.getBirthDate()) : "");
            json.put("address",obj.getAddress() );
            json.put("email",obj.getEmail() );
            json.put("password",obj.getPassword() );
            String s = utilityClass.getInstance().imgToBase64String(obj.getProfilePic());
            json.put("ProfilePic",s );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    private boolean fromJson(JSONObject fObj) {
        boolean res = false;
        try {
            setId(fObj.getInt("volID"));
            setfName(fObj.getString("fname"));
            setlName(fObj.getString("lname"));
            setBirthDate(utilityClass.getInstance().getDateFromString(fObj.getString("birthDate")));
            setAddress(fObj.getString("address"));
            setEmail(fObj.getString("email"));
            setPassword(fObj.getString("password"));
            // get image encoded string
            byte[] decodedString = Base64.decode(fObj.getString("ProfilePic"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            setProfilePic(decodedByte);

            res = true;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return res;
    }
}
