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

//    public long median(List<Long> list) {
//        Long median = 0L;
//        if (list.size() > 0) {
//            if (list.size() % 2 == 0) {
//                int middleIdx = list.size() / 2;
//                median = ((list.get(middleIdx) + list.get(middleIdx - 1)) / 2);
//            } else {
//                median = list.get(list.size() / 2);
//            }
//        }
//        return median;
//    }

    public List<Long> getLessThanMedianList(List<Long> init) {
        return init.subList(0, init.size() / 2);
    }


    public void removeLessThanMedianFromMap(Multimap<Long, String> map, List<Long> lessThanMedian) {
        for(Long l : lessThanMedian) {
            map.removeAll(l);
        }
    }
//    public List<Long> greaterThanMedianList(Map<String, Long> initialMap, long median) {
//        List<Long> greaterThanMedianList = new ArrayList<>();
//        for(Long l : list) {
//            if(l >= median) {
//                greaterThanMedianList.add(l);
//            }
//        }
//        return greaterThanMedianList;
//    }

//    public Map<String, Long> greaterThanMedianMap(Map<String, Long> initialMap,
//                                                  List<Long> greaterThanMedianList) {
//        //TODO: Problem here:
//        Collection<Long> collection = initialMap.values();
//        Map<String, Long> greaterThanMedianMap = new HashMap<>();
//        for(Long l : greaterThanMedianList) {
//            if(collection.contains(l)) {
//                greaterThanMedianMap.put()
//            }
//        }
//        return greaterThanMedianMap;
//    }

    public Map<String, Long> genresToPercentsMap(Multimap<Long, String> greaterThanMedianMap,
                                                 long sumOfGreaterThanMedianListenedQties) {
        long sum = sumOfGreaterThanMedianListenedQties;
        Map<String, Long> genreToPercents = new HashMap<>();

        for(Long listenedQty : greaterThanMedianMap.keySet()) {
            Collection<String> valuesForKey = greaterThanMedianMap.get(listenedQty);
            long percents = (listenedQty * 100) / sum;
            long percentsForEachGenre = percents / valuesForKey.size();
            for(String genre : valuesForKey) {
                genreToPercents.put(genre, percentsForEachGenre);
            }
        }
//        for (Map.Entry<Long, String> entry : greaterThanMedianMap.entr) {
//            long listenedQty = entry.getKey();
//            long percents = (listenedQty * 100) / sum;
//            genreToPercents.put(entry.getValue(), percents);
//        }
        return genreToPercents;
    }
}
