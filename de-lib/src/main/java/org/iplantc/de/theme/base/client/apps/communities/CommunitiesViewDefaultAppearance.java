package org.iplantc.de.theme.base.client.apps.communities;

import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.theme.base.client.apps.AppsMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

import com.sencha.gxt.widget.core.client.tree.TreeStyle;

/**
 * @author aramsey
 */
public class CommunitiesViewDefaultAppearance implements CommunitiesView.Appearance {

    public interface CommunityViewResources extends ClientBundle {

        @Source("../bookIcon-inbetween.png")
        ImageResource category();

        @Source("../bookIcon-open.png")
        ImageResource categoryOpen();

        @Source("../bookIcon.png")
        ImageResource subCategory();
    }

    private final CommunityViewResources resources;
    private final IplantDisplayStrings iplantDisplayStrings;
    private final AppsMessages appsMessages;

    public CommunitiesViewDefaultAppearance() {
        this(GWT.<CommunityViewResources> create(CommunityViewResources.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<AppsMessages> create(AppsMessages.class));
    }

    CommunitiesViewDefaultAppearance(final CommunityViewResources resources,
                                     final IplantDisplayStrings iplantDisplayStrings,
                                     final AppsMessages appsMessages) {
        this.resources = resources;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.appsMessages = appsMessages;
    }

    @Override
    public void setTreeIcons(TreeStyle style) {
        style.setNodeCloseIcon(resources.category());
        style.setNodeOpenIcon(resources.categoryOpen());
        style.setLeafIcon(resources.subCategory());
    }

    @Override
    public String communities() {
        return appsMessages.communities();
    }

    @Override
    public String loadingMask() {
        return iplantDisplayStrings.loadingMask();
    }

    @Override
    public String failedToLoadCommunities() {
        return appsMessages.failedToLoadCommunities();
    }
}
