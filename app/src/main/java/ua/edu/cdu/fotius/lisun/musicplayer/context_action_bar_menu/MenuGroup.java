package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MenuGroup {

    public static int MINIMAL_GROUP_ID = 1;
    public static int ADDITIONAL_GROUP_ID = 2;

    private HashMap<Integer, MenuItem> mIdToCABMenuItem;
    private int mGroupId;

    public MenuGroup(int groupId) {
        mIdToCABMenuItem = new HashMap<Integer, MenuItem>();
        mGroupId = groupId;
    }

    public void add(int menuItemId, String title, int iconResource, Command command, int showAsAction) {
        mIdToCABMenuItem.put(menuItemId, new MenuItem(title, iconResource, command, showAsAction));
    }

    public boolean execute(int itemId) {
        MenuItem menuItem = mIdToCABMenuItem.get(itemId);
        if(menuItem == null) {
            return false;
        }
        menuItem.execute();
        return true;
    }

    public int getId() {
        return mGroupId;
    }

    public void fillMenuWithThisGroup(android.view.Menu menu) {
        Iterator iter = mIdToCABMenuItem.entrySet().iterator();
        Map.Entry<Integer, MenuItem> pair;
        Integer itemId;
        MenuItem menuItem;
        int showAsAction;
        while (iter.hasNext()) {
            pair = (Map.Entry<Integer, MenuItem>) iter.next();
            itemId = pair.getKey();
            menuItem = pair.getValue();
            showAsAction = menuItem.getShowAsAction();
            menu.add(mGroupId, itemId, android.view.Menu.NONE, menuItem.getTitle()).setIcon(menuItem.getIconResource())
                    .setShowAsAction(showAsAction);
        }
    }
}
