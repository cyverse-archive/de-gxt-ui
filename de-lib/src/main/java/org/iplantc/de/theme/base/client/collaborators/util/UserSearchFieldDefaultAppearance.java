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
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author aramsey
 */
public class UserSearchFieldDefaultAppearance implements UserSearchField.UserSearchFieldAppearance {

    public static class MaskUtil {

        private String originalSearchTerm;
        private String[] regExGroups;
        private String delimitedRegExSearch;
        private RegExp delimitedPattern;
        private RegExp groupedPattern;
        private RegExp domainPattern;
        private String groupedRegExSearch;
        private int matchStartIndex;
        private int matchEndIndex;

        /**
         * Set the current search term
         * @param originalSearchTerm
         */
        void setSearchTerm(String originalSearchTerm) {
            if (!originalSearchTerm.equals(this.originalSearchTerm)) {
                this.originalSearchTerm = originalSearchTerm;
                setRegExGroupsAndSearches(originalSearchTerm);
            }
        }

        /**
         * Example:
         * searchTerm = foo*bar
         * delimitedRegExSearch = (.*)foo(.*)bar(.*)
         * regExGroups = [foo, bar]
         * value = secretfoosecret@bar.com
         * regex matches = ["secret", "secret@", ".com"]
         * return = ***foo***@bar.com
         *
         * searchTerm = foo@bar
         * delimitedRegExSearch = (.*)foo@bar(.*)
         * regExGroups = [foo@bar]
         * value = secretfoo@bar.com
         * regex matches = ["secret"]
         * return = ***foo@bar.com
         *
         * searchTerm = f*o@bar
         * delimitedRegExSearch = (.*)f(.*)o@bar(.*)
         * regExGroups = ["f", "o@bar"]
         * value = secretfoo@bar.com
         * regex matches = ["secret", "o"]
         * return = ***f***o@bar.com
         *
         * @param email
         * @return A StringBuilder containing the masked string
         */
        StringBuilder maskEmail(String email) {
            StringBuilder maskedValue = new StringBuilder();
            matchStartIndex = 0;
            matchEndIndex = 0;
            if (groupedPattern == null || !groupedPattern.getSource().equals(groupedRegExSearch)) {
                groupedPattern = getRegExPattern(groupedRegExSearch);
            }
            if (domainPattern == null) {
                domainPattern = RegExp.compile("@([^@]+)$");
            }

            MatchResult matches = groupedPattern.exec(email);
            if (matches == null) {
                return maskedValue;
            }

            MatchResult domainMatch = domainPattern.exec(email);
            if (domainMatch.getGroupCount() == 0) {
                return maskedValue;
            }

            String domainString = domainMatch.getGroup(0);
            int domainStart = domainMatch.getIndex();

            int[] groupIndexes = getCapturedGroupIndexes(matches);
            int totalSubGroups = matches.getGroupCount();

            for (int i = 1; i < totalSubGroups; i++) {
                maskedValue.append("***");
                // If I've matched entirely into the domain
                int startIndex = groupIndexes[2 * i];
                if (startIndex >= domainStart) {
                    return handleDomainMatch(i, maskedValue, totalSubGroups, startIndex, domainStart, domainString, groupIndexes);
                }
                // If I've overlapped into the domain
                int endIndex = groupIndexes[2 * i + 1];
                if (endIndex >= domainStart) {
                    return handleOverlapMatch(i, maskedValue, totalSubGroups, startIndex, endIndex, domainStart, domainString, matches, groupIndexes);
                }
                // Otherwise keep printing my search groups
                setMaskedRegions(maskedValue, matches, i);
                maskedValue.append(matches.getGroup(i));
            }
            maskedValue.append("***").append(domainString);

            return maskedValue;
        }

        private void setMaskedRegions(StringBuilder maskedValue, MatchResult matches, int i) {
            if (i == 1) {
                matchStartIndex = maskedValue.length();
            }
            if (i == matches.getGroupCount() - 1) {
                matchEndIndex = maskedValue.length() + matches.getGroup(i).length();
            }
        }

        /**
         * Handles masking when a matching subgroup overlaps with the domain portion of the email
         * @param i
         * @param maskedValue
         * @param totalSubGroups
         * @param startIndex
         * @param endIndex
         * @param domainStart
         * @param domainString
         * @param matches
         * @param groupIndexes
         * @return
         */
        private StringBuilder handleOverlapMatch(int i,
                                                 StringBuilder maskedValue,
                                                 int totalSubGroups,
                                                 int startIndex,
                                                 int endIndex,
                                                 int domainStart,
                                                 String domainString,
                                                 MatchResult matches,
                                                 int[] groupIndexes) {
            if (i == 1) {
                matchStartIndex = maskedValue.length();
            }
            int finalMatchEndIndex = groupIndexes[2 * totalSubGroups - 1];
            matchEndIndex = maskedValue.length() + (finalMatchEndIndex - startIndex) + 1;
            maskedValue.append(matches.getGroup(i));
            int remainder = endIndex - domainStart + 1;
            maskedValue.append(domainString.substring(remainder));
            return maskedValue;
        }

