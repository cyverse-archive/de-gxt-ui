package org.iplantc.de.theme.base.client.commons.views.sharing;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.sharing.Sharing;
import org.iplantc.de.commons.client.views.sharing.SharingPermissionNameCell;
import org.iplantc.de.resources.client.IplantResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author aramsey
 */
public class SharingPermissionNameCellDefaultAppearance implements SharingPermissionNameCell.SharingPermissionNameCellAppearance {
    interface Templates extends XTemplates {
        @XTemplates.XTemplate("<span id='{debugID}'>{name}</span>")
        SafeHtml subject(String name, String debugID);

        @XTemplates.XTemplate("<img src='{uri}'/><span id='{debugID}'> {name}</span>")
        SafeHtml group(SafeUri uri, String name, String debugID);
    }

    private IplantResources iplantResources;
    private Templates templates;

    public SharingPermissionNameCellDefaultAppearance() {
        this(GWT.<IplantResources>create(IplantResources.class),
             GWT.<Templates>create(Templates.class));
    }

    public SharingPermissionNameCellDefaultAppearance(IplantResources iplantResources,
                                                      Templates templates) {
        this.iplantResources = iplantResources;
        this.templates = templates;
    }

    @Override
    public void render(SafeHtmlBuilder safeHtmlBuilder, Sharing sharing, String debugID) {
        String subjectName = sharing.getSubject().getSubjectDisplayName();
        if (Subject.GROUP_IDENTIFIER.equals(sharing.getSourceId())) {
            safeHtmlBuilder.append(templates.group(iplantResources.list().getSafeUri(), subjectName, debugID));
        } else {
            safeHtmlBuilder.append(templates.subject(subjectName, debugID));
        }
    }
}
