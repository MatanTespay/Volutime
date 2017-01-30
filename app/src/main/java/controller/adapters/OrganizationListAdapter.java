package controller.adapters;

/**
 * Created by Matan on 28/01/2017.
 */

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.caldroidsample.R;

import java.util.List;

import model.Organization;
import utils.RoundedImageView;
import utils.utilityClass;

/**
 * Created by Plumillon Forge.
 */
public class OrganizationListAdapter extends GenericRecyclerViewAdapter<Organization> {

    public OrganizationListAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);

        /**
         * when clicking on item in recycler
         */
        OnViewHolderClick handler = new OnViewHolderClick() {
            @Override
            public void onClick(View view, int position) {
                if(getList() != null) {
                    Organization org = getList().get(position);
                    String name = org.getName();
                    remove(org);
                    utilityClass.getInstance().showToast(R.string.item_clicked, new Object[]{name});
                }
            }
        };

        setClickListener(handler);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.org_row_layout, viewGroup, false);

        return view;
    }

    @Override
    protected void bindView(Organization item, GenericRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        if (item != null) {
             TextView name = (TextView) viewHolder.getView(R.id.org_title);
             TextView address = (TextView) viewHolder.getView(R.id.org_description);
             ImageView image = (ImageView) viewHolder.getView(R.id.org_image);
             name.setText(item.getName());

            Bitmap icon = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.no_pic);
            if(item.getProfilePic() != null){
                icon = item.getProfilePic();
            }

            if(icon != null){
                Bitmap rouderBitmap = RoundedImageView.getCroppedBitmap(icon,300);
                image.setImageBitmap(rouderBitmap);
            }
            // image.setImageBitmap(item.getProfilePic());
             address.setText(item.getAddress());
             LinearLayout.LayoutParams normalLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            viewHolder.itemView.setLayoutParams(normalLayoutParams);



        }
    }


}
