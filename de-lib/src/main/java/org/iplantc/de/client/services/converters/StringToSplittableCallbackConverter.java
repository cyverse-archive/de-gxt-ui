package org.iplantc.de.client.services.converters;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

public class StringToSplittableCallbackConverter extends AsyncCallbackConverter<String, Splittable> {

    public StringToSplittableCallbackConverter(AsyncCallback<Splittable> callback) {
        super(callback);
    }

    @Override
    protected Splittable convertFrom(String object) {
        return StringQuoter.split(object);
    }

}
