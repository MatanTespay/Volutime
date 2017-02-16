package controller.fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caldroidsample.R;

//import android.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.ManagerDB;
import model.Organization;
import model.VolEvent;
import utils.utilityClass;

import static android.R.id.list;
import static com.caldroidsample.R.string.checkDates;
import static java.lang.Character.UnicodeBlock.of;

public class EventFragment extends DialogFragment {

    EditText txtDate_s, txtTime_s,txtDate_e, txtTime_e, txtTitle, txtDetails;
    List<Organization> orgs = new ArrayList<>();
    List<String> orgsLables = new ArrayList<>();
    ArrayAdapter<String> adapter;
    //initial number to the popup
    private int sYear, sMonth, sDay, sHour, sMinute;
    private int eYear, eMonth, eDay, eHour, eMinute;

    //the numbers that will create the user selected dates
    private int s_Year, s_Month, s_Day, s_Hour = -1, s_Minute = -1; // start time and date values
    private int e_Year, e_Month, e_Day, e_Hour = -1, e_Minute= -1; // end time and date values

    EventFragment dialog;
    int userId;
    VolEvent event;
    Boolean isEditState = false;
    Date start, end;
    View view;
    private Spinner orgSpin;
    Button btnEdit,btnRemove,btnDate_s,btnTime_s,btnDate_e,btnTime_e;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialog = this;
         view = inflater.inflate(R.layout.event_fragment, container, false);
         btnEdit  = (Button) view.findViewById(R.id.btnEditEvent);
         btnRemove  = (Button) view.findViewById(R.id.btnRemoveEvent);
         btnDate_s = (Button) view.findViewById(R.id.btn_date_s);
         btnTime_s  = (Button) view.findViewById(R.id.btn_time_s);
         btnDate_e = (Button) view.findViewById(R.id.btn_date_e);
         btnTime_e  = (Button) view.findViewById(R.id.btn_time_e);

        txtTitle  = (EditText) view.findViewById(R.id.event_title);
        txtDetails  = (EditText) view.findViewById(R.id.event_details);

        txtDate_s  = (EditText) view.findViewById(R.id.in_date_s);
        txtTime_s  = (EditText) view.findViewById(R.id.in_time_s);
        txtDate_e  = (EditText) view.findViewById(R.id.in_date_e);
        txtTime_e  = (EditText) view.findViewById(R.id.in_time_e);
        orgSpin = (Spinner) view.findViewById(R.id.orgSpin);

        txtDate_s.setVisibility(View.VISIBLE);
        txtTime_s.setVisibility(View.VISIBLE);
        txtDate_e.setVisibility(View.VISIBLE);
        txtDate_e.setVisibility(View.VISIBLE);
        Bundle args = getArguments();
        if(args != null){

            userId = args.getInt("userID");
            isEditState = args.getBoolean("isEditState");
            if(!isEditState){
                btnRemove.setText(getResources().getString(R.string.btnRemove));
                btnEdit.setText(getResources().getString(R.string.btnEdit));
            }else {
                btnRemove.setText(getResources().getString(R.string.btnCancel));
                btnEdit.setText(getResources().getString(R.string.btnSave));
            }

            event = (VolEvent) args.getSerializable("eventData");
            if(event!= null){
                fillDataOfEvent();

            }

            orgs = fill_with_data();
            if(orgs.size() > 0){
                for (Organization o: orgs
                     ) {
                    orgsLables.add(o.getName());
                }
            }

            if(orgsLables.size() > 0){

            }

            setListener();
        }

