package watmok.tacoma.uw.edu.mylogin.hike;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dovi on 11/7/2016.
 */

public class Hike implements Serializable {

    private String mHikeName;
    private String mShortDescription;

    public static final String HIKE_NAME = "Hike_Name";
    public static final String SHORT_DESCRIPTION = "Short_Description";

    public Hike(String theHikeName, String theShortDescription) {
        mHikeName = theHikeName;
        mShortDescription = theShortDescription;
    }

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

    public String getmHikeName() {
        return mHikeName;
    }

    public void setmHikeName(String mHikeName) {
        this.mHikeName = mHikeName;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public void setmShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }
}
