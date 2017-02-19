package controller.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.caldroidsample.R;

import controller.activities.MainActivity;
import model.ManagerDB;
import model.Organization;
import model.UserType;
import utils.utilityClass;

import static com.caldroidsample.R.drawable.profile;
import static com.caldroidsample.R.id.fab;

/**
 * Created by Matan on 11/02/2017.
 */

public class OrgProfileFragment extends Fragment {

    private int orgId;
    AppCompatActivity act = null;
    Organization thisOrg;
    UserType type;
    EditText emailVal ;
    EditText addressVal;
    ImageView pic;
    EditText password ;
    TextView lblPass ;
    Boolean flag ; //initialized with edit state
    public OrgProfileFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.fragment_org_profile, container, false);

        // get all elements from XML
        emailVal = (EditText) v.findViewById(R.id.email);
        addressVal = (EditText) v.findViewById(R.id.address);
        pic= (ImageView) v.findViewById(R.id.image);
        password =(EditText) v.findViewById(R.id.password);
        lblPass = (TextView)  v.findViewById(R.id.lblPass);
        flag=false;
            if(getActivity() instanceof  MainActivity) {
                act = (MainActivity) getActivity();
                type = ((MainActivity) act).getUserType();
                if (type.equals(UserType.orgType)) {
                }
                int volId = 0;
                Bundle args = getArguments();
                if (args != null) {
                    orgId = getArguments().getInt("orgID");
                    //the other argument
                    volId = getArguments().getInt("volID", -1);
                }
                if (volId == -1) {// volunteer wasn't send so the user is organization

                    ((MainActivity) act).toggleFab(true, android.R.drawable.ic_menu_edit);


                    View.OnClickListener a = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!flag) {
                                //first Click Or edit state
                                password.setVisibility(View.VISIBLE);
                                lblPass.setVisibility(View.VISIBLE);
                                password.setText(thisOrg.getPassword());
                                emailVal.setEnabled(true);
                                addressVal.setEnabled(true);
                                pic.setEnabled(true);
                                password.setEnabled(true);
                                lblPass.setEnabled(true);
                                ((MainActivity) act).toggleFab(true, android.R.drawable.ic_menu_save);//show add button
                                flag = true;

                            } else if (flag) {                                //save mode
                                if (checkFields()) {
                                    // update the org
                                    thisOrg.setAddress(addressVal.getText().toString());
                                    thisOrg.setEmail(emailVal.getText().toString());
                                    thisOrg.setPassword(password.getText().toString());
                                    // TODO thisOrg.setProfilePic();
                                    long r = ManagerDB.getInstance().updateOrg(thisOrg);
                                    if (r > 0) {
                                        flag = false;
                                        utilityClass.getInstance().showToast(R.string.successOnSave, new Object[]{});
                                        ((MainActivity) act).toggleFab(true, android.R.drawable.ic_menu_edit);

                                        password.setVisibility(View.GONE);
                                        lblPass.setVisibility(View.GONE);
                                        emailVal.setEnabled(false);
                                        addressVal.setEnabled(false);
                                        pic.setEnabled(false);
                                        password.setEnabled(false);
                                    } else
                                        utilityClass.getInstance().showToast(R.string.errorOnSave, new Object[]{});
                                }


                            }

                            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            //        .setAction("Action", null).show();
                        }
                    };

                    ((MainActivity) act).toggleFabListiner(a);

                }


                // the user is volunteer
                else
                    ((MainActivity) act).toggleFab(false, android.R.drawable.ic_menu_edit);

                //get orgId from intent


                //act.setSupportActionBar((Toolbar) v.findViewById(R.id.toolbar));
                //act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                // Set Collapsing Toolbar layout to the screen
                CollapsingToolbarLayout collapsingToolbar =
                        (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);

                //Enabled false
                emailVal.setEnabled(false);
                addressVal.setEnabled(false);
                pic.setEnabled(false);
                password.setEnabled(false);
                lblPass.setEnabled(false);
                password.setVisibility(View.GONE);
                lblPass.setVisibility(View.GONE);
                //get organization fields
                thisOrg = ManagerDB.getInstance().readOrganization(orgId);
                // set org details
                if (thisOrg != null) {
                    collapsingToolbar.setTitle(thisOrg.getName());
                    emailVal.setText(thisOrg.getEmail());
                    addressVal.setText(thisOrg.getAddress());
                    pic.setImageBitmap(thisOrg.getProfilePic());
                }
            }
        return v;

    }

    /**
     * check if all field are not empty
     * @return
     */
    private boolean checkFields() {
       boolean check=true;
        if (password.getText().toString()=="") {
            password.setError(getString(R.string.error_field_required));
            check=false;

        }
        if (emailVal.getText().toString()=="") {
            emailVal.setError(getString(R.string.error_invalid_password));
            check=false;
        }
        if (addressVal.getText().toString()=="") {
            emailVal.setError(getString(R.string.error_invalid_password));
            check=false;
        }
        return check;
    }


}


