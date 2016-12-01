package org.iplantc.de.shared.events;

import org.iplantc.de.client.models.WindowType;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.List;

/**
 * @author aramsey
 */
public class ServiceDown extends GwtEvent<ServiceDown.ServiceDownHandler> {
    public static Type<ServiceDownHandler> TYPE = new Type<ServiceDownHandler>();
    private List<WindowType> windowTypes;
    private SelectEvent.SelectHandler handler;

    public ServiceDown(List<WindowType> windowTypes, SelectEvent.SelectHandler handler) {
        this.windowTypes = windowTypes;
        this.handler = handler;
    }

    public Type<ServiceDownHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ServiceDownHandler handler) {
        handler.onServiceDown(this);
    }

    public List<WindowType> getWindowTypes() {
        return windowTypes;
    }

    public SelectEvent.SelectHandler getSelectionHandler() {
        return handler;
    }

    public static interface ServiceDownHandler extends EventHandler {
        void onServiceDown(ServiceDown event);
    }
}
