package org.iplantc.de.theme.base.client.admin.communities;

import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.admin.desktop.client.ontologies.OntologiesView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.theme.base.client.admin.BelphegorErrorStrings;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;

import com.sencha.gxt.core.client.XTemplates;

import java.util.List;

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
