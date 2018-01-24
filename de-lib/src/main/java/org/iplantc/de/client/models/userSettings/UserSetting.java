package org.iplantc.de.client.models.userSettings;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.webhooks.Webhook;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

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

    @PropertyName("default_output_folder")
    Folder getDefaultOutputFolder();

    @PropertyName("default_output_folder")
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

    @PropertyName("system_default_output_dir")
    Folder getSystemDefaultOutputDir();

    @PropertyName("system_default_output_dir")
    void setSystemDefaultOutputDir(Folder value);

    String getLastFolder();
    void setLastFolder(String value);

    Boolean isEnableWaitTimeMessage();
    void setEnableWaitTimeMessage(Boolean enable);

    List<Webhook> getWebhooks();

    void setWebhooks(List<Webhook> webhooks);

    Boolean isEnableHPCPrompt();

    void setEnableHPCPrompt(Boolean enableHpcPrompt);
}

