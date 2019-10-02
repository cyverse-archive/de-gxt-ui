package org.iplantc.de.theme.base.client.tools;

import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;

import com.google.gwt.core.client.GWT;

/**
 * Created by sriram on 4/21/17.
 */
public class ManageToolsViewDefaultAppearance implements ManageToolsView.ManageToolsViewAppearance {

    private final ToolMessages toolMessages;
    private final IplantDisplayStrings iplantDisplayStrings;

    public ManageToolsViewDefaultAppearance() {
        this(GWT.<ToolMessages> create(ToolMessages.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class));
    }

    ManageToolsViewDefaultAppearance(final ToolMessages toolMessages,
                                     final IplantDisplayStrings iplantDisplayStrings) {
        this.toolMessages = toolMessages;
        this.iplantDisplayStrings = iplantDisplayStrings;
    }

    @Override
    public String edit() {
        return iplantDisplayStrings.edit();
    }

    @Override
    public String name() {
        return toolMessages.name();
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
    public String toolDeleted(String name) {
        return toolMessages.toolDeleted(name);
    }

    @Override
    public int sharingDialogWidth() {
        return 600;
    }

    @Override
    public int sharingDialogHeight() {
        return 500;
    }

    @Override
    public String manageSharing() {
        return toolMessages.manageSharing();
    }

    @Override
    public String done() {
        return toolMessages.done();
    }

    @Override
    public String toolInfoError() {
        return toolMessages.toolInfoError();
    }

    @Override
    public String windowWidth() {
        return "600";
    }

    @Override
    public String windowHeight() {
        return "500";
    }

    @Override
    public int windowMinWidth() {
        return 1000;
    }

    @Override
    public int windowMinHeight() {
        return 500;
    }

    @Override
    public String create() {
        return iplantDisplayStrings.create();
    }
}
