/**
 * @author sriram
 */
import React, {Component} from "react";
import {FormattedMessage, IntlProvider} from "react-intl";
import Menu, {MenuItem} from "@material-ui/core/Menu";
import DEHyperlink from "../../../src/util/hyperlink/DEHyperLink";
import styles from "../style";
import {css} from "aphrodite";
import Divider from "@material-ui/core/Divider";

class Notifications extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
        this.handleNotificationsClick = this.handleNotificationsClick.bind(this);
        this.onMenuItemSelect = this.onMenuItemSelect.bind(this);
    }

    handleNotificationsClick() {
        this.setState({anchorEl: document.getElementById(this.props.anchor)});
    }

    handleClose = () => {
        this.setState({anchorEl: null});
    };

    onMenuItemSelect(event) {
        console.log("selected item =>" + event.currentTarget.id);
        this.props.notificationClicked(event.currentTarget.id);
    }

    render() {
        const {
            anchorEl,
        } = this.state;
        const notifications = this.props.notifications;
        const unSeenCount = this.props.unSeenCount;
        const messages = (notifications && notifications.messages && notifications.messages.length > 0) ? notifications.messages : [];
        return (
            <IntlProvider locale='en' defaultLocale='en' messages={this.props.messages}>
                <span>
                      <img className={css(styles.menuIcon)}
                           src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAMAAABHPGVmAAAACXBIWXMAAC4jAAAuIwF4pT92AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAGBQTFRF////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////BmKR8wAAAB90Uk5TAAYJEhwiJ1VYWWR5e4CJjpWWp7HJyuXm7e7w8ff6/lycuGgAAAD0SURBVHgB7c1LsoJADAXQx1/5NNqoaZXm7n+Xz7IYMNCCFJOkzNnA+fvMGGOSxDl6cS5JdCfOYeacJauIMCPSnWBBb1IUTYOFpikKdUlZDsOAD4ahLEs9SV3HiG9ijHWtI2lbrGpb+UlVTRNWTVNVyU6y7H7HJiFkmeSk77FZ38tN0vT5xGaPR5pKTY5HsBwOUpPzGSyn008n1ytYLhepCRFYbjepyTiCZRylJmCz5M0SSyyxxBJLLOk6sHWdtIRf8Jv9Cb/gNwKS7wW/EZBgtx2JJdoZIiwQ6U28x4L3epM8DwGzEPLcktXGe3rx/l1oS4wx/4jHcRyd/1M5AAAAAElFTkSuQmCC"
                           alt="Notifications" onClick={this.handleNotificationsClick}></img>
                    <span id='notifyCount' className={css(styles.unSeenCount)}>
                        {(unSeenCount === "0") ? "" : unSeenCount}
                    </span>
                    <Menu id='notificationsMenu' anchorEl={anchorEl} open={Boolean(anchorEl)}
                          onClose={this.handleClose}>
                        {(messages.length > 0) ?
                            messages.map(n => {
                                return (
                                    <span key={n.message.id}>
                                        <MenuItem id={n.message.id}
                                                  onClick={this.onMenuItemSelect}>
                                            {n.message.text}
                                        </MenuItem>
                                        <Divider/>
                                    </span>
                                )
                            }).reverse() : (
                                <MenuItem id="noNotify"
                                          onClick={this.onMenuItemSelect}>
                                    No notifications to display!
                                </MenuItem>
                            )}
                        <MenuItem>
                            {(unSeenCount > 10) ?
                                <span id="viewNew"><DEHyperlink onClick={this.props.viewNewNotification}
                                    text={<FormattedMessage id="newNotifications"
                                                            values={{count: unSeenCount}}/>}/></span>
                                :
                                <span id="viewAll"><DEHyperlink onClick={this.props.viewAllNotification}
                                    text={<FormattedMessage id="viewAllNotifi"/>}/></span>
                            }
                            <span style={{margin: '20px'}}> </span>
                            <span id="markAll"><DEHyperlink onClick={this.props.markAllAsSenn}
                                text={<FormattedMessage id="markAllRead"/>}/></span>
                        </MenuItem>
                    </Menu>
                </span>
            </IntlProvider>
        );

    }
}

export default Notifications;



