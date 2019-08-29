package org.iplantc.de.client.services.converters;

import org.iplantc.de.shared.DECallback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

public class StringToSplittableDECallbackConverter extends DECallbackConverter<String, Splittable> {

    public StringToSplittableDECallbackConverter(DECallback<Splittable> callback) {
        super(callback);
    }

    @Override
    protected Splittable convertFrom(String object) {
        return StringQuoter.split(object);
    }

}
