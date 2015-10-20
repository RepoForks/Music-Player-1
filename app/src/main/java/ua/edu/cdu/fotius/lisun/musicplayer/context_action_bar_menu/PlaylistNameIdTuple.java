package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

public class PlaylistNameIdTuple {
    private String mName;
    private long mId;

    public PlaylistNameIdTuple(long id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }
}
