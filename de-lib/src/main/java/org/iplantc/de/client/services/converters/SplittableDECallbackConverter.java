package org.iplantc.de.client.services.converters;

import org.iplantc.de.shared.DECallback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

/**
 * @author aramsey
 *
 * A callback converter that returns a Splittable object
 */
public class SplittableDECallbackConverter extends DECallbackConverter<String, Splittable> {

    public SplittableDECallbackConverter(DECallback<Splittable> callback) {
        super(callback);
    }

    @Override
    protected Splittable convertFrom(String result) {
        return StringQuoter.split(result);
    }

}
