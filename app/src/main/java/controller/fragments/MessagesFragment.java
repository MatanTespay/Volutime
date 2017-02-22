package controller.fragments;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caldroidsample.R;

import controller.activities.MainActivity;
import model.UserType;

import static controller.activities.MainActivity.navItemIndex;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {

    public int userID;
    private UserType userType;

    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle args = getArguments();
        if(args != null) {
            userID = args.getInt("volID");
            userType = (UserType) args.get("userType");
        }

        if(this.userType != null){
            if(this.userType.equals(UserType.orgType)) {
                // load orgs data from server
                if(getActivity() instanceof MainActivity){
                    MainActivity activity = ((MainActivity)getActivity());
                    if(activity.isDoOrgGet()){
                        activity.loadDataFromServer(9);
                    }
                }
            }
        }



        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

}
