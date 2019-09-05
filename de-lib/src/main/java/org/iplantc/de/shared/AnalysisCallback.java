package org.iplantc.de.shared;

import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author aramsey
 */
public abstract class AnalysisCallback<T> implements DECallback<T> {

    protected ReactSuccessCallback callback;

    @Override
    public List<WindowType> getWindowTypes() {
        return Lists.newArrayList(WindowType.ANALYSES);
    }
}
