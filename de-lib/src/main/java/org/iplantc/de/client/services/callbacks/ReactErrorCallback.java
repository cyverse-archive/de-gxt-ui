package org.iplantc.de.client.services.callbacks;

/**
 * Created by sriram on 5/11/18.
 */
import jsinterop.annotations.JsFunction;

/**
 * Created by sriram on 3/13/18.
 */
@JsFunction
public interface ReactErrorCallback {
   void onError(int httpStatusCode, String errorMessage);
}

