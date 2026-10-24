package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyCacheTest {

    private long now = 0;

    private MyCache<String, String> cache(int capacity, long ttl) {
        return new MyCache<>(capacity, ttl, () -> now);
    }

    @Test
    void putAndGet() {
        MyCache<String, String> cache = cache(3, 0);
        cache.put("a", "1");
        assertEquals("1", cache.get("a"));
        assertNull(cache.get("missing"));
        assertTrue(cache.containsKey("a"));
        assertEquals(1, cache.size());
    }

    @Test
    void nullKeyOrValueThrows() {
        MyCache<String, String> cache = cache(3, 0);
        assertThrows(NullPointerException.class, () -> cache.put(null, "1"));
        assertThrows(NullPointerException.class, () -> cache.put("a", null));
        assertThrows(NullPointerException.class, () -> cache.get(null));
        assertThrows(NullPointerException.class, () -> cache.containsKey(null));
        assertThrows(NullPointerException.class, () -> cache.remove(null));
    }

    @Test
    void invalidCapacityThrows() {
        assertThrows(IllegalArgumentException.class, () -> new MyCache<String, String>(0, 0));
    }

    @Test
    void putExistingKeyUpdatesValue() {
        MyCache<String, String> cache = cache(2, 0);
        cache.put("a", "1");
        cache.put("a", "2");
        assertEquals("2", cache.get("a"));
        assertEquals(1, cache.size());
    }

    @Test
    void evictsLeastRecentlyUsed() {
        MyCache<String, String> cache = cache(2, 0);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.get("a");          // тепер "b" найдавніший за використанням
        cache.put("c", "3");     // переповнення: має піти "b"

        assertNull(cache.get("b"));
        assertEquals("1", cache.get("a"));
        assertEquals("3", cache.get("c"));
        assertEquals(2, cache.size());
    }

    @Test
    void entryExpiresAfterTtl() {
        MyCache<String, String> cache = cache(3, 100);
        cache.put("a", "1");

        now = 99;
        assertEquals("1", cache.get("a"));

        now = 100;
        assertNull(cache.get("a"));
        assertFalse(cache.containsKey("a"));
        assertEquals(0, cache.size());
    }

    @Test
    void expiredEntryIsDroppedBeforeLiveOneWhenEvicting() {
        MyCache<String, String> cache = cache(2, 100);
        now = 0;
        cache.put("a", "1");     // застаріє на 100
        now = 50;
        cache.put("b", "2");     // застаріє на 150
        now = 60;
        cache.get("a");          // "a" стала найсвіжішою, "b" найдавнішою
        now = 100;
        cache.put("c", "3");     // "a" вже застаріла, її й видаляємо, а не живу "b"

        assertEquals("2", cache.get("b"));
        assertEquals("3", cache.get("c"));
        assertNull(cache.get("a"));
    }

    @Test
    void putRefreshesTtl() {
        MyCache<String, String> cache = cache(3, 100);
        cache.put("a", "1");
        now = 80;
        cache.put("a", "2");
        now = 150;
        assertEquals("2", cache.get("a"));
    }

    @Test
    void removeReturnsValueAndDeletesEntry() {
        MyCache<String, String> cache = cache(3, 0);
        cache.put("a", "1");
        assertEquals("1", cache.remove("a"));
        assertNull(cache.remove("a"));
        assertEquals(0, cache.size());
    }

    @Test
    void clearEmptiesCache() {
        MyCache<String, String> cache = cache(3, 0);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.clear();
        assertEquals(0, cache.size());
        assertNull(cache.get("a"));
    }
}