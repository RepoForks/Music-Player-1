package ua.edu.cdu.fotius.lisun.musicplayer.recommendations;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import ua.edu.cdu.fotius.lisun.musicplayer.model.ListenLog;
import ua.edu.cdu.fotius.lisun.musicplayer.model.StoragesSyncAsyncTask;

public class FavoriteGenresRetrieverAsyncTask extends AsyncTask<Void, Void, Map<String, Long>> {

    public static interface Callback {
        public void onFavoriteRetrieved(Map<String, Long> favorites);
    }

    private Context mContext;
    private Callback mCallback;

    public FavoriteGenresRetrieverAsyncTask(Context context, Callback callback) {
        mContext = context;
        mCallback = callback;
    }

    @Override
    protected Map<String, Long> doInBackground(Void... params) {
        Map<String, Long> genresListenedQtyMap = getGroupedByFavoriteAttr();
        //remove duplicates
        Set<Long> unduplicatedListenedQty = new HashSet<>(genresListenedQtyMap.values());

        displaySet(unduplicatedListenedQty);

        return null;
    }

    private Map<String, Long> getGroupedByFavoriteAttr() {
        Realm realm = Realm.getInstance(mContext);
        RealmResults<ListenLog> allObjects = realm.allObjects(ListenLog.class);
        HashMap<String, Long> genreListenedQtyMap = new HashMap<>();
        for(ListenLog log : allObjects) {
            String genre = log.getGenre().toLowerCase();
            Long listenedQty = genreListenedQtyMap.get(genre);
            Long value = null;
            if(listenedQty == null) {
                value = log.getListenedCounter();
            } else {
                value = listenedQty + log.getListenedCounter();
            }
            genreListenedQtyMap.put(genre, value);
        }
        return genreListenedQtyMap;
    }

    @Override
    protected void onPostExecute(Map<String, Long> favorites) {
        super.onPostExecute(favorites);
        mCallback.onFavoriteRetrieved(favorites);
    }

    //TODO: only for debug. can be deleted
    public static void displaySet(Set<Long> set) {
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Log.d("DISPLAY SET", "ID: " + iterator.next());
        }
    }

}
