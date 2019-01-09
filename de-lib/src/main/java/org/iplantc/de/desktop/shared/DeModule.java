package org.iplantc.de.desktop.shared;

import jsinterop.annotations.JsType;

/**
 * top level items don't need '.' prefix. so we can have gwt-debug-analysesWindow instead of
 * gwt-debug-.analysesWindow
 * 
 * @author sriram
 * @author jstroot
 * 
 */
@JsType
public interface DeModule {
    @JsType
    interface Ids {
        String DESKTOP = "desktop";
        /**
         * sub-items
         */
        String NOTIFICATION_BUTTON = ".notificationButton";
        String USER_PREF_MENU = ".userPrefMenu";
        String HELP_MENU =".helpMenu";
        String FORUMS_BUTTON = ".forumsButton";

        /**
         * window tool buttons
         */
        String WIN_MAX_BTN = ".maximize";
        String WIN_RESTORE_BTN = ".restore";
        String WIN_MIN_BTN = ".minimize";
        String WIN_CLOSE_BTN = ".close";
        String WIN_LAYOUT_BTN = ".layout";


        String DATA_BTN = ".dataBtn";
        String APPS_BTN = ".appsBtn";
        String ANALYSES_BTN = ".analysesBtn";
        String FEEDBACK_BTN = ".feedbackBtn";
        String TASK_BAR = ".deTaskBar";
        String PREFERENCES_LINK = ".preferences";
        String COLLABORATORS_LINK = ".collaborators";
        String USER_MANUAL_LINK = ".userManual";
        String INTRO_LINK = ".introduction";
        String ABOUT_LINK = ".about";
        String LOGOUT_LINK = ".logout";
        String SUPPORT_BTN = ".support";
        String FAQS_LINK = ".faqs";
        String FORUMS_LINK = ".forum";
        String FEEDBACK_LINK = ".feedback";
    }

    interface WindowIds {

        /**
         * top level items grouping
         *
         */
        String ANALYSES_WINDOW = "analysesWindow";
        String APPS_WINDOW = "appsWindow";
        String APP_EDITOR_WINDOW = "appEditorWindow";
        String DISK_RESOURCE_WINDOW = "diskResourceWindow";
        String APP_LAUNCH_WINDOW = "appLaunchWindow";
        String NOTIFICATION = "notificationWindow";
        String WORKFLOW_EDITOR = "workflowEditorWindow";
        String SIMPLE_DOWNLOAD = "simpleDownloadWindow";
        String ABOUT_WINDOW = "aboutWindow";
        String MANAGE_TOOLS_WINDOW = "manageToolsWindow";
        String COLLABORATORS_WINDOW = "collaboratorsWindow";
        String FILE_VIEWER = "fileViewerWindow";
        String TEAMS_WINDOW = "teamsWindow";
        String COMMUNITIES_WINDOW = "communitiesWindow";
    }
}
