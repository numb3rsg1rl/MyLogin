package watmok.tacoma.uw.edu.mylogin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import watmok.tacoma.uw.edu.mylogin.hike.Hike;

import java.util.List;

/**
 * This class is used for the the Favorites Fragment to show the list of the user selected
 * favorite Trails
 */
public class MyHikeRecyclerViewAdapter2 extends RecyclerView.Adapter<MyHikeRecyclerViewAdapter2.ViewHolder> {

    private final List<Hike> mValues;
    private final FavoritesFragment.OnListFragmentInteractionListener mListener;

    /**
     * A constructor for the adapter
     * @param items A list of Hikes to be displayed
     * @param listener An interaction listener in case someone clicks on a Hike.
     */
    public MyHikeRecyclerViewAdapter2(List<Hike> items, FavoritesFragment.OnListFragmentInteractionListener listener) {
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
    public MyHikeRecyclerViewAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hike, parent, false);
        return new MyHikeRecyclerViewAdapter2.ViewHolder(view);
    }

    /**
     * To bind the view holder to the actual data from a hike, and its onClickListener
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MyHikeRecyclerViewAdapter2.ViewHolder holder, int position) {
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
     * @return the size of the Hike List
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

        /**
         * Handles the view content in the fragment
         * @param view
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mIdView.setTextSize(14);
            mContentView.setTextSize(12);
        }

        /**
         * This method returns the content in String form
         * @return content in String form
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
