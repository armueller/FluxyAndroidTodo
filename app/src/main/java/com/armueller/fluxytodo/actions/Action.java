package com.armueller.fluxytodo.actions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by armueller on 4/10/15.
 */
public abstract interface Action {
    public abstract ActionType getType();
    public abstract DataBundle getData();
}
