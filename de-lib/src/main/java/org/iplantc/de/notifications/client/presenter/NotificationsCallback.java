package org.iplantc.de.notifications.client.presenter;

import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsFunction;

/**
 * Created by sriram on 7/12/18.
 */
@JsFunction
@SuppressWarnings("unusable-by-js")
public interface NotificationsCallback {
    void onFetchNotifications(Splittable notifications, int total);
}
