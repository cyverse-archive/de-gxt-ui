package org.iplantc.de.apps.client.events;

import org.iplantc.de.client.models.AppTypeFilter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by sriram on 7/3/18.
 */
public class AppTypeFilterChangedEvent
        extends GwtEvent<AppTypeFilterChangedEvent.AppTypeFilterChangedEventHandler> {

    public static final GwtEvent.Type<AppTypeFilterChangedEventHandler> TYPE = new GwtEvent.Type<>();
    private final AppTypeFilter filter;

    public AppTypeFilterChangedEvent(AppTypeFilter filter) {
        this.filter = filter;
    }

    @Override
    public Type<AppTypeFilterChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AppTypeFilterChangedEventHandler handler) {
        handler.onTypeFilterChanged(this);
    }

    public AppTypeFilter getFilter() {
        return filter;
    }

    public interface AppTypeFilterChangedEventHandler extends EventHandler {
        void onTypeFilterChanged(AppTypeFilterChangedEvent event);
    }
}
