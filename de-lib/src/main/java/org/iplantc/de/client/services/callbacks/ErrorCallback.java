package org.iplantc.de.client.services.callbacks;

/**
 * Created by sriram on 5/11/18.
 */
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsFunction;

/**
 * Created by sriram on 3/13/18.
 */
@JsFunction
@SuppressWarnings("unusable-by-js")
public interface ErrorCallback {
   void onError(int httpStatusCode, String errorMessage);
}

