package org.iplantc.de.theme.base.client.admin.communities;

import org.iplantc.de.admin.desktop.client.communities.ManageCommunitiesView;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.theme.base.client.apps.AppsMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author aramsey
 */
public class ManageCommunitiesViewDefaultAppearance implements ManageCommunitiesView.Appearance {

    private final IplantDisplayStrings iplantDisplayStrings;
    private final AppsMessages appsMessages;
    private IplantResources iplantResources;

    public ManageCommunitiesViewDefaultAppearance() {
        this(GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<AppsMessages> create(AppsMessages.class),
             GWT.<IplantResources> create(IplantResources.class));
    }

    ManageCommunitiesViewDefaultAppearance(final IplantDisplayStrings iplantDisplayStrings,
                                           final AppsMessages appsMessages,
                                           IplantResources iplantResources) {
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.appsMessages = appsMessages;
        this.iplantResources = iplantResources;
    }

    @Override
    public String communityNameLabel() {
        return appsMessages.communityName();
    }

    @Override
    public String communityDescLabel() {
        return iplantDisplayStrings.description();
    }

    @Override
    public String adminsSectionHeader() {
        return appsMessages.communityAdmins();
    }

    @Override
    public String adminPrivilegesExplanation() {
        return appsMessages.adminPrivilegesExplanation();
    }

    @Override
    public ImageResource deleteIcon() {
        return iplantResources.deleteIcon();
    }

    @Override
    public String loadingMask() {
        return iplantDisplayStrings.loadingMask();
    }

    @Override
    public String failedToAddCommunityAdmin(Subject admin, Group community) {
        return appsMessages.failedToAddCommunityAdmin(admin.getSubjectDisplayName(), community.getName());
    }

    @Override
    public String add() {
        return iplantDisplayStrings.add();
    }

    @Override
    public ImageResource addButton() {
        return iplantResources.add();
    }

    @Override
    public String remove() {
        return iplantDisplayStrings.remove();
    }

    @Override
    public String failedToRemoveCommunityAdmin(Subject admin, Group community) {
        return appsMessages.failedToRemoveCommunityAdmin(admin.getSubjectDisplayName(), community.getName());
    }
}
