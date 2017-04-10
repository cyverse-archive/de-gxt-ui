package org.iplantc.de.theme.base.client.collaborators;

import org.iplantc.de.collaborators.client.views.ManageCollaboratorsView;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantContextualHelpStrings;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.theme.base.client.grid.CheckBoxColumnDefaultAppearance.CheckBoxColumnResources;
import com.sencha.gxt.theme.base.client.grid.CheckBoxColumnDefaultAppearance.CheckBoxColumnStyle;
import com.sencha.gxt.theme.base.client.grid.CheckBoxColumnDefaultAppearance.CheckBoxColumnTemplates;

/**
 * @author jstroot
 */
public class ManageCollaboratorsViewDefaultAppearance implements ManageCollaboratorsView.Appearance {

    /**
     * This class is a copy of {@link CheckBoxColumnTemplates}, but with fields for
     * debug ids.
     */
    public interface CheckBoxColumnDebugTemplates extends XTemplates {
       @XTemplates.XTemplate("<div id='{debugId}' class='{style.hdChecker}'></div>")
        SafeHtml renderDebugHeader(CheckBoxColumnStyle style, String debugId);
    }

    private final CheckBoxColumnResources resources;
    private final CheckBoxColumnStyle style;
    private final CheckBoxColumnDebugTemplates templates;
    private IplantDisplayStrings iplantDisplayStrings;
    private IplantContextualHelpStrings iplantContextualHelpStrings;
    private IplantResources iplantResources;
    private CollaboratorsDisplayStrings displayStrings;

    public ManageCollaboratorsViewDefaultAppearance() {
        this(GWT.<CheckBoxColumnResources> create(CheckBoxColumnResources.class),
             GWT.<CheckBoxColumnDebugTemplates>create(CheckBoxColumnDebugTemplates.class),
             GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<IplantContextualHelpStrings>create(IplantContextualHelpStrings.class),
             GWT.<IplantResources> create(IplantResources.class),
             GWT.<CollaboratorsDisplayStrings> create(CollaboratorsDisplayStrings.class));
    }

    ManageCollaboratorsViewDefaultAppearance(final CheckBoxColumnResources resources,
                                             final CheckBoxColumnDebugTemplates templates,
                                             IplantDisplayStrings iplantDisplayStrings,
                                             IplantContextualHelpStrings iplantContextualHelpStrings,
                                             IplantResources iplantResources,
                                             CollaboratorsDisplayStrings displayStrings) {
        this.resources = resources;
        this.style = resources.style();
        this.templates = templates;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantContextualHelpStrings = iplantContextualHelpStrings;
        this.iplantResources = iplantResources;
        this.displayStrings = displayStrings;

        style.ensureInjected();
    }

    @Override
    public SafeHtml renderCheckBoxColumnHeader(String debugId) {
        // Pull in checkbox column appearance resources
        return templates.renderDebugHeader(style, debugId);
    }

    @Override
    public String collaborators() {
        return iplantDisplayStrings.collaborators();
    }

    @Override
    public String collaboratorsHelp() {
        return iplantContextualHelpStrings.collaboratorsHelp();
    }

    @Override
    public String manageGroups() {
        return displayStrings.manageGroups();
    }

    @Override
    public String delete() {
        return iplantDisplayStrings.delete();
    }

    @Override
    public ImageResource deleteIcon() {
        return iplantResources.delete();
    }

    @Override
    public String manageCollaborators() {
        return displayStrings.manageCollaborators();
    }

    @Override
    public ImageResource shareIcon() {
        return iplantResources.share();
    }

    @Override
    public String noCollaborators() {
        return iplantDisplayStrings.noCollaborators();
    }

    @Override
    public String myCollaborators() {
        return iplantDisplayStrings.myCollaborators();
    }

    @Override
    public String selectCollabs() {
        return iplantDisplayStrings.selectCollabs();
    }
}
