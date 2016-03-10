import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.edu.cdu.fotius.lisun.musicplayer.recommendations.FavoritesStatAnalyser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class FavoriteStatAnalyzerTest {

    private FavoritesStatAnalyser favoritesStatAnalyser;

    @Before
    public void oneTimeSetUp() {
        favoritesStatAnalyser = new FavoritesStatAnalyser();
    }

    @Test
    public void testUnduplicatedSortedListSize() {
        Map<String, Long> map = getMapWithDuplicates();
        List<Long> list = favoritesStatAnalyser.unduplicatedSortedListFrom(map);
        assertEquals(7, list.size());
    }

    @Test
    public void testUnduplicatedSortedListContent() {
        Map<String, Long> map = getMapWithDuplicates();
        List<Long> list = favoritesStatAnalyser.unduplicatedSortedListFrom(map);
        assertTrue(list.equals(getUnduplicatedSortedExpected()));
    }


    private Map<String, Long> getMapWithDuplicates() {
        Map<String, Long> map = new HashMap<>();
        map.put("a", 248L);
        map.put("b", 35L);
        map.put("c", 248L);
        map.put("d", 1L);
        map.put("e", 15L);
        map.put("f", 35L);
        map.put("g", 1L);
        map.put("h", 1L);
        map.put("j", 44L);
        map.put("k", 2L);
        map.put("l", 167L);
        return map;
    }

    private List<Long> getUnduplicatedSortedExpected() {
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

    @Test
    public void testMedian1(){
        long median = favoritesStatAnalyser.median(getListForMedian1());
        assertEquals(12L, median);
    }

    private List<Long> getListForMedian1() {
        List<Long> list = new ArrayList<>();
        list.add(12L);
        return list;
    }

    @Test
    public void testMedian2(){
        long median = favoritesStatAnalyser.median(getListForMedian2());
        assertEquals(130L, median);
    }

    private List<Long> getListForMedian2() {
        List<Long> list = new ArrayList<>();
        list.add(12L);
        list.add(248L);
        return list;
    }

    @Test
    public void testMedian3(){
        long median = favoritesStatAnalyser.median(getListForMedian3());
        assertEquals(248L, median);
    }

    private List<Long> getListForMedian3() {
        List<Long> list = new ArrayList<>();
        list.add(12L);
        list.add(248L);
        list.add(249L);
        return list;
    }

    @Test
    public void testMedian4(){
        long median = favoritesStatAnalyser.median(getListForMedian4());
        assertEquals(376L, median);
    }

    private List<Long> getListForMedian4() {
        List<Long> list = new ArrayList<>();
        list.add(12L);
        list.add(248L);
        list.add(255L);
        list.add(376L);
        list.add(387L);
        list.add(476L);
        list.add(876L);
        return list;
    }

    @Test
    public void testMedian5(){
        long median = favoritesStatAnalyser.median(getListForMedian5());
        assertEquals(381L, median);
    }

    private List<Long> getListForMedian5() {
        List<Long> list = new ArrayList<>();
        list.add(12L);
        list.add(248L);
        list.add(255L);
        list.add(376L);
        list.add(387L);
        list.add(476L);
        list.add(876L);
        list.add(932L);
        return list;
    }

    @Test
    public void testGreaterThanMedianList1() {
        long median = 145L;
        List<Long> actual = favoritesStatAnalyser
                .greaterThanMedianList(median, getInitListForGreaterThanMedianTest1());
        assertTrue(actual.equals(getExpectedListForGreaterThanMedianTest1()));
    }

    private List<Long> getInitListForGreaterThanMedianTest1() {
        List<Long> list = new ArrayList<>();
        list.add(145L);
        return list;
    }

    private List<Long> getExpectedListForGreaterThanMedianTest1() {
        List<Long> list = new ArrayList<>();
        list.add(145L);
        return list;
    }

    @Test
    public void testGreaterThanMedianList2() {
        long median = 0L;
        List<Long> actual = favoritesStatAnalyser
                .greaterThanMedianList(median, getInitListForGreaterThanMedianTest2());
        assertTrue(actual.equals(getExpectedListForGreaterThanMedianTest2()));
    }

    private List<Long> getInitListForGreaterThanMedianTest2() {
        List<Long> list = new ArrayList<>();
        return list;
    }

    private List<Long> getExpectedListForGreaterThanMedianTest2() {
        List<Long> list = new ArrayList<>();
        return list;
    }

    @Test
    public void testGreaterThanMedianList3() {
        long median = 376L;
        List<Long> actual = favoritesStatAnalyser
                .greaterThanMedianList(median, getInitListForGreaterThanMedianTest3());
        assertTrue(actual.equals(getExpectedListForGreaterThanMedianTest3()));
    }

    private List<Long> getInitListForGreaterThanMedianTest3() {
        List<Long> list = new ArrayList<>();
        list.add(12L);
        list.add(248L);
        list.add(255L);
        list.add(376L);
        list.add(387L);
        list.add(476L);
        list.add(876L);
        return list;
    }

    private List<Long> getExpectedListForGreaterThanMedianTest3() {
        List<Long> list = new ArrayList<>();
        list.add(376L);
        list.add(387L);
        list.add(476L);
        list.add(876L);
        return list;
    }

    @Test
    public void testGreaterThanMedianMap1() {
        Map<Long, String> actual = favoritesStatAnalyser.greaterThanMedianMap(getInitMapForGreaterThanMedianMapTest1(), getInitListForGreaterThanMedianMapTest1());
        assertTrue(actual.equals(getExpectedMapForGreaterThanMedianMapTest1()));
    }

    private BiMap<String, Long> getInitMapForGreaterThanMedianMapTest1() {
        BiMap<String, Long> map = HashBiMap.create();
        return map;
    }

    private List<Long> getInitListForGreaterThanMedianMapTest1() {
        List<Long> list = new ArrayList<>();
        return list;
    }

    private Map<Long, String> getExpectedMapForGreaterThanMedianMapTest1() {
        return new HashMap<>();
    }

    @Test
    public void testGreaterThanMedianMap2() {
        Map<Long, String> actual = favoritesStatAnalyser.greaterThanMedianMap(getInitMapForGreaterThanMedianMapTest2(), getInitListForGreaterThanMedianMapTest2());
        assertTrue(actual.equals(getExpectedMapForGreaterThanMedianMapTest2()));
    }

    private BiMap<String, Long> getInitMapForGreaterThanMedianMapTest2() {
        BiMap<String, Long> map = HashBiMap.create();
        map.put("a", 248L);
        map.put("b", 35L);
        map.put("c", 248L);
        map.put("d", 1L);
        map.put("e", 15L);
        map.put("f", 35L);
        map.put("g", 1L);
        map.put("h", 1L);
        map.put("j", 44L);
        map.put("k", 2L);
        map.put("l", 167L);
        return map;
    }

    private List<Long> getInitListForGreaterThanMedianMapTest2() {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(15L);
        list.add(44L);
        list.add(167L);
        list.add(248L);
        return list;
    }

    private Map<Long, String> getExpectedMapForGreaterThanMedianMapTest2() {
        Map<Long, String> map = new HashMap<>();
        map.put(1L, "d");
        map.put(2L, "d");
        map.put(15L, "d");
        map.put(44L, "d");
        map.put(167L, "d");
        map.put(248L, "d");
        return map;
    }
}
