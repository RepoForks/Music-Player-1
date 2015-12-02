package ua.edu.cdu.fotius.lisun.musicplayer.activities;

public class TrackInfo {

    private long mTrackId;
    private String mTitle;
    private String mAlbum;
    private String mArtist;
    private int mYear;

    public TrackInfo(long trackId) {
        this.mTrackId = trackId;
    }

    public long getTrackId() {
        return mTrackId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public void setAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int mYear) {
        this.mYear = mYear;
    }
}
