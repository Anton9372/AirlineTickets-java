package airline.tickets.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public final class InMemoryCache<K, V> {

    private final Map<K, V> cacheMap;
    private final int maxSize;

    public InMemoryCache(@Value("${cache.maxSize}") final int maxSize) {
        this.maxSize = maxSize;
        this.cacheMap = new LinkedHashMap<>(maxSize);
    }

    public V get(final K key) {
        return cacheMap.get(key);
    }

    public void put(final K key, final V value) {
        if (cacheMap.size() >= maxSize) {
            evictOldestEntry();
        }
        cacheMap.put(key, value);
    }

    public void remove(final K key) {
        cacheMap.remove(key);
    }

    public void clear() {
        cacheMap.clear();
    }

    private void evictOldestEntry() {
        if (!cacheMap.isEmpty()) {
            K eldestKey = cacheMap.keySet().iterator().next();
            cacheMap.remove(eldestKey);
        }
    }
}
