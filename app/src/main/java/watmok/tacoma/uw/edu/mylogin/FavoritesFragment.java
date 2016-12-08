package watmok.tacoma.uw.edu.mylogin;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import watmok.tacoma.uw.edu.mylogin.hike.Hike;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Favorites Fragment Displays a list of trails that the user has defined as their
 * favorite via the Switch in the Hike Detail Activity
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FavoritesFragment extends Fragment {

    /**
     * Fields used to identify the column count and webservice URL for objects calling this fragment.
     */
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String HIKES_URL = "http://cssgate.insttech.washington.edu/~debergma/hikes.php?cmd=hikes";

    /**
     * Variable to keep track of column count
     */
    private int mColumnCount = 1;
    /**
     * Listener for interaction with the fragment, taken from the Activity that contains it.
     */
    private FavoritesFragment.OnListFragmentInteractionListener mListener;
    /**
     * the RecyclerView that will be used to display Hikes inside this fragment
     */
    private RecyclerView mRecyclerView;

    FavoritesDataBaseAdapter favDataBaseAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoritesFragment() {
    }

    /**
     * for creating a HikeFragment instance. Not used.
     * @param columnCount the number of columns in the list
     * @return a new HikeFragment
     */
    public static FavoritesFragment newInstance(int columnCount) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * Standard onCreate method
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    /**
     * Inflates the fragment, using the RecyclerView.
     * Then starts the AsyncTask that will retrieve the Hike data from the web service.
     *
     * @param inflater inflates the Fragment Container
     * @param container
     * @param savedInstanceState
     * @return show the Favorites Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hike_list2, container, false);
        favDataBaseAdapter = new FavoritesDataBaseAdapter(getActivity());
        favDataBaseAdapter = favDataBaseAdapter.open();
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            FavoritesFragment.DownloadHikesTask task = new FavoritesFragment.DownloadHikesTask();
            task.execute(HIKES_URL);
        }
        return view;
    }


    /**
     * Standard onAttach for attaching to the Activity's context
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FavoritesFragment.OnListFragmentInteractionListener) {
            mListener = (FavoritesFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    /**
     * Standard onDetach, kills the InteractionListener
     */
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(Hike hike);
    }

    /**
     * A nested AsyncTask class that performs the actual business of connecting to the web service.
     */
    private class DownloadHikesTask extends AsyncTask<String, Void, String> {

        /**
         * Uses the URL(s) for the webservice to check for service and connect to the Hike database
         * @param urls the url(s) for the web service
         * @return a String with a JSON message, if successful, or an error message if something
         * went wrong.
         */
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            StringBuilder builder = new StringBuilder();
            HttpURLConnection urlConnection = null;

            for (String url:urls) {
                try {

                    URL urlObject = new URL(url);
                    urlConnection  = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s=reader.readLine())!=null) {
                        builder.append(s);
                    }
                    response = builder.toString();

                } catch (Exception e) {
                    response = "Unable to download the Hike list. Reason: " + e.getMessage();
                }
            }

            return response;
        }

        /**
         * Makes a toast if there was an error message to display it.
         * Otherwise, calls the Hike class parseHikeJSON() method to fill the Hike list, and
         * then passes that list with the HikeFragments mListener to the RecycleViewAdapter.
         * @param result
         */
        protected void onPostExecute(String result) {
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(),
                        result,Toast.LENGTH_LONG).show();
                return;
            }
            List<Hike> hikeList = new ArrayList<>();
            result = Hike.parseHikeJSON(result,hikeList, favDataBaseAdapter);
            //check list for trailname existing in sqlite table. If not, remove hike from view

            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(),
                        result,Toast.LENGTH_LONG).show();
                return;
            }
            if (!hikeList.isEmpty() && hikeList != null) {
                MyFavoritesRecyclerViewAdapter adapter =new MyFavoritesRecyclerViewAdapter(hikeList,mListener);
                mRecyclerView.setAdapter(adapter);
            }
        }
    }
}
