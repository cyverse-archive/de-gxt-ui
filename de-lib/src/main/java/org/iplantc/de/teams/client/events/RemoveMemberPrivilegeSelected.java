package org.iplantc.de.teams.client.events;

import org.iplantc.de.client.models.groups.Privilege;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class RemoveMemberPrivilegeSelected
        extends GwtEvent<RemoveMemberPrivilegeSelected.RemoveMemberPrivilegeSelectedHandler> {
    public static interface RemoveMemberPrivilegeSelectedHandler extends EventHandler {
        void onRemoveMemberPrivilegeSelected(RemoveMemberPrivilegeSelected event);
    }

    public interface HasRemoveMemberPrivilegeSelectedHandlers {
        HandlerRegistration addRemoveMemberPrivilegeSelectedHandler(RemoveMemberPrivilegeSelectedHandler handler);
    }

    public static Type<RemoveMemberPrivilegeSelectedHandler> TYPE =
            new Type<RemoveMemberPrivilegeSelectedHandler>();

    private Privilege privilege;

    public RemoveMemberPrivilegeSelected(Privilege privilege) {
        this.privilege = privilege;
    }

    public Type<RemoveMemberPrivilegeSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RemoveMemberPrivilegeSelectedHandler handler) {
        handler.onRemoveMemberPrivilegeSelected(this);
    }

    public Privilege getPrivilege() {
        return privilege;
    }
}
