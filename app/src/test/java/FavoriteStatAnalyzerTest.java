import com.google.common.collect.Multimap;

import org.junit.Before;
import org.junit.Test;

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
    public void testLessThanMedianListOdd() {
        List<Long> init = testValuesGenerator.getInitListForLessThanMedianTest();
        List<Long> actual = favoritesStatAnalyser.getLessThanMedianList(init);
        assertTrue(actual.equals(testValuesGenerator.getLessThanMedianList()));
    }

    @Test
    public void testLessThanMedianMap() {
        Multimap<Long, String> init = testValuesGenerator.getMapWithDuplicates();
        List<Long> list = testValuesGenerator.getLessThanMedianList();
        favoritesStatAnalyser.removeLessThanMedianFromMap(init, list);
        assertTrue(init.equals(testValuesGenerator.getExpectedGreaterThanMedianMap()));
    }

    public void testSumOfGreaterThanMedian() {
        long expected = testValuesGenerator.getExpectedGreaterThanMedianSum();
        long actual = favoritesStatAnalyser.sumOfGreaterThanMedian(testValuesGenerator.getExpectedGreaterThanMedianMap());
        assertEquals(expected, actual);
    }

    @Test
    public void testGenresToPercentsMap() {
        Map<String, Long> actual = favoritesStatAnalyser.genresToPercentsMap(testValuesGenerator.getExpectedGreaterThanMedianMap(),
                testValuesGenerator.getExpectedGreaterThanMedianSum());
        Map<String, Long> expected = testValuesGenerator.getExpectedGenresToPercentsMap();
        assertTrue(expected.equals(actual));
    }
}
