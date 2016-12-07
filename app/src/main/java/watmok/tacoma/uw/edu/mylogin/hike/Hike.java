package watmok.tacoma.uw.edu.mylogin.hike;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;



import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import watmok.tacoma.uw.edu.mylogin.FavoritesDataBaseAdapter;

/**
 * A class that parses and stores instances of Hikes to be displayed,
 * using JSON data from a web service.
 */

public class Hike implements Serializable {

    FavoritesDataBaseAdapter favDataBaseAdapter;
    /**
     * Strings for a name of the Hike, and a short description of it.
     */
    private String mHikeName;
    private String mShortDescription;
    private String mLongDescription;
    private LatLng mCoordinates;
    private Bitmap mPicture;
    private String mLength;
    private String mMaxElevation;
    private String mElevationGain;
    private String mReviews;
    private String mPicUrlEnding;

    /**
     * Identifiers for parsing the JSON string
     */
    private static final String HIKE_NAME = "Hike_Name";
    private static final String SHORT_DESCRIPTION = "Short_Description";
    private static final String TRAIL_COORDINATES = "Trailhead_Coordinates";
    private static final String PICTURE = "Picture";
    private static final String LENGTH = "Length_In_Miles";
    private static final String MAX_ELEV = "Max_Elevation_Ft";
    private static final String ELEV_GAIN = "Elevation_Gain_Ft";
    private static final String LONG_DESCRIPTION = "Long_Description";
    private static final String REVIEWS ="Reviews";
    private static final String PICS_URL = "Pic_Url";

    /**
     * Constructor for contrusting a hikes object with only the name and description.
     * @param theHikeName
     * @param theShortDescription
     */
    private Hike(String theHikeName, String theShortDescription) {
        mHikeName = theHikeName;
        mShortDescription = theShortDescription;
        mCoordinates = null;
    }

    /**
     * Constructor for contructing a hikes object with only the name and description,
     * and the trailhead coordinates.
     * @param theHikeName
     * @param theShortDescription
     * @param theCoordinates
     */
    private Hike(String theHikeName, String theShortDescription, String theCoordinates) {

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


    private Hike(String theHikeName, String theLongDescription, String theCoordinates,
                 String theLength, String theElevGain, String theMaxElev, String theReviews,
                 String thePicUrl) {
        mHikeName = theHikeName;
        mLength = theLength;
        mLongDescription = theLongDescription;
        mElevationGain = theElevGain;
        mMaxElevation = theMaxElev;
        mReviews = theReviews;
        mPicUrlEnding = thePicUrl;
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
    public static String getHikeName() {
        return HIKE_NAME;
    }

    public static String getShortDescription() {
        return SHORT_DESCRIPTION;
    }

    public static String getTrailCoordinates() {
        return TRAIL_COORDINATES;
    }

    /**
     * Parses a JSON String into a JSON array, and uses it to fill a List of Hike objects.
     * @author Daniel Bergman
     * @param hikeJSON The JSON string
     * @param hikeList The list of Hike objects
     * @return Returns String reason, an error message if something went wrong.
     */
    public static String parseHikeJSON(String hikeJSON, List<Hike> hikeList) {
        String reason = null;

        if (hikeJSON != null) {
            try {
                JSONArray array = new JSONArray(hikeJSON);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Hike hike;
                        if (object.has(TRAIL_COORDINATES)) {
                            if (object.has(SHORT_DESCRIPTION)) {
                                hike = new Hike(object.getString(HIKE_NAME), object.getString(SHORT_DESCRIPTION),
                                        object.getString(TRAIL_COORDINATES));
                            } else {

                                hike = new Hike(object.getString(HIKE_NAME), object.getString(LONG_DESCRIPTION),
                                        object.getString(TRAIL_COORDINATES), object.getString(LENGTH),
                                        object.getString(ELEV_GAIN), object.getString(MAX_ELEV),
                                        object.getString(REVIEWS), object.getString(PICS_URL));
                            }
                        } else {
                            hike = new Hike(object.getString(HIKE_NAME), object.getString(SHORT_DESCRIPTION));
                        }

//                        boolean issaved;
//                        ArrayList<String> saved = hike.getSavedHikesList();
//                        for (String name : saved) {
//                            if (hike.getmHikeName().equals(name)) {
//                                issaved=true;
//                            }
//                        }

                        hikeList.add(hike);
                    }
            } catch (JSONException e) {
                reason = "Unable to parse data. Reason: " + e.getMessage();
            }
        }

        return reason;
    }

    /**
     * Parses a JSON String into a JSON array, and uses it to fill a List of Hike objects.
     * @author Daniel Bergman
     * @param hikeJSON The JSON string
     * @param hikeList The list of Hike objects
     * @return Returns String reason, an error message if something went wrong.
     */
    public static String parseHikeJSON(String hikeJSON, List<Hike> hikeList, FavoritesDataBaseAdapter adapter) {
        String reason = null;

        if (hikeJSON != null) {
            try {
                JSONArray array = new JSONArray(hikeJSON);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Hike hike;
                    if (object.has(TRAIL_COORDINATES)) {
                        if (object.has(SHORT_DESCRIPTION)) {
                            hike = new Hike(object.getString(HIKE_NAME), object.getString(SHORT_DESCRIPTION),
                                    object.getString(TRAIL_COORDINATES));
                        } else {

                            hike = new Hike(object.getString(HIKE_NAME), object.getString(LONG_DESCRIPTION),
                                    object.getString(TRAIL_COORDINATES), object.getString(LENGTH),
                                    object.getString(ELEV_GAIN), object.getString(MAX_ELEV),
                                    object.getString(REVIEWS), object.getString(PICS_URL));
                        }
                    } else {
                        hike = new Hike(object.getString(HIKE_NAME), object.getString(SHORT_DESCRIPTION));
                    }

                        ArrayList<String> saved = adapter.getAllEntries();
                        for (String name : saved) {
                            if (hike.getmHikeName().equals(name)) {
                                hikeList.add(hike);
                            }
                        }


                }
            } catch (JSONException e) {
                reason = "Unable to parse data. Reason: " + e.getMessage();
            }
        }

        return reason;
    }

    public String getmLongDescription() {
        return mLongDescription;
    }

    public void setmLongDescription(String mLongDescription) {
        this.mLongDescription = mLongDescription;
    }

    public void setmCoordinates(LatLng mCoordinates) {
        this.mCoordinates = mCoordinates;
    }

    public Bitmap getmPicture() {
        return mPicture;
    }

    public void setmPicture(Bitmap mPicture) {
        this.mPicture = mPicture;
    }

    public String getmLength() {
        return mLength;
    }

    public void setmLength(String mLength) {
        this.mLength = mLength;
    }

    public String getmMaxElevation() {
        return mMaxElevation;
    }

    public void setmMaxElevation(String mMaxElevation) {
        this.mMaxElevation = mMaxElevation;
    }

    public String getmElevationGain() {
        return mElevationGain;
    }

    public void setmElevationGain(String mElevationGain) {
        this.mElevationGain = mElevationGain;
    }

    public String getmReviews() {
        return mReviews;
    }

    public void setmReviews(String mReviews) {
        this.mReviews = mReviews;
    }

    public String getmPicUrlEnding() {
        return mPicUrlEnding;
    }

    public void setmPicUrlEnding(String mPicUrlEnding) {
        this.mPicUrlEnding = mPicUrlEnding;
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
