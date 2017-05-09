package org.iplantc.de.theme.base.client.collaborators.cells;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.views.cells.GroupNameCell;
import org.iplantc.de.resources.client.DiskResourceNameCellStyle;
import org.iplantc.de.resources.client.IplantResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author aramsey
 */
public class GroupNameCellDefaultAppearance implements GroupNameCell.GroupNameCellAppearance {

    interface Templates extends XTemplates {
        @XTemplates.XTemplate("<span name='{elementName}' class='{style.nameStyle}' id='{debugID}'>{group.name}</span>")
        SafeHtml group(String elementName, DiskResourceNameCellStyle style, Group group, String debugID);
    }

    private final GroupNameCellDefaultAppearance.Templates templates;
    private final DiskResourceNameCellStyle defaultStyle;

    public GroupNameCellDefaultAppearance() {
        this(GWT.<IplantResources>create(IplantResources.class),
             GWT.<Templates>create(Templates.class));
    }

    public GroupNameCellDefaultAppearance(IplantResources iplantResources,
                                          Templates templates) {
        this.defaultStyle = iplantResources.diskResourceNameCss();
        this.templates = templates;
        defaultStyle.ensureInjected();
    }

    @Override
    public void render(SafeHtmlBuilder safeHtmlBuilder, Group group, String debugID) {
        if (group == null) {
            return;
        }

        safeHtmlBuilder.append(templates.group(CLICKABLE_ELEMENT_NAME, defaultStyle, group, debugID));
    }
}
