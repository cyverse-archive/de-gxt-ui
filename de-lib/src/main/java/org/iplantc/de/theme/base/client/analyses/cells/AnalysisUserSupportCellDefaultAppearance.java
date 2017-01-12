package org.iplantc.de.theme.base.client.analyses.cells;

import org.iplantc.de.analysis.client.views.cells.AnalysisUserSupportCell;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;

/**
 * Created by sriram on 11/17/16.
 */
public class AnalysisUserSupportCellDefaultAppearance implements AnalysisUserSupportCell.AnalysisUserSupportCellAppearance {

    public interface AnalysisUserCellStyles extends CssResource {
        @ClassName("support")
        String support();
    }

    public interface AnalysisUserSupportCellResources extends ClientBundle {
        @Source("AnalysisUserSupportCell.css")
        AnalysisUserCellStyles css();

    }

    interface Templates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<img name='{0}' title='{1}' class='{2}' src='{3}' />")
        SafeHtml imgCell(String name, String toolTip, String className, SafeUri imgSrc);
        @SafeHtmlTemplates.Template("<span name=\"{0}\" title=\" {3}\" class='{2}'>{1}</span>")
        SafeHtml analysis(String elementName, SafeHtml analysisName, String className, String tooltip);

    }

    private final IplantDisplayStrings displayStrings;
    private final IplantResources iplantResources;
    private final AnalysisUserSupportCellResources resources;
    private final Templates template;

    public AnalysisUserSupportCellDefaultAppearance() {
        this(GWT.<AnalysisUserSupportCellResources> create(AnalysisUserSupportCellResources.class),
             GWT.<Templates> create(Templates.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<IplantResources> create(IplantResources.class));
    }

    AnalysisUserSupportCellDefaultAppearance (final AnalysisUserSupportCellResources resources,
                                          final Templates template,
                                          final IplantDisplayStrings displayStrings,
                                          final IplantResources iplantResources){
        this.resources = resources;
        this.template = template;
        this.displayStrings = displayStrings;
        this.iplantResources = iplantResources;
        resources.css().ensureInjected();
    }

    @Override
    public void doOnMouseOut(Element eventTarget, Analysis value) {
        if (eventTarget.getAttribute("name").equalsIgnoreCase(ELEMENT_NAME)
            && !Strings.isNullOrEmpty(value.getResultFolderId())) {
            eventTarget.getStyle().setTextDecoration(Style.TextDecoration.NONE);
        }
    }

    @Override
    public void doOnMouseOver(Element eventTarget, Analysis value) {
        if (eventTarget.getAttribute("name").equalsIgnoreCase(ELEMENT_NAME)
            && !Strings.isNullOrEmpty(value.getResultFolderId())) {
            eventTarget.getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        }
    }

    @Override
    public void render(Cell.Context context, Analysis value, SafeHtmlBuilder sb) {
        sb.append(template.analysis(ELEMENT_NAME,
                                SafeHtmlUtils.fromString(value.getStatus()),
                                 resources.css().support(),
                                displayStrings.analysisHelp()));
    }


}
