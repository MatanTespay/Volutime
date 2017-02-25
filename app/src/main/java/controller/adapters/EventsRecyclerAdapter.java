package controller.adapters;

/**
 * Created by Matan on 28/01/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caldroidsample.R;

import controller.caldroid.CaldroidSampleActivity;
import controller.fragments.EventFragment;
import model.Organization;
import model.VolEvent;
import utils.utilityClass;

/**
 * Created by Plumillon Forge.
 */
public class EventsRecyclerAdapter extends GenericRecyclerViewAdapter<VolEvent> {

    private int selectedPosition=-1;
    public EventsRecyclerAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);

        /**
         * when clicking on item in recycler
         */
        OnViewHolderClick handler = new OnViewHolderClick() {
            @Override
            public void onClick(View view, int position) {
                if(getList() != null) {
                    //show a dialog on item click
                    if(EventsRecyclerAdapter.this.getContext() instanceof  FragmentActivity){
                        //get the FragmentManager from the context (the activity that extends FragmentActivity)
                        FragmentActivity frag = (FragmentActivity)EventsRecyclerAdapter.this.getContext();
                        android.app.FragmentManager fm = frag.getFragmentManager();
                        //create the dialog
                        EventFragment datesDialog = new EventFragment();
                        Bundle args = new Bundle();
                        VolEvent e = getList().get(position);
                        selectedPosition = position;

                        if(EventsRecyclerAdapter.this.getContext() instanceof CaldroidSampleActivity){

                            int userId = ((CaldroidSampleActivity)EventsRecyclerAdapter.this.getContext()).getUserID();
                            args.putInt("currentEventID", e.getVolEventID());
                            args.putInt("userID", userId);
                            args.putBoolean("isEditState", false);
                            args.putBoolean("isNew", false);

                            datesDialog.setArguments(args);
                            datesDialog.show(fm, frag.getResources().getResourceName(R.layout.fragment_event_list));

                        }
                    }
                }
            }
        };

        setClickListener(handler);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.event_recycler_item, viewGroup, false);

        return view;
    }

    /**
     * Shows the details of one event by the position param.
     * @param item
     * @param viewHolder
     * @param position
     */
    @Override
    protected void bindView(VolEvent item, ViewHolder viewHolder, int position) {
        if (item != null) {
             TextView title = (TextView) viewHolder.getView(R.id.title);
             TextView description = (TextView) viewHolder.getView(R.id.description);
             TextView startTime = (TextView) viewHolder.getView(R.id.startTime);
             TextView endTime = (TextView) viewHolder.getView(R.id.endTime);

             title.setText(item.getTitle());
             startTime.setText(utilityClass.getInstance().getStringFromDateTime(item.getStartTime()));
             endTime.setText(utilityClass.getInstance().getStringFromDateTime(item.getEndTime()));
             description.setText(item.getDetails());

            LinearLayout.LayoutParams normalLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            viewHolder.itemView.setLayoutParams(normalLayoutParams);

        }
    }

    /**
     * removes the event by it's position
     */
    public void removeItem(){
        super.remove(getList().get(selectedPosition));
    }

    public void updateItem(VolEvent item){
        getList().set(selectedPosition, item);
        super.notifyItemChanged(selectedPosition);
    }

    public void addItem(VolEvent newItem){
        super.insert(0, newItem);
       /* getList().add(0, newItem);
        super.notifyDataSetChanged();*/
    }

}
