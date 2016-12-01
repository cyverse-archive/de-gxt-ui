package org.iplantc.de.shared;

import org.iplantc.de.client.models.WindowType;

import java.util.List;

/**
 * @author aramsey
 */
public interface DECallback<T> {

    List<WindowType> getWindowTypes();

    void onFailure(Integer statusCode, Throwable exception);

    void onSuccess(T result);
}
