package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import java.util.ArrayList;

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

    public void execute(ArrayList<Long> idsOverWhichToExecute) {
        mCommand.execute(idsOverWhichToExecute);
    }
}
