package controller.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caldroidsample.R;

import controller.activities.MainActivity;
import model.ManagerDB;
import model.Organization;
import model.UserType;
import static com.caldroidsample.R.drawable.profile;
import static com.caldroidsample.R.id.fab;

/**
 * Created by Matan on 11/02/2017.
 */

public class OrgProfileFragment extends Fragment {

    private int orgId;
    AppCompatActivity act = null;
    UserType type;
    public OrgProfileFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_org_profile, container, false);

        if(getActivity() instanceof  MainActivity){
            act = (MainActivity)getActivity();
            type = ((MainActivity) act).getUserType();
            if(type.equals(UserType.orgType)){
                ((MainActivity) act).toggleFab(true ,android.R.drawable.ic_menu_edit );
               /* View.OnClickListener a =   new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //if the Fragment is OrgProfileFragment
                        //Edit profile
                        setClick();
                        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //        .setAction("Action", null).show();
                    }
                };

                ((MainActivity) act).toggleFabListiner(a);
*/
            }
            else
            ((MainActivity) act).toggleFab(false ,android.R.drawable.ic_menu_edit );
        }
        /*((MainActivity)act).getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //event listener
                // able to edit all elements
            }
        });*/


                //get orgId from intent
        Bundle args = getArguments();
        if(args != null) {

            orgId = getArguments().getInt("orgID");
        }

        //act.setSupportActionBar((Toolbar) v.findViewById(R.id.toolbar));
        //act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);

        // get all elements from XML
        TextView emailVal = (TextView) v.findViewById(R.id.email);
        TextView addressVal = (TextView) v.findViewById(R.id.address);
        ImageView pic= (ImageView) v.findViewById(R.id.image);
        //get organization fields
        Organization org= ManagerDB.getInstance().readOrganization(orgId);
        // set org details
        if( org!=null) {
            collapsingToolbar.setTitle(org.getName());
            emailVal.setText(org.getEmail());
            addressVal.setText(org.getAddress()+ org.getEmail());
            pic.setImageBitmap(org.getProfilePic());
        }
        return v;

    }

    private void setClick() {

    }

}


