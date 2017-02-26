package model;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import network.utils.NetworkConnector;
import network.utils.NetworkResListener;
import network.utils.ResStatus;

import static android.R.attr.id;
import static com.caldroidsample.R.id.email;
import static model.UserType.volType;

/**
 * Created by Faina0502 on 31/01/2017.
 */

public class ManagerDB implements NetworkResListener {
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
            NetworkConnector.getInstance().registerListener(instance);
        }

        return instance;
    }

    /**
     * release instance
     */
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
            if(db == null){
                db = new VolutimeDB(context);
            }

            db.open();
        }
    }

    public void closeDataBase() {
        if(db!=null){
            db.close();
        }
    }

    /**
     *
     * @param vol
     * @return
     */
    private boolean syncUpdateVolunteer(Volunteer vol) {
        boolean res = false;
        if (db != null) {
            Volunteer v = readVolunteer(vol.getEmail());
            if( v != null){
                //update
                vol.setId(v.getId());
                int cnt  = db.updateVolunteer(vol);
                res = true;
            }else{
                //insert
                long id = db.addVolunteer(vol);
                if(id != -1)
                    res = true;
            }
        }
        return res;
    }

    /**
     *
     * @param org
     * @return
     */
    private boolean syncUpdateOrganization(Organization org) {
        boolean res = false;
        if (db != null) {
            Organization o = readOrganization(org.getEmail());
            if( o != null){
                //update
                org.setId(o.getId());
                int cnt  = db.updateOrg(org);
                if(cnt > 0)
                    res = true;
            }else{
                //insert
                long id = db.addOrganization(org);
                if(id != -1)
                    res = true;
            }
        }
        return res;
    }

    /**
     *
     * @param volEvent
     * @return
     */
    private boolean syncUpdateVolEvent(VolEvent volEvent) {
        boolean res = false;
        if (db != null) {
            VolEvent event = readEvent(volEvent.getVolEventID());
            if( event != null){
                //update
                int cnt  = db.updateEvent(volEvent);
                if(cnt > 0)
                    res = true;
            }else{
                //insert
                long id = db.addEvent(volEvent);
                if(id != -1)
                    res = true;
            }
        }
        return res;
    }

    /**
     *
     * @param volunteer
     * @return
     */
    public long addVolunteer(Volunteer volunteer){
        return  db.addVolunteer(volunteer);
    }

    /**
     *
     * @param volunteer
     * @return
     */
    public boolean registerVolunteerUser(Volunteer volunteer){
        if(db!=null && volunteer!=null) {
          if(db.addVolunteer(volunteer) != -1)
            //to send a message that everything is alright
            return true;
        }
        return false;
    }

    /**
     *
     * @param organization
     * @return
     */
    public long addOrganization(Organization organization){
        return db.addOrganization(organization);
    }

    /**
     *
     * @param volId
     * @return
     */
    public Volunteer readVolunteer(int volId){
        if(db!=null){
            return  db.readVolunteer(volId);
        }
        return  null;
    }

    //read vol
    public Volunteer readVolunteer(String email) {
        if(db!=null){
            return  db.readVolunteer(email);
        }
        return  null;
    }

    /**
     *
     * @return
     */
    public ArrayList<Volunteer> readAllVolunteers() {
        if(db!=null){
            return  db.readAllVolunteers();
        }
        return  null;
    }

    /**
     *
     * @param id
     * @return organization that is in the db otherwise null
     */
    public Organization readOrganization(int id){
        if(db!=null){
            return  db.readOrganization(id);
        }
        return  null;
    }

    /**
     * get organization by email
     * @param email
     * @return
     */
    public Organization readOrganization(String email){
        if(db!=null){
            return  db.readOrganization(email);
        }
        return  null;
    }

    /**
     * get organization by pass and email
     * @param email
     * @param password
     * @return
     */
    public Organization getOrgUser(String email, String password) {
        Organization org = null;
        if(db!=null && email!=null&& password!=null){
            org = db.getOrgUser(email,password);
        }
        return org;
    }

    /**
     * add the new organization user volunteer at
     * @param volID
     * @param orgID
     * @param startDate
     * @param endDate
     * @return
     */
    public Long addOrgToVolunteer(int volID ,int orgID, String startDate, String endDate){

        if(db != null){
            return  db.addOrgToVolunteer(volID,orgID,startDate,endDate);
        }

        return -1L;
    }

    /**
     * get all id's of organization
     * @param userID
     * @return
     */
    public ArrayList<Integer> getOrgIdsOfVol(int userID){
        if(db!=null){
            return  db.getOrgIdsOfVol(userID);
        }
        return null;
    }

    /**
     *
     * @returns all orgs in db
     */
    public List<Organization> getAllOrgs(){
        if(db!=null)
        {
           return db.getAllOrgs();
        }
        return null;
    }

    /**
     * gets the vol and the org ids and returns the dates that the volunteer started the volunteering activity
     * in the organization and the date the volunteer stopped
     * if the stopped date is unknown equals NULL
     * @param volId
     * @param orgID
     * @return
     */
    public VolAtOrg getVolAtOrg(int volId, int orgID){
        if(db!=null)
        {
            return db.getVolAtOrg(volId,orgID);
        }
        return null;
    }

    /**
     * delete connection from org ti vol
     * @param volAtOrg
     * @return
     */
    public int deleteVolAtOrg(VolAtOrg volAtOrg){
        if(db!=null)
        {
            return db.deleteVolAtOrg(volAtOrg);
        }
        return -1;
    }

    public int updateVolAtOrg(VolAtOrg volAtOrg){
        if(db!=null){
            return  db.updateVolAtOrg(volAtOrg);
        }
        return  -1;
    }

    /**
     *
     * @param org
     * @return
     */
    public  long updateOrg(Organization org){
        if(db!=null){
            long result = db.updateOrg(org);
            if(result > 0){
                JSONObject jObj = Organization.toJson(org);
                NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.UPDATE_ORG_REQ,jObj );
            }
            return   result;
        }
        return  -1;
    }


    public long addEvent(VolEvent event){
        if(db!=null){
            long result = db.addEvent(event);
            if(result > 0){
                JSONObject jObj = VolEvent.toJson(event);
                NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.INSERT_EVENT_REQ,jObj );
            }
            return   result;
        }

        return -1;
    }

    public VolEvent readEvent(int id){
        if(db!=null)
            return db.readEvent(id);
        return null;
    }

    public List<VolEvent> readEventsForUserByMonth(int month , int userId){
        if(db!=null)
            return db.readEventsForUserByMonth(month, userId);

        return null;
    }

    public int updateEvent(VolEvent event){
        if(db!=null){
            int result = db.updateEvent(event);
            if(result > 0){
                JSONObject jObj = VolEvent.toJson(event);
                NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.UPDATE_EVENT_REQ,jObj );
            }
            return  result;
        }
        return  -1;
    }

    public int deleteEvent(VolEvent event){
        if(db!=null){
            int result = db.deleteEvent(event);
            if(result > 0){
                JSONObject jObj = VolEvent.toJson(event);
                NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.DELETE_EVENT_REQ,jObj );
            }
            return  result;
        }
        return  -1;
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

    @Override
    public void onPreUpdate(String type) {
        Toast.makeText(context,"Sync stated...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostUpdate(byte[] res, ResStatus status, String type) {
        Toast.makeText(context,"Sync finished...status " + status.toString(),Toast.LENGTH_SHORT).show();

    }

    /**
     * update all volunteers from server
     * @param res
     */
    public void updateVolunteers(byte[] res) {
        if(res == null){
            return;
        }
        try {
            String content = new String(res, "UTF-8");
            List<Volunteer> list = Volunteer.parseJson(content);
            if(list!=null && list.size()>0){
                for(Volunteer vol :list){
                    syncUpdateVolunteer(vol);
                }
            }
        }
        catch(Throwable t){
            t.printStackTrace();
        }

    }

    /**
     * update all organization petch from server
     *
     * @param res
     */
    public void updateOrganization(byte[] res) {
        if(res == null){
            return;
        }
        try {
            String content = new String(res, "UTF-8");
            List<Organization> list = Organization.parseJson(content);
            if(list!=null && list.size()>0){
                for(Organization vol :list){
                    syncUpdateOrganization(vol);
                }
            }
        }
        catch(Throwable t){
            t.printStackTrace();
        }

    }

    /**
     * update event
     * @param res
     */
    public void updatVolEvent(byte[] res) {
        if(res == null){
            return;
        }
        try {
            String content = new String(res, "UTF-8");
            List<VolEvent> list = VolEvent.parseJson(content);
            if(list!=null && list.size()>0){
                for(VolEvent volEvent :list){
                    syncUpdateVolEvent(volEvent);
                }
            }
        }
        catch(Throwable t){
            t.printStackTrace();
        }

    }
}
