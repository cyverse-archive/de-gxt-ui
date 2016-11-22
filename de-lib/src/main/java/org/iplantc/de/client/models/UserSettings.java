package org.iplantc.de.client.models;

import org.iplantc.de.client.KeyBoardShortcutConstants;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.userSettings.UserSetting;
import org.iplantc.de.client.models.userSettings.UserSettingAutoBeanFactory;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;

/**
 * A singleton hold user general settings
 *
 * @author sriram
 */
public class UserSettings {

    private final KeyBoardShortcutConstants SHORTCUTS = GWT.create(KeyBoardShortcutConstants.class);
    private final DiskResourceAutoBeanFactory diskResourceFactory =
            GWT.create(DiskResourceAutoBeanFactory.class);
    private final UserSettingAutoBeanFactory factory = GWT.create(UserSettingAutoBeanFactory.class);
    private UserInfo userInfo;

    private UserSetting userSetting;
    private static UserSettings instance;
    private boolean userSessionConnection = true;
    private static String ANALYSES_DIR = "analyses";

    public UserSettings(final UserSetting userSetting) {
        setUserSettings(userSetting);
    }

    private UserSettings() {
        this.userInfo = UserInfo.getInstance();
    }

    public static UserSettings getInstance() {
        if (instance == null) {
            instance = new UserSettings();
        }

        return instance;
    }

    public void setUserSettings(UserSetting userSetting) {
        this.userSetting = userSetting;
        setUserSettings();
    }

    public UserSetting getUserSetting() {
        return userSetting;
    }

    void setUserSettings() {
        if (userSetting == null) {
            userSetting = factory.getUserSetting().as();
        }

        if (userSetting.isEnableAnalysisEmailNotification() == null) {
            setEnableAnalysisEmailNotification(true);
        }
        if (userSetting.isEnableImportEmailNotification() == null) {
            setEnableImportEmailNotification(true);
        }

        if (Strings.isNullOrEmpty(userSetting.getDefaultFileSelectorPath())) {
            String homePath = userInfo.getHomePath();
            setDefaultFileSelectorPath(homePath);
        }
        if (userSetting.isRememberLastPath() == null) {
            setRememberLastPath(true);
        }

        if (userSetting.isSaveSession() == null) {
            setSaveSession(true);
        }

        if (userSetting.getDefaultOutputFolder() == null) {
            String homePath = userInfo.getHomePath();
            String outputDir = homePath + "/" + ANALYSES_DIR;
            setDefaultOutputFolder(buildFolder(outputDir));
        }

        if (userSetting.getSystemDefaultOutputDir() == null) {
            setSystemDefaultOutputFolder(userSetting.getDefaultOutputFolder());
        }

        if (Strings.isNullOrEmpty(userSetting.getLastFolder())) {
            String homePath = userInfo.getHomePath();
            setLastPath(homePath);
        }

        if (userSetting.isEnableWaitTimeMessage() == null) {
            setEnableWaitTimeMessage(true);
        }

        parseKeyboardShortcuts();
    }

    void parseKeyboardShortcuts() {
        if (Strings.isNullOrEmpty(userSetting.getDataKBShortcut())) {
            setDataShortCut(SHORTCUTS.dataKeyShortCut());
        }

        if (Strings.isNullOrEmpty(userSetting.getAppsKBShortcut())) {
            setAppsShortCut(SHORTCUTS.appsKeyShortCut());
        }

        if (Strings.isNullOrEmpty(userSetting.getAnalysisKBShortcut())) {
            setAnalysesShortCut(SHORTCUTS.analysisKeyShortCut());
        }

        if (Strings.isNullOrEmpty(userSetting.getNotificationKBShortcut())) {
            setNotifyShortCut(SHORTCUTS.notifyKeyShortCut());
        }

        if (Strings.isNullOrEmpty(userSetting.getCloseKBShortcut())) {
            setCloseShortCut(SHORTCUTS.closeKeyShortCut());
        }
    }

    public void setDataShortCut(String c) {
        userSetting.setDataKBShortcut(c);
    }

    public String getDataShortCut() {
        return userSetting.getDataKBShortcut();
    }

    public void setAppsShortCut(String c) {
        userSetting.setAppsKBShortcut(c);
    }

    public String getAppsShortCut() {
        return userSetting.getAppsKBShortcut();
    }

    public void setAnalysesShortCut(String c) {
        userSetting.setAnalysisKBShortcut(c);
    }

