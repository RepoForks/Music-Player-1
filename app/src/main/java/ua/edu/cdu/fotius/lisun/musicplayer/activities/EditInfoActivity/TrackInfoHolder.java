package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

import java.util.HashMap;

public class TrackInfoHolder {

    private HashMap<String, BaseTrackInfo> mMap;

    public TrackInfoHolder() {
        mMap = new HashMap<String, BaseTrackInfo>();
    }

    public void put(String key, BaseTrackInfo value) {
        mMap.put(key, value);
    }

    public BaseTrackInfo get(String key) {
        return mMap.get(key);
    }
}
