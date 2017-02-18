package controller.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.ManagerDB;
import model.Organization;
import model.VolEvent;
import utils.utilityClass;

import static java.lang.Character.UnicodeBlock.of;

public class EventFragment extends DialogFragment {

    EditText txtDate_s, txtTime_s,txtDate_e, txtTime_e, txtTitle, txtDetails;
    List<Organization> orgs = new ArrayList<>();
    List<String> orgsLables = new ArrayList<>();
    //initial number to the popup
    private int sYear, sMonth, sDay, sHour, sMinute;
    private int eYear, eMonth, eDay, eHour, eMinute;
    private int selectedPos= -1;
    //the numbers that will create the user selected dates
    private int s_Year, s_Month, s_Day, s_Hour = -1, s_Minute = -1; // start time and date values
    private int e_Year, e_Month, e_Day, e_Hour = -1, e_Minute= -1; // end time and date values
    final private int ACTION_NEW = 1 ;
    final private int ACTION_UPDATE = 2 ;
    final private int ACTION_REMOVE = 3 ;
    EventFragment dialogFrag;
    AlertDialog alert;
    ArrayAdapter<String> adapter;
    int userId;
    VolEvent event;
    Boolean isNew , isEditState = false;
    Date start, end;
    View view;
    private Spinner orgSpin;
    Button btnEdit,btnRemove,btnDate_s,btnTime_s,btnDate_e,btnTime_e;
    Activity parentAct = null;
    private View orgLayout;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //region Inflate the layout for this fragment

         parentAct = getActivity();
         alert = AskOption();
         view = inflater.inflate(R.layout.event_fragment, container, false);
         orgLayout = view.findViewById(R.id.OrgLayout);
         btnEdit  = (Button) view.findViewById(R.id.btnEditEvent);
         btnRemove  = (Button) view.findViewById(R.id.btnRemoveEvent);
         btnDate_s = (Button) view.findViewById(R.id.btn_date_s);
         btnTime_s  = (Button) view.findViewById(R.id.btn_time_s);
         btnDate_e = (Button) view.findViewById(R.id.btn_date_e);
         btnTime_e  = (Button) view.findViewById(R.id.btn_time_e);
         dialogFrag = this;
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
        //endregion

        Bundle args = getArguments();

