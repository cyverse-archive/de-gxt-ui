package org.iplantc.de.client.models.collaborators;

import org.iplantc.de.client.models.HasSettableId;

import com.google.gwt.regexp.shared.MatchResult;
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
    String GROUP_LONG_NAME_REGEX = ".*collaborator-lists:(.+)";

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

    default boolean isCollaboratorList() {
        return getSourceId().equals(GROUP_IDENTIFIER);
    }

    default String getSubjectDisplayName() {
        String subjectName = getName();
        if (!isCollaboratorList() || !hasCollaboratorListLongName(subjectName)) {
            return subjectName;
        } else {
            return getCollaboratorListDisplayName(subjectName);
        }
    }

    default boolean hasCollaboratorListLongName(String name) {
        RegExp regex = RegExp.compile(GROUP_LONG_NAME_REGEX);
        return regex.test(name);
    }

    /**
     * Currently subject.getName from the GET /subjects endpoints returns the full name, for example
     * iplant:de:de-2:users:aramsey:collaborator-lists:test
     *
     * Instead, we want to return the part that comes after "collaborator-lists:"
     * @param name
     * @return
     */
    default String getCollaboratorListDisplayName(String name) {
        RegExp regex = RegExp.compile(GROUP_LONG_NAME_REGEX);
        MatchResult match = regex.exec(name);
        return match.getGroup(1);
    }
}
