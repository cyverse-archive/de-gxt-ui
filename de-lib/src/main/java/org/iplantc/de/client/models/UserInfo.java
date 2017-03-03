package org.iplantc.de.client.models;

import org.iplantc.de.client.models.bootstrap.AppsInfo;
import org.iplantc.de.client.models.bootstrap.DataInfo;
import org.iplantc.de.client.models.bootstrap.Preferences;
import org.iplantc.de.client.models.bootstrap.Session;
import org.iplantc.de.client.models.bootstrap.SystemIds;
import org.iplantc.de.client.models.bootstrap.UserBootstrap;
import org.iplantc.de.client.models.bootstrap.UserProfile;
import org.iplantc.de.client.models.bootstrap.Workspace;
import org.iplantc.de.client.models.userSettings.UserSetting;
import org.iplantc.de.shared.DEProperties;

import com.google.common.base.Strings;
import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;
import java.util.Map;

/**
 * Holds all the information about an user.
 *
 * Note: init() must be called when using this class for the first time in the application.
 *
 * @author sriram
 *
 */
public class UserInfo {

    private static UserInfo instance;

    /**
     * Get an instance of UserProfile.
     *
     * @return a singleton instance of the object.
     */
    public static UserInfo getInstance() {
        if (instance == null) {
            instance = new UserInfo();
        }

        return instance;
    }

    private final CommonModelAutoBeanFactory factory = GWT.create(CommonModelAutoBeanFactory.class);
    private UserBootstrap userBootstrap;

    private UserProfile userProfile;
    private DataInfo dataInfo;
    private Preferences preferences;
    private Session session;
    private Workspace workspace;
    private SystemIds systemIds;
    private AppsInfo appsInfo;
    private List<WindowState> savedOrderedWindowStates;
    private static String AGAVE_AUTH_KEY = "agave";

    /**
     * Constructs a default instance of the object with all fields being set to null.
     */
    private UserInfo() {
    }

    /**
     * Initializes UserProfile object.
     *
     * This method must be called before using any other member functions of this class
     *
     * @param userInfoJson json to initialize user info.
     */
    public void init(String userInfoJson) {
        userBootstrap = AutoBeanCodex.decode(factory, UserBootstrap.class, userInfoJson).as();

        userProfile = userBootstrap.getUserProfile();
        dataInfo = userBootstrap.getDataInfo();
        preferences = userBootstrap.getPreferences();
        session = userBootstrap.getSession();
        appsInfo = userBootstrap.getAppsInfo();
        if (appsInfo != null) {
            workspace = appsInfo.getWorkspace();
            systemIds = appsInfo.getSystemsIds();
        }
    }

    public boolean hasErrors() {
        return hasUserProfileError() ||
               hasDataInfoError() ||
               hasPreferencesError() ||
               hasSessionError() ||
               hasAppsInfoError();
    }

    public boolean hasUserProfileError() {
        return Strings.isNullOrEmpty(userProfile.getEmail()) ||
               Strings.isNullOrEmpty(userProfile.getFirstName()) ||
               Strings.isNullOrEmpty(userProfile.getFullUsername()) ||
               Strings.isNullOrEmpty(userProfile.getLastName()) ||
               Strings.isNullOrEmpty(userProfile.getUsername());
   }

    public boolean hasDataInfoError() {
        return dataInfo != null && dataInfo.getError() != null;
    }

    public boolean hasPreferencesError() {
        return preferences != null && preferences.getError() != null;
    }

    public boolean hasSessionError() {
        return session != null && session.getError() != null;
    }

    public boolean hasAppsInfoError() {
        return appsInfo != null && appsInfo.getError() != null;
    }

    public Splittable getDataInfoError() {
        return hasDataInfoError() ? dataInfo.getError() : null;
    }

    public Splittable getPreferencesError() {
        return hasPreferencesError() ? preferences.getError() : null;
    }

    public Splittable getSessionsError() {
        return hasSessionError() ? session.getError() : null;
    }

    public Splittable getAppsInfoError() {
        return hasAppsInfoError() ? appsInfo.getError() : null;
    }

