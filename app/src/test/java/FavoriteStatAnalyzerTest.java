import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.edu.cdu.fotius.lisun.musicplayer.recommendations.FavoritesStatAnalyser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class FavoriteStatAnalyzerTest {

    private FavoritesStatAnalyser favoritesStatAnalyser;
    private TestValuesGenerator testValuesGenerator;

    @Before
    public void oneTimeSetUp() {
        favoritesStatAnalyser = new FavoritesStatAnalyser();
        testValuesGenerator = new TestValuesGenerator();
    }

    @Test
    public void testUnduplicatedSortedListContent() {
        Multimap<Long, String> map = testValuesGenerator.getMapWithDuplicates();
        List<Long> list = favoritesStatAnalyser.unduplicatedSortedListFrom(map);
        assertTrue(list.equals(testValuesGenerator.getUnduplicatedSortedExpected()));
    }

    @Test
    public void testLessThanMedianMap() {
        Multimap<Long, String> init = testValuesGenerator.getMapWithDuplicates();
        List<Long> list = testValuesGenerator.getLessThanMedianList();
        favoritesStatAnalyser.removeLessThanMedianFromMap(init, list);
        assertTrue(init.equals(testValuesGenerator.getExpectedGreaterThanMedianMap()));
    }

    @Test
    public void testSumOfGreaterThanMedian() {
        long expected = testValuesGenerator.getExpectedGreaterThanMedianSum();
        long actual = favoritesStatAnalyser.sumOfGreaterThanMedian(testValuesGenerator
                .getExpectedGreaterThanMedianMap());
        assertEquals(expected, actual);
    }

    @Test
    public void testGenresToPercentsMap() {
        Map<String, Long> actual = favoritesStatAnalyser.genresToPercentsMap(
                getExpectedGreaterThanMedianMap(),
                getExpectedGreaterThanMedianSum());
        Map<String, Long> expected = getExpectedGenresToPercentsMap();
        assertTrue(expected.equals(actual));
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
