package watmok.tacoma.uw.edu.mylogin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import watmok.tacoma.uw.edu.mylogin.HikeFragment.OnListFragmentInteractionListener;

import watmok.tacoma.uw.edu.mylogin.hike.Hike;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Hike} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 *
 */
public class MyHikeRecyclerViewAdapter extends RecyclerView.Adapter<MyHikeRecyclerViewAdapter.ViewHolder> {

    private final List<Hike> mValues;
    private final OnListFragmentInteractionListener mListener;

    /**
     * A constructior for the adapter
     * @param items A list of Hikes to be displayed
     * @param listener An interaction listener in case someone clicks on a Hike.
     *                 Currently does nothing, but will be used in the future to display more info
     *                 on a separate page/fragment.
     */
    public MyHikeRecyclerViewAdapter(List<Hike> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * Creates the view holder class below that displays views of Hikes
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hike, parent, false);
        return new ViewHolder(view);
    }

    /**
     * To bind the view holder to the acual data from a hike, and its onClickListener
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getmHikeName());
        holder.mContentView.setText(mValues.get(position).getmShortDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * Returns the size of the Hike list
     * @return
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * A nested class for creating the actual ViewHolder, extended from RecylerView.ViewHolder
     * without any changes.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Hike mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
