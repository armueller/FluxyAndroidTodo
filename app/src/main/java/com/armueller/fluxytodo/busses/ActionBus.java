package com.armueller.fluxytodo.busses;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Inject;

/**
 * Created by armueller on 3/8/15.
 */
public class ActionBus extends Bus {
    @Inject
    public ActionBus() {
        super(ThreadEnforcer.ANY);
    }
}
