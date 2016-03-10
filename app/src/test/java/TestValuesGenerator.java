import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestValuesGenerator {
    public Multimap<Long, String> getMapWithDuplicates() {
        Multimap<Long, String> map = HashMultimap.create();
        map.put(248L, "a");
        map.put(35L, "b");
        map.put(248L, "c");
        map.put(1L, "d");
        map.put(15L, "e");
        map.put(35L, "f");
        map.put(1L, "g");
        map.put(1L, "h");
        map.put(44L, "j");
        map.put(2L, "k");
        map.put(167L, "l");
        return map;
    }

    public List<Long> getUnduplicatedSortedExpected() {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(15L);
        list.add(35L);
        list.add(44L);
        list.add(167L);
        list.add(248L);
        return list;
    }

    public List<Long> getInitListForLessThanMedianTest() {
       return getUnduplicatedSortedExpected();
    }

    public List<Long> getLessThanMedianList() {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(15L);
        return list;
    }

    public Multimap<Long, String> getExpectedGreaterThanMedianMap() {
        Multimap<Long, String> map = HashMultimap.create();
        map.put(248L, "a");
        map.put(35L, "b");
        map.put(248L, "c");
        map.put(35L, "f");
        map.put(44L, "j");
        map.put(167L, "l");
        return map;
    }

    public long getExpectedGreaterThanMedianSum() {
        return 777;
    }

    public Map<String, Long> getExpectedGenresToPercentsMap() {
        long sum = getExpectedGreaterThanMedianSum();
        Map<String, Long> map = new HashMap<>();
        map.put("a", (248L * 100 / sum));
        map.put("b", (35L * 100 / sum));
        map.put("c", (248L * 100 / sum));
        map.put("f", (35L * 100 / sum));
        map.put("j", (44L * 100 / sum));
        map.put("l", (167L * 100 / sum));
        return map;
    }
}
