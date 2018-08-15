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
                              onClick={() => presenter.onPreferencesClick()}>
                        <DEHyperlink text={getMessage("preferences")}
                                     onClick={this.handleClose}/>
                    </MenuItem>
                    <MenuItem id={build(ids.DESKTOP, ids.COLLABORATORS_LINK)}
                              onClick={() => presenter.onCollaboratorsClick()}>
                        <DEHyperlink text={getMessage("collaboration")}
                                     onClick={this.handleClose}/>
                    </MenuItem>
                      <Divider />
                    <MenuItem id={build(ids.DESKTOP, ids.USER_MANUAL_LINK)}
                              onClick={() => presenter.onDocumentationClick()}>
                        <DEHyperlink text={getMessage("documentation")}
                                     onClick={this.handleClose}/>
                    </MenuItem>
                     <MenuItem id={build(ids.DESKTOP, ids.INTRO_LINK)}
                               onClick={doIntro}>
                        <DEHyperlink text={getMessage("introduction")}
                                     onClick={this.handleClose}
                        />
                     </MenuItem>
                    <MenuItem id={build(ids.DESKTOP, ids.ABOUT_LINK)}
                              onClick={() => presenter.onAboutClick()}>
                        <DEHyperlink text={getMessage("about")}
                                     onClick={this.handleClose}/>
                    </MenuItem>
                     <Divider />
                    <MenuItem id={build(ids.DESKTOP, ids.LOGOUT_LINK)}
                              onClick={() => presenter.doLogout(false)}>
                        <DEHyperlink text={getMessage("logout")}
                                     onClick={this.handleClose}/>
                    </MenuItem>
                </Menu>
            </span>
        );
    }
}

export default withStyles(styles)(withI18N(UserMenu, intlData));