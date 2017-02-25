package controller.fragments;


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
public class AdFragment extends Fragment {

    //image of ad in bottom of fragment
    ImageView imgAd;
    private static final String TAG_AD_IMG_ID = "img_id";
    public AdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad, container, false);
        imgAd = (ImageView) view.findViewById(R.id.img_ad);

        return view;

    }

    /**
     *Switch images of ads
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            imgAd.setImageResource(bundle.getInt(TAG_AD_IMG_ID));
        }
    }

}