        //region check params and set dialogFrag state and values for controls
        if(args != null){

            userId = args.getInt("userID");
            isEditState = args.getBoolean("isEditState");
            isNew = args.getBoolean("isNew");

            //set buttons on start
            if(!isEditState){
                btnRemove.setText(getResources().getString(R.string.btnRemove));
                btnEdit.setText(getResources().getString(R.string.btnEdit));
            }else {
                btnRemove.setText(getResources().getString(R.string.btnCancel));
                btnEdit.setText(getResources().getString(R.string.btnSave));
            }

            if(!isNew){
                //existing event
                //get data from db
                orgLayout.setVisibility(View.GONE);
                int eventId = args.getInt("currentEventID");
                if(eventId > 0){
                    event = ManagerDB.getInstance().readEvent(eventId);
                    if(event!= null){
                        fillDataOfEvent();

                    }
                }
            }else{
                //new event
                orgLayout.setVisibility(View.VISIBLE);
                orgs = fill_with_data();
                if(orgs.size() > 0){
                    for (Organization o: orgs ) {
                        orgsLables.add(o.getName());
                    }
                }

                if(orgsLables.size() > 0){

                    adapter =new ArrayAdapter<>( getActivity(), android.R.layout.simple_list_item_1,orgsLables);
                    orgSpin.setAdapter(adapter);
                    orgSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        public void onItemSelected(AdapterView<?> parentView,
                                                   View selectedItemView, int position, long id) {
                            int pos = orgSpin.getSelectedItemPosition();
                            setSelectedPos(pos);

                        }

                        public void onNothingSelected(AdapterView<?> arg0) {// do nothing
                        }

                    });
                }
            }

            setListener();
        }
        //endregion

        return view;

    }

    /**
     * set time for start
     */
    private void setTimeStart(int H, int M){
        Calendar c = Calendar.getInstance();
        s_Hour = H;
        s_Minute = M;

        c.set(s_Year, s_Month,s_Day,s_Hour,s_Minute);
        this.start = c.getTime();

    }
    /**
     * set time for end
     */
    private void setTimeEnd(int H, int M){
        Calendar c = Calendar.getInstance();
        e_Hour = H;
        e_Minute = M;

        c.set(e_Year, e_Month,e_Day,e_Hour,e_Minute);
        this.end = c.getTime();

    }
    /**
     * set date for start
     */
    private void setDateStart(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        s_Year = year;
        s_Month = monthOfYear;
        s_Day = dayOfMonth;
        c.set(s_Year, s_Month,s_Day,s_Hour,s_Minute);
        this.start = c.getTime();

    }
    /**
     * set date for end
     */
    private void setDateEnd(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        e_Year = year;
        e_Month = monthOfYear;
        e_Day = dayOfMonth;
        c.set(e_Year, e_Month,e_Day,e_Hour,e_Minute);
        this.end = c.getTime();


    }

    /**
     * check if dates and time are valid
     * @return return boolean - true if ok , false otherwise
     */
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

    private void delete(){
        int effectedRows = ManagerDB.getInstance().deleteEvent(event);
        if(effectedRows > 0){

            notifyActivity(0L, 3);
            dialogFrag.dismiss();
            utilityClass.getInstance().showToast(R.string.successOnDelete,new Object[]{});
        }else{
            utilityClass.getInstance().showToast(R.string.errorOnDelete,new Object[]{});
        }
    }
    /**
     * set listeners for controls in dialogFrag
     */
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
                    //check if dates are ok
                    if(!checkDates()){
                        utilityClass.getInstance().showToast(R.string.checkDates,new Object[]{});
                        return;
                    }

                    if(isNew){
                        //save event and then notify calendar activity
                        VolEvent newEvent = new VolEvent();
                        newEvent.setVolID(userId);
                        Organization o = orgs.get(selectedPos);
                        newEvent.setOrgID(o.getId());
                        newEvent.setStartTime(start);
                        newEvent.setEndTime(end);
                        newEvent.setDetails(txtDetails.getText().toString());
                        newEvent.setTitle(txtTitle.getText().toString());

                        Long newId = ManagerDB.getInstance().addEvent(newEvent);
                        if(newId > 0){

                            // change dialogFrag state
                            btnRemove.setText(getResources().getString(R.string.btnRemove));
                            btnEdit.setText(getResources().getString(R.string.btnEdit));
                            isEditState = false;
                            SetFieldsState(isEditState);

                            //event saved - go notify
                            notifyActivity(newId,1);
                            utilityClass.getInstance().showToast(R.string.successOnSave,new Object[]{});
                            dialogFrag.dismiss();

                        }
                        else{
                            //notify on error
                            utilityClass.getInstance().showToast(R.string.errorOnSave,new Object[]{});
                        }
                    }else{
                        //update event

                        event.setVolID(userId);
                        event.setStartTime(start);
                        event.setEndTime(end);
                        event.setDetails(txtDetails.getText().toString());
                        event.setTitle(txtTitle.getText().toString());

                        int effectedRows = ManagerDB.getInstance().updateEvent(event);
                        if(effectedRows > 0){
                            // change dialogFrag state
                            btnRemove.setText(getResources().getString(R.string.btnRemove));
                            btnEdit.setText(getResources().getString(R.string.btnEdit));
                            isEditState = false;
                            Long tempId = new Long(event.getVolEventID());
                            SetFieldsState(isEditState);
                            notifyActivity(tempId,2);
                            dialogFrag.dismiss();
                            utilityClass.getInstance().showToast(R.string.successOnSave,new Object[]{});
                        }
                        else{
                            utilityClass.getInstance().showToast(R.string.errorOnSave,new Object[]{});
                        }
                    }
                }
            }

        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(!isEditState){
                    // on read mode - remove item
                    alert.show();
                }else {
                    // on edit mode
                    if(isNew){
                        //discard new changes and close popup
                        isEditState = false;
                        dialogFrag.dismiss();
                    }
                    else{
                        //discard changes and reset to old values
                        btnRemove.setText(getResources().getString(R.string.btnRemove));
                        btnEdit.setText(getResources().getString(R.string.btnEdit));
                        isEditState = false;
                        SetFieldsState(isEditState);
                        if(event != null){
                            fillDataOfEvent();
                        }
                    }
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
                                txtTime_s.setText(hourOfDay + ":" + ((minute < 10) ? "0" + minute : minute));
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
                                txtTime_e.setText(hourOfDay + ":" + ((minute < 10) ? "0" + minute : minute));
                                setTimeEnd(hourOfDay, minute);

                            }
                        }, eHour, eMinute, true);
                timePickerDialog.show();

            }

        });

        if(orgsLables.size() > 0){
            orgSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> parentView,
                                           View selectedItemView, int position, long id) {
                    int pos = orgSpin.getSelectedItemPosition();
                    setSelectedPos(pos);

                }

                public void onNothingSelected(AdapterView<?> arg0) {// do nothing
                }

            });
        }
    }

    /**
     * send result of insertion to calender activity ro process the result
     * @param id new event id
     */
    private void notifyActivity(Long id, int action) {

        OnEventInteractionListener mListener = null;

        if(parentAct != null && parentAct  instanceof  OnEventInteractionListener ){
            mListener = (OnEventInteractionListener) parentAct;
            Bundle args = new Bundle();
            args.putLong("currentEventID", id);
            args.putInt("action", action);
            mListener.onEventCreated(args);

        }
    }

    /**
     * fill event data in dialogFrag
     */
    private void fillDataOfEvent() {

        isEditState = false;
        //fill data
        txtTitle.setText(event.getTitle());
        txtDetails.setText(event.getDetails());
        Calendar cal = Calendar.getInstance();

        cal.setTime(event.getStartTime());
        String time = cal.get((Calendar.HOUR_OF_DAY)) + ":"+ cal.get((Calendar.MINUTE)) + ":"+ cal.get((Calendar.SECOND)) ;

        txtTime_s.setText(time);
        setDateStart(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
        setTimeStart(cal.get((Calendar.HOUR_OF_DAY)),cal.get((Calendar.MINUTE)));

        cal.setTime(event.getEndTime());
        time = cal.get((Calendar.HOUR_OF_DAY)) + ":"+ cal.get((Calendar.MINUTE)) + ":"+ cal.get((Calendar.SECOND)) ;
        txtTime_e.setText(time);
        setDateEnd(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
        setTimeEnd(cal.get((Calendar.HOUR_OF_DAY)),cal.get((Calendar.MINUTE)));
        txtDate_e.setText(utilityClass.getInstance().getSortStringFromDateTime(event.getEndTime()));
        txtDate_s.setText(utilityClass.getInstance().getSortStringFromDateTime(event.getStartTime()));

        //disable
        SetFieldsState(isEditState);


    }

    /**
     * set state of fields in dialogFrag
     * @param state - true : enable, false : disable
     */
    public void SetFieldsState(boolean state){

        txtTitle.setEnabled(state);
        txtDetails.setEnabled(state);
        btnTime_s.setEnabled(state);
        btnTime_e.setEnabled(state);
        btnDate_s.setEnabled(state);
        btnDate_e.setEnabled(state);
        orgSpin.setEnabled(state);

    }

    /**
     * get object data from DB
     * @return
     */
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

    /**
     * savwe selected position of spinner
     * @param selectedPos
     */
    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    /**
     * interface for interaction with parent activity
     */
    public interface OnEventInteractionListener {
        // TODO: Update argument type and name

        /**
         * on new event creation
         * @param bundle args to send to activity.
         */
        void onEventCreated(Bundle bundle);
    }

    /**
     * This function construct the alert AlertDialog
     * @return {@link AlertDialog} an alert AlertDialog
     */
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle(R.string.alertPopTitle)
                .setMessage(R.string.alertPopMsg)
                .setIcon(R.drawable.attention_48)
                .setPositiveButton(R.string.alertLblPositive, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //delete event
                        delete();
                    }

                })
                .setNegativeButton(R.string.alertLblNegative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
