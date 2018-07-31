package org.iplantc.de.desktop.client.presenter.callbacks;

import jsinterop.annotations.JsFunction;

/**
 * Created by sriram on 6/5/18.
 */
@JsFunction
public interface NotificationMarkAsSeenCallback {
    void onMarkSeen(int unSeenCount);
}
