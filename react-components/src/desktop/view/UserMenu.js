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
import { withStyles } from "@material-ui/core/styles";
import PersonIcon from "@material-ui/icons/Person";
import ReactDOM from "react-dom";

class UserMenu extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
        this.onUserMenuClick = this.onUserMenuClick.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.userBtn = React.createRef();
    }

    componentDidMount() {
        let ele = ReactDOM.findDOMNode(this.userBtn.current);
        ele.setAttribute("data-intro", tour.SettingsMenu.message);
        ele.setAttribute("data-position", tour.SettingsMenu.position);
        ele.setAttribute("data-step", tour.SettingsMenu.step);
    }

    handleClose() {
        this.setState({anchorEl: null});
    };

    onUserMenuClick(event) {
        this.setState({anchorEl: document.getElementById(this.props.anchor)});
    }

    render() {
        const {anchorEl} = this.state;
        const {classes, presenter, doIntro} = this.props;
        return (
            <span>
                <PersonIcon id={build(ids.DESKTOP, ids.USER_PREF_MENU)}
                            className={classes.menuIcon}
                            onClick={this.onUserMenuClick}
                            ref={this.userBtn}
                />
                <Menu id={build(ids.DESKTOP, ids.USER_PREF_MENU)}
                      anchorEl={anchorEl}
                      open={Boolean(anchorEl)}
                      onClose={this.handleClose}>
                    <MenuItem id={build(ids.DESKTOP, ids.PREFERENCES_LINK)}
                              onClick={() => {
                                  presenter.onPreferencesClick();
                                  this.handleClose();
                              }}>
                        <DEHyperlink text={getMessage("preferences")}/>
                    </MenuItem>
                    <MenuItem id={build(ids.DESKTOP, ids.COLLABORATORS_LINK)}
                              onClick={() => {
                                  presenter.onCollaboratorsClick();
                                  this.handleClose();
                              }}>
                        <DEHyperlink text={getMessage("collaborators")}/>
                    </MenuItem>
                    <MenuItem id={build(ids.DESKTOP, ids.TEAMS_LINK)}
                              onClick={() => {
                                  presenter.onTeamsClick();
                                  this.handleClose();
                              }}>
                        <DEHyperlink text={getMessage("teams")}/>
                    </MenuItem>
                      <Divider />
                    <MenuItem id={build(ids.DESKTOP, ids.USER_MANUAL_LINK)}
                              onClick={() => {
                                  presenter.onDocumentationClick();
                                  this.handleClose();
                              }}>
                        <DEHyperlink text={getMessage("documentation")}/>
                    </MenuItem>
                     <MenuItem id={build(ids.DESKTOP, ids.INTRO_LINK)}
                               onClick={() => {
                                   doIntro();
                                   this.handleClose();
                               }}>
                        <DEHyperlink text={getMessage("introduction")}/>
                     </MenuItem>
                    <MenuItem id={build(ids.DESKTOP, ids.ABOUT_LINK)}
                              onClick={() => {
                                  presenter.onAboutClick();
                                  this.handleClose();
                              }}>
                        <DEHyperlink text={getMessage("about")}/>
                    </MenuItem>
                     <Divider />
                    <MenuItem id={build(ids.DESKTOP, ids.LOGOUT_LINK)}
                              onClick={() => {
                                  presenter.doLogout(false);
                                  this.handleClose();
                              }}>
                        <DEHyperlink text={getMessage("logout")}/>
                    </MenuItem>
                </Menu>
            </span>
        );
    }
}

export default withStyles(styles)(withI18N(UserMenu, intlData));
