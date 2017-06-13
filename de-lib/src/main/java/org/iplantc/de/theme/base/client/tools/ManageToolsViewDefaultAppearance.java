package org.iplantc.de.theme.base.client.tools;

import org.iplantc.de.tools.client.views.manage.ManageToolsToolbarView;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by sriram on 4/21/17.
 */
public class ManageToolsViewDefaultAppearance implements ManageToolsToolbarView.ManageToolsToolbarAppearance,
                                                         ManageToolsView.ManageToolsViewAppearance {

    private final ToolMessages toolMessages;
    private final IplantDisplayStrings iplantDisplayStrings;
    private final IplantResources iplantResources;

    public ManageToolsViewDefaultAppearance() {
        this(GWT.<ToolMessages> create(ToolMessages.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<IplantResources> create(IplantResources.class));
    }

    ManageToolsViewDefaultAppearance(final ToolMessages toolMessages,
                                     final IplantDisplayStrings iplantDisplayStrings,
                                     final IplantResources iplantResources) {
        this.toolMessages = toolMessages;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantResources = iplantResources;
    }

    @Override
    public String tools() {
        return toolMessages.tools();
    }

    @Override
    public String requestTool() {
        return toolMessages.requestTool();
    }

    @Override
    public String edit() {
        return toolMessages.edit();
    }

    @Override
    public String delete() {
        return toolMessages.delete();
    }

    @Override
    public String useInApp() {
        return toolMessages.useInApp();
    }

    @Override
    public String share() {
        return toolMessages.share();
    }

    @Override
    public String shareCollab() {
        return toolMessages.shareCollab();
    }

    @Override
    public String sharePublic() {
        return toolMessages.submitForUse();
    }

    @Override
    public String name() {
        return toolMessages.name();
    }

    @Override
    public String version() {
        return toolMessages.version();
    }

    @Override
    public String imaName() {
        return toolMessages.imaName();
    }

    @Override
    public String status() {
        return toolMessages.status();
    }

    @Override
    public String mask() {
        return iplantDisplayStrings.loadingMask();
    }

    @Override
    public int nameWidth() {
        return 150;
    }

    @Override
    public int imgNameWidth() {
        return 200;
    }

    @Override
    public int tagWidth() {
        return 50;
    }

    public String tag() {
        return toolMessages.tag();
    }

    @Override
    public String shareTools() {
        return toolMessages.shareTools();
    }

    public String deleteTool() {
        return toolMessages.delete();
    }

    @Override
    public String confirmDelete() {
        return toolMessages.confirmDelete();
    }

    @Override
    public String appsLoadError() {
        return toolMessages.appsLoadError();
    }
    public String toolAdded(String name) {
        return toolMessages.toolAdded(name);
    }

    @Override
    public String toolUpdated(String name) {
        return toolMessages.toolUpdated(name);
    }

    @Override
    public String editDialogWidth() {
        return "600px";
    }

    @Override
    public String editDialogHeight() {
        return "300px";
    }

    @Override
    public String toolDeleted(String name) {
        return toolMessages.toolDeleted(name);
    }

    @Override
    public String submitForPublicUse() {
        return toolMessages.submitForUse();
    }

    @Override
    public String refresh() {
        return toolMessages.refresh();
    }

    @Override
    public ImageResource refreshIcon() {
        return iplantResources.refresh();
    }

    @Override
    public ImageResource shareToolIcon() {
        return iplantResources.share();
    }

    @Override
    public ImageResource submitForPublicIcon() {
        return iplantResources.submitForPublic();
    }

    @Override
    public String searchTools() {
        return toolMessages.searchTools();
    }

    @Override
    public String addTool() {
        return toolMessages.addTool();
    }

    @Override
    public ImageResource addIcon() {
        return iplantResources.add();
    }

    @Override
    public ImageResource requestToolIcon() {
        return iplantResources.add();
    }

    @Override
    public ImageResource editIcon() {
        return iplantResources.edit();
    }

    @Override
    public ImageResource deleteIcon() {
        return iplantResources.delete();
    }

    @Override
    public ImageResource useInAppIcon() {
        return iplantResources.arrowUp();
    }
}
