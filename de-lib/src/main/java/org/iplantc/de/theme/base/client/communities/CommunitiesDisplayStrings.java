package org.iplantc.de.theme.base.client.communities;

import com.google.gwt.i18n.client.Messages;

import java.util.List;

public interface CommunitiesDisplayStrings extends Messages {
    String windowHeading();

    String communitiesHelp();

    String appSelectionHeader();

    String agaveAppsNotSupportedToolTip();

    String joinCommunityFailure();

    String leaveCommunityFailure();

    String removeCommunityAdminFailure(@PluralCount List<String> failedUsers);

    String addCommunityAdminFailure(@PluralCount List<String> failedUsers);
}
