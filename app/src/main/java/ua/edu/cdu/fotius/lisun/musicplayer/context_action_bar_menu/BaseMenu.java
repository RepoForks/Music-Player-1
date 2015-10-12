package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

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

    public void execute(int itemId) {
        boolean isExecuted = mMinimalGroup.execute(itemId);
        if(!isExecuted) {
            mAdditionalGroup.execute(itemId);
        }
    }

    public abstract MenuGroup initializeOrGetMinimalGroup();
    public abstract MenuGroup initializeOrGetAdditionalGroup();
}
