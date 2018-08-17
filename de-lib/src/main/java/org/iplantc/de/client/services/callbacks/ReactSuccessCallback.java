package org.iplantc.de.client.services.callbacks;

import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsFunction;

/**
 * A generic callback function to talk between GWT and React
 */
@JsFunction
@SuppressWarnings("unusable-by-js")
public interface ReactSuccessCallback {
    void onSuccess(Splittable data);
}