        /**
         * Handles masking when a matching subgroup exists entirely within the domain portion of the email
         *
         * @param i
         * @param maskedValue
         * @param totalSubGroups
         * @param startIndex
         * @param domainStart
         * @param domainString
         * @param groupIndexes
         * @return
         */
        private StringBuilder handleDomainMatch(int i,
                                                StringBuilder maskedValue,
                                                int totalSubGroups,
                                                int startIndex,
                                                int domainStart,
                                                String domainString,
                                                int[] groupIndexes) {
            if (i == 1) {
                matchStartIndex = maskedValue.length() + (startIndex - domainStart);
            }
            int finalMatchEndIndex = groupIndexes[2 * totalSubGroups - 1];
            matchEndIndex = maskedValue.length() + (finalMatchEndIndex - domainStart) + 1;
            maskedValue.append(domainString);
            return maskedValue;
        }

        /** Calculates where each captured group's starting and ending index in the string is
         *
         * @param matches
         * @return an array where each subgroup's starting index is at i * 2, and the end index is at i * 2 + 1
         */
        private int[] getCapturedGroupIndexes(MatchResult matches) {
            int[] groupIndexes = new int[2 * matches.getGroupCount()];
            int startingIndex = matches.getIndex();
            int currentIndex = 0;
            String matchedString = matches.getGroup(0);
            for (int i = 1; i < matches.getGroupCount(); i++) {
                String subGroup = matches.getGroup(i);
                groupIndexes[2 * i] = matchedString.indexOf(subGroup, currentIndex) + startingIndex;
                groupIndexes[2 * i + 1] = groupIndexes[2 * i] + subGroup.length() - 1;
                currentIndex += subGroup.length();
            }
            return groupIndexes;
        }

        /**
         * Example:
         * searchTerm = foo*bar
         * delimitedRegExSearch = (.*)foo(.*)bar(.*)
         * regExGroups = [foo, bar]
         * value = foosecretbar
         * matches = ["", "secret", ""]
         * return = ***foo***bar***
         *
         * searchTerm = foobar
         * delimitedRegExSearch = (.*)foobar(.*)
         * regExGroups = [foobar]
         * value = secretfoobarsecret
         * matches = ["secret", "secret"]
         * return "***foobar***"
         *
         * @param value
         * @return A StringBuilder containing the masked result
         */
        StringBuilder mask(String value) {
            StringBuilder maskedValue = new StringBuilder();
            if (delimitedPattern == null || !delimitedPattern.getSource().equals(delimitedRegExSearch)) {
                delimitedPattern = getRegExPattern(delimitedRegExSearch);
            }

            MatchResult matches = delimitedPattern.exec(value);
            if (matches == null) {
                return maskedValue;
            }

            maskedValue.append("***");
            for (String regExGroup : regExGroups) {
                maskedValue.append(regExGroup);
                maskedValue.append("***");
            }

            matchStartIndex = 3;
            matchEndIndex = maskedValue.length() - 3;

            return maskedValue;
        }

        int getMatchStartIndex() {
            return matchStartIndex;
        }

        int getMatchEndIndex() {
            return matchEndIndex;
        }

        /**
         * Converts a string containing a regex to a RegExp object
         * If there is an invalid regex expression in the string, all regex metacharacters will be treated as literals
         * @param regexString
         * @return The compiled regex pattern
         */
        private RegExp getRegExPattern(String regexString) {
            RegExp pattern;
            try {
                pattern = RegExp.compile(regexString);
            } catch (Exception exception) {
                // For when the search has regex metacharacters that yields an invalid regex expression
                pattern = RegExp.compile(RegExp.quote(regexString));
            }
            return pattern;
        }

        /**
         * Takes a search term and trims any outer wildcards or duplicated wildcards
         * Examples:
         * foobar -> foobar
         * foo*bar -> foo*bar
         * **foo**bar** -> foo*bar
         * @param searchTerm
         * @return The cleaned up string
         */
        private String trimExtraWildcards(String searchTerm) {
            searchTerm = searchTerm.replaceAll("^\\*+", "");
            searchTerm = searchTerm.replaceAll("\\*+$", "");
            searchTerm = searchTerm.replaceAll("\\*+", "*");
            return searchTerm;
        }

