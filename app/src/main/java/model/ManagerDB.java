package model;

import android.content.Context;

import static model.UserType.volType;

/**
 * Created by Faina0502 on 31/01/2017.
 */

public class ManagerDB {
    //
    private static ManagerDB instance = null;
    private Context context = null;
    private VolutimeDB db = null;

    public ManagerDB() {
    }

    //singelton
    public static ManagerDB getInstance(){
        if(instance== null){
            instance = new ManagerDB();
        }

        return instance;
    }
    public static void releaseInstance() {
        if (instance != null) {
            instance.clean();
            instance = null;
        }
    }

    private void clean() {

    }


    public Context getContext() {
        return context;
    }
    public void openDataBase(Context context) {
        this.context = context;
        if (context != null) {
            db = new VolutimeDB(context);
            db.open();
        }
    }


    public void closeDataBase() {
        if(db!=null){
            db.close();
        }
    }

    public boolean registerVolunteerUser(Volunteer volunteer){
        if(db!=null && volunteer!=null) {
          if(db.addVolunteer(volunteer) != -1)
            //to send a message that everything is alright
            return true;
        }
        return false;
    }

    public void registerOrgUser(Organization organization){
        if(db!=null && organization!=null) {
            db.addOrganization(organization);
            //to send a message that everything is alright
        }

    }
    public void verifyUser(UserType type, String email, String pass) {
        if (type != null || pass != null || email != null) {

            if (type.equals(volType)) {
                VerifyVolUser(email , pass);
            } else{
                VerifyOrg(email, pass);

            }
        }
    }

    private Organization VerifyOrg(String email, String pass) {
        Organization org = null;
        return org;
    }

    public Volunteer VerifyVolUser(String email , String password) {
        Volunteer vol=null;
        if(db!=null && email!=null&& password!=null){
            vol = db.getVolunteerUser(email,password);
            if(vol!=null)
                return vol;
            //TODO alert all messages

        }
        return vol;
    }
}
