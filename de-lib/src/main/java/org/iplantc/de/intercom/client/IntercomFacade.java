package org.iplantc.de.intercom.client;

import org.iplantc.de.shared.DEProperties;

import com.google.web.bindery.autobean.shared.Splittable;

/**
 * A facade class that manages interaction with Intercom
 * Created by sriram on 8/1/17.
 */
public class IntercomFacade {

    static boolean intercom_enabled = DEProperties.getInstance().isIntercomEnabled();

    /**
     * Log User login event
     *
     * @param userId      Username of the user logged in
     * @param email       Email of the user logged in
     * @param appId       Intercom app id to use
     * @param companyId   Company unique id
     * @param companyName Company unique name
     */
    public static native void login(String userId, String email, String appId, String companyId, String companyName) /*-{
        var enabled = @org.iplantc.de.intercom.client.IntercomFacade::intercom_enabled;
        if (enabled) {
            $wnd.Intercom('boot', {
                app_id: appId,
                email: email,
                user_id: userId,
                created_at: Date.now(),
                company: {
                    id: companyId,
                    name: companyName
                }
            })
        }

    }-*/;

    /**
     * Log User logout event
     */
    public static native void logout() /*-{
        var enabled = @org.iplantc.de.intercom.client.IntercomFacade::intercom_enabled;
        if (enabled) {
            $wnd.Intercom('shutdown');
        }
    }-*/;


    /**
     * Log user events
     *
     * @param event        Event name
     * @param metadataJson Event metadata
     */
    public static native void trackEvent(String event, Splittable metadataJson) /*-{
        var enabled = @org.iplantc.de.intercom.client.IntercomFacade::intercom_enabled;
        if (enabled) {
            if (metadataJson) {
                $wnd.Intercom('trackEvent', event, metadataJson);
            } else {
                $wnd.Intercom('trackEvent', event);
            }
        }
    }-*/;


}
