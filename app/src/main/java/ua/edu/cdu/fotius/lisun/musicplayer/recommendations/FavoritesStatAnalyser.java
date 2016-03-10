package ua.edu.cdu.fotius.lisun.musicplayer.recommendations;

import com.google.common.collect.BiMap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FavoritesStatAnalyser {

    public List<Long> unduplicatedSortedListFrom(Multimap<Long, String> map) {
        Set<Long> unduplicatedListenedQty = map.keySet();
        List<Long> list = new ArrayList<>(unduplicatedListenedQty);
        Collections.sort(list);
        return list;
    }

    public List<Long> getLessThanMedianList(List<Long> init) {
        return init.subList(0, init.size() / 2);
    }

    public void removeLessThanMedianFromMap(Multimap<Long, String> map, List<Long> lessThanMedian) {
        for(Long l : lessThanMedian) {
            map.removeAll(l);
        }
    }

    public long sumOfGreaterThanMedian(Multimap<Long, String> greaterThanMedianMap) {
        long sum = 0;
        for(Long single : greaterThanMedianMap.keys()) {
            sum += single;
        }
        return sum;
    }

    public Map<String, Long> genresToPercentsMap(Multimap<Long, String> greaterThanMedianMap,
                                                 long sumOfGreaterThanMedianListenedQties) {
        long sum = sumOfGreaterThanMedianListenedQties;
        Map<String, Long> genreToPercents = new HashMap<>();

        for(Long listenedQty : greaterThanMedianMap.keySet()) {
            Collection<String> valuesForKey = greaterThanMedianMap.get(listenedQty);
            long percents = (listenedQty * 100) / sum;
            for(String genre : valuesForKey) {
                genreToPercents.put(genre, percents);
            }
        }
        return genreToPercents;
    }
}
