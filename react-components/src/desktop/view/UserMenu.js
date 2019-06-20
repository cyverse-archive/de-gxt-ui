/**
 * @author sriram
 */
import React, { Component } from "react";
import ReactDOM from "react-dom";

import ids from "../ids";
import intlData from "../messages";
import styles from "../style";
import tour from "../NewUserTourSteps";

import { build, DEHyperlink, getMessage, withI18N } from "@cyverse-de/ui-lib";

import Divider from "@material-ui/core/Divider";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import PersonIcon from "@material-ui/icons/Person";
import Tooltip from "@material-ui/core/Tooltip";

import { withStyles } from "@material-ui/core/styles";

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
        this.setState({ anchorEl: null });
    }

    onUserMenuClick(event) {
        this.setState({ anchorEl: document.getElementById(this.props.anchor) });
    }

    render() {
        const { anchorEl } = this.state;
        const { classes, presenter } = this.props;
        return (
            <span>
                <Tooltip title={getMessage("preferencesTooltip")}>
                    <PersonIcon
                        id={build(ids.DESKTOP, ids.USER_PREF_MENU)}
                        className={classes.menuIcon}
                        onClick={this.onUserMenuClick}
                        ref={this.userBtn}
                    />
                </Tooltip>
                <Menu
                    id={build(ids.DESKTOP, ids.USER_PREF_MENU)}
                    anchorEl={anchorEl}
                    open={Boolean(anchorEl)}
                    onClose={this.handleClose}
                >
                    <MenuItem
                        id={build(ids.DESKTOP, ids.PREFERENCES_LINK)}
                        onClick={() => {
                            presenter.onPreferencesClick();
                            this.handleClose();
                        }}
                    >
                        <DEHyperlink text={getMessage("preferences")} />
                    </MenuItem>
                    <MenuItem
                        id={build(ids.DESKTOP, ids.COLLABORATORS_LINK)}
                        onClick={() => {
                            presenter.onCollaboratorsClick();
                            this.handleClose();
                        }}
                    >
                        <DEHyperlink text={getMessage("collaborators")} />
                    </MenuItem>
                    <MenuItem
                        id={build(ids.DESKTOP, ids.TEAMS_LINK)}
                        onClick={() => {
                            presenter.onTeamsClick();
                            this.handleClose();
                        }}
                    >
                        <DEHyperlink text={getMessage("teams")} />
                    </MenuItem>
                    <MenuItem
                        id={build(ids.DESKTOP, ids.COMMUNITIES_LINK)}
                        onClick={() => {
                            presenter.onCommunitiesClick();
                            this.handleClose();
                        }}
                    >
                        <DEHyperlink text={getMessage("communities")} />
                    </MenuItem>
                    <Divider />
                    <MenuItem
                        id={build(ids.DESKTOP, ids.LOGOUT_LINK)}
                        onClick={() => {
                            presenter.doLogout(false);
                            this.handleClose();
                        }}
                    >
                        <DEHyperlink text={getMessage("logout")} />
                    </MenuItem>
                </Menu>
            </span>
        );
    }
}

export default withStyles(styles)(withI18N(UserMenu, intlData));
