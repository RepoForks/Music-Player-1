package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;

import android.view.Menu;

import java.util.Iterator;
import java.util.Map;

public class MenuWrapper {

    private Menu mMenu;

    public MenuWrapper(Menu menu) {
        mMenu = menu;
    }

    public void setContent(MenuCommandsContainer commandsContainer) {
//        Iterator iter = commandsContainer.getIterator();
//        Map.Entry<Integer, MenuItem> pair;
//        Integer itemId;
//        MenuItem menuItem;
//        int showAsAction;
//        while (iter.hasNext()) {
//            pair = (Map.Entry<Integer, MenuItem>) iter.next();
//            itemId = pair.getKey();
//            menuItem = pair.getValue();
//            showAsAction = menuItem.getShowAsAction();
//            mMenu.add(commandsContainer.getId(), itemId, android.view.Menu.NONE, menuItem.getTitle()).setIcon(menuItem.getIconResource())
//                    .setShowAsAction(showAsAction);
//        }
        commandsContainer.initIterator();
        while (commandsContainer.hasNext()) {
            commandsContainer.next();
            mMenu.add(commandsContainer.getId(), commandsContainer.getItemId(), android.view.Menu.NONE, commandsContainer.getItemTitle()).setIcon(commandsContainer.getItemIconResource())
                    .setShowAsAction(commandsContainer.getShowAsAction());
        }
        commandsContainer.flushIterator();
    }
}
