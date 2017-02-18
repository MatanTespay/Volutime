package controller.caldroid;

import android.annotation.SuppressLint;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.TimeZone;

import caldroid.CaldroidFragment;
import caldroid.CaldroidListener;
import controller.fragments.EventFragment;
import controller.fragments.EventListFragment;
import model.ManagerDB;
import model.VolEvent;
import utils.utilityClass;

import static android.R.attr.action;
import static android.media.CamcorderProfile.get;
import static com.caldroidsample.R.id.fab;

@SuppressLint("SimpleDateFormat")
public class CaldroidSampleActivity extends AppCompatActivity implements EventListFragment.OnFragmentInteractionListener ,
         EventFragment.OnEventInteractionListener {
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    Map<Date, List<VolEvent>> monthEvents;
    SimpleDateFormat formatter ;
    int userID;
    private FloatingActionButton btnNewEvent;
    private Date selectedDate;
    // set color to specific dates
    private void fillEventsInCalendar(Calendar cal) {

        Calendar c = cal;
        getEventsForCurrentMonth(c.get(Calendar.MONTH));

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


            //map.put(entry.getKey(), bgColor);

        }

        caldroidFragment.setBackgroundDrawableForDates(map);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caldroid);
        monthEvents = new HashMap<>();
        formatter = utilityClass.getInstance().getSortformatter();

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        //caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
        caldroidFragment = new CaldroidSampleCustomFragment();


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

            fillEventsInCalendar(cal);
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
                    Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
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


    }

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
        }


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

    private void getEventsForCurrentMonth(int month) {

        //ManagerDB.getInstance().resetDB();


        int lastDayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar cal = Calendar.getInstance();
        cal.getInstance(TimeZone.getTimeZone("Israel"));

        List<VolEvent> events = new ArrayList<>();

        events = ManagerDB.getInstance().readEventsForUserByMonth(month, userID);
        monthEvents = new HashMap<>();

        Collections.sort(events, new Comparator<VolEvent>() {
            public int compare(VolEvent o1, VolEvent o2) {
                if (o1.getStartTime() == null || o2.getStartTime() == null)
                    return 0;
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });

        for (int i = 1; i < lastDayOfMonth; i++) {
            List<VolEvent> eventsOfDay = new ArrayList<>();

            Date date = null;
            for (VolEvent e : events) {
                cal.setTime(e.getStartTime());
                int d = cal.get(Calendar.DATE);
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
/*
                if(e != null){

                    fillEventsInCalendar();

                    caldroidFragment.refreshView();
                    utilityClass.getInstance().showToast(R.string.successOnSave,new Object[]{});

                }*/
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
                    c.setTime(e.getStartTime());

                    fillEventsInCalendar(c);
                    caldroidFragment.refreshView();
                }

            }else{
                //on remove on current month
                c = Calendar.getInstance();
                c.setTime(selectedDate);
                fillEventsInCalendar(c);
                caldroidFragment.refreshView();
            }



        }
    }
}
