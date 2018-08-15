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
import HelpIcon from "@material-ui/icons/Help";
import ReactDOM from "react-dom";

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
        this.setState({anchorEl: document.getElementById(this.props.anchor)});
    };

    handleClose() {
        this.setState({anchorEl: null});
    };

    render() {
        const {anchorEl} = this.state;
        const {classes, presenter} = this.props;
        return (
            <span>
                   <HelpIcon
                       id={ids.HELP_ICON}
                       className={classes.menuIcon}
                       onClick={this.handleClick}
                       ref={this.helpBtn}
                   />
                 <Menu id={build(ids.DESKTOP, ids.HELP_MENU)}
                       anchorEl={anchorEl}
                       open={Boolean(anchorEl)}
                       onClose={this.handleClose}
                 >
                 <MenuItem id={build(ids.DESKTOP, ids.FAQS_LINK)}
                           onClick={() => presenter.onFaqSelect()}>
                     <DEHyperlink text={getMessage("faqLink")} onClick={this.handleClose}/>
                 </MenuItem>
                 <MenuItem id={build(ids.DESKTOP, ids.FORUMS_LINK)}
                           onClick={() => presenter.onForumsBtnSelect()}>
                     <DEHyperlink text={getMessage("forumsLink")} onClick={this.handleClose}/>
                 </MenuItem>
                 <MenuItem id={build(ids.DESKTOP, ids.FEEDBACK_LINK)}
                           onClick={() => presenter.onFeedbackSelect()}>
                     <DEHyperlink text={getMessage("feedbackLink")} onClick={this.handleClose}/>
                 </MenuItem>
             </Menu>
            </span>
        );
    }
}

export default withStyles(styles)(withI18N(Help, intlData));