/**
 * @author sriram
 */
import React, { Component } from "react";

import introJs from "intro.js";
import Sockette from "sockette";
import { injectIntl } from "react-intl";

import { build, withI18N } from "@cyverse-de/ui-lib";

import appsImg from "../../resources/images/apps.png";
import analysesImg from "../../resources/images/analyses.png";
import dataImg from "../../resources/images/data.png";

import constants from "../../constants";
import Help from "./Help";
import ids from "../ids";
import Notifications from "./Notifications";
import messages from "../messages";
import UserMenu from "./UserMenu";
import styles from "../style";
import Taskbar from "./Taskbar";
import tour from "../NewUserTourSteps";

import { withStyles } from "@material-ui/core";

class DesktopView extends Component {
    constructor(props) {
        super(props);
        this.state = {
            windows: this.props.windowConfigList,
            unSeenCount: 0,
            notifications: {},
            notificationsError: false,
            notificationLoading: false,
        };
        this.handleDesktopClick = this.handleDesktopClick.bind(this);
        this.onNotificationClicked = this.onNotificationClicked.bind(this);
        this.getWebSocketUrl = this.getWebSocketUrl.bind(this);
        this.handleMessage = this.handleMessage.bind(this);
        this.displayNotification = this.displayNotification.bind(this);
        this.onMarkAllAsSeenClicked = this.onMarkAllAsSeenClicked.bind(this);
        this.onViewAllNotificationsClicked = this.onViewAllNotificationsClicked.bind(
            this
        );
        this.onViewNewNotificationsClicked = this.onViewNewNotificationsClicked.bind(
            this
        );
        this.onTaskButtonClick = this.onTaskButtonClick.bind(this);
        this.updateApplicationTitle = this.updateApplicationTitle.bind(this);
        this.getNotifications = this.getNotifications.bind(this);
        this.initIntro = this.initIntro.bind(this);
        this.dataBtn = React.createRef();
        this.appsBtn = React.createRef();
        this.analysesBtn = React.createRef();
        this.doIntro = this.doIntro.bind(this);
    }

    componentDidMount() {
        this.getNotifications();
        new Sockette(this.getWebSocketUrl(), {
            maxAttempts: 10,
            onmessage: this.handleMessage,
            onreconnect: (e) => console.log("Reconnecting...", e),
            onmaximum: (e) => console.log("Stop Attempting!", e),
            onerror: (e) => console.log("Error:", e),
        });

        this.initIntro();
    }

    initIntro() {
        this.dataBtn.current.setAttribute(
            "data-intro",
            tour.DataWindow.message
        );
        this.dataBtn.current.setAttribute(
            "data-position",
            tour.DataWindow.position
        );
        this.dataBtn.current.setAttribute("data-step", tour.DataWindow.step);
        this.appsBtn.current.setAttribute(
            "data-intro",
            tour.AppsWindow.message
        );
        this.appsBtn.current.setAttribute(
            "data-position",
            tour.AppsWindow.position
        );
        this.appsBtn.current.setAttribute("data-step", tour.AppsWindow.step);
        this.analysesBtn.current.setAttribute(
            "data-intro",
            tour.AnalysesWindow.message
        );
        this.analysesBtn.current.setAttribute(
            "data-position",
            tour.AnalysesWindow.position
        );
        this.analysesBtn.current.setAttribute(
            "data-step",
            tour.AnalysesWindow.step
        );
    }

    getNotifications() {
        this.setState({ notificationLoading: true });
        this.props.presenter.getNotifications(
            (notifications) => {
                this.setState({
                    unSeenCount: parseInt(notifications.unseen_total, 10),
                    notifications: notifications,
                    notificationsError: false,
                    notificationLoading: false,
                });
            },
            (httpStatusCode, errMsg) => {
                this.setState({
                    notifications: {},
                    unSeenCount: 0,
                    notificationsError: true,
                    notificationLoading: false,
                });
            }
        );
    }

