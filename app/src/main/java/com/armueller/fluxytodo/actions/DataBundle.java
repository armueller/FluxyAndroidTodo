package com.armueller.fluxytodo.actions;

import java.util.HashMap;

/**
 * Created by armueller on 4/10/15.
 */
public class DataBundle<T extends DataKey> {
    HashMap<T, Object> map;

    public DataBundle() {
        map = new HashMap<T, Object>();
    }

    public void put(T key, Object data) {
        map.put(key, data);
    }

    public Object get(T key, Object defaultValue) {
        Object o = map.get(key);
        if (o == null) {
            return defaultValue;
        }

        return o;
    }
}
