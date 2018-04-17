package org.iplantc.de.diskResource.client.events.search;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * An event that fires when the user clicks on the option to save a disk resource query
 */
public class SaveDataSearchClicked extends GwtEvent<SaveDataSearchClicked.SaveDataSearchClickedHandler> {

    public interface SaveDataSearchClickedHandler extends EventHandler {
        void onSaveDataSearchClicked(SaveDataSearchClicked event);
    }

    public interface HasSaveDataSearchClickedHandlers {
        HandlerRegistration addSaveDataSearchClickedHandler(SaveDataSearchClickedHandler handler);
    }

    public static Type<SaveDataSearchClickedHandler> TYPE = new Type<SaveDataSearchClickedHandler>();
    private String name;
    private Splittable query;

    public SaveDataSearchClicked(String name, Splittable query) {
        this.name = name;
        this.query = query;
    }

    public String getName() {
        return name;
    }

    public Splittable getQuery() {
        return query;
    }

    public Type<SaveDataSearchClickedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SaveDataSearchClickedHandler handler) {
        handler.onSaveDataSearchClicked(this);
    }
}
