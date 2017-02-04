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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatActivity act = (AppCompatActivity)getActivity();
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        //act.setSupportActionBar((Toolbar) v.findViewById(R.id.toolbar));
        //act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Test the view");

        // get image from dbb insted.
        Bitmap picture = BitmapFactory.decodeResource(this.getResources(), R.drawable.profile);

        TextView placeVal = (TextView) v.findViewById(R.id.place_detail);
        placeVal.setText("Some Some Some Some Some Some Some Some Some Some Some Some Some Some Some ");

        TextView locVal = (TextView) v.findViewById(R.id.place_location);
        locVal.setText("Some Some Some Some Some !!!!");


        //set imgage of profile
        ImageView placePicutre = (ImageView) v.findViewById(R.id.image);
        placePicutre.setImageBitmap(picture);
        return v;

    }

}
