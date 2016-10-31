package org.iplantc.de.shared;

import org.iplantc.de.client.models.WindowType;

/**
 * @author aramsey
 */
public abstract class AppsCallback<T> implements DECallback<T> {

    @Override
    public WindowType getWindowType() {
        return WindowType.APPS;
    }
}
