package controller.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caldroidsample.R;

import model.ManagerDB;
import model.Volunteer;
import utils.RoundedImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private int userID = 0;
    private Volunteer vol;
    public ProfileFragment() {
        // Required empty public constructor
    }

    private void fillUserData(View v){

        Bundle args = getArguments();
        if(args != null) {
            userID = args.getInt("volID");
            this.vol = ManagerDB.getInstance().readVolunteer(this.userID);
            if(this.vol != null){

                // Set Collapsing Toolbar layout to the screen
                CollapsingToolbarLayout collapsingToolbar =
                        (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
                collapsingToolbar.setTitle(vol.getfName() +  " " + vol.getlName());

                if(vol.getProfilePic() != null){
                    Bitmap roundBitmap = RoundedImageView.getCroppedBitmap(vol.getProfilePic(),300);
                    ImageView iamge = (ImageView) v.findViewById(R.id.image);
                    iamge.setImageBitmap(roundBitmap);
                }

                TextView user_address = (TextView) v.findViewById(R.id.user_address);
                user_address.setText(vol.getAddress());

                TextView user_email = (TextView) v.findViewById(R.id.user_email);
                user_email.setText(vol.getEmail());


                //set imgage of profile

            }

        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatActivity act = (AppCompatActivity)getActivity();
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        //act.setSupportActionBar((Toolbar) v.findViewById(R.id.toolbar));
        //act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillUserData(v);

        return v;

    }

}
