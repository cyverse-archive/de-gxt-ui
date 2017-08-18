package org.iplantc.de.theme.base.client.teams.cells;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.teams.client.views.cells.TeamNameCell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.debug.client.DebugInfo;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * The default appearance for what the user sees when hovering over a team name
 *
 * @author aramsey
 */
public class TeamNameCellDefaultAppearance implements TeamNameCell.TeamNameCellAppearance {

    interface MyCss extends CssResource {
        @CssResource.ClassName("team_name")
        String teamName();
    }

    interface Resources extends ClientBundle {
        @Source("TeamNameCell.gss")
        TeamNameCellDefaultAppearance.MyCss css();
    }

    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<div class='{0}' name='{2}'>{1}</div>")
        SafeHtml cell(String className, String groupName, String elementName);

        @SafeHtmlTemplates.Template("<div class='{0}' name='{3}' id='{2}'>{1}</div>")
        SafeHtml debugCell(String className, String groupName, String debugId, String elementName);
    }

    private final TeamNameCellDefaultAppearance.Templates templates;
    private final TeamNameCellDefaultAppearance.Resources resources;

    public TeamNameCellDefaultAppearance() {
        this(GWT.<TeamNameCellDefaultAppearance.Templates>create(TeamNameCellDefaultAppearance.Templates.class),
             GWT.<TeamNameCellDefaultAppearance.Resources>create(TeamNameCellDefaultAppearance.Resources.class));
    }

    TeamNameCellDefaultAppearance(final TeamNameCellDefaultAppearance.Templates templates,
                                  final TeamNameCellDefaultAppearance.Resources resources) {
        this.templates = templates;
        this.resources = resources;
        this.resources.css().ensureInjected();
    }

    @Override
    public void render(SafeHtmlBuilder sb, Group group, String debugId) {
        String className = resources.css().teamName();
        String groupName = group.getSubjectDisplayName();
        if (DebugInfo.isDebugIdEnabled()) {
            sb.append(templates.debugCell(className,
                                          groupName,
                                          debugId,
                                          TeamNameCell.TeamNameCellAppearance.CLICKABLE_ELEMENT_NAME));
        } else {
            sb.append(templates.cell(className, groupName, TeamNameCell.TeamNameCellAppearance.CLICKABLE_ELEMENT_NAME));
        }
    }
}
