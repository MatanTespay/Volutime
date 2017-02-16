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
import com.caldroidsample.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import model.ManagerDB;
import model.Organization;
import utils.utilityClass;



/**
 * Created by Matan on 11/02/2017.
 */

public class AllOrgsDialogFragment extends DialogFragment {
    private int s_Year, s_Month, s_Day; // start date values
    private int e_Year, e_Month, e_Day; // end  date values
    public  int userId ;
    Date start, end;
    View view;
    private int selectedPos=0;
    List<String> orgsLables = new ArrayList<>();
    AllOrgsDialogFragment dialog;
    private Spinner orgSpin;
    Button btnSave,btnRemove, btnDate_s, btnDate_e;
    EditText txtDate_s, txtDate_e;
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
        txtDate_e  = (EditText) view.findViewById(R.id.in_date_e);
        txtDate_s  = (EditText) view.findViewById(R.id.in_date_s);
        orgSpin =(Spinner) view.findViewById(R.id.orgSpin);
        Bundle args = getArguments();
        if(args != null) {
            userId = args.getInt("volID");
            orgs = fill_with_data();
            if (orgs.size() > 0) {
                for (Organization o : orgs) {
                    orgsLables.add(o.getName());
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>( getActivity(), android.R.layout.simple_list_item_1,orgsLables);
                 orgSpin.setAdapter(adapter);
                orgSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> parentView,
                                               View selectedItemView, int position, long id) {
                        // Object item = parentView.getItemAtPosition(position);

                        int pos = orgSpin.getSelectedItemPosition();
                        //int pos = orgSpin.getSelectedItemPosition() + 1;
                        setSelectedPos(pos);

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {// do nothing
                    }

                });
                setListener();
            }

        }

        return view;
    }

    public AllOrgsDialogFragment(){
    }
    public List<Organization> fill_with_data() {
        List<Organization> data = new ArrayList<>();
        if(userId > 0){
            data= ManagerDB.getInstance().getAllOrgs();
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
       //todo

            }

        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                //todo

            }

        });
        btnDate_e.setOnClickListener(new View.OnClickListener() {
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
    private void setDateStart(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        s_Year = year;
        s_Month = monthOfYear;
        s_Day = dayOfMonth;
        c.set(s_Year, s_Month,s_Day);

    }
    private void setDateEnd(int year, int monthOfYear, int dayOfMonth)
    {
        Calendar c = Calendar.getInstance();
        e_Year = year;
        e_Month = monthOfYear;
        e_Day = dayOfMonth;
        c.set(e_Year, e_Month,e_Day);


    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }
}


