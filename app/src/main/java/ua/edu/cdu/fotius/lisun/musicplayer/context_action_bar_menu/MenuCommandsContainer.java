package ua.edu.cdu.fotius.lisun.musicplayer.context_action_bar_menu;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MenuCommandsContainer {

    public static int MINIMAL_GROUP_ID = 1;
    public static int ADDITIONAL_GROUP_ID = 2;

    private HashMap<Integer, MenuItem> mIdToCABMenuItem;
    private Iterator mIterator;
    private Map.Entry<Integer, MenuItem> mMapEntry;

    private int mContainerId;

    public MenuCommandsContainer(int containerId) {
        mIdToCABMenuItem = new HashMap<Integer, MenuItem>();
        mContainerId = containerId;
    }

    public void add(int menuItemId, String title, int iconResource, Command command, int showAsAction) {
        mIdToCABMenuItem.put(menuItemId, new MenuItem(title, iconResource, command, showAsAction));
    }

    public boolean execute(int itemId, long[] idsOverWhichToExecute) {
        MenuItem menuItem = mIdToCABMenuItem.get(itemId);
        if(menuItem == null) {
            return false;
        }
        menuItem.execute(idsOverWhichToExecute);
        return true;
    }

    public int getId() {
        return mContainerId;
    }

    public void initIterator() {
        mIterator = mIdToCABMenuItem.entrySet().iterator();
    }

    public void flushIterator() {
        mIterator = null;
    }

    public boolean hasNext() {
        return mIterator.hasNext();
    }

    public void next() {
        mMapEntry = (Map.Entry<Integer, MenuItem>) mIterator.next();
    }

    public int getItemId() {
        return mMapEntry.getKey();
    }

    public String getItemTitle() {
        return mMapEntry.getValue().getTitle();
    }

    public int getItemIconResource() {
        return mMapEntry.getValue().getIconResource();
    }

    public int getShowAsAction() {
        return mMapEntry.getValue().getShowAsAction();
    }

//    public void fillMenuWithThisGroup(android.view.Menu menu) {
//        Iterator iter = mIdToCABMenuItem.entrySet().iterator();
//        Map.Entry<Integer, MenuItem> pair;
//        Integer itemId;
//        MenuItem menuItem;
//        int showAsAction;
//        while (iter.hasNext()) {
//            pair = (Map.Entry<Integer, MenuItem>) iter.next();
//            itemId = pair.getKey();
//            menuItem = pair.getValue();
//            showAsAction = menuItem.getShowAsAction();
//            menu.add(mContainerId, itemId, android.view.Menu.NONE, menuItem.getTitle()).setIcon(menuItem.getIconResource())
//                    .setShowAsAction(showAsAction);
//        }
//    }
}