    public String getAnalysesShortCut() {
        return userSetting.getAnalysisKBShortcut();
    }

    public void setNotifyShortCut(String c) {
        userSetting.setNotificationKBShortcut(c);
    }

    public String getNotifyShortCut() {
        return userSetting.getNotificationKBShortcut();
    }

    /**
     * @param enableAnalysisEmailNotification the enableAnalysisEmailNotification to set
     */
    public void setEnableAnalysisEmailNotification(boolean enableAnalysisEmailNotification) {
        userSetting.setEnableAnalysisEmailNotification(enableAnalysisEmailNotification);
    }

    /**
     * @return the enableAnalysisEmailNotification
     */
    public boolean isEnableAnalysisEmailNotification() {
        return userSetting.isEnableAnalysisEmailNotification();
    }

    /**
     * @param defaultFileSelectorPath the defaultFileSelectorPath to set
     */
    public void setDefaultFileSelectorPath(String defaultFileSelectorPath) {
        userSetting.setDefaultFileSelectorPath(defaultFileSelectorPath);
    }

    /**
     * @return the defaultFileSelectorPath
     */
    public String getDefaultFileSelectorPath() {
        return userSetting.getDefaultFileSelectorPath();
    }

    public boolean hasUserSessionConnection() {
        return userSessionConnection;
    }

    /**
     * @param rememberLastPath the rememberLastPath to set
     */
    public void setRememberLastPath(boolean rememberLastPath) {
        userSetting.setRememberLastPath(rememberLastPath);
    }

    /**
     * @return the rememberLastPath
     */
    public boolean isRememberLastPath() {
        return userSetting.isRememberLastPath();
    }

    /**
     * @param saveSession
     */
    public void setSaveSession(boolean saveSession) {
        userSetting.setSaveSession(saveSession);
    }

    public void setUserSessionConnection(boolean connected) {
        this.userSessionConnection = connected;
    }

    public boolean isSaveSession() {
        return userSetting.isSaveSession();
    }

    /**
     * @param defaultOutputFolder the new default output folder.
     */
    public void setDefaultOutputFolder(Folder defaultOutputFolder) {
        userSetting.setDefaultOutputFolder(defaultOutputFolder);
    }

    private Folder buildFolder(String defaultOutputFolder) {
        Folder folder = diskResourceFactory.folder().as();
        folder.setId(defaultOutputFolder);
        folder.setPath(defaultOutputFolder);
        return folder;
    }

    /**
     * @return the default output folder.
     */
    public Folder getDefaultOutputFolder() {
        return userSetting.getDefaultOutputFolder();
    }

    /**
     * @return the closeShortCut
     */
    public String getCloseShortCut() {
        return userSetting.getCloseKBShortcut();
    }

    /**
     * @param closeShortCut the closeShortCut to set
     */
    public void setCloseShortCut(String closeShortCut) {
        userSetting.setCloseKBShortcut(closeShortCut);
    }

    /**
     * @return the systemDefaultOutputFolder
     */
    public Folder getSystemDefaultOutputFolder() {
        return userSetting.getSystemDefaultOutputDir();
    }

    /**
     * @param systemDefaultOutputFolder the systemDefaultOutputFolder to set
     */
    public void setSystemDefaultOutputFolder(Folder systemDefaultOutputFolder) {
        userSetting.setSystemDefaultOutputDir(systemDefaultOutputFolder);
        GWT.log("System Default Output folder set: path = " + systemDefaultOutputFolder.getPath());
    }

    /**
     * @return the lastPath
     */
    public String getLastPath() {
        return userSetting.getLastFolder();
    }

    /**
     * @param lastPath the lastPath to set
     */
    public void setLastPath(String lastPath) {
        userSetting.setLastFolder(lastPath);
    }

    public boolean isEnableImportEmailNotification() {
        return userSetting.isEnableImportEmailNotification();
    }

    public void setEnableImportEmailNotification(boolean enableImportEmailNotification) {
        userSetting.setEnableImportEmailNotification(enableImportEmailNotification);
    }

    public boolean isEnableWaitTimeMessage() {
        return userSetting.isEnableWaitTimeMessage();
    }

    public void setEnableWaitTimeMessage(boolean enableWaitTimeMessage) {
        userSetting.setEnableWaitTimeMessage(enableWaitTimeMessage);
    }
}
