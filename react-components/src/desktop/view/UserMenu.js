/**
 * @author sriram
 */
import React, { Component } from "react";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import Divider from "@material-ui/core/Divider";
import DEHyperlink from "../../../src/util/hyperlink/DEHyperLink";
import styles from "../style";
import ids from "../ids";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import intlData from "../messages";
import build from "../../util/DebugIDUtil";
import tour from "../NewUserTourSteps";
import userImg from "../../resources/images/user.png";
import { withStyles } from "@material-ui/core/styles";

class UserMenu extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
        this.onUserMenuClick = this.onUserMenuClick.bind(this);
        this.onMenuItemSelect = this.onMenuItemSelect.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.userBtn = React.createRef();
    }

    componentDidMount() {
        this.userBtn.current.setAttribute("data-intro", tour.SettingsMenu.message);
        this.userBtn.current.setAttribute("data-position", tour.SettingsMenu.position);
        this.userBtn.current.setAttribute("data-step", tour.SettingsMenu.step);
    }

    handleClose() {
        this.setState({anchorEl: null});
    };

    onUserMenuClick(event) {
        this.setState({anchorEl: document.getElementById(this.props.anchor)});
    }

    onMenuItemSelect(event) {
        let id = event.currentTarget.id;
        let presenter = this.props.presenter;
        switch (id) {
            case build(ids.DESKTOP, ids.PREFERENCES_LINK):
                presenter.onPreferencesClick();
                break;
            case build(ids.DESKTOP, ids.COLLABORATORS_LINK):
                presenter.onCollaboratorsClick();
                break;

            case build(ids.DESKTOP, ids.USER_MANUAL_LINK):
                presenter.onDocumentationClick();
                break;

            case build(ids.DESKTOP, ids.INTRO_LINK):
                this.doIntro();
                break;
            case build(ids.DESKTOP, ids.ABOUT_LINK):
                presenter.onAboutClick();
                break;

            case build(ids.DESKTOP, ids.LOGOUT_LINK):
                presenter.doLogout(false);
                break;
            default:
                break; //do nothing
        }
    }

    render() {
        const {anchorEl} = this.state;
        const classes = this.props.classes;
        return (
            <span>
                    <img className={classes.menuIcon}
                         src={userImg}
                         alt="User Menu"
                         onClick={this.onUserMenuClick}
                         ref={this.userBtn}>
                    </img>
                <Menu id={build(ids.DESKTOP, ids.USER_PREF_MENU)}
                      anchorEl={anchorEl}
                      open={Boolean(anchorEl)}
                      onClose={this.handleClose}>
                    <MenuItem id={build(ids.DESKTOP, ids.PREFERENCES_LINK)}
                              onClick={this.onMenuItemSelect}>
                        <DEHyperlink text={getMessage("preferences")}
                                     onClick={this.handleClose}/>
                    </MenuItem>
                    <MenuItem id={build(ids.DESKTOP, ids.COLLABORATORS_LINK)}
                              onClick={this.onMenuItemSelect}>
                        <DEHyperlink text={getMessage("collaboration")}
                                     onClick={this.handleClose}/>
                    </MenuItem>
                      <Divider />
                    <MenuItem id={build(ids.DESKTOP, ids.USER_MANUAL_LINK)}
                              onClick={this.onMenuItemSelect}>
                        <DEHyperlink text={getMessage("documentation")}
                                     onClick={this.handleClose}/>
                    </MenuItem>
                    <MenuItem id={build(ids.DESKTOP, ids.INTRO_LINK)}
                              onClick={this.props.doIntro}>
                        <DEHyperlink text={getMessage("introduction")}
                                     onClick={this.handleClose}/>
                    </MenuItem>
                    <MenuItem id={build(ids.DESKTOP, ids.ABOUT_LINK)}
                              onClick={this.onMenuItemSelect}>
                        <DEHyperlink text={getMessage("about")}
                                     onClick={this.handleClose}/>
                    </MenuItem>
                     <Divider />
                    <MenuItem id={build(ids.DESKTOP, ids.LOGOUT_LINK)}
                              onClick={this.onMenuItemSelect}>
                        <DEHyperlink text={getMessage("logout")}
                                     onClick={this.handleClose}/>
                    </MenuItem>
                </Menu>
            </span>
        );
    }
}

export default withStyles(styles)(withI18N(UserMenu, intlData));