package controller.adapters;

/**
 * Created by Matan on 28/01/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.data;
import static android.R.id.list;

/**
 * Created by Plumillon Forge.
 */
public abstract class GenericRecyclerViewAdapter<T> extends RecyclerView.Adapter<GenericRecyclerViewAdapter.ViewHolder> {
    private List<T> mList = Collections.emptyList();
    private Context mContext;
    private OnViewHolderClick mListener;

    /**
     * interface to implements click on Adapter items
     */
    public interface OnViewHolderClick {
        void onClick(View view, int position);
    }

    /**
     * build the view for the object data
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Map<Integer, View> mMapView;
        private OnViewHolderClick mListener;

        public ViewHolder(View view, OnViewHolderClick listener) {
            super(view);
            mMapView = new HashMap<>();
            mMapView.put(0, view);
            mListener = listener;

            if (mListener != null)
                view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null)
                mListener.onClick(view, getAdapterPosition());
        }
// initialize view List
        public void initViewList(int[] idList) {
            for (int id : idList)
                initViewById(id);
        }
// initialize view by Id
        public void initViewById(int id) {
            View view = (getView() != null ? getView().findViewById(id) : null);

            if (view != null)
                mMapView.put(id, view);
        }

        public View getView() {
            return getView(0);
        }

        public View getView(int id) {
            if (mMapView.containsKey(id))
                return mMapView.get(id);
            else
                initViewById(id);

    return mMapView.get(id);
}
}

    /**
     * create data to the view holder
     * @param context
     * @param viewGroup
     * @param viewType
     * @return
     */
    protected abstract View createView(Context context, ViewGroup viewGroup, int viewType);

    /**
     * bind the data to the view holder
     * @param item
     * @param viewHolder
     * @param position
     */
    protected abstract void bindView(T item, ViewHolder viewHolder,int position);

    public GenericRecyclerViewAdapter(Context context) {
        this(context, null);
    }

    public GenericRecyclerViewAdapter(Context context, OnViewHolderClick listener) {
        super();
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(createView(mContext, viewGroup, viewType), mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        bindView(getItem(position), viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public T getItem(int index) {
        return ((mList != null && index < mList.size()) ? mList.get(index) : null);
    }

    public Context getContext() {
        return mContext;
    }

    public void setList(List<T> list) {
        mList = list;
    }

    public List<T> getList() {
        return mList;
    }

    public void setClickListener(OnViewHolderClick listener) {
        mListener = listener;
    }

/*    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }*/

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, T item) {
        mList.add(position, item);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(T item) {
        int position = mList.indexOf(item);
        mList.remove(position);
        notifyItemRemoved(position);
    }
}
