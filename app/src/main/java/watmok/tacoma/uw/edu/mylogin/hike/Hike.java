package watmok.tacoma.uw.edu.mylogin.hike;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

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

    /**
     * Identifiers for parsing the JSON string
     */
    public static final String HIKE_NAME = "Hike_Name";
    public static final String SHORT_DESCRIPTION = "Short_Description";

    public Hike(String theHikeName, String theShortDescription) {
        mHikeName = theHikeName;
        mShortDescription = theShortDescription;
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
                    Hike hike = new Hike(object.getString(Hike.HIKE_NAME),object.getString(Hike.SHORT_DESCRIPTION));
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
}
