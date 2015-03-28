package com.armueller.fluxytodo.busses;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by armueller on 3/8/15.
 */
public class DataBus extends Bus {
    public DataBus() {
        super(ThreadEnforcer.MAIN);
    }

    public DataBus(ThreadEnforcer thread) {
        super(thread);
    }
}
