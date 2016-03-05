package ua.edu.cdu.fotius.lisun.musicplayer.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class ListenLog extends RealmObject{

    public static String TRACK_ID_ATTR = "trackId";
    public static String LISTENED_COUNTER_ATTR = "listenedCounter";

    @PrimaryKey
    private long trackId;

    private long listenedCounter;

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public long getListenedCounter() {
        return listenedCounter;
    }

    public void setListenedCounter(long listenedCounter) {
        this.listenedCounter = listenedCounter;
    }
}