    /**
     * Get user's email address.
     *
     * @return email address.
     */
    public String getEmail() {
        return userProfile == null || hasUserProfileError() ? null : userProfile.getEmail();
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return userProfile == null || hasUserProfileError() ? null : userProfile.getFirstName();
    }

    /**
     * Gets the full username.
     *
     * @return the fully qualified username.
     */
    public String getFullUsername() {
        return userProfile == null || hasUserProfileError() ? null : userProfile.getFullUsername();
    }

    public UserSetting getUserPreferences() {
        return preferences;
    }

    /**
     * @return the path to the user's home directory.
     */
    public String getHomePath() {
        if (dataInfo == null || hasDataInfoError()) {
            String irodsHome = DEProperties.getInstance().getIrodsHomePath();
            String username = userProfile.getUsername();

            if (Strings.isNullOrEmpty(irodsHome) || Strings.isNullOrEmpty(username)) {
                return "";
            }

            return irodsHome + "/" + username;
        }
        return dataInfo.getHomePath();
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return userProfile == null || hasUserProfileError() ? null : userProfile.getLastName();
    }

    public Long getLoginTime() {
        return session == null || hasSessionError() ? null : session.getLoginTime();
    }

    /**
     * @return the savedOrderedWindowStates
     */
    public List<WindowState> getSavedOrderedWindowStates() {
        return savedOrderedWindowStates;
    }

    /**
     * @return the path to the user's trash.
     */
    public String getTrashPath() {
        if (dataInfo == null || hasDataInfoError()) {
            String baseTrashPath = getBaseTrashPath();
            String username = userProfile.getUsername();

            if (Strings.isNullOrEmpty(baseTrashPath) || Strings.isNullOrEmpty(username)) {
                return "";
            }

            return baseTrashPath + "/" + username;
        }
        return dataInfo.getTrashPath();
    }

    /**
     * @return the base trash path of the data store for all users.
     */
    public String getBaseTrashPath() {
        if (dataInfo == null || hasDataInfoError()) {
            String baseTrashPath = DEProperties.getInstance().getBaseTrashPath();

            if (Strings.isNullOrEmpty(baseTrashPath)) {
                return "";
            }

            return baseTrashPath;
        }
        return dataInfo.getBaseTrashPath();
    }

    public void setDataInfo(DataInfo dataInfo) {
        this.dataInfo = dataInfo;
    }

    /**
     * Gets the username for the user.
     *
     * This value corresponds to an entry in LDAP.
     *
     * @return a string representing the username for the user.
     */
    public String getUsername() {
        return userProfile == null || hasUserProfileError() ? null : userProfile.getUsername();
    }

    /**
     * Gets the workspace id for the user.
     *
     * @return a string representing the identifier for workspace.
     */
    public String getWorkspaceId() {
        return workspace == null ? null : workspace.getWorkspaceId();
    }

    /**
     * @return the newUser
     */
    public boolean isNewUser() {
        return workspace == null || hasAppsInfoError() ? true : workspace.isNewUser();
    }

    /**
     * @param savedOrderedWindowStates the savedOrderedWindowStates to set
     */
    public void setSavedOrderedWindowStates(List<WindowState> savedOrderedWindowStates) {
        this.savedOrderedWindowStates = savedOrderedWindowStates;
    }

    public void setAuthRedirects(Map<String, String> redirects) {
        if (session == null) {
            session = factory.session().as();
        } else {
            session.setError(null);
        }
        session.setAuthRedirects(redirects);
    }

    public Map<String, String> getAuthRedirects() {
        return session == null || hasSessionError() ? null : session.getAuthRedirects();
    }

    public boolean hasAgaveRedirect() {
        return getAuthRedirects() != null && session.getAuthRedirects().containsKey(AGAVE_AUTH_KEY);
    }

    public String getAgaveRedirect() {
        return !hasAgaveRedirect() ? null : session.getAuthRedirects().get(AGAVE_AUTH_KEY);
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public SystemIds getSystemIds() {
        return systemIds;
    }

}

