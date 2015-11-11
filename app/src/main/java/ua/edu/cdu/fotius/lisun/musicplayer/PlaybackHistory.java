package ua.edu.cdu.fotius.lisun.musicplayer;

import java.util.Vector;

/**
 * TODO:
 * Created by andrei on 09.07.2015.
 * Possible to create parent class for this class
 * and Playlist class, cause they have a lot of common
 * methods
 */
public class PlaybackHistory {

    public static final int MAX_HISTORY_SIZE = 100;
    private Vector<Integer> mHistory = new Vector<Integer>(MAX_HISTORY_SIZE);

    private static PlaybackHistory instance = null;

    public static PlaybackHistory getInstance() {
        if(instance == null) {
            instance = new PlaybackHistory();
        }
        return instance;
    }

    private PlaybackHistory() {}

    public int get(int index) {
        return mHistory.get(index);
    }

    public void add(int positionAtPlaylist) {
        mHistory.add(positionAtPlaylist);
    }

    public int remove(int index) {
        return mHistory.remove(index);
    }

    public void clear() {
        mHistory.clear();
    }

    public int size() {
        return mHistory.size();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < this.size(); i++) {
            int id = this.get(i);
            if (id < 0) {
                continue;
            } else {
                result.append(id + ";");
            }
        }
        return result.toString();
    }

    public void fromString(String history, int playlistSize) {
        if (history != null && history.length() > 1) {
            //clear history
            clear();
            String[] splitedHistory = history.split(";");
            int historyElement = 0;
            for(int i = 0; i < splitedHistory.length; i++) {
                try {
                    historyElement = Integer.parseInt(splitedHistory[i]);
                    if(historyElement > playlistSize){
                        //History is bogus
                        //clear previously added elements
                        //and return
                        mHistory.clear();
                        return;
                    }
                    mHistory.add(historyElement);
                } catch(NumberFormatException ex) {
                    //History is bogus
                    //clear previously added elements
                    //and return
                    mHistory.clear();
                    return;
                }
            }
        }
    }
}
