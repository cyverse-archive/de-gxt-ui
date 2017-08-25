package org.iplantc.de.notifications.shared;

/**
 * @author aramsey
 */
public class Notifications {

    public interface UnseenIds {
        String NOTIFICATIONS_MENU = "notificationsMenu";
        String EMPTY_NOTIFICATION = ".emptyNotification";
        String MARK_ALL_SEEN = ".markAllSeenLink";
        String SEE_ALL_NOTIFICATIONS = ".seeAllNotificationsLink";
        String RETRY_NOTIFICATIONS = ".retryNotifications";
        String RETRY_BTN = ".retryBtn";
        String NOTIFICATION_LIST = ".notificationList";
    }

    public interface JoinRequestIds {
        String CANCEL_BTN = ".cancelBtn";
        String JOIN_REQUEST_DLG = "joinRequestDlg";
        String JOIN_REQUEST_VIEW = ".view";
        String APPROVE_BTN = ".approveBtn";
        String DENY_BTN = ".denyBtn";
        String SET_PRIVILEGE_DLG = "setPrivilegeDlg";
        String OK_BTN = ".okBtn";
        String DENY_REQUEST_DLG = "denyRequestDlg";
    }
}
