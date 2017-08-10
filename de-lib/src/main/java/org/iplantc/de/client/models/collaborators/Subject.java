package org.iplantc.de.client.models.collaborators;

import org.iplantc.de.client.models.HasSettableId;

import com.google.common.base.Strings;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * The autobean representation of a Grouper subject
 * Also some helper methods to determine if the subject is a collaborator list and how
 * to get the display name
 * @author aramsey
 */
public interface Subject extends HasSettableId, HasName {

    String GROUP_IDENTIFIER = "g:gsa";
    String GROUP_NAME_DELIMITER = ":";
    String LIST_LONG_NAME_REGEX = ".*collaborator-lists:(.+)";
    String TEAM_LONG_NAME_REGEX = ".*teams:.+:(.+)";

    @AutoBean.PropertyName("first_name")
    String getFirstName();

    @AutoBean.PropertyName("first_name")
    void setFirstName(String firstName);

    @AutoBean.PropertyName("last_name")
    String getLastName();

    @AutoBean.PropertyName("last_name")
    void setLastName(String lastName);

    @AutoBean.PropertyName("email")
    String getEmail();

    @AutoBean.PropertyName("email")
    void setEmail(String email);

    @AutoBean.PropertyName("institution")
    String getInstitution();

    @AutoBean.PropertyName("institution")
    void setInstitution(String institution);

    @AutoBean.PropertyName("attribute_values")
    List<String> getAttributes();

    @AutoBean.PropertyName("attribute_values")
    void setAttributes(List<String> attributes);

    @AutoBean.PropertyName("source_id")
    String getSourceId();

    @AutoBean.PropertyName("source_id")
    void setSourceId(String sourceId);

    @AutoBean.PropertyName("display_name")
    String getDisplayName();

    @AutoBean.PropertyName("display_name")
    void setDisplayName(String name);

    default boolean isGroup() {
        return GROUP_IDENTIFIER.equals(getSourceId());
    }

    default String getSubjectDisplayName() {
        String subjectName = getName();
        if (Strings.isNullOrEmpty(subjectName)) {
            subjectName = getFirstName() + " " + getLastName();
        }
        if (!isGroup() || !hasGroupLongName(subjectName)) {
            return subjectName;
        } else {
           return getGroupShortName();
        }
    }

    /**
     * The "name" field in a Subject (or Group) autobean should have the displayable short name.
     * For example, the list called "iplant:de:de-2:users:aramsey:collaborator-lists:test-list"
     * should only have "test-list" in the "name" field.
     * However if the subject is a team, the team name (to distinguish teams with the same
     * names created by different people) is in the format of `subject_id:team_name`.  The UI
     * for now only wants to display the team name.
     * @return
     */
    default String getGroupShortName() {
        String name = getName();
        if (Strings.isNullOrEmpty(name)) {
            return "";
        }
        int lastIndex = name.lastIndexOf(GROUP_NAME_DELIMITER);
        if (lastIndex > 0) {
            return name.substring(lastIndex + 1);
        }
        return name;
    }

    /**
     * A group name created from the DE is not allowed to have a : character in it
     * If the : character exists in the name, the name field might contain the full extended name that
     * includes the grouper folder structure or it might be a team
     * @param name
     * @return
     */
    default boolean hasGroupLongName(String name) {
        return isGroup() && getName() != null && getName().contains(GROUP_NAME_DELIMITER);
    }

    /**
     * Since collaborator lists and teams are both groups, the only way to distinguish the
     * two is by the "display_name" field which contains the entire grouper folder structure e.g.
     * iplant:de:de-2:users:aramsey:collaborator-lists:test-list
     * @return
     */
    default boolean isCollaboratorList() {
        RegExp regex = RegExp.compile(LIST_LONG_NAME_REGEX);
        return regex.test(getDisplayName());
    }

    default boolean isTeam() {
        RegExp regex = RegExp.compile(TEAM_LONG_NAME_REGEX);
        return regex.test(getDisplayName());
    }
}
