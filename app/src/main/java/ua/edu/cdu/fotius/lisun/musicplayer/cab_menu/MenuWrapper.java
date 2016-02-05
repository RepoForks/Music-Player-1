package ua.edu.cdu.fotius.lisun.musicplayer.cab_menu;

import android.view.*;

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
                android.view.MenuItem item = mMenu.add(commandsContainer.getId(), commandsContainer.getItemId(),
                        android.view.Menu.NONE,
                        commandsContainer.getItemTitle());
                int iconRes = commandsContainer.getItemIconResource();
                if (iconRes != MenuItem.NO_ICON_ID) {
                    item.setIcon(iconRes);
                }
                item.setShowAsAction(commandsContainer.getShowAsAction());
            }
            commandsContainer.flushIterator();
        }
    }
}
