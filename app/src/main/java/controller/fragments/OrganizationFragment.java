package controller.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.caldroidsample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import controller.activities.MainActivity;
import controller.adapters.OrganizationListAdapter;
import model.ManagerDB;
import model.Organization;
import model.UserType;

import static android.R.attr.button;
import static android.R.attr.id;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrganizationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrganizationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganizationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context context;
    public int userID;
    private UserType userType;
    List<Organization> items;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Organization> searchResult;

    public OrganizationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganizationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrganizationFragment newInstance(String param1, String param2) {
        OrganizationFragment fragment = new OrganizationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_organization, container, false);
        context = view.getContext();
        items = fill_with_data();
        Bundle args = getArguments();
        if(args != null) {
            userID = args.getInt("volID");

        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        /*OrganizationAdapter orgAdapter = new OrganizationAdapter(context, items);
        recyclerView.setAdapter(orgAdapter);*/

        OrganizationListAdapter adapter = new OrganizationListAdapter( getActivity(), null);
        adapter.setList(items);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

       //  Set Action listener for click fab button to add organization
        Activity act = this.getActivity();
        if(act instanceof  MainActivity) {
            ((MainActivity) act).getFab().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    openDialogFragment();

                }
            });
        }


        return view;
    }

    /**
     * opens a dialog Fragment for volunteer to add his org.
     * sends as argument userID
     */
    private void openDialogFragment() {
        //open dialog fragment to add org.
        Bundle args = new Bundle();
        android.app.FragmentManager fm = getActivity().getFragmentManager();
        AllOrgsDialogFragment orgsDialog = new AllOrgsDialogFragment();
        args.putInt("volID", this.userID);
        args.putBoolean("isEditState", true);
        args.putBoolean("isNew", true);
        orgsDialog.setArguments(args);
        orgsDialog.show(fm,"orgsDialog");
        // not sending an org id


    }

    /**
     * get data from DB
     */
    private void getData(){
        items = fill_with_data();
        searchResult =   new ArrayList<>();
        searchResult.addAll(items);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle bundle) {
        if (mListener != null) {
            //calling a method on listener - the main activity
            mListener.onFragmentInteraction(bundle);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Bundle bundle);
    }

    public List<Organization> fill_with_data() {
        List<Organization> data = new ArrayList<>();
        MainActivity act  = (MainActivity) getActivity();
        if(act != null){
            int id = act.getVol().getId();
           // int id = 1;
            if(id > 0){
                List<Integer> orgsIds = ManagerDB.getInstance().getOrgIdsOfVol(id);
                for (Integer i :orgsIds) {
                    Organization o = ManagerDB.getInstance().readOrganization(i);
                    data.add(o);
                }
            }
        }

        return data;
    }
}
