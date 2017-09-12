package org.iplantc.de.theme.base.client.collaborators.util;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.theme.base.client.apps.AppSearchHighlightAppearance;

import com.google.common.base.Strings;
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
        SafeHtml render(SafeHtml name, String institution, SafeHtml email, SafeHtml id, SafeUri img);
    }

    private UserTemplate userTemplate;
    private IplantDisplayStrings iplantDisplayStrings;
    private IplantResources iplantResources;
    private AppSearchHighlightAppearance appSearchHighlightAppearance;
    private UserSearchFieldDisplayStrings displayStrings;

    public UserSearchFieldDefaultAppearance() {
        this(GWT.<UserTemplate>create(UserTemplate.class),
             GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<IplantResources> create(IplantResources.class),
             GWT.create(AppSearchHighlightAppearance.class),
             GWT.create(UserSearchFieldDisplayStrings.class));
    }

    public UserSearchFieldDefaultAppearance(UserTemplate userTemplate,
                                            IplantDisplayStrings iplantDisplayStrings,
                                            IplantResources iplantResources,
                                            AppSearchHighlightAppearance appSearchHighlightAppearance,
                                            UserSearchFieldDisplayStrings displayStrings) {
        this.userTemplate = userTemplate;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantResources = iplantResources;
        this.appSearchHighlightAppearance = appSearchHighlightAppearance;
        this.displayStrings = displayStrings;
    }

    @Override
    public void render(Cell.Context context, Subject subject, String searchTerm, SafeHtmlBuilder sb) {
        SafeHtml name = getHighlightedName(subject, searchTerm);
        String institution = getInstitution(subject);
        SafeHtml email = getHighlightedEmail(subject, searchTerm);
        SafeHtml id = getHighlightedId(subject, searchTerm);
        SafeUri uri = getImageUri(subject);

        sb.append(userTemplate.render(name, institution, email, id, uri));
    }

    @Override
    public String searchCollab() {
        return iplantDisplayStrings.searchCollab();
    }

    @Override
    public String collaboratorDisplayName(Subject c) {
        return c.getName();
    }

    SafeHtml getHighlightedName(Subject subject, String searchTerm) {
        return appSearchHighlightAppearance.highlightText(subject.getSubjectDisplayName(), searchTerm);
    }

    String getInstitution(Subject subject) {
        if (!subject.isGroup()) {
            return subject.getInstitution();
        }
        return null;
    }

    SafeUri getImageUri(Subject subject) {
        if (subject.isGroup()) {
            if (subject.isCollaboratorList()) {
                return iplantResources.list().getSafeUri();
            } else {
                return iplantResources.group().getSafeUri();
            }
        }

        return null;
    }

    SafeHtml getHighlightedId(Subject subject, String searchTerm) {
        String id = subject.getId();
        if (subject.isGroup() || Strings.isNullOrEmpty(id) || Strings.isNullOrEmpty(searchTerm)) {
            return null;
        }

        RegExp pattern = RegExp.compile(searchTerm, "ig");
        MatchResult matches = pattern.exec(id);
        if (matches != null) {
            String userNameMask = userNameMask(matches.getGroup(0));
            return appSearchHighlightAppearance.highlightText(userNameMask, searchTerm);
        }

        return null;
    }

    String userNameMask(String searchTerm) {
        return displayStrings.userNameMask(searchTerm);
    }

    String emailNoMask(String domain) {
        return displayStrings.emailNoMask(domain);
    }

    String emailMask(String searchTerm, String domain) {
        return displayStrings.emailMask(searchTerm, domain);
    }

    SafeHtml getHighlightedEmail(Subject subject, String searchTerm) {
        String fullEmail = subject.getEmail();
        if (subject.isGroup() || Strings.isNullOrEmpty(fullEmail) || Strings.isNullOrEmpty(searchTerm)) {
            return null;
        }

        RegExp pattern = RegExp.compile(searchTerm, "i");
        MatchResult exec = pattern.exec(fullEmail);
        if (exec != null) {
            //Find last occurrence of "@" and any characters after
            RegExp domainPattern = RegExp.compile("@([^@]+)$", "g");
            String domain = domainPattern.exec(fullEmail).getGroup(0);
            String email = fullEmail.substring(0, fullEmail.indexOf(domain));
            String emailMask = pattern.test(email) ? emailMask(exec.getGroup(0), domain) : emailNoMask(domain);

            return appSearchHighlightAppearance.highlightText(emailMask, searchTerm);
        }

        return null;
    }
}
