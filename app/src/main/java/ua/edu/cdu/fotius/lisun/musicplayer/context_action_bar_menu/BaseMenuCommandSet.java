package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

public abstract class BaseMenuCommandSet {

    private MenuCommandsContainer mMinimalGroup = null;
    private MenuCommandsContainer mAdditionalGroup = null;

    protected MenuCommandsContainer getMinimalGroup() {
        return mMinimalGroup;
    }

    protected void setMinimalGroup(MenuCommandsContainer minimalGroup) {
        mMinimalGroup = minimalGroup;
    }

    protected MenuCommandsContainer getAdditionalGroup() {
        return mAdditionalGroup;
    }

    protected void setAdditionalGroup(MenuCommandsContainer additionalGroup) {
        mAdditionalGroup = additionalGroup;
    }

    public void execute(int itemId, long[] idsOverWhichToExecute) {
        boolean isExecuted = mMinimalGroup.execute(itemId, idsOverWhichToExecute);
        if(!isExecuted) {
            mAdditionalGroup.execute(itemId, idsOverWhichToExecute);
        }
    }

    public abstract MenuCommandsContainer initializeOrGetMinimalGroup();
    public abstract MenuCommandsContainer initializeOrGetAdditionalGroup();
}
