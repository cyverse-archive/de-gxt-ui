package org.iplantc.de.shared;

import org.iplantc.de.client.models.WindowType;

/**
 * @author aramsey
 */
public interface DECallback<T> {

    WindowType getWindowType();

    void onFailure(Integer statusCode, Throwable exception);

    void onSuccess(T result);
}
