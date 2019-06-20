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

import { withStyles } from "@material-ui/core/styles";

import Divider from "@material-ui/core/Divider";
import HelpIcon from "@material-ui/icons/Help";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";

class Help extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
        this.handleClose = this.handleClose.bind(this);
        this.handleClick = this.handleClick.bind(this);
        this.helpBtn = React.createRef();
    }

    componentDidMount() {
        let ele = ReactDOM.findDOMNode(this.helpBtn.current);
        ele.setAttribute("data-intro", tour.HelpMenu.message);
        ele.setAttribute("data-position", tour.HelpMenu.position);
        ele.setAttribute("data-step", tour.HelpMenu.step);
    }

    handleClick(event) {
        this.setState({ anchorEl: document.getElementById(this.props.anchor) });
    }

    handleClose() {
        this.setState({ anchorEl: null });
    }

    render() {
        const { anchorEl } = this.state;
        const { classes, presenter, doIntro } = this.props;
        return (
            <span>
                <HelpIcon
                    id={build(ids.DESKTOP, ids.HELP_MENU)}
                    className={classes.menuIcon}
                    onClick={this.handleClick}
                    ref={this.helpBtn}
                />
                <Menu
                    id={build(ids.DESKTOP, ids.HELP_MENU)}
                    anchorEl={anchorEl}
                    open={Boolean(anchorEl)}
                    onClose={this.handleClose}
                >
                    <MenuItem
                        id={build(ids.DESKTOP, ids.INTRO_LINK)}
                        onClick={() => {
                            doIntro();
                            this.handleClose();
                        }}
                    >
                        <DEHyperlink text={getMessage("introduction")} />
                    </MenuItem>
                    <MenuItem
                        id={build(ids.DESKTOP, ids.LEARNING_CENTER_LINK)}
                        onClick={() => {
                            presenter.onLearningCenterBtnClick();
                            this.handleClose();
                        }}
                    >
                        <DEHyperlink text={getMessage("learningCenterLink")} />
                    </MenuItem>
                    <MenuItem
                        id={build(ids.DESKTOP, ids.USER_MANUAL_LINK)}
                        onClick={() => {
                            presenter.onDocumentationClick();
                            this.handleClose();
                        }}
                    >
                        <DEHyperlink text={getMessage("documentation")} />
                    </MenuItem>
                    <MenuItem
                        id={build(ids.DESKTOP, ids.FAQS_LINK)}
                        onClick={() => {
                            presenter.onFaqSelect();
                            this.handleClose();
                        }}
                    >
                        <DEHyperlink text={getMessage("faqLink")} />
                    </MenuItem>
                    <Divider />
                    <MenuItem
                        id={build(ids.DESKTOP, ids.FEEDBACK_LINK)}
                        onClick={() => {
                            presenter.onFeedbackSelect();
                            this.handleClose();
                        }}
                    >
                        <DEHyperlink text={getMessage("feedbackLink")} />
                    </MenuItem>
                    <MenuItem
                        id={build(ids.DESKTOP, ids.ABOUT_LINK)}
                        onClick={() => {
                            presenter.onAboutClick();
                            this.handleClose();
                        }}
                    >
                        <DEHyperlink text={getMessage("about")} />
                    </MenuItem>
                </Menu>
            </span>
        );
    }
}

export default withStyles(styles)(withI18N(Help, intlData));
