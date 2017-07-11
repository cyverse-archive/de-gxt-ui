package org.iplantc.de.theme.base.client.teams.cells;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.teams.client.views.cells.TeamInfoCell;
import org.iplantc.de.theme.base.client.teams.TeamsDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.debug.client.DebugInfo;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

/**
 * The default appearance for the Team info button
 *
 * @author aramsey
 */
public class TeamInfoCellDefaultAppearance implements TeamInfoCell.TeamInfoCellAppearance {

    interface MyCss extends CssResource {
        @CssResource.ClassName("team_info")
        String teamInfoButton();
    }

    interface Resources extends ClientBundle {
        @Source("TeamInfoCell.gss")
        TeamInfoCellDefaultAppearance.MyCss css();
    }

    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<img class='{0}' name='{3}' qtip='{2}' src='{1}'/>")
        SafeHtml cell(String imgClassName, SafeUri img, String toolTip, String name);

        @SafeHtmlTemplates.Template("<img class='{0}' name='{4}' qtip='{2}' src='{1}' id='{3}'/>")
        SafeHtml debugCell(String imgClassName, SafeUri img, String toolTip, String debugId, String name);
    }

    private final TeamInfoCellDefaultAppearance.Templates templates;
    private final TeamInfoCellDefaultAppearance.Resources resources;
    private final TeamsDisplayStrings displayStrings;
    private final IplantResources iplantResources;


    public TeamInfoCellDefaultAppearance() {
        this(GWT.<TeamInfoCellDefaultAppearance.Templates>create(TeamInfoCellDefaultAppearance.Templates.class),
             GWT.<TeamInfoCellDefaultAppearance.Resources>create(TeamInfoCellDefaultAppearance.Resources.class),
             GWT.<TeamsDisplayStrings>create(TeamsDisplayStrings.class),
             GWT.<IplantResources>create(IplantResources.class));
    }

    TeamInfoCellDefaultAppearance(final TeamInfoCellDefaultAppearance.Templates templates,
                                  final TeamInfoCellDefaultAppearance.Resources resources,
                                  final TeamsDisplayStrings displayStrings,
                                  final IplantResources iplantResources) {
        this.templates = templates;
        this.resources = resources;
        this.displayStrings = displayStrings;
        this.iplantResources = iplantResources;
        this.resources.css().ensureInjected();
    }

    @Override
    public void render(SafeHtmlBuilder sb, Group group, String debugId) {
        String imgClassName, tooltip;
        imgClassName = resources.css().teamInfoButton();
        tooltip = displayStrings.teamInfoBtnToolTip();
        final SafeUri safeUri = iplantResources.info().getSafeUri();
        if (DebugInfo.isDebugIdEnabled()) {
            sb.append(templates.debugCell(imgClassName, safeUri, tooltip, debugId,
                                          TeamInfoCell.TeamInfoCellAppearance.CLICKABLE_ELEMENT_NAME));
        } else {
            sb.append(templates.cell(imgClassName, safeUri, tooltip, TeamInfoCell.TeamInfoCellAppearance.CLICKABLE_ELEMENT_NAME));
        }
    }
}
