package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import java.util.HashMap;

public class TrackInfoHolder {

    private HashMap<String, InfoElement> mMap;

    public TrackInfoHolder() {
        mMap = new HashMap<String, InfoElement>();
    }

    public void put(String key, InfoElement value) {
        mMap.put(key, value);
    }

    public InfoElement get(String key) {
        return mMap.get(key);
    }

    public void setDataAt(String key, String data, BaseValidator.ValidationResult validationResult) {
        mMap.get(key).setData(data, validationResult);
    }
}
