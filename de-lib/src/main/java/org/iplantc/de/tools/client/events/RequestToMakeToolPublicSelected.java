package org.iplantc.de.tools.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Created by sriram on 5/15/17.
 */
public class RequestToMakeToolPublicSelected extends GwtEvent<RequestToMakeToolPublicSelected.RequestToMakeToolPublicSelectedHandler> {


    public static final Type<RequestToMakeToolPublicSelectedHandler> TYPE = new Type<>();

    @Override
    public Type<RequestToMakeToolPublicSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RequestToMakeToolPublicSelectedHandler requestToMakeToolPublicSelectedHandler) {
        requestToMakeToolPublicSelectedHandler.onRequestToMakeToolPublicSelected(this);
    }

    public static interface HasRequestToMakeToolPublicSelectedHandlers {
        HandlerRegistration addRequestToMakeToolPublicSelectedHandler(RequestToMakeToolPublicSelectedHandler handler);
    }

    public static interface RequestToMakeToolPublicSelectedHandler extends EventHandler {
        void onRequestToMakeToolPublicSelected(RequestToMakeToolPublicSelected event);
    }
}
