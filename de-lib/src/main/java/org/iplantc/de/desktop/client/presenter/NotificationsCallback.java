package org.iplantc.de.desktop.client.presenter;

import com.google.web.bindery.autobean.shared.Splittable;
import jsinterop.annotations.JsFunction;

/**
 * Created by sriram on 5/1/18.
 */
@JsFunction
@SuppressWarnings("unusable-by-js")
public interface NotificationsCallback {
    void onFetchNotifications(Splittable notifications);
}
