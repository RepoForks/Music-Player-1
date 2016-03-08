package ua.edu.cdu.fotius.lisun.musicplayer.recommendations;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.edu.cdu.fotius.lisun.musicplayer.recommendations.FavoritesStatAnalyser;

public class FavoriteStatAnalyzerTest extends TestCase{

    private FavoritesStatAnalyser favoritesStatAnalyser;

    @BeforeClass
    public void oneTimeSetUp() {
        favoritesStatAnalyser = new FavoritesStatAnalyser();
    }

    @Test
    public void testUnduplicatedSortedListFrom() {
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
        List<Long> list = favoritesStatAnalyser.unduplicatedSortedListFrom(map);
        assertEquals(7, list.size());
    }
}
