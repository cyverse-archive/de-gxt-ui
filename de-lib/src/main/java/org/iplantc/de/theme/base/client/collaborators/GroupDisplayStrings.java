package org.iplantc.de.theme.base.client.collaborators;

import com.google.gwt.i18n.client.Messages;

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
}