    componentWillReceiveProps(nextProps) {
        this.setState({ windows: nextProps.windowConfigList });
        if (
            nextProps.unseen_count > -1 &&
            nextProps.unseen_count !== this.state.unSeenCount
        ) {
            this.setState({ unSeenCount: nextProps.unseen_count }); //if -1, do not update count;
        }
        if (nextProps.isNewUser) {
            this.doIntro();
        }
    }

    handleDesktopClick(event) {
        var id = event.currentTarget.id;
        switch (id) {
            case build(ids.DESKTOP, ids.DATA_BTN):
                this.props.presenter.onDataWinBtnSelect();
                break;
            case build(ids.DESKTOP, ids.APPS_BTN):
                this.props.presenter.onAppsWinBtnSelect();
                break;
            case build(ids.DESKTOP, ids.ANALYSES_BTN):
                this.props.presenter.onAnalysesWinBtnSelect();
                break;
            default:
                //do nothing
                break;
        }
    }

    onNotificationClicked(messageId) {
        const { notifications } = this.state;
        if (notifications && notifications.messages) {
            let found = notifications.messages.find(function(n) {
                return n.message.id === messageId;
            });
            if (found) {
                this.props.presenter.onNotificationSelected(
                    found,
                    (updatedUnSeenCount) => {
                        this.setState({
                            unSeenCount: updatedUnSeenCount,
                        });
                        found.seen = true;
                    },
                    (httpStatusCode, errMsg) => {
                        // do nothing for now
                    }
                );
            }
        }
    }

    handleMessage(event) {
        console.log(event.data);
        let push_msg = null;
        try {
            push_msg = JSON.parse(event.data);
        } catch (e) {
            return;
        }
        if (push_msg.total) {
            this.setState({ unSeenCount: push_msg.total });
        }

        let message = push_msg.message;
        if (message) {
            let category = message.type;
            if (category === "team") {
                message.message.text =
                    message.message.text + message.payload.team_name; // attach team name to team notifications.
            }

            let notifyQueue = this.state.notifications;
            if (notifyQueue.messages) {
                notifyQueue.messages.push(message);
            } else {
                notifyQueue.messages = [];
                notifyQueue.messages.push(message);
            }
            this.setState({ notifications: notifyQueue });
            this.displayNotification(message, category);
            if (
                notifyQueue.messages.length > constants.NEW_NOTIFICATIONS_LIMIT
            ) {
                //max 10 notifications in notifications menu
                notifyQueue.messages.shift();
            }
        }
    }

    displayNotification(notification, category) {
        let displayText = notification.message.text;
        let analysisStatus =
            notification.type === "analysis" ? notification.payload.status : "";
        this.props.presenter.displayNotificationPopup(
            displayText,
            category,
            analysisStatus
        );
    }

    getWebSocketUrl() {
        let location = window.location;
        let protocol =
            location.protocol.toLowerCase() === "https:"
                ? constants.WSS_PROTOCOL
                : constants.WS_PROTOCOL;
        let host = location.hostname;
        let port = location.port;
        const notificationUrl =
            protocol +
            host +
            (port ? ":" + port : "") +
            constants.NOTIFICATION_WS;
        return notificationUrl;
    }

    onViewNewNotificationsClicked() {
        this.props.presenter.doSeeNewNotifications();
    }

    onViewAllNotificationsClicked() {
        this.props.presenter.doSeeAllNotifications();
    }

    onMarkAllAsSeenClicked(shouldNotifyUser) {
        this.props.presenter.doMarkAllSeen(
            shouldNotifyUser,
            (updatedUnSeenCount) => {
                this.setState({
                    unSeenCount: updatedUnSeenCount,
                });
                const { notifications } = this.state;
                notifications.messages.forEach(function(n) {
                    n.seen = true;
                });
            },
            (httpStatusCode, errMsg) => {
                // do nothing for now
            }
        );
    }

    onTaskButtonClick(windowConfig) {
        this.props.presenter.onTaskButtonClicked(windowConfig);
    }

