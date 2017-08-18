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
    private boolean hasOptInPrivilege;
    private boolean isAdmin;

    public PrivilegeAndMembershipLoaded(boolean isAdmin,
                                        boolean hasOptInPrivilege,
                                        boolean isMember) {
        this.isAdmin = isAdmin;
        this.hasOptInPrivilege = hasOptInPrivilege;
        this.isMember = isMember;
    }

    public boolean enableLeaveTeam() {
        return isMember;
    }

    public boolean enableDeleteTeam() {
        return isAdmin;
    }

    public boolean enableJoinTeam() {
        return !isMember && !isAdmin && hasOptInPrivilege;
    }

    public boolean enableRequestToJoinTeam() {
        return !isMember && !isAdmin && !hasOptInPrivilege;
    }

    public Type<PrivilegeAndMembershipLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(PrivilegeAndMembershipLoadedHandler handler) {
        handler.onPrivilegeAndMembershipLoaded(this);
    }
}
