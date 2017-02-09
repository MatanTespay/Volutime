package controller.fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.caldroidsample.R;

//import android.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import controller.activities.MainActivity;
import model.ManagerDB;
import model.Organization;
import model.VolEvent;
import utils.utilityClass;

import static android.R.attr.id;
import static com.caldroidsample.R.string.btnCancel;
import static com.caldroidsample.R.string.btnEdit;

public class EventFragment extends DialogFragment {

    EditText txtDate_s, txtTime_s,txtDate_e, txtTime_e, txtTitle, txtDetails;
    List<Organization> orgs = new ArrayList<>();
    private int sYear, sMonth, sDay, sHour, sMinute;
    private int eYear, eMonth, eDay, eHour, eMinute;
    int userId;
    VolEvent event;
    Boolean isEditState = false;
    View view;
    private Spinner orgSpin;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.event_fragment, container, false);
        Button btnEdit  = (Button) view.findViewById(R.id.btnEditEvent);
        Button btnRemove  = (Button) view.findViewById(R.id.btnRemoveEvent);
        Button btnDate_s = (Button) view.findViewById(R.id.btn_date_s);
        Button btnTime_s  = (Button) view.findViewById(R.id.btn_time_s);
        Button btnDate_e = (Button) view.findViewById(R.id.btn_date_e);
        Button btnTime_e  = (Button) view.findViewById(R.id.btn_time_e);

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
            event = (VolEvent) args.getSerializable("eventData");
            if(event!= null){
                fillDataOfEvent();
            }

            orgs = fill_with_data();
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {

                }

            });
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {

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

                                    txtDate_s.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }
                            }, sYear, sMonth, sDay);
                    datePickerDialog.show();
                }

            });
            btnTime_s.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {

                }

            });
            btnDate_e.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {

                }

            });
            btnTime_e.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {

                }

            });
        }

        return view;

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
        //SetFieldsState(false,R.id.DateTimeLayout_s);
        //SetFieldsState(false,R.id.DateTimeLayout_e);
        SetFieldsState(false,R.id.DetailsLayout);
        SetFieldsState(false,R.id.OrgLayout);
        SetFieldsState(false,R.id.titleLayout);

    }

    public void SetFieldsState(boolean state, int layoutId){

        LinearLayout myLayout = (LinearLayout) view.findViewById(layoutId);
        for ( int i = 0; i < myLayout.getChildCount();  i++ ){
            View view = myLayout.getChildAt(i);
            view.setEnabled(state);
        }
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

}
