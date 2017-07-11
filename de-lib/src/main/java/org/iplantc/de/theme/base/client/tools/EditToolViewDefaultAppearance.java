package org.iplantc.de.theme.base.client.tools;

import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.tools.client.views.manage.EditToolView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Created by sriram on 4/27/17.
 */
public class EditToolViewDefaultAppearance implements EditToolView.EditToolViewAppearance {


    private final ToolMessages toolMessages;
    private final IplantDisplayStrings iplantDisplayStrings;
    private final IplantResources iplantResources;

    public EditToolViewDefaultAppearance() {
        this(GWT.<ToolMessages>create(ToolMessages.class),
             GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<IplantResources>create(IplantResources.class));
    }

    EditToolViewDefaultAppearance(final ToolMessages toolMessages,
                                  final IplantDisplayStrings iplantDisplayStrings,
                                  final IplantResources iplantResources) {
        this.toolMessages = toolMessages;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantResources = iplantResources;
    }


    @Override
    public String toolName() {
        return toolMessages.toolName();
    }

    @Override
    public String description() {
        return toolMessages.description();
    }

    @Override
    public String imgName() {
        return toolMessages.imaName();
    }

    @Override
    public String tag() {
        return toolMessages.tag();
    }

    @Override
    public String dockerUrl() {
        return toolMessages.dockerUrl();
    }

    @Override
    public String cpuShare() {
        return toolMessages.cpuShare();
    }

    @Override
    public String memLimit() {
        return toolMessages.memLimit();
    }

    @Override
    public String nwMode() {
        return toolMessages.nwMode();
    }

    @Override
    public String timeLimit() {
        return toolMessages.timeLimit();
    }

    @Override
    public String edit() {
        return toolMessages.edit();
    }

    @Override
    public String create() {
        return toolMessages.create();
    }

    @Override
    public String restrictions() {
        return toolMessages.restrictions();
    }

    @Override
    public String toolInfo() {
        return toolMessages.toolInfo();
    }

    @Override
    public String version() {
        return toolMessages.version();
    }

    @Override
    public SafeHtml buildRequiredFieldLabel(String label) {
        return SafeHtmlUtils.fromTrustedString("<span style='color:red; top:-5px;' >*</span> " + label);
    }

    @Override
    public String entryPoint() {
        return toolMessages.entryPoint();
    }
}
