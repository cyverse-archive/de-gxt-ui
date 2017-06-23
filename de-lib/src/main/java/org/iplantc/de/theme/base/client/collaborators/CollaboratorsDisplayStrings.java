package org.iplantc.de.theme.base.client.collaborators;

import com.google.gwt.i18n.client.Messages;

import java.util.List;

/**
 * @author aramsey
 */
public interface CollaboratorsDisplayStrings extends Messages {

    String manageGroups();

    String manageCollaborators();

    String collaboratorTab();

    String collaboratorListTab();

    String groupNameLabel();

    String groupDescriptionLabel();

    String newGroupDetailsHeading();

    String editGroupDetailsHeading(String name);

    String deleteGroupConfirmHeading(String name);

    String deleteGroupConfirm(String name);

    String unableToAddMembers(@PluralCount List<String> memberString);

    String groupCreatedSuccess(String name);

    String memberDeleteFail(@PluralCount List<String> memberNames);

    String groupSelfAdd();

    String memberAddToGroupsSuccess(String subjectDisplayName);

    String groupNameValidationMsg(String restrictedChars);

    String addGroup();
}
