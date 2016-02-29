package ua.edu.cdu.fotius.lisun.musicplayer.model;

import io.realm.RealmObject;

public class ListeningLog extends RealmObject{

    private long trackId;
    //TODO: default value
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
