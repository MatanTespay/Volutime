package controller.fragments;

import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.caldroidsample.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import controller.activities.MainActivity;
import model.ManagerDB;
import model.Organization;
import model.VolAtOrg;
import utils.utilityClass;



/**
 * Created by Matan on 11/02/2017.
 */

public class AllOrgsDialogFragment extends DialogFragment {
    private int s_Year, s_Month, s_Day; // start date values
    private int e_Year, e_Month, e_Day; // end  date values
    public  int userId ;
    TextView txtOrgName;
    int orgToShow = -1;
    Date start, end;
    VolAtOrg result=null;
    View view;
    private int selectedPos;
    List<String> orgsLables = new ArrayList<>();
    AllOrgsDialogFragment dialog;
    private Spinner orgSpin;
    Button btnSave,btnRemove, btnDate_s, btnDate_e;
    EditText txtDate_s, txtDate_e;
    boolean isNew = true;
    Boolean isEditState = false;
    List<Organization> orgs = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.dialog_fragment_all_orgs, container, false);

        dialog = this;
        btnSave = (Button) view.findViewById(R.id.btnSaveOrg);
        btnRemove = (Button) view.findViewById(R.id.btnRemove);
        btnDate_s = (Button) view.findViewById(R.id.btn_date_s);
        btnDate_e = (Button) view.findViewById(R.id.btn_date_e);
        txtOrgName = (TextView) view.findViewById(R.id.orgName);
        txtDate_e  = (EditText) view.findViewById(R.id.in_date_e);
        txtDate_s  = (EditText) view.findViewById(R.id.in_date_s);
        orgSpin =(Spinner) view.findViewById(R.id.orgSpin);

        Bundle args = getArguments();
        setListener();
        if(args != null) {
            userId = args.getInt("volID");
            isEditState = args.getBoolean("isEditState");
            isNew = args.getBoolean("isNew");
            orgToShow = args.getInt("OrgID", -1);
            if (!isEditState) {

                // read mode no org to show
                btnRemove.setText(getResources().getString(R.string.btnRemove));
                btnSave.setText(getResources().getString(R.string.btnEdit));
                btnDate_e.setEnabled(false);
                btnDate_s.setEnabled(false);
            }
            else {
                btnRemove.setText(getResources().getString(R.string.btnCancel));
                btnSave.setText(getResources().getString(R.string.btnSave));
            }

                    //change visibility
                    TextView lblSpinner = (TextView) view.findViewById(R.id.lblOrg);
                    lblSpinner.setVisibility(View.GONE);
                    orgSpin.setVisibility(View.GONE);
                    txtOrgName.setVisibility(View.VISIBLE);
                    // get All data from db and show it on the Dialog frag.

                    // get the object and set as a class object
                    result = ManagerDB.getInstance().getVolAtOrg(userId, orgToShow);
                    Organization org = ManagerDB.getInstance().readOrganization(result.getOrgID());
                    txtOrgName.setText(org.getName());
                    if (result != null) {

                        txtDate_s.setText(utilityClass.getInstance().getSortStringFromDateTime(result.getStartDate()));
                        txtDate_e.setText(utilityClass.getInstance().getSortStringFromDateTime(result.getEndDate()));

                    } else {
                        txtDate_s.setText(" ");
                        txtDate_e.setText(" ");
                    }
                }
            else {
                    //edit or add a new org to vol mode
                    if (isNew) {
                        // add new organization to volunteer
                        btnRemove.setText("Cancel");
                        btnSave.setText("Save");
                        //show the spinner
                        orgSpin.setVisibility(View.VISIBLE);
                        //hide the txtView
                        txtOrgName.setVisibility(View.GONE);
                        //fill the spinner data
                        orgs = getAllOrgs();
                        orgs.removeAll(getOrgsOfVol());

                        if (orgs.size() > 0) {
                            for (Organization o : orgs) {
                                orgsLables.add(o.getName());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, orgsLables);
                            orgSpin.setAdapter(adapter);
                            // spinner contain data
                            orgSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                // set selected org to spinner
                                public void onItemSelected(AdapterView<?> parentView,
                                                           View selectedItemView, int position, long id) {

                                    int pos = orgSpin.getSelectedItemPosition();
                                    setSelectedPos(pos);

                                }

                                public void onNothingSelected(AdapterView<?> arg0) {
                                    //do nothing ,checks later

                                }

                            });

                        }
                    }
                    else {
                        //isNew is false, get data from DB already done
                        orgSpin.setVisibility(View.INVISIBLE);
                        txtOrgName.setVisibility(View.VISIBLE);
                        btnDate_e.setEnabled(true);
                        btnDate_s.setEnabled(true);
                    }
                }



        return view;
    }


    public AllOrgsDialogFragment(){
    }
    public List<Organization> getAllOrgs() {
        List<Organization> data = new ArrayList<>();
        if(userId > 0){
            data= ManagerDB.getInstance().getAllOrgs();
        }

        return data;
    }

    public List<Organization> getOrgsOfVol() {
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

    private boolean checkDates(){
        if(start == null || end  == null)
            return false;

        if(start.after(end)) {
            utilityClass.getInstance().showToast(R.string.checkDates, new Object[]{});
            return false;
        }
    return  true;
    }

    private void setListener() {
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

            if(isNew || isEditState)
            {
                //cancel

                //Alert
            }
            if(!isEditState){
                //remove
                removeVolAtOrgObj();
                //exit from dialog

            }

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
       //todo

                if(isNew) {
                    Organization selectedOrg = orgs.get(orgSpin.getSelectedItemPosition());
                    if (checkDates()) {
                        String startDate = utilityClass.getInstance().getStringFromDateTime(start);
                        String endDate = utilityClass.getInstance().getStringFromDateTime(end);
                        long r = ManagerDB.getInstance().addOrgToVolunteer(userId, selectedOrg.getId(), startDate, endDate);
                        System.out.print(r);

                    } else {
                        utilityClass.getInstance().showToast(R.string.checkDatesOnly, new Object[]{});
                    }
                }

                if(!isEditState){
                    //read mode , means remove and edit
                    //click on edit button
                    btnSave.setText("Save");
                    btnRemove.setText("Cancel");
                    btnDate_s.setEnabled(true);
                    btnDate_e.setEnabled(true);





                    isEditState=false;
                }
                else{
              //  !

                }



            }

        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                //todo

            }

        });
        btnDate_s.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int sYear,sMonth,sDay;
                sYear = c.get(Calendar.YEAR);
                sMonth = c.get(Calendar.MONTH);
                sDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                setDateStart(year, monthOfYear, dayOfMonth);
                                txtDate_s.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, sYear, sMonth, sDay);
                datePickerDialog.show();
            }

        });

        btnDate_e.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int eYear,eMonth,eDay;
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


    }

    private void removeVolAtOrgObj() {
        if(result!=null){
           int r= ManagerDB.getInstance().deleteVolAtOrg(result);
            if(r!= -1)
            {
                //Toast delete was successfull
            }
        }
    }


    private void setDateStart(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        s_Year = year;
        s_Month = monthOfYear;
        s_Day = dayOfMonth;
        c.set(s_Year, s_Month,s_Day);
        this.start = c.getTime();

    }
    private void setDateEnd(int year, int monthOfYear, int dayOfMonth)
    {
        Calendar c = Calendar.getInstance();
        e_Year = year;
        e_Month = monthOfYear;
        e_Day = dayOfMonth;
        c.set(e_Year, e_Month,e_Day);
        this.end = c.getTime();

    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public Spinner getOrgSpin() {
        return orgSpin;
    }
}


