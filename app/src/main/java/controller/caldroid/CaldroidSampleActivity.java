package controller.caldroid;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import controller.fragments.FragmentDates;
import model.ManagerDB;
import model.VolEvent;
import utils.utilityClass;

import static android.media.CamcorderProfile.get;

@SuppressLint("SimpleDateFormat")
public class CaldroidSampleActivity extends AppCompatActivity implements FragmentDates.OnFragmentInteractionListener {
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    Map<Date, List<VolEvent>> monthEvents;
    SimpleDateFormat formatter ;
    int userID;
    // set color to specific dates
    private void setCustomResourceForDates() {

        getEventsForCurrentMonth(1);

        Map<Date, Drawable> map = new HashMap<>();

        ColorDrawable myColor = new ColorDrawable(getResources().getColor(R.color.caldroid_light_red));
        for (Map.Entry<Date, List<VolEvent>> entry : monthEvents.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            map.put(entry.getKey(), myColor);

        }

        caldroidFragment.setBackgroundDrawableForDates(map);


        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
           /* ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
            ColorDrawable green = new ColorDrawable(getResources().getColor(R.color.mash));
            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            caldroidFragment.setTextColorForDate(R.color.white, greenDate);*/
        }
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
        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();

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

        final TextView calendar_textview = (TextView) findViewById(R.id.calendar_textview);

        final Button btnAddEvent = (Button) findViewById(R.id.btnAdd);

        // Customize the calendar
        btnAddEvent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // open new event frag'
                openNewEvent();
            }
        });

        Button showDialogButton = (Button) findViewById(R.id.show_dialog_button);

        final Bundle state = savedInstanceState;
        showDialogButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Setup caldroid to use as dialog
                dialogCaldroidFragment = new CaldroidFragment();
                dialogCaldroidFragment.setCaldroidListener(listener);

                // If activity is recovered from rotation
                final String dialogTag = getResources().getString(R.string.dialog_fragment_tag);
                if (state != null) {
                    dialogCaldroidFragment.restoreDialogStatesFromKey(
                            getSupportFragmentManager(), state,
                            getResources().getString(R.string.dialog_caldroid_saved_state), dialogTag);
                    Bundle args = dialogCaldroidFragment.getArguments();
                    if (args == null) {
                        args = new Bundle();
                        dialogCaldroidFragment.setArguments(args);
                    }
                } else {
                    // Setup arguments
                    Bundle bundle = new Bundle();
                    // Setup dialogTitle
                    dialogCaldroidFragment.setArguments(bundle);
                }

                dialogCaldroidFragment.show(getSupportFragmentManager(),
                        dialogTag);
            }
        });
    }

    private void openNewEvent() {
        android.app.FragmentManager fm = this.getFragmentManager();
        EventFragment datesDialog = new EventFragment();

        Bundle args = new Bundle();

        args.putInt("userID", this.userID);
        args.putBoolean("isEditState", true);

        datesDialog.setArguments(args);
        datesDialog.show(fm, getResources().getResourceName(R.layout.dates_fragment));
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
            FragmentDates datesDialog = new FragmentDates();

            Bundle args = new Bundle();

            args.putSerializable("events", events);
            args.putSerializable("dateTitle", dateTitle);


            datesDialog.setArguments(args);
            datesDialog.show(fm, getResources().getResourceName(R.layout.dates_fragment));
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

        events = ManagerDB.getInstance().readEventsForUserByMonth(2, 1);

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
    public void onFragmentInteraction(Bundle bundle) {


    }
}
