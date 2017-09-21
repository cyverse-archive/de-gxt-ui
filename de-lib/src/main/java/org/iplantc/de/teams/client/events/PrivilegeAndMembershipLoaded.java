package org.iplantc.de.teams.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event that will fire when EditTeamPresenter has collected the user's privileges and membership status for the team
 */
public class PrivilegeAndMembershipLoaded
        extends GwtEvent<PrivilegeAndMembershipLoaded.PrivilegeAndMembershipLoadedHandler> {
    public static interface PrivilegeAndMembershipLoadedHandler extends EventHandler {
        void onPrivilegeAndMembershipLoaded(PrivilegeAndMembershipLoaded event);
    }

    public interface HasPrivilegeAndMembershipLoadedHandlers {
        HandlerRegistration addPrivilegeAndMembershipLoadedHandler(PrivilegeAndMembershipLoadedHandler handler);
    }
    public static Type<PrivilegeAndMembershipLoadedHandler> TYPE =
            new Type<PrivilegeAndMembershipLoadedHandler>();

    private boolean isMember;
    private boolean hasVisibleMembers;
    private boolean isAdmin;

    public PrivilegeAndMembershipLoaded(boolean isAdmin, boolean isMember, boolean hasVisibleMembers) {
        this.isAdmin = isAdmin;
        this.isMember = isMember;
        this.hasVisibleMembers = hasVisibleMembers;
    }

    public boolean isMember() {
        return isMember;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean enableRequestToJoinTeam() {
        return !isMember && !isAdmin;
    }

    public Type<PrivilegeAndMembershipLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(PrivilegeAndMembershipLoadedHandler handler) {
        handler.onPrivilegeAndMembershipLoaded(this);
    }

    public boolean hasVisibleMembers() {
        return hasVisibleMembers;
    }
}
