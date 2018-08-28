package org.iplantc.de.theme.base.client.notifications;

import com.google.gwt.i18n.client.Messages;

/**
 * @author aramsey
 */
public interface JoinTeamRequestDisplayStrings extends Messages {

    String addMemberFail(String requesterName, String teamName);

    String joinTeamSuccess(String requesterName, String teamName);

    String denyRequestSuccess(String requesterName, String teamName);

}
