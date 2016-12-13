package org.iplantc.de.apps.client.events;

import org.iplantc.de.apps.client.events.EditAppEvent.EditAppEventHandler;
import org.iplantc.de.client.models.HasQualifiedId;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EditAppEvent extends GwtEvent<EditAppEventHandler> {

    public interface EditAppEventHandler extends EventHandler {

        void onEditApp(EditAppEvent event);
    }

    public static final GwtEvent.Type<EditAppEventHandler> TYPE = new GwtEvent.Type<>();
    private final HasQualifiedId appToEdit;
    private final boolean isUserIntegratorAndAppPublic;

    public EditAppEvent(HasQualifiedId appToEdit, boolean isUserIntegratorAndAppPublic) {
        this.appToEdit = appToEdit;
        this.isUserIntegratorAndAppPublic = isUserIntegratorAndAppPublic;
    }

    @Override
    protected void dispatch(EditAppEventHandler handler) {
        handler.onEditApp(this);
    }

    @Override
    public GwtEvent.Type<EditAppEventHandler> getAssociatedType() {
        return TYPE;
    }

    public HasQualifiedId getAppToEdit() {
        return appToEdit;
    }

    public boolean isUserIntegratorAndAppPublic() {
        return isUserIntegratorAndAppPublic;
    }

}