package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import java.util.ArrayList;

public abstract class BaseMenu {

    private MenuGroup mMinimalGroup = null;
    private MenuGroup mAdditionalGroup = null;

    protected MenuGroup getMinimalGroup() {
        return mMinimalGroup;
    }

    protected void setMinimalGroup(MenuGroup minimalGroup) {
        mMinimalGroup = minimalGroup;
    }

    protected MenuGroup getAdditionalGroup() {
        return mAdditionalGroup;
    }

    protected void setAdditionalGroup(MenuGroup additionalGroup) {
        mAdditionalGroup = additionalGroup;
    }

    public void execute(int itemId, ArrayList<Long> idsOverWhichToExecute) {
        boolean isExecuted = mMinimalGroup.execute(itemId, idsOverWhichToExecute);
        if(!isExecuted) {
            mAdditionalGroup.execute(itemId, idsOverWhichToExecute);
        }
    }

    public abstract MenuGroup initializeOrGetMinimalGroup();
    public abstract MenuGroup initializeOrGetAdditionalGroup();
}
