package org.iplantc.de.theme.base.client.collaborators.cells;

import org.iplantc.de.collaborators.client.views.cells.SubjectDeleteCell;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

public class SubjectDeleteCellDefaultAppearance implements SubjectDeleteCell.SubjectDeleteCellAppearance {

    interface MyCss extends CssResource {
        @CssResource.ClassName("delete")
        String deleteClass();
    }

    interface Resources extends ClientBundle {
        @Source("DeleteCell.gss")
        MyCss css();
    }

    interface Templates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<img name='{4}' id='{3}' class='{0}' qtip='{2}' src='{1}'/>")
        SafeHtml debugCell(String imgClassName, SafeUri img, String toolTip, String debugId, String name);
    }

    private final Templates templates;
    private final Resources resources;
    private final IplantDisplayStrings displayStrings;
    private final IplantResources iplantResources;


    public SubjectDeleteCellDefaultAppearance() {
        this(GWT.<Templates> create(Templates.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<Resources> create(Resources.class),
             GWT.<IplantResources> create(IplantResources.class));
    }

    SubjectDeleteCellDefaultAppearance(final Templates templates,
                                       final IplantDisplayStrings displayStrings,
                                       final Resources resources,
                                       final IplantResources iplantResources) {
        this.templates = templates;
        this.resources = resources;
        this.displayStrings = displayStrings;
        this.iplantResources = iplantResources;
        this.resources.css().ensureInjected();
    }

    @Override
    public void render(SafeHtmlBuilder sb, String debugId) {
        String imgClassName, tooltip;
        imgClassName = resources.css().deleteClass();
        tooltip = displayStrings.delete();
        final SafeUri safeUri = iplantResources.delete().getSafeUri();
        sb.append(templates.debugCell(imgClassName, safeUri, tooltip, debugId,
                                      SubjectDeleteCell.SubjectDeleteCellAppearance.CLICKABLE_ELEMENT_NAME));
    }
}
