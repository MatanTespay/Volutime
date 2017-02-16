package model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static com.caldroidsample.R.id.email;
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

    public long addVolunteer(Volunteer volunteer){
        return  db.addVolunteer(volunteer);
    }

    public boolean registerVolunteerUser(Volunteer volunteer){
        if(db!=null && volunteer!=null) {
          if(db.addVolunteer(volunteer) != -1)
            //to send a message that everything is alright
            return true;
        }
        return false;
    }

    public long addOrganization(Organization organization){
        return db.addOrganization(organization);
    }

    public Volunteer readVolunteer(int id){
        if(db!=null){
            return  db.readVolunteer(id);
        }
        return  null;
    }

    public Organization readOrganization(int id){
        if(db!=null){
            return  db.readOrganization(id);
        }
        return  null;
    }

    public Organization getOrgUser(String email, String password) {
        Organization org = null;
        if(db!=null && email!=null&& password!=null){
            org = db.getOrgUser(email,password);
        }
        return org;
    }

    public Long addOrgToVolunteer(int volID ,int orgID, String startDate, String endDate){

        if(db != null){
            return  db.addOrgToVolunteer(volID,orgID,startDate,endDate);
        }

        return -1L;
    }

    public ArrayList<Integer> getOrgIdsOfVol(int userID){
        return db.getOrgIdsOfVol(userID);
    }
    public List<Organization> getAllOrgs(){
        if(db!=null)
        {
           return db.getAllOrgs();
        }
        return null;
    }

    public Long addEvent(VolEvent event){
        if(db!=null)
            return db.addEvent(event);
        return -1L;
    }

    public List<VolEvent> readEventsForUserByMonth(int month , int userId){
        if(db!=null)
            return db.readEventsForUserByMonth(month, userId);

        return null;
    }

    public Volunteer getVolunteerUser(String email , String password) {
        Volunteer vol=null;
        if(db!=null && email!=null&& password!=null){
            vol = db.getVolunteerUser(email,password);
        }
        return vol;
    }

    public void resetDB() {
        if(db!=null){
            db.resetDB();
        }
    }
}
