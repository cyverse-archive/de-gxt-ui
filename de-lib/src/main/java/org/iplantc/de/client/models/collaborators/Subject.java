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
     * Currently subject.getName from the GET /subjects endpoints returns the full name, for example
     * iplant:de:de-2:users:aramsey:collaborator-lists:test
     *
     * Instead, we want to return the part that comes after the last colon character
     * @param name
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
     * If the : character exists in the name, the name must be the full extended name that
     * includes the grouper folder structure
     * @param name
     * @return
     */
    default boolean hasGroupLongName(String name) {
        return isGroup() && getName() != null && getName().contains(GROUP_NAME_DELIMITER);
    }

    default boolean isCollaboratorList() {
        RegExp regex = RegExp.compile(LIST_LONG_NAME_REGEX);
        return regex.test(getName());
    }

    default boolean isTeam() {
        RegExp regex = RegExp.compile(TEAM_LONG_NAME_REGEX);
        return regex.test(getName());
    }
}
