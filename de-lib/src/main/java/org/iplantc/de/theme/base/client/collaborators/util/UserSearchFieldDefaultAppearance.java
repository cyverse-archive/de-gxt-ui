package org.iplantc.de.theme.base.client.collaborators.util;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
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
        if (subject.getSourceId().equals("ldap")) {
            sb.append(userTemplate.render(subject.getName(), subject.getInstitution(), null));
        } else {
            String name = getCollaboratorListDisplayName(subject.getName());
            sb.append(userTemplate.render(name, null, iplantResources.viewCurrentCollabs().getSafeUri()));
        }

    }

    /**
     * Currently subject.getName from the GET /subjects endpoints returns the full name, for example
     * iplant:de:de-2:users:aramsey:collaborator-lists:test
     *
     * Instead, we want to return the part that comes after "collaborator-lists:"
     * @param name
     * @return
     */
    String getCollaboratorListDisplayName(String name) {
        RegExp regex = RegExp.compile(".*collaborator-lists:(.+)");
        MatchResult match = regex.exec(name);
        return match.getGroup(1);
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
