package org.iplantc.de.preferences.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event description of Reset HPC Token clicked from preference dialog
 * Created by sriram on 8/28/17.
 */
public class ResetHpcTokenClicked extends GwtEvent<ResetHpcTokenClicked.ResetHpcTokenClickedHandler> {


    public static Type<ResetHpcTokenClickedHandler>  TYPE = new Type<>();

    @Override
    public Type<ResetHpcTokenClickedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ResetHpcTokenClickedHandler resetHpcTokenClickedHandler) {
        resetHpcTokenClickedHandler.onResetHpcClicked(this);
    }

    public interface  ResetHpcTokenClickedHandler extends EventHandler {
        void onResetHpcClicked(ResetHpcTokenClicked event);
    }

    public interface HasResetHpcLabelClickedHandlers {
        HandlerRegistration addResetHpcTokenClickedHandlers(ResetHpcTokenClickedHandler handler);
    }


}
