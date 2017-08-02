package org.iplantc.de.intercom.client;

/**
 * Created by sriram on 8/1/17.
 */
public class IntercomFacade {

    public static native void login(String userId,
                                    String email,
                                    String appId,
                                    String companyId,
                                    String companyName) /*-{
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

    }-*/;

    public static native void logout() /*-{
        $wnd.Intercom('shutdown');
    }-*/;

}
