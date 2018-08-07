/**
 * @author sriram
 */
import React, { Component } from "react";
import MenuItem from "@material-ui/core/MenuItem";
import DEHyperlink from "../../../src/util/hyperlink/DEHyperLink";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import ids from "../ids";
import build from "../../util/DebugIDUtil";
import RefreshIcon from "@material-ui/icons/Refresh";
import Button from "@material-ui/core/Button";
import CircularProgress from "@material-ui/core/CircularProgress";
import notificationImg from "../../resources/images/notification.png";
import tour from "../NewUserTourSteps";
import { withStyles } from "@material-ui/core/styles";
import styles from "../style";
import Menu from "@material-ui/core/Menu";
import Divider from "@material-ui/core/Divider";
import intlData from "../messages";


function ErrorComponent(props) {
    return (
        <div style={{outline: 'none'}}>
            <div className={props.classes.notificationError}>
                {getMessage("notificationError")}
            </div>
            <div id={build(ids.DESKTOP,ids.RETRY_BTN)}>
                <Button variant="fab"
                        mini="true"
                        className={props.classes.errorRetryButton}
                        onClick={props.onClick}>
                    <RefreshIcon />
                </Button>
            </div>
        </div>
    )
}

function NotificationFooter(props) {
    return (
        <MenuItem>
            {(props.unSeenCount > 10) ?
                <div>
                     <span id={build(ids.DESKTOP, ids.NEW_NOTIFICATIONS)}>
                         <DEHyperlink
                             onClick={props.viewNewNotification}
                             text={getMessage("newNotifications", {values: {count: props.unSeenCount}})}/>
                     </span>
                    <span style={{margin: '20px'}}> </span>
                    <span id={build(ids.DESKTOP, ids.MARK_ALL_SEEN)}>
                            <DEHyperlink
                                onClick={() => {
                                    props.markAllAsSeen(true)
                                }}
                                text={getMessage("markAllRead")}/>
                    </span>
                </div>
                :
                <span id={build(ids.DESKTOP, ids.SEE_ALL_NOTIFICATIONS)}>
                    <DEHyperlink
                        onClick={props.viewAllNotification}
                        text={getMessage("viewAllNotifi")}/>
                </span>
            }
        </MenuItem>
    );
}

function Notification(props) {
    const {notification, onClick} = props;
    let notificationStyle = {
        fontSize: 10,
    };
    if (!notification.seen) {
        notificationStyle.backgroundColor = '#99d9ea';
        notificationStyle.borderBottom = 1;
    }

    return (
        <span style={{outline: 'none'}}>
                    <MenuItem
                        id={notification.message.id}
                        onClick={onClick}
                        style={notificationStyle}>
                        {notification.message.text}
                        {notification.payload.access_url &&
                        <InteractiveAnalysisUrl notification={notification}/>
                        }
                    </MenuItem>
                    <Divider/>
        </span>
    );

}

function InteractiveAnalysisUrl(props) {
    return (
        <span>
            {getMessage("interactiveAnalysisUrl")}
            <a href={props.notification.payload.access_url}
               target="_blank">
                    {getMessage("urlPrompt")}
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
        this.handleNotificationsClick = this.handleNotificationsClick.bind(this);
        this.onMenuItemSelect = this.onMenuItemSelect.bind(this);
        this.notificationBtn = React.createRef();
    }

    handleNotificationsClick() {
        this.setState({anchorEl: document.getElementById(this.props.anchor)});
        const {unSeenCount, markAllAsSeen} = this.props;
        //if unseencount < 10, mark them as read
        if (unSeenCount > 0 && unSeenCount < 10) {
            markAllAsSeen(false);
        }

    }

    handleClose = () => {
        this.setState({anchorEl: null});
    };

    onMenuItemSelect(event) {
        this.props.notificationClicked(event.currentTarget.id);
        this.handleClose();
    }

    componentDidMount() {
        this.notificationBtn.current.setAttribute("data-intro", tour.NotificationWindow.message);
        this.notificationBtn.current.setAttribute("data-position", tour.NotificationWindow.position);
        this.notificationBtn.current.setAttribute("data-step", tour.NotificationWindow.step);
    }

    render() {
        const {
            anchorEl,
        } = this.state;

        const {notifications, unSeenCount, classes, notificationLoading, error} = this.props;
        const messages = (notifications && notifications.messages && notifications.messages.length > 0) ? notifications.messages : [];


        return (
            <React.Fragment>
                    <img className={classes.menuIcon}
                         src={notificationImg}
                         alt="Notifications"
                         onClick={this.handleNotificationsClick}
                         ref={this.notificationBtn}></img>
                {unSeenCount !== 0 &&
                <span id='notifyCount'
                      className={classes.unSeenCount}>
                        {unSeenCount}
                    </span>
                }
                <Menu id={build(ids.DESKTOP, ids.NOTIFICATIONS_MENU)}
                      anchorEl={anchorEl}
                      open={Boolean(anchorEl)}
                      onClose={this.handleClose}
                      className={classes.notificationMenu}
                >
                        {notificationLoading ? (
                                <CircularProgress size={30}
                                                  className={classes.loadingStyle}
                                                  thickness={7}/>
                            )
                            : (error ? (
                                    <ErrorComponent classes={classes}
                                                    onClick={this.props.fetchNotifications}/>
                                ) : (
                                    (messages.length > 0) ?
                                        messages.map(n => {
                                            return (
                                                <Notification key={n.message.id}
                                                              notification={n}
                                                              onClick={this.onMenuItemSelect}/>
                                            )
                                        }).reverse() : (
                                            <MenuItem id={build(ids.DESKTOP, ids.EMPTY_NOTIFICATION)}
                                                      onClick={this.onMenuItemSelect}>
                                                <div className={classes.notificationError}>
                                                    {getMessage("noNotifications")}
                                                </div>
                                            </MenuItem>
                                        )))}
                    <NotificationFooter unSeenCount={unSeenCount}
                                        viewAllNotification={this.props.viewAllNotification}
                                        markAllAsSeen={this.props.markAllAsSeen}
                                        viewNewNotification={this.props.viewNewNotification}
                                        />
                    </Menu>
            </React.Fragment>
        );
    }
}

export default withStyles(styles)(withI18N(Notifications, intlData));



