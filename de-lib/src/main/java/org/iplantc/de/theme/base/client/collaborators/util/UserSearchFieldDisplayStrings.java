package org.iplantc.de.theme.base.client.collaborators.util;

import com.google.gwt.i18n.client.Messages;

public interface UserSearchFieldDisplayStrings extends Messages {
    String userNameMask(String searchTerm);

    String emailNoMask(String domain);

    String emailMask(String searchTerm, String domain);
}
