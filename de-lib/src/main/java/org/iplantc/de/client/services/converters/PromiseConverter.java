package org.iplantc.de.client.services.converters;

import com.google.gwt.query.client.plugins.deferred.PromiseRPC;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author aramsey
 */
public abstract class PromiseConverter<F, T> implements AsyncCallback<F> {

    private final PromiseRPC<T> promiseRpc;

    public PromiseConverter(PromiseRPC<T> promiseRPC) {
        this.promiseRpc = promiseRPC;
    }

    @Override
    public void onFailure(Throwable caught) {
        promiseRpc.onFailure(caught);
    }


    @Override
    public void onSuccess(F result) {
        promiseRpc.onSuccess(convertFrom(result));
    }

    protected abstract T convertFrom(F object);
}
