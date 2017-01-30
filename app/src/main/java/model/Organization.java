package model;

/**
 * Created by Matan on 28/01/2017.
 */

import android.graphics.Bitmap;


public class Organization {

    private Long id;
    private String name;
    private String address;
    private String email;
    private String password;
    private Bitmap profilePic;

    public Organization(String name, String address,Bitmap profilePic){
        this(0L,  name, address,  "",  "",  profilePic);
    }

    public Organization(String name, String address, String email , String password,Bitmap profilePic){
        this(0L,  name, address,  email,  password,  profilePic);
    }

    public Organization(Long id , String name, String address, String email, String password, Bitmap profilePic){
        this.id =id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.profilePic =profilePic;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
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
}
