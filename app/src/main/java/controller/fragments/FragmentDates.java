package controller.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caldroidsample.R;

import java.util.HashMap;
import java.util.List;

import controller.adapters.EventsRecyclerAdapter;
import controller.adapters.OrganizationListAdapter;
import model.Organization;
import model.VolEvent;

import static android.R.attr.data;


/**
 * Created by Matan on 08/02/2017.
 */

public class FragmentDates extends DialogFragment {
    private Context context;
    List<VolEvent> items;
    String title = null;
    private OnFragmentInteractionListener mListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dates_fragment, container);
        context = view.getContext();
        if (getArguments() != null) {

            items = (List<VolEvent>) getArguments().getSerializable("events");
            title = getArguments().getString("dateTitle");
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_events);
            TextView titleTextView = (TextView) view.findViewById(R.id.titleOfDay);
            titleTextView.setText(title);
            EventsRecyclerAdapter adapter = new EventsRecyclerAdapter( getActivity(), null);
            adapter.setList(items);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            itemAnimator.setAddDuration(1000);
            itemAnimator.setRemoveDuration(1000);
            recyclerView.setItemAnimator(itemAnimator);

        }

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentDates.OnFragmentInteractionListener) {
            mListener = (FragmentDates.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){

        }


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Bundle bundle);
    }

}
