package controller.caldroid;

import android.annotation.SuppressLint;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.caldroidsample.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import caldroid.CaldroidFragment;
import caldroid.CaldroidListener;
import controller.fragments.EventFragment;
import controller.fragments.EventListFragment;
import controller.notification.ScheduleClient;
import model.ManagerDB;
import model.VolEvent;
import network.utils.NetworkConnector;
import network.utils.NetworkResListener;
import network.utils.ResStatus;
import utils.utilityClass;

import static android.media.CamcorderProfile.get;

@SuppressLint("SimpleDateFormat")
public class CaldroidSampleActivity extends AppCompatActivity implements EventListFragment.OnFragmentInteractionListener ,
         EventFragment.OnEventInteractionListener, NetworkResListener {

    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    Map<Date, List<VolEvent>> monthEvents;
    SimpleDateFormat formatter ;
    int userID;
    private FloatingActionButton btnNewEvent;
    private Date selectedDate;
    private ScheduleClient scheduleClient;
    private boolean doEventGet; // if true then loadData
    private ProgressDialog progressDialog = null;
    Bundle savedInstanceState;
    // set color to specific dates
    private void fillEventsInCalendar(Calendar cal) {

        //Calendar c = cal;
        getEventsForCurrentMonth(cal.get(Calendar.MONTH));

        Map<Date, Drawable> map = new HashMap<>();

        ColorDrawable startEventColor = new ColorDrawable(getResources().getColor(R.color.caldroid_dark_red));
        ColorDrawable continuousBgColor = new ColorDrawable(getResources().getColor(R.color.caldroid_light_red));
        for (Map.Entry<Date, List<VolEvent>> entry : monthEvents.entrySet()) {

            //check all event of this the date.
            for (VolEvent event: entry.getValue()) {
                //if the event is on couple of days
                if(event.getStartTime().getTime() < event.getEndTime().getTime()){
                    List<Date> duration = utilityClass.getDaysBetweenDates(event.getStartTime(), event.getEndTime());
                    for (int i = 0; i < duration.size(); i++) {
                        if( i == 0){
                            map.put(duration.get(i), startEventColor);
                        }else{
                            map.put(duration.get(i), continuousBgColor);
                        }
                    }
                }
                else{
                    map.put(event.getStartTime(), startEventColor);
                }
            }


        }

        caldroidFragment.setBackgroundDrawableForDates(map);
    }

    /**
     * continue to load calander after getting data
     * @param savedInstanceState
     */
    private void loadCalender(Bundle savedInstanceState){

        // Setup arguments
        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Intent intent = getIntent();
            userID = intent.getIntExtra("userID",0);
            doEventGet = intent.getBooleanExtra("doEventGet", false);

            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
                    CaldroidFragment.SUNDAY);

            // Uncomment this line to use Caldroid in compact mode
            args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroidsample.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);

            //fillEventsInCalendar(cal);
        }

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                /*Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onChangeMonth(int month, int year) {
                /*String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();*/
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,1);
                fillEventsInCalendar(cal);
                caldroidFragment.refreshView();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
             /*   Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/
                selectedDate = date;
                showDialogDates(date);
                return;

            }


            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                   /* Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();*/
                }

            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);


        btnNewEvent = (FloatingActionButton) findViewById(R.id.addNew);


        // Customize the calendar
        btnNewEvent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // open new event frag'
                openNewEvent();
            }
        });


        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caldroid);
        this.savedInstanceState = savedInstanceState;
        ManagerDB.getInstance().openDataBase(this);
        monthEvents = new HashMap<>();
        formatter = utilityClass.getInstance().getSortformatter();

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        //caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
        caldroidFragment = new CaldroidSampleCustomFragment();

        //get params and load calendar
        Intent intent = getIntent();
        if(intent != null ){
            userID = intent.getIntExtra("userID", 0);
            doEventGet = intent.getBooleanExtra("doEventGet", false);

            // if true loadData from server
            if(doEventGet) {
                NetworkConnector.getInstance().registerListener(this);
                NetworkConnector.getInstance().getVolevents();
                doEventGet =false;
            }else{
                loadCalender(savedInstanceState);
            }

        }
        else{
            //load default calendar if now params found
            loadCalender(savedInstanceState);

        }

    }

    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }

    @Override
    protected void onPause() {
        ManagerDB.getInstance().closeDataBase();
        super.onPause();
    }

    @Override
    protected void onResume() {
        ManagerDB.getInstance().openDataBase(this);
        super.onResume();
    }

    /**
     * Show dialog fragment with all the events according to volunteer
     */
    private void openNewEvent() {
        android.app.FragmentManager fm = this.getFragmentManager();
        EventFragment datesDialog = new EventFragment();

        Bundle args = new Bundle();

        args.putInt("userID", this.userID);
        args.putBoolean("isEditState", true);
        args.putBoolean("isNew", true);

        datesDialog.setArguments(args);
        datesDialog.show(fm, getResources().getResourceName(R.layout.fragment_event_list));
    }

    /**
     * show event per user by the chosen date
     * @param day
     */
    private void showDialogDates(Date day) {

        String dateTitle="";
        ArrayList<VolEvent> events = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        int dayNum = cal.get(Calendar.DATE);

        // iterate all events of the month
        for (Map.Entry<Date, List<VolEvent>> entry : monthEvents.entrySet()) {
            cal.setTime(entry.getKey());
            int dayToCheck = cal.get(Calendar.DATE);
            //check the events of the given day
            if (dayNum == dayToCheck) {
                //iterate all events of the day
                List<VolEvent> eventsFoTheDay = entry.getValue();
                events = new ArrayList<VolEvent>();
                for (VolEvent e : eventsFoTheDay) {
                    //for each event set the event data in the detail (Child list in adapter)
                    events.add(e);
                    dateTitle = formatter.format(day);

                }

            }
        }


        //show dialog for dates with events
        if(events.size() > 0){
            android.app.FragmentManager fm = this.getFragmentManager();
            EventListFragment datesDialog = new EventListFragment();

            Bundle args = new Bundle();

            args.putSerializable("events", events);
            args.putSerializable("dateTitle", dateTitle);


            datesDialog.setArguments(args);
            datesDialog.show(fm, getResources().getResourceName(R.id.fragment_event_list));
        }/*else{
            utilityClass.getInstance().showToast(R.string.no_events,0,new Object[]{});
        }*/


    }

    public int getUserID() {
        return userID;
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    /**
     * get events by month number
     * @param month
     */
    private void getEventsForCurrentMonth(int month) {

        // get events
        int lastDayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar cal = Calendar.getInstance();
        List<VolEvent> events = new ArrayList<>();

        events = ManagerDB.getInstance().readEventsForUserByMonth(month, userID);
        monthEvents = new HashMap<>();

        //sort events by date
        Collections.sort(events, new Comparator<VolEvent>() {
            public int compare(VolEvent o1, VolEvent o2) {
                if (o1.getStartTime() == null || o2.getStartTime() == null)
                    return 0;
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });

        //group events by day
        for (int i = 1; i <= lastDayOfMonth; i++) {
            List<VolEvent> eventsOfDay = new ArrayList<>();
            Date date = null;
            for (VolEvent e : events) {
                cal.setTime(e.getStartTime());
                int d = cal.get(Calendar.DAY_OF_MONTH);
                if (d == i) {
                    if (date == null)
                        date = e.getStartTime();
                    eventsOfDay.add(e);
                }
            }
            if (date != null)
                monthEvents.put(date, eventsOfDay);
        }
    }

    @Override
    public void onEventItemSelected(Bundle bundle) {


    }

    /**
     * Create notification
     * @param theEvent
     */
    private void setNotification(VolEvent theEvent){

        Calendar c = Calendar.getInstance();
        c.setTime(theEvent.getStartTime());
        Bundle args = new Bundle();

        args.putInt("eventID", theEvent.getVolEventID());

        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        scheduleClient.setAlarmForNotification(c,args );
    }

    /**
     * interface method - after event (create/update/remove)
     * @param args
     */
    @Override
    public void onEventCreated(Bundle args) {

        if(args != null){

            Long eId = args.getLong("currentEventID");
            int action = args.getInt("action");
            VolEvent e = null;
            int intId = eId.intValue();
            if (intId > 0) {
                // get the event created or updated
                e = ManagerDB.getInstance().readEvent(intId);
            }

            //update the list_event_dialog for changes
            Fragment event_list_dialog = getFragmentManager().findFragmentByTag(getResources().getResourceName(R.id.fragment_event_list));
            if(event_list_dialog != null ){
                if(action == 1){ // new
                    if(e != null){
                        //update recycler with new
                        ((EventListFragment)event_list_dialog).getAdapter().addItem(e);
                    }
                }
                else if(action == 2){ // update
                    if(e != null){
                        ((EventListFragment)event_list_dialog).getAdapter().updateItem(e);
                    }
                }
                else if(action == 3){ //remove
                    if(event_list_dialog != null ){
                        ((EventListFragment)event_list_dialog).getAdapter().removeItem();
                        int count = ((EventListFragment)event_list_dialog).getAdapter().getList().size();
                        if(count == 0){
                            ((EventListFragment)event_list_dialog).dismiss();
                        }
                    }
                }
            }

            Calendar c = null;
            if(e != null){
                //on new or update in current month
                if((e.getStartTime().getMonth() +1) == caldroidFragment.getMonth()){
                    c = Calendar.getInstance();
                    //c.setTime(e.getStartTime());
                    c.set(e.getStartTime().getYear(),(e.getStartTime().getMonth()+1),1);
                    fillEventsInCalendar(c);
                    caldroidFragment.refreshView();

                    //set a notification for this event
                    setNotification(e);
                }

            }else{
                //on remove on current month
                c = Calendar.getInstance();
                c.set( selectedDate.getYear(),(selectedDate.getMonth()+1),1);
                fillEventsInCalendar(c);
                caldroidFragment.refreshView();
            }



        }
    }

    /**
     * method to update ui before dong task
     * @param resource
     */
    @Override
    public void onPreUpdate(String resource) {
        progressDialog = new ProgressDialog(this);
        String s = String.format(getResources().getString(R.string.preUpdate),resource );
        progressDialog.setTitle(s);
        progressDialog.setMessage(getResources().getString(R.string.updateMsg));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     *
     * @param  res  - the data
     * @param status - the status of the update process
     * @param type
     */
    @Override
    public void onPostUpdate(byte[] res, ResStatus status, String type) {

        NetworkConnector.getInstance().unregisterListener(this);
        if(type.equals("10")){ //events
            String s = getResources().getString(R.string.events);
            utilityClass.getInstance().showToast(R.string.preUpdate,1,s);
            doEventGet = false;
            progressDialog.dismiss();
            ManagerDB.getInstance().updatVolEvent(res);
            loadCalender(this.savedInstanceState);
        }
        else{
            Toast.makeText(this,"download failed...",Toast.LENGTH_LONG).show();
        }
    }
}
