package org.iplantc.de.admin.apps.client.presenter.callbacks;

import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsFunction;

/**
 * Created by sriram on 3/27/18.
 */
@JsFunction
public interface AppStatsSearchCallback {
    void onSearchCompleted(Splittable result);
}
