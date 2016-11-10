package org.iplantc.de.client.models.userSettings;

import org.iplantc.de.client.models.diskResources.Folder;

/**
 * @author aramsey
 */
public interface UserSetting {

    Boolean isEnableAnalysisEmailNotification();
    void setEnableAnalysisEmailNotification(Boolean enable);

    Boolean isEnableImportEmailNotification();
    void setEnableImportEmailNotification(Boolean enable);

    String getDefaultFileSelectorPath();
	void setDefaultFileSelectorPath(String value);

    Boolean isRememberLastPath();
	void setRememberLastPath(Boolean remember);

    Boolean isSaveSession();
	void setSaveSession(Boolean save);

    Folder getDefaultOutputFolder();
	void setDefaultOutputFolder(Folder value);

    String getDataKBShortcut();
	void setDataKBShortcut(String value);

    String getAppsKBShortcut();
	void setAppsKBShortcut(String value);

    String getAnalysisKBShortcut();
	void setAnalysisKBShortcut(String value);

    String getNotificationKBShortcut();
	void setNotificationKBShortcut(String value);

    String getCloseKBShortcut();
	void setCloseKBShortcut(String value);

    Folder getSystemDefaultOutputDir();
	void setSystemDefaultOutputDir(Folder value);

    String getLastFolder();
	void setLastFolder(String value);

    Boolean isEnableWaitTimeMessage();
	void setEnableWaitTimeMessage(Boolean enable);

    
}
