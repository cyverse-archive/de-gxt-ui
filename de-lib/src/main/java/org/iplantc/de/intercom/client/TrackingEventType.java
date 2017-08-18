package org.iplantc.de.intercom.client;

/**
 * User events to track
 *
 * Created by sriram on 8/2/17.
 */
public interface TrackingEventType {
    String DATA_WINDOW_OPEN = "Data Window Opened";

    String DATA_WINDOW_CLOSED = "Data Window Closed";

    String APPS_WINDOW_OPEN = "Apps Window Opened";

    String APPS_WINDOW_CLOSED = "Apps Window Closed";

    String ANALYSIS_WINDOW_OPEN = "Analyses Window Opened";

    String ANALYSIS_WINDOW_CLOSE = "Analyses Window Closed";

    String JOB_LAUNCHED = "Job Launched";

    String HELP_BUTTON_CLICKED = "Help Button Clicked";

    String ANALYSIS_HELP_CLICKED = "Analysis Help Clicked";

    String ABOUT_WINDOW_OPEN = "About Window Opened";

    String ABOUT_WINDOW_CLOSED = "About Window Closed";

    String APP_INT_WINDOW_OPEN = "App Integration Window Open";

    String APP_INT_WINDOW_CLOSED = "App Integration Window Closed";

    String APP_WIZARD_OPEN = "App Wizard Window Open";

    String APP_WIZARD_CLOSED = "App Wizard Window Closed";

    String DATA_VIEWER_WINDOW_OPEN = " Data Viewer Window Opened";

    String DATA_VIEWER_WINDOW_CLOSED = "Data Viewer Window Closed";

    String NOTIFICATION_WINDOW_OPEN = "Notification Window Open";

    String NOTIFICATION_WINDOW_CLOSED = "Notification Window Closed";

    String SIMPLE_DOWNLOAD_WINDOW_OPEN = "Simple Download Window Open";

    String SIMPLE_DOWNLOAD_WINDOW_CLOSED = "Simple Download Window Closed";

    String WORKFLOW_INT_WINDOW_OPEN = "Workflow Integration Window Open";

    String WORKFLOW_INT_WINDOW_CLOSED = "Workflow Integration Window Closed";

    String SYS_MESSAGE_WINDOW_OPEN = "System Message Window Open";

    String SYS_MESSAGE_WINDOW_CLOSED = "System Message Window Closed";

    String MANAGE_TOOLS_WINDOW_OPEN = "Manage Tools Window Open";

    String MANAGE_TOOLS_WINDOW_CLOSED = "Manage Tools Window Closed";

    String COLLAB_WINDOW_OPEN = "Collaborator Window Open";

    String COLLAB_WINDOW_CLOSED = "Collaborator Window Closed";

    String URL_IMPORT_SUBMITTED = "URL Import Submitted";

    String SIMPLE_UPLOAD_SUBMITTED = "Simple Upload Submitted";

    String ANALYSIS_USER_SUPPORT_REQUESTED = "Analysis User Support Requested";

    String DATA_FAQ_CLICKED = "Data FAQ Clicked";

    String APPS_FAQ_CLICKED = "Apps FAQ Clicked";

    String ANALYSES_FAQ_CLICKED = "Analyses FAQ Clicked";
}
