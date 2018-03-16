package org.iplantc.de.diskResource.client.presenters.callbacks;

import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsFunction;

/**
 * Created by sriram on 3/9/18.
 */
@JsFunction
@SuppressWarnings("unusable-by-js")
public interface TagsSearchCallback {
    void searchComplete(Splittable[] tags);
}
