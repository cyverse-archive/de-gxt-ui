/**
 * @author sriram
 */
import React, {Component} from "react";
import UserMenu from "./UserMenu";
import Notifications from "./Notifications";
import Help from "./Help";
import Taskbar from "./Taskbar";
import intlData from "../messages";
import styles from "../style";
import injectSheet from "react-jss";
import ids from "../ids";
import constants from "../../constants";
import Sockette from "sockette";
import withI18N from "../../util/I18NWrapper";
import build from "../../util/DebugIDUtil";

class DesktopView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            windows: this.props.windowConfigList,
            unSeenCount: 0,
            notifications: {},
        }
        this.handleDesktopClick = this.handleDesktopClick.bind(this);
        this.onNotificationClicked = this.onNotificationClicked.bind(this);
        this.getWSUrl = this.getWSUrl.bind(this);
        this.handleMessage = this.handleMessage.bind(this);
        this.displayNotification = this.displayNotification.bind(this);
        this.onMarkAllAsSeenClicked = this.onMarkAllAsSeenClicked.bind(this);
        this.onViewAllNotificationsClicked = this.onViewAllNotificationsClicked.bind(this);
        this.onViewNewNotificationsClicked = this.onViewNewNotificationsClicked.bind(this);
        this.onTaskButtonClick = this.onTaskButtonClick.bind(this);
        this.updateApplicationTitle = this.updateApplicationTitle.bind(this);
    }

    componentDidMount() {
        this.props.presenter.getNotifications((notifications) => {
            this.setState({
                unSeenCount: notifications.unseen_total,
                notifications: notifications,
            });
        });
        new Sockette(this.getWSUrl(), {
            maxAttempts: 10,
            onmessage: e => this.handleMessage(e),
            onreconnect: e => console.log('Reconnecting...', e),
            onmaximum: e => console.log('Stop Attempting!', e),
            onerror: e => console.log('Error:', e)
        });

    }

    componentWillReceiveProps(nextProps) {
        this.setState({windows: nextProps.windowConfigList});
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
        console.log(messageId);
        const {notifications} = this.state;
        if(notifications && notifications.messages){
            let found = notifications.messages.find(function (n) {
               console.log(n.message.id + "<===>" + messageId);
               return n.message.id === messageId;
            });
            console.log("found=>" + found);
            if(found) {
                this.props.presenter.onNotificationSelected(found,(updatedUnSeenCount) => {
                    this.setState({
                        unSeenCount:updatedUnSeenCount,
                    });
                }, (httpStatusCode, errMsg) => {
                    // do nothing for now
                });
            }
        }
    }

    handleMessage(event) {
        console.log(event.data);
        let msg = null;
        try {
            msg = JSON.parse(event.data);
        } catch (e) {
            return;
        }
        console.log("count-->" + msg.total);
        console.log("msg-->" + msg.message);
        if (msg.total) {
            this.setState({unSeenCount: msg.total});
        }
        if (msg.message) {
            let notifyQueue = this.state.notifications;
            if(notifyQueue.messages) {
                notifyQueue.messages.push(msg.message);
            } else {
                notifyQueue.messages = [];
                notifyQueue.messages.push(msg.message)
            }
            this.setState({notifications: notifyQueue});
            this.displayNotification(msg.message);
        }
    }

    displayNotification(message) {
        let displayText = message.message.text;
        let category = message.type;
        let analysisStatus = (message.type==="analysis")? message.payload.status : "";
        this.props.presenter.displayNotificationPopup(displayText, category, analysisStatus);
    }


    getWSUrl() {
        let location = window.location;
        let protocol = (location.protocol === "HTTPS") ? constants.WSS_PROTOCOL : constants.WS_PROTOCOL;
        let host = location.hostname;
        let port = location.port;
        const notificationUrl = protocol + host + (port ? ':' + port : '') + constants.NOTIFICATION_WS;
        console.log(notificationUrl);
        return notificationUrl;
    }

    onViewNewNotificationsClicked() {
        this.props.presenter.doSeeNewNotifications();
    }

    onViewAllNotificationsClicked() {
       this.props.presenter.doSeeAllNotifications();
    }

    onMarkAllAsSeenClicked() {
       this.props.presenter.doMarkAllSeen(true,(updatedUnSeenCount) => {
            this.setState({
                unSeenCount:updatedUnSeenCount,
            });
        }, (httpStatusCode, errMsg) => {
            // do nothing for now
        });
    }

    onTaskButtonClick(windowConfig) {
        this.props.presenter.onTaskButtonClicked(windowConfig);
    }

    updateApplicationTitle(count) {
        if (count > 0) {
            document.title = constants.DE + " (" + count + ")";
        } else {
            document.title = constants.DE;
        }
    }

    render() {
        const {windows, unSeenCount, notifications}  = this.state;
        const classes = this.props.classes;
        this.updateApplicationTitle(unSeenCount);
        return (
            <div className={classes.body}>
                <div className={classes.header}>
                    <div className={classes.logo}>
                        <span className={classes.logoContainer}>{constants.CYVERSE_DE}</span>
                        <span className={classes.userMenuContainer}>
                            <nav>
                                <Notifications
                                    anchor={build(ids.DESKTOP, ids.NOTIFICATION_MENU_ANCHOR)}
                                    notifications={notifications}
                                    unSeenCount={unSeenCount}
                                    presenter={this.props.presenter}
                                    notificationClicked={this.onNotificationClicked}
                                    viewNewNotification={this.onViewNewNotificationsClicked}
                                    viewAllNotification={this.onViewAllNotificationsClicked}
                                    markAllAsSeen={this.onMarkAllAsSeenClicked}/>
                                <UserMenu anchor={build(ids.DESKTOP, ids.USER_MENU_ANCHOR)}
                                          presenter={this.props.presenter}/>
                                <Help anchor={build(ids.DESKTOP, ids.HELP_MENU_ANCHOR)}
                                      presenter={this.props.presenter}/>
                                <span id={build(ids.DESKTOP, ids.NOTIFICATION_MENU_ANCHOR)}
                                      className={classes.notificationMenuPosition}/>
                                <span id={build(ids.DESKTOP, ids.USER_MENU_ANCHOR)}
                                      className={classes.userMenuPosition}/>
                                <span id={build(ids.DESKTOP, ids.HELP_MENU_ANCHOR)}
                                      className={classes.helpMenuPosition}/>
                            </nav>
                        </span>
                    </div>
                </div>
                <div id={this.props.desktopContainerId} className={classes.desktop}>
                    <img className={classes.data}
                         id={build(ids.DESKTOP, ids.DATA_BTN)}
                         alt="data" onClick={this.handleDesktopClick}
                         src="data:image/png;base64,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"></img>
                    <img className={classes.apps}
                         id={build(ids.DESKTOP, ids.APPS_BTN)}
                         alt="apps"
                         onClick={this.handleDesktopClick}
                         src="data:image/png;base64,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"></img>
                    <img className={classes.analyses}
                         id={build(ids.DESKTOP, ids.ANALYSES_BTN)}
                         onClick={this.handleDesktopClick}
                         alt="analyses"
                         src="data:image/png;base64,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"></img>
                </div>
                <div>
                    <Taskbar windows={windows} taskButtonClickHandler={this.onTaskButtonClick}/>
                </div>
            </div>);
    }
}

export default injectSheet(styles)(withI18N(DesktopView, intlData));