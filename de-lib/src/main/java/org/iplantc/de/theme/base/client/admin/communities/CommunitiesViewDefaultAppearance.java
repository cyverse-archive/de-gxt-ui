package org.iplantc.de.theme.base.client.admin.communities;

import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

import com.sencha.gxt.widget.core.client.tree.TreeStyle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aramsey
 */
public class CommunitiesViewDefaultAppearance implements AdminCommunitiesView.Appearance {

    private CommunityDisplayStrings displayStrings;
    private IplantResources iplantResources;
    private IplantDisplayStrings iplantDisplayStrings;

    public CommunitiesViewDefaultAppearance() {
        this(GWT.<CommunityDisplayStrings>create(CommunityDisplayStrings.class),
             GWT.<IplantResources>create(IplantResources.class),
             GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class));
    }

    CommunitiesViewDefaultAppearance(CommunityDisplayStrings displayStrings,
                                     IplantResources iplantResources,
                                     IplantDisplayStrings iplantDisplayStrings) {
        this.displayStrings = displayStrings;
        this.iplantResources = iplantResources;
        this.iplantDisplayStrings = iplantDisplayStrings;
    }

    @Override
    public String addCommunity() {
        return displayStrings.addCommunity();
    }

    @Override
    public ImageResource addIcon() {
        return iplantResources.add();
    }

    @Override
    public String deleteCommunity() {
        return displayStrings.deleteCommunity();
    }

    @Override
    public ImageResource deleteIcon() {
        return iplantResources.delete();
    }

    @Override
    public ImageResource blueFolder() {
        return iplantResources.category();
    }

    @Override
    public String editCommunity() {
        return displayStrings.editCommunity();
    }

    @Override
    public ImageResource editIcon() {
        return iplantResources.edit();
    }

    @Override
    public String emptySearchFieldText() {
        return iplantDisplayStrings.searchEmptyText();
    }

    @Override
    public String categorize() {
        return displayStrings.categorize();
    }

    @Override
    public String loadingMask() {
        return iplantDisplayStrings.loadingMask();
    }

    @Override
    public String name() {
        return iplantDisplayStrings.name();
    }

    @Override
    public String description() {
        return iplantDisplayStrings.description();
    }

    @Override
    public String externalAppDND(String appLabels) {
        return displayStrings.externalAppDND(appLabels);
    }

    @Override
    public String appAddedToCommunity(String appName, String communityName) {
        return displayStrings.appAddedToCommunity(appName, communityName);
    }

    @Override
    public void setTreeIcons(TreeStyle style) {
        style.setNodeCloseIcon(iplantResources.category());
        style.setNodeOpenIcon(iplantResources.category_open());
        style.setLeafIcon(iplantResources.subCategory());
    }

    @Override
    public String clearCommunitySelection() {
        return displayStrings.clearCommunitySelection();
    }

    @Override
    public String categorizeDialogWidth() {
        return "400px";
    }

    @Override
    public String categorizeDialogHeight() {
        return "400px";
    }

    @Override
    public String selectCommunitiesFor(App targetApp) {
        return displayStrings.selectCommunitiesFor(targetApp.getName());
    }

    @Override
    public String communityAvusSet(App app, List<Group> selectedCommunities) {
        if (selectedCommunities == null || selectedCommunities.size() == 0) {
            return displayStrings.appRemovedFromCommunities(app.getName());
        }
        List<String> communityNames = selectedCommunities.stream().map(Group::getName).collect(Collectors.toList());
        return displayStrings.appTaggedWithCommunities(app.getName(), communityNames);
    }

    @Override
    public String confirmDeleteCommunityTitle() {
        return displayStrings.confirmDeleteCommunityTitle();
    }

    @Override
    public String confirmDeleteCommunityMessage(String communityName) {
        return displayStrings.confirmDeleteCommunityMessage(communityName);
    }

    @Override
    public String communityDeleted(Group community) {
        return displayStrings.communityDeleted(community.getName());
    }

    @Override
    public String failedToAddCommunityAdmin(Subject admin, Group community) {
        return displayStrings.failedToAddCommunityAdmin(admin.getSubjectDisplayName(), community.getName());
    }

    @Override
    public String searchFieldWidth() {
        return "200px";
    }

    @Override
    public String communityPanelHeader() {
        return displayStrings.communityPanelHeader();
    }

    @Override
    public String hierarchyPreviewHeader() {
        return displayStrings.hierarchyPreviewHeader();
    }

    @Override
    public String communityTreePanel() {
        return displayStrings.communityTreePanel();
    }

    @Override
    public String hierarchyTreePanel() {
        return displayStrings.hierarchyTreePanel();
    }
}
