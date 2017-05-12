package org.iplantc.de.theme.base.client.collaborators.util;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author aramsey
 */
public class UserSearchFieldDefaultAppearance implements UserSearchField.UserSearchFieldAppearance {

    interface UserTemplate extends XTemplates {
        @XTemplate(source = "UserSearchResult.html")
        SafeHtml render(Subject c);
    }

    private UserTemplate userTemplate;
    private IplantDisplayStrings iplantDisplayStrings;

    public UserSearchFieldDefaultAppearance() {
        this(GWT.<UserTemplate>create(UserTemplate.class),
             GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class));
    }

    public UserSearchFieldDefaultAppearance(UserTemplate userTemplate,
                                            IplantDisplayStrings iplantDisplayStrings) {
        this.userTemplate = userTemplate;
        this.iplantDisplayStrings = iplantDisplayStrings;
    }

    @Override
    public void render(Cell.Context context, Subject subject, SafeHtmlBuilder sb) {
        sb.append(userTemplate.render(subject));
    }

    @Override
    public String searchCollab() {
        return iplantDisplayStrings.searchCollab();
    }

    @Override
    public String collaboratorDisplayName(Subject c) {
        return c.getFirstName() + c.getLastName();
    }
}
