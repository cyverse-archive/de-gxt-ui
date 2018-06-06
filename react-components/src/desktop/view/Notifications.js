/**
 * @author sriram
 */
import React, {Component} from "react";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import DEHyperlink from "../../../src/util/hyperlink/DEHyperLink";
import styles from "../style";
import {css} from "aphrodite";
import Divider from "@material-ui/core/Divider";
import withI18N, {getMessage} from "../../util/I18NWrapper";
import intlData from "../messages";
import ids from "../ids";
import build from "../../util/DebugIDUtil";

class Notifications extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
        this.handleNotificationsClick = this.handleNotificationsClick.bind(this);
        this.onMenuItemSelect = this.onMenuItemSelect.bind(this);
        this.getNotification = this.getNotification.bind(this);
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

    getNotification(notification) {
        if (notification.seen) {
            return (
                <span key={notification.message.id}>
                                        <MenuItem id={notification.message.id}
                                                  onClick={this.onMenuItemSelect}>
                                            {notification.message.text}
                                        </MenuItem>
                                        <Divider/>
                </span>
            );
        } else {
            return (
                <span key={notification.message.id}>
                                        <MenuItem id={notification.message.id}
                                                  onClick={this.onMenuItemSelect}
                                                  style={{backgroundColor: '#e2e2e2'}}>
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
        const notifications = this.props.notifications;
        const unSeenCount = this.props.unSeenCount;
        const messages = (notifications && notifications.messages && notifications.messages.length > 0) ? notifications.messages : [];
        return (
                <span>
                      <img className={css(styles.menuIcon)}
                           src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAMAAABHPGVmAAAACXBIWXMAAC4jAAAuIwF4pT92AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAGBQTFRF////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////BmKR8wAAAB90Uk5TAAYJEhwiJ1VYWWR5e4CJjpWWp7HJyuXm7e7w8ff6/lycuGgAAAD0SURBVHgB7c1LsoJADAXQx1/5NNqoaZXm7n+Xz7IYMNCCFJOkzNnA+fvMGGOSxDl6cS5JdCfOYeacJauIMCPSnWBBb1IUTYOFpikKdUlZDsOAD4ahLEs9SV3HiG9ijHWtI2lbrGpb+UlVTRNWTVNVyU6y7H7HJiFkmeSk77FZ38tN0vT5xGaPR5pKTY5HsBwOUpPzGSyn008n1ytYLhepCRFYbjepyTiCZRylJmCz5M0SSyyxxBJLLOk6sHWdtIRf8Jv9Cb/gNwKS7wW/EZBgtx2JJdoZIiwQ6U28x4L3epM8DwGzEPLcktXGe3rx/l1oS4wx/4jHcRyd/1M5AAAAAElFTkSuQmCC"
                           alt="Notifications" onClick={this.handleNotificationsClick}></img>
                    {unSeenCount !== "0" &&
                    <span id='notifyCount'
                          className={css(styles.unSeenCount)}>
                        {unSeenCount}
                    </span>
                    }
                    <Menu id={build(ids.DESKTOP, ids.NOTIFICATIONS_MENU)}
                          anchorEl={anchorEl}
                          open={Boolean(anchorEl)}
                          onClose={this.handleClose}>
                        {(messages.length > 0) ?
                            messages.map(n => {
                                return (
                                    this.getNotification(n)
                                )
                            }).reverse() : (
                                <MenuItem id={build(ids.DESKTOP, ids.EMPTY_NOTIFICATION)}
                                          onClick={this.onMenuItemSelect}>
                                    {getMessage("noNotifications")}
                                </MenuItem>
                            )}
                        <MenuItem>
                            {(unSeenCount > 10) ?
                                <span id={build(ids.DESKTOP, ids.NEW_NOTIFICATIONS)}><DEHyperlink
                                    onClick={this.props.viewNewNotification}
                                    text={getMessage("newNotifications", {values: {count: unSeenCount}})}/></span>
                                :
                                <span id={build(ids.DESKTOP, ids.SEE_ALL_NOTIFICATIONS)}><DEHyperlink
                                    onClick={this.props.viewAllNotification}
                                    text={getMessage("viewAllNotifi")}/></span>
                            }
                            <span style={{margin: '20px'}}> </span>
                            <span id={build(ids.DESKTOP, ids.MARK_ALL_SEEN)}><DEHyperlink
                                onClick={this.props.markAllAsSeen}
                                text={getMessage("markAllRead")}/></span>
                        </MenuItem>
                    </Menu>
                </span>
        );

    }
}

export default withI18N(Notifications, intlData);



