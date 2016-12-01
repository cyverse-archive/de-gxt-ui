package org.iplantc.de.client.models.bootstrap;

import org.iplantc.de.client.models.HasStatus;
import org.iplantc.de.client.models.userSettings.UserSetting;

import com.google.web.bindery.autobean.shared.Splittable;

/**
 *
 * @author aramsey
 */
public interface Preferences extends HasStatus, UserSetting {

    Splittable getError();

}
