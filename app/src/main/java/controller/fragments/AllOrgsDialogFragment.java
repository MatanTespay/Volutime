package controller.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.Date;
import java.util.List;

import controller.activities.MainActivity;
import model.ManagerDB;
import model.Organization;
import model.VolAtOrg;
import utils.utilityClass;

import static com.caldroidsample.R.string.btnEdit;


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
    Activity parentAct = null;
    View view;
    AlertDialog alert;
    private int selectedPos;
    List<String> orgsLables = new ArrayList<>();
    AllOrgsDialogFragment dialog;
    private Spinner orgSpin;
    Button btnSave,btnRemove, btnDate_s, btnDate_e , btnShowOrg;
    EditText txtDate_s, txtDate_e;
    boolean isNew = true;
    Boolean isEditState = false;
    List<Organization> orgs = new ArrayList<>();
    private View orgLayout;
    private View nameLayout;
    TextView lblSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.dialog_fragment_all_orgs, container, false);

        dialog = this;
        alert = AskOption() ;
        parentAct = getActivity();
        btnSave = (Button) view.findViewById(R.id.btnSaveOrg);
        btnRemove = (Button) view.findViewById(R.id.btnRemove);
        btnDate_s = (Button) view.findViewById(R.id.btn_date_s);
        btnDate_e = (Button) view.findViewById(R.id.btn_date_e);
        txtOrgName = (TextView) view.findViewById(R.id.orgName);
        txtDate_e  = (EditText) view.findViewById(R.id.in_date_e);
        txtDate_s  = (EditText) view.findViewById(R.id.in_date_s);
        orgSpin =(Spinner) view.findViewById(R.id.orgSpin);
        btnShowOrg = (Button) view.findViewById(R.id.btnShowOrg);
        lblSpinner = (TextView) view.findViewById(R.id.lblOrg);
        orgLayout = view.findViewById(R.id.OrgLayout);
        nameLayout = view.findViewById(R.id.nameLayout);
        Bundle args = getArguments();
       // setListener();
        if(args != null) {
            userId = args.getInt("volID");
            isEditState = args.getBoolean("isEditState");
            isNew = args.getBoolean("isNew");
            orgToShow = args.getInt("OrgID", -1);
            if (!isEditState) {

                // read mode no org to show
                btnRemove.setText(getResources().getString(R.string.btnRemove));
                btnSave.setText(getResources().getString(btnEdit));
                btnDate_e.setEnabled(false);
                btnDate_s.setEnabled(false);
            }
            else {
                //write mode
                btnRemove.setText(getResources().getString(R.string.btnCancel));
                btnSave.setText(getResources().getString(R.string.btnSave));
                btnDate_e.setEnabled(true);
                btnDate_s.setEnabled(true);
            }
            if (!isNew) {
                    //change visibility
                    //object exist in
                    lblSpinner.setVisibility(View.GONE);
                    orgSpin.setVisibility(View.GONE);
                    orgLayout.setVisibility(View.GONE);
                    txtOrgName.setVisibility(View.VISIBLE);
                    nameLayout.setVisibility(View.VISIBLE);
                    btnShowOrg.setVisibility(View.VISIBLE);
                // the name of the org is not updated
                    txtOrgName.setEnabled(false);
                    // get All data from db and show it on the Dialog frag.
                    // get the object and set as a class object
                    fillDataOfVolAtOrg();
            } else {
                //is new
                //edit or add a new org to vol mode
                // add new organization to volunteer
                btnRemove.setText(getResources().getString(R.string.btnCancel));
                btnSave.setText(getResources().getString(R.string.btnSave));
                //show the spinner
                orgSpin.setVisibility(View.VISIBLE);
                lblSpinner.setVisibility(View.VISIBLE);
                //hide the edit
                orgLayout.setVisibility(View.VISIBLE);
                btnShowOrg.setVisibility(View.GONE);
                txtOrgName.setVisibility(View.GONE);
                nameLayout.setVisibility(View.GONE);
                //fill the spinner data
                orgs = getAllOrgs();
                orgs.removeAll(getOrgsOfVol());
                if (orgs.size() > 0) {
                    for (Organization o : orgs) {
                        orgsLables.add(o.getName());
                    }
                    if (orgsLables.size() > 0) {
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
                else{
                    //no orgs to select.
                    // the user dosent have organization - need to add organization in OrganizationFragment
                    utilityClass.getInstance().showToast(R.string.no_orgs_in_sys,1,new Object[]{});

                    btnRemove.setEnabled(true);
                    btnSave.setEnabled(false);
                    btnDate_e.setEnabled(false);
                    btnDate_s.setEnabled(false);

                }
            }



            }
            setListener();

//end on create
        return view;
    }

    /**
     * Dialog constructor
     */
    public AllOrgsDialogFragment(){
    }




    /**
     * gets all organization from db
     * @return list of orgs
     */
    public List<Organization> getAllOrgs() {
        List<Organization> data = new ArrayList<>();
        if(userId > 0){
            data= ManagerDB.getInstance().getAllOrgs();
        }

        return data;
    }

    /**
     * gets all organization connected to volunteer
     * @return list of orgs
     */
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

    /**
     * check that start date is earlier then end date
     * @return true if check is alright and false otherwise
     */
    private boolean checkDates(){
        if(start == null || end  == null)
            return false;

        if(start.after(end)) {
            utilityClass.getInstance().showToast(R.string.checkDates,0, new Object[]{});
            return false;
        }
    return  true;
    }

    /**
     * all listeners in dialog
     */
    private void setListener() {
/**
 * on click te button the vol can see information about his org.
 */
        btnShowOrg.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Bundle args = new Bundle();
                Fragment orgFrag = new OrgProfileFragment()  ;
                // send the two parameters so the orgProfile will know this is vol
                args.putInt("orgID", orgToShow);
                args.putInt("volID",userId );
                orgFrag.setArguments(args);
                //close the dialog
                dialog.dismiss();
                if(parentAct instanceof MainActivity){
                    android.support.v4.app.FragmentTransaction fragmentTransaction = ((MainActivity)parentAct).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, orgFrag, getResources().getString(R.string.org_profile));
                    fragmentTransaction.commit();

                }

            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                if(!isEditState){
                    // on read mode - remove item
                    //  show the alert - the remove will be done in the Ask... method
                    alert.show();

                }else {
                    // on edit mode
                    if(isNew){
                        //discard new changes and close popup
                        isEditState = false;
                        dialog.dismiss();
                    }
                    else{
                        //discard changes and reset to old values
                        btnRemove.setText(getResources().getString(R.string.btnRemove));
                        btnSave.setText(getResources().getString(btnEdit));
                        btnDate_e.setEnabled(false);
                        btnDate_s.setEnabled(false);
                        isEditState = false;
                        if(result != null){
                            fillDataOfVolAtOrg();
                        }
                    }
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //todo

               if (!isEditState) {
                   //read mode , change button from remove and edit
                   //click on edit button
                   btnSave.setText("Save");
                   btnRemove.setText("Cancel");
                   btnDate_s.setEnabled(true);
                   btnDate_e.setEnabled(true);
                   isEditState=true;


               } else {
                   // on edit mode save changes and switch to read mode
                   //check if dates are ok
                   if (!checkDates()) {
                       utilityClass.getInstance().showToast(R.string.checkDates,0, new Object[]{});
                       return;
                   }

                   if (isNew) {
                       Organization selectedOrg = orgs.get(orgSpin.getSelectedItemPosition());
                       if (checkDates()) {
                           String startDate = utilityClass.getInstance().getStringFromDateTime(start);
                           String endDate = utilityClass.getInstance().getStringFromDateTime(end);
                           long r = ManagerDB.getInstance().addOrgToVolunteer(userId, selectedOrg.getId(), startDate, endDate);
                           if (r > 0) {
                               isEditState = false;
                               utilityClass.getInstance().showToast(R.string.successOnSave,0, new Object[]{});
                               dialog.dismiss();
                               notifyActivity();

                           } else {
                               utilityClass.getInstance().showToast(R.string.errorOnSave,0, new Object[]{});
                           }

                       }
                   }else {
                           //update result object from on Create View
                           result.setStartDate(start);
                           result.setEndDate(end);
                           int effectedRows = ManagerDB.getInstance().updateVolAtOrg(result);
                           if (effectedRows > 0) {
                               // change dialogFrag state
                               btnRemove.setText(getResources().getString(R.string.btnRemove));
                               btnSave.setText(getResources().getString(btnEdit));
                               isEditState = false;
                               dialog.dismiss();
                               utilityClass.getInstance().showToast(R.string.successOnSave,0, new Object[]{});

                               notifyActivity();

                           } else {
                               utilityClass.getInstance().showToast(R.string.errorOnSave,0, new Object[]{});
                           }
                   }



               }
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

    /**
     *
     * update the parent
     */
    public interface OnVolAtOrgInteractionListener {
        // TODO: Update argument type and name

        /**
         * on new event creation
         * @param bundle args to send to activity.
         */
        void onVolAtOrgAction(Bundle bundle);
    }

    /**
     * send result of insertion to calender activity ro process the result
     */
    private void notifyActivity() {

        OnVolAtOrgInteractionListener mListener = null;

        if(parentAct != null && parentAct  instanceof  OnVolAtOrgInteractionListener ){
            mListener = (OnVolAtOrgInteractionListener) parentAct;
            Bundle args = new Bundle();

            mListener.onVolAtOrgAction(args);

        }
    }
    private void fillDataOfVolAtOrg() {
        result = ManagerDB.getInstance().getVolAtOrg(userId, orgToShow);
        Organization org = ManagerDB.getInstance().readOrganization(result.getOrgID());
        txtOrgName.setText(org.getName());

        if (result != null) {
            start = result.getStartDate();
            end = result.getEndDate();
            txtDate_s.setText(utilityClass.getInstance().getSortStringFromDateTime(result.getStartDate()));
            txtDate_e.setText(utilityClass.getInstance().getSortStringFromDateTime(result.getEndDate()));

        }
    }


    private void removeVolAtOrgObj() {
        if(result!=null){
           int r= ManagerDB.getInstance().deleteVolAtOrg(result);
            if(r!= -1)
            {
                //Toast delete was successfully
                dialog.dismiss();
                notifyActivity();
                utilityClass.getInstance().showToast(R.string.successOnDelete,0,new Object[]{});
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
                        //delete
                        removeVolAtOrgObj();

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


