package io.kimmking.kmq.store;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/14 下午6:19
 */

@Data
public class Indexer {

    static Map<String, List<Entry>> index = new HashMap<>();
    static Map<Long, Entry> mappings = new HashMap<>();

    @Data
    public static class Entry {
        long id;
        int offset;
        int length;

        public Entry(long id, int offset, int length) {
            this.offset = offset;
            this.length = length;
        }
    }

    public static void addEntry(String topic, long id, int offset, int length) {
        List<Entry> entries = index.get(topic);
        if(entries == null) {
            entries = new java.util.ArrayList<>();
            index.put(topic, entries);
        }
        Entry e = new Entry(id, offset, length);
        entries.add(e);
        mappings.put(id, e);
    }

    public static List<Entry> getEntries(String topic) {
        return index.get(topic);
    }

    public static Entry getEntry(long id) {
        return mappings.get(id);
    }

}
