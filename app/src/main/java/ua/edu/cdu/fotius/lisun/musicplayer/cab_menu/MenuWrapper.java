package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.view.Menu;

public class MenuWrapper {

    private Menu mMenu;

    public MenuWrapper(Menu menu) {
        mMenu = menu;
    }

    public void setContent(MenuCommandsContainer commandsContainer) {
        if (commandsContainer != null) {
            commandsContainer.initIterator();
            while (commandsContainer.hasNext()) {
                commandsContainer.next();
                mMenu.add(commandsContainer.getId(), commandsContainer.getItemId(),
                        android.view.Menu.NONE,
                        commandsContainer.getItemTitle()).setIcon(commandsContainer.getItemIconResource())
                        .setShowAsAction(commandsContainer.getShowAsAction());
            }
            commandsContainer.flushIterator();
        }
    }
}
