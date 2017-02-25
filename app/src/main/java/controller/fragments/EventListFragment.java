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

import java.util.List;

import controller.adapters.EventsRecyclerAdapter;
import model.ManagerDB;
import model.VolEvent;


/**
 * Created by Matan on 08/02/2017.
 */

public class EventListFragment extends DialogFragment {
    private Context context;
    List<VolEvent> items;
    String title = null;
    private OnFragmentInteractionListener mListener;
    private EventsRecyclerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container);
        context = view.getContext();
        //ManagerDB.getInstance().resetDB();

        if (getArguments() != null) {
            //get argument of the class
            items = (List<VolEvent>) getArguments().getSerializable("events");
            title = getArguments().getString("dateTitle");
            // get th elements of the XML
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_events);
            TextView titleTextView = (TextView) view.findViewById(R.id.titleOfDay);

            titleTextView.setText(title);
            // create adapter to show  the list
            adapter = new EventsRecyclerAdapter( getActivity(), null);
            adapter.setList(items);
            recyclerView.setAdapter(adapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            //
            itemAnimator.setAddDuration(1000);
            itemAnimator.setRemoveDuration(1000);
            recyclerView.setItemAnimator(itemAnimator);

        }

        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.layout_bg);
        return view;
    }

    /**
     *
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventListFragment.OnFragmentInteractionListener) {
            mListener = (EventListFragment.OnFragmentInteractionListener) context;
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
        void onEventItemSelected(Bundle bundle);
    }

    public EventsRecyclerAdapter getAdapter() {
        return adapter;
    }
}