        /**
         * Takes a search term and splits it by wildcard characters, saves the result into `regexGroups`
         * Also saves a regex string into `delimitedRegExSearch` where each split is delimited by (.*)
         *
         * Example:
         * searchTerm = foo*bar
         * regExGroups = [foo, bar]
         * delimitedRegExSearch = foo(.*)bar
         * groupedRegExSearch = (foo).*(bar)
         * @param searchTerm
         */
        private void setRegExGroupsAndSearches(String searchTerm) {
            searchTerm = trimExtraWildcards(searchTerm);

            regExGroups = searchTerm.split("\\*");

            delimitedRegExSearch = searchTerm.replace("*", "(.*)");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < regExGroups.length ; i++) {
                if (i != 0) {
                    sb.append(".*");
                }
                sb.append("(").append(regExGroups[i]).append(")");
            }

            groupedRegExSearch = sb.toString();
        }
    }

    interface UserTemplate extends XTemplates {
        @XTemplate(source = "UserSearchResult.html")
        SafeHtml render(SafeHtml name, String institution, SafeHtml email, SafeHtml id, SafeUri img);
    }

    private UserTemplate userTemplate;
    private IplantDisplayStrings iplantDisplayStrings;
    private IplantResources iplantResources;
    private AppSearchHighlightAppearance appSearchHighlightAppearance;
    private UserSearchFieldDisplayStrings displayStrings;
    private MaskUtil maskUtil;
    public final String HIGHLIGHT_START = "<font style='background: #FF0'>";
    public final String HIGHLIGHT_END = "</font>";

    public UserSearchFieldDefaultAppearance() {
        this(GWT.<UserTemplate>create(UserTemplate.class),
             GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<IplantResources> create(IplantResources.class),
             GWT.create(AppSearchHighlightAppearance.class),
             GWT.create(UserSearchFieldDisplayStrings.class),
             GWT.create(MaskUtil.class));
    }

    public UserSearchFieldDefaultAppearance(UserTemplate userTemplate,
                                            IplantDisplayStrings iplantDisplayStrings,
                                            IplantResources iplantResources,
                                            AppSearchHighlightAppearance appSearchHighlightAppearance,
                                            UserSearchFieldDisplayStrings displayStrings,
                                            MaskUtil maskUtil) {
        this.userTemplate = userTemplate;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantResources = iplantResources;
        this.appSearchHighlightAppearance = appSearchHighlightAppearance;
        this.displayStrings = displayStrings;
        this.maskUtil = maskUtil;
    }

    @Override
    public void render(Cell.Context context, Subject subject, String searchTerm, SafeHtmlBuilder sb) {
        maskUtil.setSearchTerm(searchTerm);
        SafeHtml name = getHighlightedName(subject, searchTerm);
        String institution = getInstitution(subject);
        SafeHtml email = getHighlightedEmail(subject);
        SafeHtml id = getHighlightedId(subject);
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
        String regexSearch = createRegExGroup(searchTerm);
        return appSearchHighlightAppearance.highlightText(subject.getSubjectDisplayName(), regexSearch);
    }

    /**
     * Transforms a search into a singular regex group
     * Examples:
     * foobar -> (foobar)
     * foo* -> (foo.*)
     *
     * @param searchTerm
     * @return
     */
    String createRegExGroup(String searchTerm) {
        // Create a regex grouping
        searchTerm = "(" + searchTerm + ")";

        // Replace wildcard with regex
        return searchTerm.replace("*", ".*");
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

    SafeHtml getHighlightedId(Subject subject) {
        String id = subject.getId();
        if (subject.isGroup() || Strings.isNullOrEmpty(id)) {
            return null;
        }

        StringBuilder mask = maskUtil.mask(id);
        if (!Strings.isNullOrEmpty(mask.toString())) {
            mask.insert(maskUtil.getMatchEndIndex(), HIGHLIGHT_END);
            mask.insert(maskUtil.getMatchStartIndex(), HIGHLIGHT_START);
            return SafeHtmlUtils.fromTrustedString(userNameMask(mask.toString()));
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

    SafeHtml getHighlightedEmail(Subject subject) {
        String fullEmail = subject.getEmail();
        if (subject.isGroup() || Strings.isNullOrEmpty(fullEmail)) {
            return null;
        }

        StringBuilder mask = maskUtil.maskEmail(fullEmail);
        if (!Strings.isNullOrEmpty(mask.toString())) {
            mask.insert(maskUtil.getMatchEndIndex(), HIGHLIGHT_END);
            mask.insert(maskUtil.getMatchStartIndex(), HIGHLIGHT_START);
            return SafeHtmlUtils.fromTrustedString(mask.toString());
        }

        return null;
    }
}
