package org.iplantc.de.theme.base.client.diskResource.metadata;

import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.diskResource.client.views.metadata.cells.TemplateInfoCell;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.debug.client.DebugInfo;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

/**
 * Created by sriram on 7/7/16.
 */
public class TemplateInfoCellDefaultAppearance implements TemplateInfoCell.TemplateInfoCellAppearance {

    interface  MyCss extends CssResource {
        @CssResource.ClassName("info")
        String info();

        String background();

    }

    interface Resources extends ClientBundle {
        @Source("TemplateInfoCell.gss")
        MyCss css();
    }

    interface Templates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<img name='{3}' class='{0}' title='{2}' src='{1}'>{2}</img>")
        SafeHtml cell(String imgClassName, SafeUri img, String templateName, String name);

        @SafeHtmlTemplates.Template("<img name='{4}' id='{3}' class='{0}' title='{2}' src='{1}'>{2}</img>")
        SafeHtml debugCell(String imgClassName, SafeUri img, String templateName, String debugId, String name);
    }

    private final Templates templates;
    private final Resources resources;
    private final IplantResources iplantResources;
    private IplantDisplayStrings iplantDisplayStrings;


    public TemplateInfoCellDefaultAppearance() {
        this(GWT.<Templates> create(Templates.class),
             GWT.<Resources> create(Resources.class),
             GWT.<IplantResources> create(IplantResources.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class));
    }

    TemplateInfoCellDefaultAppearance(final Templates templates,
                                      final Resources resources,
                                      final IplantResources iplantResources,
                                      IplantDisplayStrings iplantDisplayStrings)  {
        this.templates = templates;
        this.resources = resources;
        this.iplantResources = iplantResources;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.resources.css().ensureInjected();
    }

    @Override
    public void render(SafeHtmlBuilder sb, MetadataTemplateInfo value) {
        String imgClassName;
        imgClassName = resources.css().info();
        final SafeUri safeUri = iplantResources.info().getSafeUri();
        if(DebugInfo.isDebugIdEnabled()){
            sb.append(templates.debugCell(imgClassName, safeUri, value.getName(), value.getId() + "-info",description()));
        } else {
            sb.append(templates.cell(imgClassName, safeUri, value.getName(), description()));
        }

    }

    @Override
    public String description() {
        return iplantDisplayStrings.description();
    }

    @Override
    public String background() {
        return resources.css().background();
    }

    @Override
    public String descriptionWidth() {
        return "500";
    }

    @Override
    public String descriptionHeight() {
        return "100";
    }
}
