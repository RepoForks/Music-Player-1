package ua.edu.cdu.fotius.lisun.musicplayer.recommendations;

import com.google.common.collect.BiMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FavoritesStatAnalyser {

    public List<Long> unduplicatedSortedListFrom(Map<String, Long> map) {
        Set<Long> unduplicatedListenedQty = new HashSet<>(map.values());
        List<Long> list = new ArrayList<>(unduplicatedListenedQty);
        Collections.sort(list);
        return list;
    }

    public long median(List<Long> list) {
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

    public List<Long> greaterThanMedianList(long median, List<Long> list) {
        List<Long> greaterThanMedianList = new ArrayList<>();
        for(Long l : list) {
            if(l >= median) {
                greaterThanMedianList.add(l);
            }
        }
        return greaterThanMedianList;
    }

    public Map<Long, String> greaterThanMedianMap(BiMap<String, Long> initialMap,
                                                  List<Long> greaterThanMedianList) {
        BiMap<Long, String> genresListenedQtyInversed = initialMap.inverse();
        Map<Long, String> greaterThanMedianMap = new HashMap<>();
        for(Long l : greaterThanMedianList) {
            greaterThanMedianMap.put(l, genresListenedQtyInversed.get(l));
        }
        return greaterThanMedianMap;
    }

    public Map<String, Long> genresToPercentsMap(Map<Long, String> greaterThanMedianMap,
                                                 long sumOfGreaterThanMedianListenedQties) {
        long sum = sumOfGreaterThanMedianListenedQties;
        Map<String, Long> genreToPercents = new HashMap<>();
        for (Map.Entry<Long, String> entry : greaterThanMedianMap.entrySet()) {
            long listenedQty = entry.getKey();
            long percents = (listenedQty * 100) / sum;
            genreToPercents.put(entry.getValue(), percents);
        }
        return genreToPercents;
    }
}
