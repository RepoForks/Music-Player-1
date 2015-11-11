package ua.edu.cdu.fotius.lisun.musicplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrei on 07.07.2015.
 */
public class Playlist {

    private static Playlist instance = null;

    public static Playlist getInstance() {
        if(instance == null) {
            instance = new Playlist();
        }
        return instance;
    }

    public static final int SHUFFLE_NONE = 0;
    public static final int SHUFFLE_NORMAL = 1;
    public static final int SHUFFLE_AUTO = 2;

    public static final int REPEAT_NONE = 0;
    public static final int REPEAT_CURRENT = 1;
    public static final int REPEAT_ALL = 2;

    private int mShuffleMode = SHUFFLE_NONE;
    private int mRepeatMode = REPEAT_NONE;

    private ArrayList<Long> mPlaylist;

    private int mPlayPos = -1;
    private int mNextPlayPos = -1;
    private int seekPosition = 0;

    private Playlist() {
       mPlaylist = new ArrayList<Long>();
    }

    public long getCurrentTrack() {
        return mPlaylist.get(mPlayPos);
    }

    public void setPlayPosition(int position) {
        mPlayPos = position;
    }

    public int getPlayPosition() {
        return mPlayPos;
    }

    public void setShuffleMode(int mShuffleMode) {
        this.mShuffleMode = mShuffleMode;
    }

    public int getShuffleMode() {
        return mShuffleMode;
    }

    public void setRepeatMode(int mRepeatMode) {
        this.mRepeatMode = mRepeatMode;
    }

    public int getRepeatMode() {
        return mRepeatMode;
    }

    public int getSeekPosition() {
        return seekPosition;
    }

    public void setSeekPosition(int seekPosition) {
        this.seekPosition = seekPosition;
    }

    public int getNextPlayPosition() {
        return mNextPlayPos;
    }

    public void setNextPlayPosition(int nextPlayPos) {
        this.mNextPlayPos = nextPlayPos;
    }

    public long getTrack(int position) {
        return mPlaylist.get(position);
    }

    public int size() {
        return mPlaylist.size();
    }

    public void addTrack(long trackId) {
        mPlaylist.add(trackId);
    }

    public void addTrackId(int position, long trackId) {
        mPlaylist.add(position, trackId);
    }

    public void addAllTracks(int position, long[] tracksIds) {
        if (position < 0) { // overwrite
            mPlaylist.clear();
            position = 0;
        }
        if (position > mPlaylist.size()) {
            position = mPlaylist.size();
        }
        List<Long> newList = new ArrayList<Long>();
        for(int i = 0; i < tracksIds.length; i++) {
            newList.add(tracksIds[i]);
        }

        mPlaylist.addAll(position, newList);
    }

    public void removeTrack(int position) {
        mPlaylist.remove(position);
    }

    /**
     * removes songs between @param fromIndex inclusive
     * and @param toIndex inclusive
     */
    public int removeTracksRange(int fromPosition, int toPosition) {
        if (toPosition < fromPosition) return 0;
        if (fromPosition < 0) fromPosition = 0;
        if (toPosition >= mPlaylist.size()) toPosition = mPlaylist.size() - 1;

        List<Long> removingList = mPlaylist.subList(fromPosition, toPosition + 1);
        int removingQuantity = removingList.size();
        removingList.clear();
        return removingQuantity;
    }

    public void clear() {
        mPlaylist.clear();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            long id = this.getTrack(i);
            if (id < 0) {
                continue;
            } else {
                result.append(id + ";");
            }
        }
        return result.toString();
    }

    public void fromString(String queue) {
        mPlaylist.clear();

        String[] splitedQueue = queue.split(";");
        for(int i = 0; i < splitedQueue.length; i++) {
            try {
                this.addTrack(Long.parseLong(splitedQueue[i]));
            } catch(NumberFormatException ex) {
                //mPlaylist queue is bogus
                //clear previously added elements
                //and return
                mPlaylist.clear();
                break;
            }
        }
    }
}