package org.iplantc.de.diskResource.client.presenters.callbacks;

import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsFunction;

/**
 * Created by sriram on 3/13/18.
 */
@JsFunction
@SuppressWarnings("unusable-by-js")
public interface TagsFetchCallback {
    void onTagsFetched(Splittable tags);
}
