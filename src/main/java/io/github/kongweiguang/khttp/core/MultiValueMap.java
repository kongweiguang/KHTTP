package io.github.kongweiguang.khttp.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 多个值的map
 *
 * @author kongweiguang
 */
public class MultiValueMap<K, V> {

    private final Map<K, List<V>> map = new HashMap<>();

    public Map<K, List<V>> map() {
        return map;
    }

    public void put(K key, V value) {
        final List<V> list = map.computeIfAbsent(key, k -> new ArrayList<>());
        list.add(value);
    }

    public List<V> get(K key) {
        return map.getOrDefault(key, new ArrayList<>());
    }

    public List<V> removeKey(K key) {
        return map.remove(key);
    }

    public boolean removeValue(K key, V value) {
        final List<V> list = map.computeIfAbsent(key, k -> new ArrayList<>());
        return list.remove(value);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MultiValueMap.class.getSimpleName() + "[", "]")
                .add("map=" + map)
                .toString();
    }
}
