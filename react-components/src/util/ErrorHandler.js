/**
 * @author aramsey
 */
import build from "./DebugIDUtil";
import ids from "./ids";
import messages from "./messages";
import styles from "./style";
import withI18N, { getMessage } from "./I18NWrapper";

import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import ErrorIcon from "@material-ui/icons/Error";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import PropTypes from "prop-types";
import React, { Component } from "react";
import { withStyles } from "@material-ui/core/styles";

class ErrorHandler extends Component {
    constructor(props) {
        super(props);

        this.state = {
            open: true,
        };

        this.onClose = this.onClose.bind(this);
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({ open: true });
    }

    onClose() {
        this.setState({ open: false });
    }

    render() {
        const { open } = this.state;
        const { errorSummary, errorDetails, classes } = this.props;

        return (
            <Dialog open={open} onClose={this.onClose} id={ids.errorHandlerDlg}>
                <DialogTitle>
                    <div className={classes.errorHandlerTitle}>
                        <ErrorIcon
                            color="error"
                            className={classes.errorIconPadding}
                        />
                        {getMessage("error")}
                    </div>
                </DialogTitle>
                <DialogContent>
                    <div dangerouslySetInnerHTML={{ __html: errorSummary }} />
                    <ExpansionPanel
                        defaultExpanded
                        style={{ marginTop: "40px" }}
                    >
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                            {getMessage("details")}
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <div
                                dangerouslySetInnerHTML={{
                                    __html: errorDetails,
                                }}
                            />
                        </ExpansionPanelDetails>
                    </ExpansionPanel>
                </DialogContent>
                <DialogActions>
                    <Button
                        id={build(ids.errorHandlerDlg, ids.okBtn)}
                        color="primary"
                        onClick={this.onClose}
                    >
                        {getMessage("ok")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

ErrorHandler.propTypes = {
    errorSummary: PropTypes.string.isRequired,
    errorDetails: PropTypes.string.isRequired,
};

export default withStyles(styles)(withI18N(ErrorHandler, messages));
