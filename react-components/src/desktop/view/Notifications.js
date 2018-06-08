/**
 * @author sriram
 */
import React, {Component} from "react";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import DEHyperlink from "../../../src/util/hyperlink/DEHyperLink";
import styles from "../style";
import Divider from "@material-ui/core/Divider";
import withI18N, {getMessage} from "../../util/I18NWrapper";
import intlData from "../messages";
import ids from "../ids";
import build from "../../util/DebugIDUtil";
import {withStyles} from "@material-ui/core/styles";
import RefreshIcon from "@material-ui/icons/Refresh";
import Button from "@material-ui/core/Button";
import CircularProgress from "@material-ui/core/CircularProgress";
import tourStrings from "../NewUserTourStrings";


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
                             onClick={this.props.viewNewNotification}
                             text={getMessage("newNotifications", {values: {count: props.unSeenCount}})}/>
                     </span>
                    <span style={{margin: '20px'}}> </span>
                    <span id={build(ids.DESKTOP, ids.MARK_ALL_SEEN)}>
                            <DEHyperlink
                                onClick={this.props.markAllAsSeen}
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
    )
}

class Notifications extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
        this.handleNotificationsClick = this.handleNotificationsClick.bind(this);
        this.onMenuItemSelect = this.onMenuItemSelect.bind(this);
        this.getNotification = this.getNotification.bind(this);
        this.notificationBtn = React.createRef();
    }

    handleNotificationsClick() {
        console.log(document.getElementById(this.props.anchor));
        this.setState({anchorEl: document.getElementById(this.props.anchor)});
    }

    handleClose = () => {
        this.setState({anchorEl: null});
    };

    onMenuItemSelect(event) {
        console.log("selected item =>" + event.currentTarget.id);
        this.props.notificationClicked(event.currentTarget.id);
    }

    componentDidMount() {
        this.notificationBtn.current.setAttribute("data-intro", tourStrings.introNotifications);
        this.notificationBtn.current.setAttribute("data-position", "left");
        this.notificationBtn.current.setAttribute("data-step", "4");
    }

    getNotification(notification) {
        if (notification.seen) {
            return (
                <span key={notification.message.id} style={{outline: 'none'}}>
                    <MenuItem id={notification.message.id}
                              onClick={this.onMenuItemSelect}
                              style={{fontSize: 10,}}>
                        {notification.message.text}
                    </MenuItem>
                    <Divider/>
                </span>
            );
        } else {
            return (
                <span key={notification.message.id} style={{outline: 'none'}}>
                    <MenuItem id={notification.message.id}
                              onClick={this.onMenuItemSelect}
                              style={{backgroundColor: '#e2e2e2', fontSize: 10,}}>
                        {notification.message.text}
                    </MenuItem>
                    <Divider/>
                </span>
            );
        }

    }

    render() {
        const {
            anchorEl,
        } = this.state;

        const {notifications, unSeenCount, classes, notificationLoading, error} = this.props;
        const messages = (notifications && notifications.messages && notifications.messages.length > 0) ? notifications.messages : [];


        return (
            <span>
                    <img className={classes.menuIcon}
                         src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAMAAABHPGVmAAAACXBIWXMAAC4jAAAuIwF4pT92AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAGBQTFRF////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////BmKR8wAAAB90Uk5TAAYJEhwiJ1VYWWR5e4CJjpWWp7HJyuXm7e7w8ff6/lycuGgAAAD0SURBVHgB7c1LsoJADAXQx1/5NNqoaZXm7n+Xz7IYMNCCFJOkzNnA+fvMGGOSxDl6cS5JdCfOYeacJauIMCPSnWBBb1IUTYOFpikKdUlZDsOAD4ahLEs9SV3HiG9ijHWtI2lbrGpb+UlVTRNWTVNVyU6y7H7HJiFkmeSk77FZ38tN0vT5xGaPR5pKTY5HsBwOUpPzGSyn008n1ytYLhepCRFYbjepyTiCZRylJmCz5M0SSyyxxBJLLOk6sHWdtIRf8Jv9Cb/gNwKS7wW/EZBgtx2JJdoZIiwQ6U28x4L3epM8DwGzEPLcktXGe3rx/l1oS4wx/4jHcRyd/1M5AAAAAElFTkSuQmCC"
                         alt="Notifications"
                         onClick={this.handleNotificationsClick}
                         ref={this.notificationBtn}></img>
                {unSeenCount !== "0" &&
                <span id='notifyCount'
                      className={classes.unSeenCount}>
                        {unSeenCount}
                    </span>
                }
                <Menu id={build(ids.DESKTOP, ids.NOTIFICATIONS_MENU)}
                      anchorEl={anchorEl}
                      open={Boolean(anchorEl)}
                      onClose={this.handleClose}
                      style={{width: '100%'}}>
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
                                                this.getNotification(n)
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
                                        viewAllNotification={this.props.viewAllNotification}/>
                    </Menu>
         </span>
        );
    }
}

export default withStyles(styles)(withI18N(Notifications, intlData));



