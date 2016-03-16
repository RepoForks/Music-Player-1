package ua.edu.cdu.fotius.lisun.musicplayer.recommendations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import ua.edu.cdu.fotius.lisun.musicplayer.model.ListenLog;

public class FavoriteGenresRetrieverAsyncTask extends AsyncTask<Context, Void, Map<String, Long>> {

    private final String TAG = getClass().getSimpleName();

    public static interface Callback {
        public void onFavoriteRetrieved(Map<String, Long> favorites);
    }

    private Callback mCallback;
    private Context mContext;

    public FavoriteGenresRetrieverAsyncTask(Context c, Callback callback) {
        mCallback = callback;
        mContext = c;
    }

    @Override
    protected Map<String, Long> doInBackground(Context... contexts) {
        Map<String, Long> genresListenedQtyMap = getGroupedByGenres(contexts[0]);

        //TODO: Debug.
        Log.d(TAG, "BEFORE");
        displayMap(genresListenedQtyMap);

        Multimap<Long, String> multimap = HashMultimap.create();
        for(Map.Entry<String, Long> entry : genresListenedQtyMap.entrySet()) {
            multimap.put(entry.getValue(), entry.getKey());
        }

        FavoritesStatAnalyser statAnalyser = new FavoritesStatAnalyser();
        List<Long>  unduplicatedSortedList = statAnalyser.unduplicatedSortedListFrom(multimap);

        List<Long> lessThanMedianList = statAnalyser.getLessThanMedianList(unduplicatedSortedList);
        unduplicatedSortedList = null;

        statAnalyser.removeLessThanMedianFromMap(multimap, lessThanMedianList);
        lessThanMedianList = null;

        long sumOfGreaterThanMedian = statAnalyser.sumOfGreaterThanMedian(multimap);

        Map<String, Long> genreToPercents = statAnalyser.genresToPercentsMap(multimap,
                sumOfGreaterThanMedian);

        //TODO: Debug.
        Log.d(TAG, "AFTER");
        displayMap(genreToPercents);

        new CachingTask(mContext, genreToPercents).execute();

        return null;
    }

    private Map<String, Long> getGroupedByGenres(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<ListenLog> allObjects = realm.allObjects(ListenLog.class);
        Map<String, Long> genreListenedQtyMap = new HashMap<>();
        for(ListenLog log : allObjects) {
            String genre = log.getGenre();
            if (genre != null) {
                genre = genre.toLowerCase();
                Long listenedQty = genreListenedQtyMap.get(genre);
                Long value = null;
                if (listenedQty == null) {
                    value = log.getListenedCounter();
                } else {
                    value = listenedQty + log.getListenedCounter();
                }
                genreListenedQtyMap.put(genre, value);
            }
        }
        return genreListenedQtyMap;
    }

    //TODO: only for debug. can be deleted
    public static void displaySet(Set<Long> set) {
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Log.d("DISPLAY SET", "Quantities: " + iterator.next());
        }
    }

    //TODO: only for debug. can be deleted
    public void displayMap(Map<String, Long> map) {
        for(Map.Entry<String, Long> entry : map.entrySet()) {
            Log.d("DISPLAY SET", "key: " + entry.getKey() + " value: " + entry.getValue());
        }
    }

}