    updateApplicationTitle(count) {
        if (count > 0) {
            document.title = `(${count}) ${constants.DE}`;
        } else {
            document.title = constants.DE;
        }
    }

    doIntro() {
        introJs().setOption("showStepNumbers", false);
        introJs().setOption("skipLabel", "Exit");
        introJs().setOption("overlayOpacity", 0);
        introJs().start();
    }

    render() {
        const {
            windows,
            unSeenCount,
            notifications,
            notificationsError,
            notificationLoading,
        } = this.state;
        const { classes, intl } = this.props;
        this.updateApplicationTitle(unSeenCount);
        return (
            <div className={classes.body}>
                <div className={classes.header}>
                    <div className={classes.logo}>
                        <span className={classes.logoContainer}>
                            {constants.CYVERSE_DE}
                        </span>
                        <span className={classes.userMenuContainer}>
                            <nav>
                                <Notifications
                                    anchor={build(
                                        ids.DESKTOP,
                                        ids.NOTIFICATION_MENU_ANCHOR
                                    )}
                                    notifications={notifications}
                                    unSeenCount={unSeenCount}
                                    notificationClicked={
                                        this.onNotificationClicked
                                    }
                                    viewNewNotification={
                                        this.onViewNewNotificationsClicked
                                    }
                                    viewAllNotification={
                                        this.onViewAllNotificationsClicked
                                    }
                                    markAllAsSeen={this.onMarkAllAsSeenClicked}
                                    error={notificationsError}
                                    notificationLoading={notificationLoading}
                                    fetchNotifications={this.getNotifications}
                                />
                                <UserMenu
                                    anchor={build(
                                        ids.DESKTOP,
                                        ids.USER_MENU_ANCHOR
                                    )}
                                    presenter={this.props.presenter}
                                />
                                <Help
                                    anchor={build(
                                        ids.DESKTOP,
                                        ids.HELP_MENU_ANCHOR
                                    )}
                                    presenter={this.props.presenter}
                                    doIntro={this.doIntro}
                                />
                                <span
                                    id={build(
                                        ids.DESKTOP,
                                        ids.NOTIFICATION_MENU_ANCHOR
                                    )}
                                    className={classes.notificationMenuPosition}
                                />
                                <span
                                    id={build(
                                        ids.DESKTOP,
                                        ids.USER_MENU_ANCHOR
                                    )}
                                    className={classes.userMenuPosition}
                                />
                                <span
                                    id={build(
                                        ids.DESKTOP,
                                        ids.HELP_MENU_ANCHOR
                                    )}
                                    className={classes.helpMenuPosition}
                                />
                            </nav>
                        </span>
                    </div>
                </div>
                {/*getMessge wont work with title attribute*/}
                <div
                    id={this.props.desktopContainerId}
                    className={classes.desktop}
                >
                    <img
                        className={classes.data}
                        id={build(ids.DESKTOP, ids.DATA_BTN)}
                        alt="data"
                        onClick={this.handleDesktopClick}
                        src={dataImg}
                        ref={this.dataBtn}
                        title={intl.formatMessage({ id: "dataToolTip" })}
                    />
                    <img
                        className={classes.apps}
                        id={build(ids.DESKTOP, ids.APPS_BTN)}
                        alt="apps"
                        onClick={this.handleDesktopClick}
                        src={appsImg}
                        ref={this.appsBtn}
                        title={intl.formatMessage({ id: "appsToolTip" })}
                    />
                    <img
                        className={classes.analyses}
                        id={build(ids.DESKTOP, ids.ANALYSES_BTN)}
                        onClick={this.handleDesktopClick}
                        alt="analyses"
                        src={analysesImg}
                        ref={this.analysesBtn}
                        title={intl.formatMessage({ id: "analysesTip" })}
                    />
                </div>
                <div>
                    <Taskbar
                        windows={windows}
                        taskButtonClickHandler={this.onTaskButtonClick}
                    />
                </div>
            </div>
        );
    }
}

export default withStyles(styles)(withI18N(injectIntl(DesktopView), messages));
