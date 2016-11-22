package org.iplantc.de.client.models.userSettings;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * @author aramsey
 */
public interface UserSettingAutoBeanFactory extends AutoBeanFactory {

    AutoBean<UserSetting> getUserSetting();

}
