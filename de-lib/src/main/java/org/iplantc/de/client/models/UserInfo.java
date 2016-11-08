package org.iplantc.de.client.models;

import org.iplantc.de.shared.DEProperties;

import com.google.common.base.Strings;
import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

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
     * Get an instance of UserInfo.
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
    private UserBootstrap userInfo;
    private List<WindowState> savedOrderedWindowStates;
    private static String AGAVE_AUTH_KEY = "agave";

    /**
     * Constructs a default instance of the object with all fields being set to null.
     */
    private UserInfo() {
    }

    /**
     * Initializes UserInfo object.
     *
     * This method must be called before using any other member functions of this class
     *
     * @param userInfoJson json to initialize user info.
     */
    public void init(String userInfoJson) {
        userInfo = AutoBeanCodex.decode(factory, UserBootstrap.class, userInfoJson).as();
    }

    /**
     * Get user's email address.
     *
     * @return email address.
     */
    public String getEmail() {
        return userInfo == null ? null : userInfo.getEmail();
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return userInfo == null ? null : userInfo.getFirstName();
    }

    /**
     * Gets the full username.
     *
     * @return the fully qualified username.
     */
    public String getFullUsername() {
        return userInfo == null ? null : userInfo.getFullUsername();
    }

    /**
     * @return the path to the user's home directory.
     */
    public String getHomePath() {
        if (userInfo == null || Strings.isNullOrEmpty(userInfo.getHomePath())) {
            String irodsHome = DEProperties.getInstance().getIrodsHomePath();
            String username = userInfo.getUsername();

            if (Strings.isNullOrEmpty(irodsHome) || Strings.isNullOrEmpty(username)) {
                return "";
            }

            return irodsHome + "/" + username;
        }
        return userInfo.getHomePath();
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return userInfo == null ? null : userInfo.getLastName();
    }

    public String getLoginTime() {
        return userInfo == null ? null : userInfo.getLoginTime();
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
        if (userInfo == null || Strings.isNullOrEmpty(userInfo.getTrashPath())) {
            String baseTrashPath = getBaseTrashPath();
            String username = userInfo.getUsername();

            if (Strings.isNullOrEmpty(baseTrashPath) || Strings.isNullOrEmpty(username)) {
                return "";
            }

            return baseTrashPath + "/" + username;
        }
        return userInfo.getTrashPath();
    }

    /**
     * @return the base trash path of the data store for all users.
     */
    public String getBaseTrashPath() {
        if (userInfo == null || Strings.isNullOrEmpty(userInfo.getBaseTrashPath())) {
            String baseTrashPath = DEProperties.getInstance().getBaseTrashPath();

            if (Strings.isNullOrEmpty(baseTrashPath)) {
                return "";
            }

            return baseTrashPath;
        }
        return userInfo.getBaseTrashPath();
    }

    /**
     * Gets the username for the user.
     *
     * This value corresponds to an entry in LDAP.
     *
     * @return a string representing the username for the user.
     */
    public String getUsername() {
        return userInfo == null ? null : userInfo.getUsername();
    }

    /**
     * Gets the workspace id for the user.
     *
     * @return a string representing the identifier for workspace.
     */
    public String getWorkspaceId() {
        return userInfo == null ? null : userInfo.getWorkspaceId();
    }

    /**
     * @return the newUser
     */
    public boolean isNewUser() {
        return userInfo == null ? false : userInfo.isNewUser();
    }

    /**
     * @param savedOrderedWindowStates the savedOrderedWindowStates to set
     */
    public void setSavedOrderedWindowStates(List<WindowState> savedOrderedWindowStates) {
        this.savedOrderedWindowStates = savedOrderedWindowStates;
    }

    public Map<String, String> getAuthRedirects() {
        return userInfo == null ? null : userInfo.getAuthRedirects();
    }

    public boolean hasAgaveRedirect() {
        return getAuthRedirects() != null && userInfo.getAuthRedirects().containsKey(AGAVE_AUTH_KEY);
    }

    public String getAgaveRedirect() {
        return !hasAgaveRedirect() ? null : userInfo.getAuthRedirects().get(AGAVE_AUTH_KEY);
    }
}

