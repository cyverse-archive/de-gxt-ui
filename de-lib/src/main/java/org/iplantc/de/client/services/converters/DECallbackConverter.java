package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.shared.DECallback;

/**
 * @author aramsey
 */
public abstract class DECallbackConverter<F, T> implements DECallback<F> {

    private final DECallback<T> callback;

    public DECallbackConverter(DECallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onSuccess(F result) {
        this.callback.onSuccess(convertFrom(result));
    }

    @Override
    public void onFailure(Integer statusCode, Throwable caught) {
        this.callback.onFailure(statusCode, caught);
    }

    @Override
    public WindowType getWindowType() {
        return callback.getWindowType();
    }

    /**
     * Converts from the given object, to the object T.
     *
     * @param object the object to be converted.
     * @return the converted object.
     */
    protected abstract T convertFrom(F object);

}