        return view;

    }
    public void onBtnEdit_click(View v){
        //TODO

    }
    private void setTimeStart(int H, int M){
        Calendar c = Calendar.getInstance();
        s_Hour = H;
        s_Minute = M;

        c.set(s_Year, s_Month,s_Day,s_Hour,s_Minute);
        this.start = c.getTime();

    }
    private void setTimeEnd(int H, int M){
        Calendar c = Calendar.getInstance();
        e_Hour = H;
        e_Minute = M;

        c.set(e_Year, e_Month,e_Day,e_Hour,e_Minute);
        this.end = c.getTime();

    }
    private void setDateStart(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        s_Year = year;
        s_Month = monthOfYear;
        s_Day = dayOfMonth;
        c.set(s_Year, s_Month,s_Day,s_Hour,s_Minute);
        this.start = c.getTime();

    }
    private void setDateEnd(int year, int monthOfYear, int dayOfMonth)
    {
        Calendar c = Calendar.getInstance();
        e_Year = year;
        e_Month = monthOfYear;
        e_Day = dayOfMonth;
        c.set(e_Year, e_Month,e_Day,e_Hour,e_Minute);
        this.end = c.getTime();


    }

    private boolean checkDates(){
        if(start == null || end  == null)
            return false;

        if(s_Hour == -1 || e_Hour == -1 || s_Minute == -1 || e_Minute == -1 ){
            utilityClass.getInstance().showToast(R.string.checkDates,new Object[]{});
            return  false;
        }


        if(start.after(end)){

            utilityClass.getInstance().showToast(R.string.checkDates,new Object[]{});
            return false;
        }
        else{

            return  true;
        }

    }
    private void setListener() {

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(!isEditState){
                    // on read mode - switch to edit mode
                    btnRemove.setText(getResources().getString(R.string.btnCancel));
                    btnEdit.setText(getResources().getString(R.string.btnSave));
                    isEditState = true;
                    SetFieldsState(isEditState);
                }else {
                    // on edit mode save changes and switch to read mode
                    if(!checkDates())
                        return;

                    btnRemove.setText(getResources().getString(R.string.btnRemove));
                    btnEdit.setText(getResources().getString(R.string.btnEdit));
                    isEditState = false;
                    SetFieldsState(isEditState);
                }
            }

        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(!isEditState){
                    // on read mode - remove item

                }else {
                    // on edit mode - cancel changes
                    btnRemove.setText(getResources().getString(R.string.btnRemove));
                    btnEdit.setText(getResources().getString(R.string.btnEdit));
                    isEditState = false;
                    SetFieldsState(isEditState);
                }
            }

        });
        btnDate_s.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                sYear = c.get(Calendar.YEAR);
                sMonth = c.get(Calendar.MONTH);
                sDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                setDateStart( year, monthOfYear,  dayOfMonth);
                                txtDate_s.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, sYear, sMonth, sDay);
                datePickerDialog.show();
            }

        });
        btnTime_s.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                sHour = c.get(Calendar.HOUR_OF_DAY);
                sMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                txtTime_s.setText(hourOfDay + ":" + ((minute == 0) ? "00" : minute));
                                setTimeStart(hourOfDay, minute);

                            }
                        }, sHour, sMinute, true);
                timePickerDialog.show();
            }

        });
        btnDate_e.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                eYear = c.get(Calendar.YEAR);
                eMonth = c.get(Calendar.MONTH);
                eDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtDate_e.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                setDateEnd( year, monthOfYear,  dayOfMonth);


                            }
                        }, eYear, eMonth, eDay);
                datePickerDialog.show();
            }

        });
        btnTime_e.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                eHour = c.get(Calendar.HOUR_OF_DAY);
                eMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                txtTime_e.setText(hourOfDay + ":" + ((minute == 0) ? "00" : minute));
                                setTimeEnd(hourOfDay, minute);

                            }
                        }, eHour, eMinute, true);
                timePickerDialog.show();

            }

        });
    }


    private void fillDataOfEvent() {
        isEditState = false;
        //fill data
        txtTitle.setText(event.getTitle());
        txtDetails.setText(event.getDetails());
        Calendar cal = Calendar.getInstance();
        cal.setTime(event.getStartTime());

        String time = cal.get((Calendar.HOUR_OF_DAY)) + ":"+ cal.get((Calendar.MINUTE)) + ":"+ cal.get((Calendar.SECOND)) ;
        txtTime_s.setText(time);
        cal.setTime(event.getEndTime());
        time = cal.get((Calendar.HOUR_OF_DAY)) + ":"+ cal.get((Calendar.MINUTE)) + ":"+ cal.get((Calendar.SECOND)) ;
        txtTime_e.setText(time);

        txtDate_e.setText(utilityClass.getInstance().getSortStringFromDateTime(event.getEndTime()));
        txtDate_s.setText(utilityClass.getInstance().getSortStringFromDateTime(event.getStartTime()));

        //disable
        SetFieldsState(isEditState);
    }

    public void SetFieldsState(boolean state){

        txtTitle.setEnabled(state);
        txtDetails.setEnabled(state);
        btnTime_s.setEnabled(state);
        btnTime_e.setEnabled(state);
        btnDate_s.setEnabled(state);
        btnDate_e.setEnabled(state);
        orgSpin.setEnabled(state);

    }

    public List<Organization> fill_with_data() {
        List<Organization> data = new ArrayList<>();
        if(userId > 0){
            List<Integer> orgsIds = ManagerDB.getInstance().getOrgIdsOfVol(userId);
            for (Integer i :orgsIds) {
                Organization o = ManagerDB.getInstance().readOrganization(i);
                data.add(o);
            }
        }
        return data;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {

        }
    }

    public interface OnEventInteractionListener {
        // TODO: Update argument type and name
        void onEventCreated(Bundle bundle);
    }
}
