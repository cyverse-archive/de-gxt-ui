package org.iplantc.de.preferences.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 */
public class PrefDlgRetryUserSessionClicked
        extends GwtEvent<PrefDlgRetryUserSessionClicked.PrefDlgRetryUserSessionClickedHandler> {

    public interface PrefDlgRetryUserSessionClickedHandler extends EventHandler {
        void onPrefDlgRetryUserSessionClicked(PrefDlgRetryUserSessionClicked event);
    }

    public interface HasPrefDlgRetryUserSessionClickedHandlers {
        HandlerRegistration addPrefDlgRetryUserSessionClickedHandlers(PrefDlgRetryUserSessionClickedHandler handler);
    }

    public static Type<PrefDlgRetryUserSessionClickedHandler> TYPE =
            new Type<PrefDlgRetryUserSessionClickedHandler>();

    public Type<PrefDlgRetryUserSessionClickedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(PrefDlgRetryUserSessionClickedHandler handler) {
        handler.onPrefDlgRetryUserSessionClicked(this);
    }
}
