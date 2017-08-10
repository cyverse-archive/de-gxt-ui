package org.iplantc.de.teams.client.events;

import org.iplantc.de.client.models.groups.Privilege;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class RemoveNonMemberPrivilegeSelected
        extends GwtEvent<RemoveNonMemberPrivilegeSelected.RemoveNonMemberPrivilegeSelectedHandler> {
    public static interface RemoveNonMemberPrivilegeSelectedHandler extends EventHandler {
        void onRemoveNonMemberPrivilegeSelected(RemoveNonMemberPrivilegeSelected event);
    }

    public interface HasRemoveNonMemberPrivilegeSelectedHandlers {
        HandlerRegistration addRemoveNonMemberPrivilegeSelectedHandler(RemoveNonMemberPrivilegeSelectedHandler handler);
    }

    public static Type<RemoveNonMemberPrivilegeSelectedHandler> TYPE =
            new Type<RemoveNonMemberPrivilegeSelectedHandler>();

    private Privilege privilege;

    public RemoveNonMemberPrivilegeSelected(Privilege privilege){
        this.privilege = privilege;
    }

    public Type<RemoveNonMemberPrivilegeSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RemoveNonMemberPrivilegeSelectedHandler handler) {
        handler.onRemoveNonMemberPrivilegeSelected(this);
    }

    public Privilege getPrivilege() {
        return privilege;
    }
}
