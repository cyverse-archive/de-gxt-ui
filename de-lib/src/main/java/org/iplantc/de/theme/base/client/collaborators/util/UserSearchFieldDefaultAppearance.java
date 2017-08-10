package org.iplantc.de.theme.base.client.collaborators.util;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author aramsey
 */
public class UserSearchFieldDefaultAppearance implements UserSearchField.UserSearchFieldAppearance {

    interface UserTemplate extends XTemplates {
        @XTemplate(source = "UserSearchResult.html")
        SafeHtml render(String name, String institution, SafeUri img);
    }

    private UserTemplate userTemplate;
    private IplantDisplayStrings iplantDisplayStrings;
    private IplantResources iplantResources;

    public UserSearchFieldDefaultAppearance() {
        this(GWT.<UserTemplate>create(UserTemplate.class),
             GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<IplantResources> create(IplantResources.class));
    }

    public UserSearchFieldDefaultAppearance(UserTemplate userTemplate,
                                            IplantDisplayStrings iplantDisplayStrings,
                                            IplantResources iplantResources) {
        this.userTemplate = userTemplate;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantResources = iplantResources;
    }

    @Override
    public void render(Cell.Context context, Subject subject, SafeHtmlBuilder sb) {
        String name = subject.getSubjectDisplayName();
        if (subject.isGroup()) {
            SafeUri uri;
            if (subject.isCollaboratorList()) {
                uri = iplantResources.list().getSafeUri();
            } else {
                uri = iplantResources.group().getSafeUri();
            }
            sb.append(userTemplate.render(name, null, uri));
        } else {
            sb.append(userTemplate.render(name, subject.getInstitution(), null));
        }
    }

    @Override
    public String searchCollab() {
        return iplantDisplayStrings.searchCollab();
    }

    @Override
    public String collaboratorDisplayName(Subject c) {
        return c.getName();
    }
}
