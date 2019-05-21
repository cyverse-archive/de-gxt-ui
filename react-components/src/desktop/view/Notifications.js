/**
 * @author sriram
 */
import React, { Component } from "react";
import ReactDOM from "react-dom";
import classnames from "classnames";

import ids from "../ids";
import intlData from "../messages";
import styles from "../style";
import tour from "../NewUserTourSteps";

import {
    build,
    DEHyperlink,
    getMessage,
    LoadingMask,
    withI18N,
} from "@cyverse-de/ui-lib";

import Badge from "@material-ui/core/Badge";
import Fab from "@material-ui/core/Fab";
import Divider from "@material-ui/core/Divider";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";

import NotificationIcon from "@material-ui/icons/Notifications";
import RefreshIcon from "@material-ui/icons/Refresh";

import { withStyles } from "@material-ui/core/styles";

function ErrorComponent(props) {
    return (
        <div style={{ outline: "none" }}>
            <div className={props.classes.notificationError}>
                {getMessage("notificationError")}
            </div>
            <div id={build(ids.DESKTOP, ids.RETRY_BTN)}>
                <Fab
                    size="small"
                    className={props.classes.errorRetryButton}
                    onClick={props.onClick}
                >
                    <RefreshIcon />
                </Fab>
            </div>
        </div>
    );
}

function NotificationFooter(props) {
    const classes = props.classes;
    return (
        <div className={classes.notification} onClick={props.onClick}>
            {props.unSeenCount > 10 ? (
                <div>
                    <span id={build(ids.DESKTOP, ids.NEW_NOTIFICATIONS)}>
                        <DEHyperlink
                            onClick={props.viewNewNotification}
                            text={getMessage("newNotifications", {
                                values: { count: props.unSeenCount },
                            })}
                        />
                    </span>
                    <span style={{ margin: "20px" }}> </span>
                    <span id={build(ids.DESKTOP, ids.MARK_ALL_SEEN)}>
                        <DEHyperlink
                            onClick={() => {
                                props.markAllAsSeen(true);
                            }}
                            text={getMessage("markAllRead")}
                        />
                    </span>
                </div>
            ) : (
                <span id={build(ids.DESKTOP, ids.SEE_ALL_NOTIFICATIONS)}>
                    <DEHyperlink
                        onClick={props.viewAllNotification}
                        text={getMessage("viewAllNotifi")}
                    />
                </span>
            )}
        </div>
    );
}

function Notification(props) {
    const { notification, onClick, classes } = props;
    let className = classes.notification;
    if (!notification.seen) {
        className = classnames(
            classes.notification,
            classes.unSeenNotificationBackground
        );
    }

    return (
        <React.Fragment>
            <div
                id={notification.message.id}
                className={className}
                onClick={onClick}
            >
                <span>
                    {notification.message.text}
                    {notification.payload.access_url && (
                        <InteractiveAnalysisUrl notification={notification} />
                    )}
                </span>
            </div>
            <Divider />
        </React.Fragment>
    );
}

function InteractiveAnalysisUrl(props) {
    return (
        <span>
            {getMessage("dot")}
            <a
                href={props.notification.payload.access_url}
                target="_blank"
                rel="noopener noreferrer"
            >
                {getMessage("interactiveAnalysisUrl")}
            </a>
        </span>
    );
}

class Notifications extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
        this.handleNotificationsClick = this.handleNotificationsClick.bind(
            this
        );
        this.onMenuItemSelect = this.onMenuItemSelect.bind(this);
        this.notificationBtn = React.createRef();
    }

    handleNotificationsClick() {
        this.setState({ anchorEl: document.getElementById(this.props.anchor) });
        const { unSeenCount, markAllAsSeen } = this.props;
        //if unseencount < 10, mark them as read
        if (unSeenCount > 0 && unSeenCount < 10) {
            markAllAsSeen(false);
        }
    }

    handleClose = () => {
        this.setState({ anchorEl: null });
    };

    onMenuItemSelect(event) {
        this.props.notificationClicked(event.currentTarget.id);
        this.handleClose();
    }

    componentDidMount() {
        let ele = ReactDOM.findDOMNode(this.notificationBtn.current);
        ele.setAttribute("data-intro", tour.NotificationWindow.message);
        ele.setAttribute("data-position", tour.NotificationWindow.position);
        ele.setAttribute("data-step", tour.NotificationWindow.step);
    }

    render() {
        const { anchorEl } = this.state;

        const {
            notifications,
            unSeenCount,
            classes,
            notificationLoading,
            error,
        } = this.props;
        const messages =
            notifications &&
            notifications.messages &&
            notifications.messages.length > 0
                ? notifications.messages
                : [];

        return (
            <React.Fragment>
                <Badge
                    id={build((ids.DESKTOP, ids.UNSEEN_NOTIFICATION_COUNT))}
                    badgeContent={unSeenCount}
                    classes={{
                        badge: classes.notificationBadge,
                        root: classes.badge,
                    }}
                >
                    <NotificationIcon
                        id={build(ids.DESKTOP, ids.NOTIFICATION_BUTTON)}
                        className={classes.notificationMenuIcon}
                        onClick={this.handleNotificationsClick}
                        ref={this.notificationBtn}
                    />
                </Badge>
                <Menu
                    id={build(ids.DESKTOP, ids.NOTIFICATIONS_MENU)}
                    anchorEl={anchorEl}
                    open={Boolean(anchorEl)}
                    onClose={this.handleClose}
                    className={classes.notificationMenu}
                >
                    <LoadingMask loading={notificationLoading}>
                        {error ? (
                            <ErrorComponent
                                classes={classes}
                                onClick={this.props.fetchNotifications}
                            />
                        ) : messages.length > 0 ? (
                            messages
                                .map((n) => {
                                    return (
                                        <Notification
                                            key={n.message.id}
                                            notification={n}
                                            onClick={this.onMenuItemSelect}
                                            classes={classes}
                                        />
                                    );
                                })
                                .reverse()
                        ) : (
                            <MenuItem
                                id={build(ids.DESKTOP, ids.EMPTY_NOTIFICATION)}
                                onClick={this.onMenuItemSelect}
                            >
                                <div className={classes.notificationError}>
                                    {getMessage("noNotifications")}
                                </div>
                            </MenuItem>
                        )}
                        <NotificationFooter
                            unSeenCount={unSeenCount}
                            viewAllNotification={this.props.viewAllNotification}
                            markAllAsSeen={this.props.markAllAsSeen}
                            viewNewNotification={this.props.viewNewNotification}
                            onClick={this.handleClose}
                            classes={classes}
                        />
                    </LoadingMask>
                </Menu>
            </React.Fragment>
        );
    }
}

export default withStyles(styles)(withI18N(Notifications, intlData));
