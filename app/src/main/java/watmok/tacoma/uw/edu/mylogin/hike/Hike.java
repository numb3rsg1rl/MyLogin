package watmok.tacoma.uw.edu.mylogin.hike;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

/**
 * A class that parses and stores instances of Hikes to be displayed,
 * using JSON data from a web service.
 */

public class Hike implements Serializable {

    /**
     * Strings for a name of the Hike, and a short description of it.
     */
    private String mHikeName;
    private String mShortDescription;
    private LatLng mCoordinates;

    /**
     * Identifiers for parsing the JSON string
     */
    public static final String HIKE_NAME = "Hike_Name";
    public static final String SHORT_DESCRIPTION = "Short_Description";
    public static final String TRAIL_COORDINATES = "Trailhead_Coordinates";

    /**
     * Constructor for contrusting a hikes object with only the name and description.
     * @param theHikeName
     * @param theShortDescription
     */
    public Hike(String theHikeName, String theShortDescription) {
        mHikeName = theHikeName;
        mShortDescription = theShortDescription;
        mCoordinates = null;
    }

    /**
     * Constructor for contrusting a hikes object with only the name and description,
     * and the trailhead coordinates.
     * @param theHikeName
     * @param theShortDescription
     * @param theCoordinates
     */
    public Hike(String theHikeName, String theShortDescription, String theCoordinates) {

        mHikeName = theHikeName;
        mShortDescription = theShortDescription;
        Scanner scanner = new Scanner(theCoordinates);

        // the following parsing code is based on the first answer on
        // http://stackoverflow.com/questions/26285086/reading-a-string-int-and-double-from-csv-file
        String nextLine = scanner.nextLine();
        Log.d("JSON of hike","String of the coordinates = " + theCoordinates);
        String[] array = nextLine.split(",");
        double lat = Double.parseDouble(array[0]);
        double lng = Double.parseDouble(array[1]);

        mCoordinates = new LatLng(lat,lng);
    }

    /**
     * Parses a JSON String into a JSON array, and uses it to fill a List of Hike objects.
     * @author Daniel Bergman
     * @param hikeJSON The JSON string
     * @param hikeList The list of Hike objects
     * @return Returns String reason, an error message if something went wrong.
     */
    public static String parseHikeJSON (String hikeJSON, List<Hike> hikeList) {
        String reason = null;

        if (hikeJSON != null) {
            try {

                JSONArray array = new JSONArray(hikeJSON);
                for (int i = 0; i<array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Hike hike;
                    if (object.has(TRAIL_COORDINATES)) {
                        hike = new Hike(object.getString(HIKE_NAME), object.getString(SHORT_DESCRIPTION),
                                object.getString(TRAIL_COORDINATES));
                    } else {
                        hike = new Hike(object.getString(HIKE_NAME), object.getString(SHORT_DESCRIPTION));
                    }
                    hikeList.add(hike);
                }
            } catch (JSONException e) {
                reason = "Unable to parse data. Reason: " + e.getMessage();
            }
        }

        return reason;
    }

    /**
     * Getter for mHikeName
     * @return an instance of mHikeName
     */
    public String getmHikeName() {
        return mHikeName;
    }

    /**
     *
     * Setter for mHikeName
     * @param mHikeName the String to set mHikeName to.
     */
    public void setmHikeName(String mHikeName) {
        this.mHikeName = mHikeName;
    }

    /**
     * Getter for mShortDescription
     * @return an instance of mShortDescription
     */
    public String getmShortDescription() {
        return mShortDescription;
    }

    /**
     * Setter for mShortDesription
     * @param mShortDescription The String to set mShortDescription to
     */
    public void setmShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    /**
     * Getter for mCoordinate
     * @return a reference to mCoordinates
     */
    public LatLng getmCoordinates() {
        return mCoordinates;
    }
}
