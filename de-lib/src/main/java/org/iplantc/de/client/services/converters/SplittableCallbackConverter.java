package org.iplantc.de.client.services.converters;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

/**
 * @author aramsey
 *
 * A callback converter that returns a Splittable object
 */
public class SplittableCallbackConverter extends AsyncCallbackConverter<String, Splittable> {

    public SplittableCallbackConverter(AsyncCallback<Splittable> callback) {
        super(callback);
    }

    @Override
    protected Splittable convertFrom(String result) {
        return StringQuoter.split(result);
    }

}
