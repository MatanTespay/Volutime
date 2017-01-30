package controller.adapters;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.caldroidsample.R;

import java.util.List;

import model.Organization;


/**
 *  custom list adapter to populate the list items in RecyclerView object
 * Created by Matan on 24/11/2016.
 */

public class OrganizationAdapter extends RecyclerView.Adapter<ViewHolder>{

    private LayoutInflater mLayoutInflater;
    private List<Organization> items = null;
    private Activity mainActivity = null;
    private static final String FRAG_TAG = "Organization_info_frag";

    /**
     * ctor of the class
     * @param mainActivity the class mainActivity
     * @param items the items to populate in the view
     */
    public OrganizationAdapter(Context mainActivity, List<Organization> items) {
        this.items = items;
        this.mainActivity = (Activity) mainActivity;
        mLayoutInflater = LayoutInflater.from(mainActivity);
    }

    /**
     *
     * @return the size of the list items
     */
    public int getItemCount() {
        return items.size();
    }


    public List<Organization> getItems () {
        return items;
    }

    /**
     * get Organization by it's id
     * @param id
     * @return
     */
    public Organization getOrganizationById(Long id){

        for (Organization p: items) {
            if(p.getId().equals(id))
                return p;
        }
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.org_row_layout, viewGroup, false));
    }

    /**
     * bind each view holder with Organization item in th recycler view
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final Organization item = items.get(position);
        viewHolder.setData(item);

        /**
         * when clicking on item in recycler
         */
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //AnimalDetailsFragment detailsFragment = new AnimalDetailsFragment();
                // send data to fragment
                Organization selectedItem = items.get(position);
                Bundle args = new Bundle();
                args.putLong("item_id",selectedItem.getId());

/*                //open the Organization item dialog fragment
                if(mainActivity instanceof FragmentActivity){
                    FragmentActivity frma = (FragmentActivity) mainActivity;
                    FragmentManager fm = frma.getFragmentManager();
                    OrganizationInfoFrag itemFrag = new OrganizationInfoFrag();
                    itemFrag.setArguments(args);
                    //itemFrag.show(fm,mainActivity.getResources().getResourceName(R.layout.fragment_edit_name ));
                    itemFrag.show(fm, mainActivity.getResources().getResourceName(R.string.dialogOrganizationTag ));

                }*/


            }
        });
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= mainActivity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.org_row_layout, null,true);

        TextView txtName = (TextView) rowView.findViewById(R.id.org_title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.org_image);
        TextView txtAddress = (TextView) rowView.findViewById(R.id.org_description);
        Organization item = items.get(position);
        txtName.setText(item.getName());

        String fileName;

        if(item.getProfilePic() != null)
            imageView.setImageBitmap(item.getProfilePic());


        txtAddress.setText(item.getAddress());

        return rowView;

    }


}

/**
 * object the holds the data for each item in list
 */
class ViewHolder extends RecyclerView.ViewHolder  {
    // Views
    private ImageView image;
    private TextView name;
    private TextView address;

    public ViewHolder(View itemView) {
        super(itemView);

        // Get references to image and name.
        image = (ImageView) itemView.findViewById(R.id.org_image);
        name = (TextView) itemView.findViewById(R.id.org_title);
        address = (TextView) itemView.findViewById(R.id.org_description);
        address.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

    /**
     * set data in the view holder
     * @param item the item with the data to populate
     */
    public void setData(Organization item) {
        image.setImageBitmap(item.getProfilePic());
        name.setText(item.getName());
        address.setText(item.getAddress());
    }

}
