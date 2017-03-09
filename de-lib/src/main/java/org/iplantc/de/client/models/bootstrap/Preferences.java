package org.iplantc.de.client.models.bootstrap;

import org.iplantc.de.client.models.HasSplittableError;
import org.iplantc.de.client.models.HasStatus;
import org.iplantc.de.client.models.userSettings.UserSetting;

/**
 *
 * @author aramsey
 */
public interface Preferences extends HasStatus, UserSetting, HasSplittableError {
}
