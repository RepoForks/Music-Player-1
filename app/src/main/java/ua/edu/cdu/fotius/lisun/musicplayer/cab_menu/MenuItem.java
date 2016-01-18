package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

public class MenuItem {



    private String mTitle;
    private int mIconResource;
    private Command mCommand;
    private int mShowAsAction;

    public MenuItem(String title, int iconResource, Command command, int showAsAction) {
        mTitle = title;
        mIconResource = iconResource;
        mCommand = command;
        mShowAsAction = showAsAction;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getIconResource() {
        return mIconResource;
    }

    public int getShowAsAction() {
        return mShowAsAction;
    }

    public void execute(long[] idsOverWhichToExecute) {
        mCommand.execute(idsOverWhichToExecute);
    }
}
