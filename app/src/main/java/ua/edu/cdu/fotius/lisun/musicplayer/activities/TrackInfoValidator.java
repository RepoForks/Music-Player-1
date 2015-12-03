package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.util.Log;

public class TrackInfoValidator {

    private TrackInfo mTestingObject;

    public TrackInfoValidator(TrackInfo testingObject) {
        mTestingObject = testingObject;
    }

    public boolean isTitleValid() {
        String title = mTestingObject.getTitle();
        if(!isStringValid(title)) {
            return false;
        }
        return true;
    }

    public boolean isAlbumValid() {
        String album = mTestingObject.getAlbum();
        if(!isStringValid(album)) {
            return false;
        }
        return true;
    }

    public boolean isArtistValid() {
        String artist = mTestingObject.getArtist();
        if(!isStringValid(artist)) {
            return false;
        }
        return true;
    }

    public boolean isYearValid() {
        String year = mTestingObject.getYear();
        if(!isStringValid(year)) {
            return false;
        }

        try {
            Integer.parseInt(year);
        } catch (NumberFormatException nfe){
            return false;
        }

        return true;
    }

    private boolean isStringValid(String s) {
        if((s == null) || (s.isEmpty())) {
            return false;
        }
        return true;
    }
}
