package model;

/**
 * Created by Matan on 28/01/2017.
 */

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


public class Organization {

    private int id;
    private String name;
    private String address;
    private String email;
    private String password;
    private Bitmap profilePic;

    public Organization(String name, String address,Bitmap profilePic){
        this(0,  name, address,  "",  "",  profilePic);
    }

    public Organization(String name, String address, String email , String password,Bitmap profilePic){
        this(0,  name, address,  email,  password,  profilePic);
    }

    public Organization(int id , String name, String address, String email, String password, Bitmap profilePic){
        this.id =id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.profilePic =profilePic;
    }
// setters and getters
    public Organization() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public static List<Organization> parseJson(String content) {

        List<Organization> list = null;
        try {

            JSONTokener jsonTokener = new JSONTokener(content);

            JSONObject json = (JSONObject) jsonTokener.nextValue();

            list = new ArrayList<>();

            JSONArray OrganizationsJsonArr = json.getJSONArray("organizations");

            for (int i = 0; i < OrganizationsJsonArr.length(); i++) {
                try {
                    JSONObject fObj = OrganizationsJsonArr.getJSONObject(i);
                    Organization o = new Organization();
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

    /**
     *
     * @param fObj
     * @return
     */
    private boolean fromJson(JSONObject fObj) {
        boolean res = false;
        try {
            setId(fObj.getInt("organizationID"));
            setName(fObj.getString("organizationName"));
            setAddress(fObj.getString("Address"));
            setEmail(fObj.getString("Email"));
            setPassword(fObj.getString("Password"));
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

    /**
     * create a json object with organization fields
     * @param obj
     * @return
     */
    public static JSONObject toJson(Organization obj){
        JSONObject json = new JSONObject();

        try {
            json.put("organizationID",obj.getId() );
            json.put("organizationName",obj.getName() );
            //json.put("lname",obj.getlName() );
            json.put("Address",obj.getAddress() );
            json.put("Email",obj.getEmail() );
            json.put("Password",obj.getPassword() );
            String s = utilityClass.getInstance().imgToBase64String(obj.getProfilePic());
            json.put("ProfilePic",(s != null) ? s : "" );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}

