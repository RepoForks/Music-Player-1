package ua.edu.cdu.fotius.lisun.musicplayer.recommendations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
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

    public FavoriteGenresRetrieverAsyncTask(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected Map<String, Long> doInBackground(Context... contexts) {
        BiMap<String, Long> genresListenedQtyMap = getGroupedByGenres(contexts[0]);

        //remove duplicates
        Set<Long> unduplicatedListenedQty = new HashSet<>(genresListenedQtyMap.values());

        //displaySet(unduplicatedListenedQty);

        List<Long> list = new ArrayList<>(unduplicatedListenedQty);
        Collections.sort(list);

        Long median = getMedian(list);

        List<Long> greaterThanMedianList = getGreateThanMedian(median, list);

        BiMap<Long, String> genresListenedQtyInversed = genresListenedQtyMap.inverse();
        Map<Long, String> greaterThanMedianMap = new HashMap<>();
        Long sum = 0L;
        for(Long l : greaterThanMedianList) {
            sum += l;
            greaterThanMedianMap.put(l, genresListenedQtyInversed.get(l));
        }

        Map<String, Long> genreToPercents = getGenresToPercentsMap(greaterThanMedianMap, sum);

        return null;
    }

    private BiMap<String, Long> getGroupedByGenres(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<ListenLog> allObjects = realm.allObjects(ListenLog.class);
        BiMap<String, Long> genreListenedQtyMap = HashBiMap.create();
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

    private Long getMedian(List<Long> list) {
        Long median = 0L;
        if (list.size() > 0) {
            if (list.size() % 2 == 0) {
                int middleIdx = list.size() / 2;
                median = ((list.get(middleIdx) + list.get(middleIdx - 1)) / 2);
            } else {
                median = list.get(list.size() / 2);
            }
        }
        return median;
    }

    private List<Long> getGreateThanMedian(long median, List<Long> list) {
        List<Long> greaterThanMedianList = new ArrayList<>();
        for(Long l : list) {
            if(l >= median) {
                greaterThanMedianList.add(l);
            }
        }
        return greaterThanMedianList;
    }

    private Map<String, Long> getGenresToPercentsMap(Map<Long, String> greaterThanMedianMap, Long sum) {
        Map<String, Long> genreToPercents = new HashMap<>();
        for (Map.Entry<Long, String> entry : greaterThanMedianMap.entrySet()) {
            long listenedQty = entry.getKey();
            long percents = (listenedQty * 100) / sum;
            genreToPercents.put(entry.getValue(), percents);
        }
        return genreToPercents;
    }

    @Override
    protected void onPostExecute(Map<String, Long> favorites) {
        super.onPostExecute(favorites);
        //mCallback.onFavoriteRetrieved(favorites);
    }

    //TODO: only for debug. can be deleted
    public static void displaySet(Set<Long> set) {
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Log.d("DISPLAY SET", "Quantities: " + iterator.next());
        }
    }

}
