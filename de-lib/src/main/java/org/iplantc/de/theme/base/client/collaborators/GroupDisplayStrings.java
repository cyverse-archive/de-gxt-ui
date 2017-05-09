package org.iplantc.de.theme.base.client.collaborators;

import com.google.gwt.i18n.client.Messages;

import java.util.List;

/**
 * @author aramsey
 */
public interface GroupDisplayStrings extends Messages {
    String noCollabLists();

    String groupNameLabel();

    String groupDescriptionLabel();

    String newGroupDetailsHeading();

    String editGroupDetailsHeading(String name);

    String deleteGroupConfirmHeading(String name);

    String deleteGroupConfirm(String name);

    String groupDeleteSuccess(String name);

    String unableToAddMembers(@PluralCount List<String> memberString);

    String groupCreatedSuccess(String name);
}
