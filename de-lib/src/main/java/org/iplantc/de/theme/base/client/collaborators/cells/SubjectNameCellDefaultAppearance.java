package org.iplantc.de.theme.base.client.collaborators.cells;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.views.cells.SubjectNameCell;
import org.iplantc.de.resources.client.DiskResourceNameCellStyle;
import org.iplantc.de.resources.client.IplantResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author aramsey
 */
public class SubjectNameCellDefaultAppearance implements SubjectNameCell.SubjectNameCellAppearance {

    interface Templates extends XTemplates {
        @XTemplates.XTemplate("<span id='{debugID}'>{name}</span>")
        SafeHtml subject(String name, String debugID);

        @XTemplates.XTemplate("<img src='{uri}'/><span name='{elementName}' class='{style.nameStyle}' id='{debugID}'> {name}</span>")
        SafeHtml group(SafeUri uri, String elementName, String name, DiskResourceNameCellStyle style, String debugID);
    }

    private IplantResources iplantResources;
    private SubjectNameCellDefaultAppearance.Templates templates;
    private final DiskResourceNameCellStyle defaultStyle;

    public SubjectNameCellDefaultAppearance() {
        this(GWT.<IplantResources> create(IplantResources.class),
             GWT.<Templates> create(Templates.class));
    }

    public SubjectNameCellDefaultAppearance(IplantResources iplantResources, Templates templates) {
        this.iplantResources = iplantResources;
        this.templates = templates;
        this.defaultStyle = iplantResources.diskResourceNameCss();
        defaultStyle.ensureInjected();
    }

    @Override
    public void render(SafeHtmlBuilder safeHtmlBuilder, Subject subject, String debugID) {
        String subjectName = subject.getSubjectDisplayName();
        if (subject.getSourceId().equals(Subject.GROUP_IDENTIFIER)) {
            safeHtmlBuilder.append(templates.group(iplantResources.list().getSafeUri(),
                                                   SubjectNameCell.SubjectNameCellAppearance.CLICKABLE_ELEMENT_NAME,
                                                   subjectName,
                                                   defaultStyle,
                                                   debugID));
        } else {
            safeHtmlBuilder.append(templates.subject(subjectName, debugID));
        }
    }
}
