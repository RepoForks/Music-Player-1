package ua.edu.cdu.fotius.lisun.musicplayer.activities;

public class TrackInfo {

    private long mTrackId;
    private String mTitle;
    private String mAlbum;
    private String mArtist;
    private String mYear;

    public TrackInfo(long trackId, String title, String album, String artist, String year) {
        mTrackId = trackId;
        mTitle = title;
        mAlbum = album;
        mArtist = artist;
        mYear = year;
    }

    public long getTrackId() {
        return mTrackId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getYear() {
        return mYear;
    }
}
