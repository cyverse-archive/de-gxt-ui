/**
 * @author sriram
 */
import React, { Component } from "react";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import DEHyperlink from "../../../src/util/hyperlink/DEHyperLink";
import styles from "../style";
import { withStyles } from "@material-ui/core/styles";
import ids from "../ids";
import intlData from "../messages";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import build from "../../util/DebugIDUtil";
import tour from "../NewUserTourSteps";
import helpImg from "../../resources/images/help.png";

class Help extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
        this.onMenuItemSelect = this.onMenuItemSelect.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.handleClick = this.handleClick.bind(this);
        this.helpBtn = React.createRef();
    }

    componentDidMount() {
        this.helpBtn.current.setAttribute("data-intro", tour.HelpMenu.message);
        this.helpBtn.current.setAttribute("data-position", tour.HelpMenu.position);
        this.helpBtn.current.setAttribute("data-step", tour.HelpMenu.step);
    }

    handleClick(event) {
        this.setState({anchorEl: document.getElementById(this.props.anchor)});
    };

    handleClose() {
        this.setState({anchorEl: null});
    };

    onMenuItemSelect(event) {
        let id = event.currentTarget.id;
        let presenter = this.props.presenter;
        switch (id) {
            case build(ids.DESKTOP, ids.FAQS_LINK):
                presenter.onFaqSelect();
                break;
            case build(ids.DESKTOP, ids.FORUMS_LINK):
                presenter.onForumsBtnSelect();
                break;
            case build(ids.DESKTOP, ids.FEEDBACK_LINK):
                presenter.onFeedbackSelect();
                break;
            default:
                break; //do nothing
        }
        this.handleClose();
    }

    render() {
        const {anchorEl} = this.state;
        const classes = this.props.classes;
        return (
            <span>
                 <img className={classes.menuIcon}
                      src={helpImg}
                      alt="Help"
                      onClick={this.handleClick}
                      ref={this.helpBtn}></img>
                 <Menu id={build(ids.DESKTOP, ids.HELP_MENU)}
                       anchorEl={anchorEl}
                       open={Boolean(anchorEl)}
                       onClose={this.handleClose}
                 >
                     <MenuItem id={build(ids.DESKTOP, ids.FAQS_LINK)}
                               onClick={this.onMenuItemSelect}>
                         <DEHyperlink text={getMessage("faqLink")} onClick={this.handleClose}/>
                     </MenuItem>
                     <MenuItem id={build(ids.DESKTOP, ids.FORUMS_LINK)}
                               onClick={this.onMenuItemSelect}>
                         <DEHyperlink text={getMessage("forumsLink")} onClick={this.handleClose}/>
                     </MenuItem>
                     <MenuItem id={build(ids.DESKTOP, ids.FEEDBACK_LINK)}
                               onClick={this.onMenuItemSelect}>
                         <DEHyperlink text={getMessage("feedbackLink")} onClick={this.handleClose}/>
                     </MenuItem>
                 </Menu>
            </span>
        );
    }
}

export default withStyles(styles)(withI18N(Help, intlData));